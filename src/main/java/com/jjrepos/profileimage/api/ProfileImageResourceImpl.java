package com.jjrepos.profileimage.api;

import com.jjrepos.atom.api.security.ProtectedResource;
import com.jjrepos.profileimage.api.adpater.representation.RepresentationAdapter;
import com.jjrepos.profileimage.api.exception.BadRequestException;
import com.jjrepos.profileimage.api.exception.NotFoundException;
import com.jjrepos.profileimage.api.representation.Metadata;
import com.jjrepos.profileimage.api.representation.PageIterator;
import com.jjrepos.profileimage.core.entity.ImageEntity;
import com.jjrepos.profileimage.core.entity.ImageSize;
import com.jjrepos.profileimage.core.entity.ImageType;
import com.jjrepos.profileimage.core.service.ProfileImageService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

/**
 * {@link ProfileImageResource} implementation.
 *
 * @author Justin Jesudass
 */
@RestController
@RequestMapping("/image")
@ProtectedResource
public class ProfileImageResourceImpl implements ProfileImageResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileImageResourceImpl.class);

    @Autowired
    private ProfileImageService imageService;

    @Autowired
    private RepresentationAdapter<ImageEntity, Metadata> imageMetadataAdapter;


    @Override
    @RequestMapping(method = RequestMethod.POST, path = "/{employeeId}", consumes = {"image/*", "application/octet-stream"})
    public ResponseEntity<Void> uploadImage(HttpEntity<byte[]> imagePayload, @PathVariable("employeeId") String employeeId,
                                            @RequestHeader("Content-Type") String imageTypeDesc, @RequestHeader(SOURCE_SYSTEM) String sourceSystem) throws IOException {
        if (StringUtils.isEmpty(sourceSystem)) {
            throw new BadRequestException("Invalid request", sourceSystem + " is required");
        }
        String imageMimeType = StringUtils.substringBefore(imageTypeDesc, ";");
        ImageType imageType = ImageType.fromString(imageMimeType);
        byte[] imageInBytes = imagePayload.getBody();

        ImageEntity image = new ImageEntity(employeeId, imageInBytes, imageType);
        image.setSourceSystem(sourceSystem);
        imageService.saveProfileImageAndScale(image);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, path = "/{employeeId}/base64", consumes = {"image/*"})
    public ResponseEntity<Void> uploadBase64EncodedImage(@RequestBody String base64Image, @PathVariable("employeeId") String employeeId,
                                                         @RequestHeader("Content-Type") String imageTypeDesc, @RequestHeader(SOURCE_SYSTEM) String sourceSystem) {
        if (StringUtils.isEmpty(sourceSystem)) {
            throw new BadRequestException("Invalid request", SOURCE_SYSTEM + " is required");
        }
        if (StringUtils.isEmpty(base64Image)) {
            throw new BadRequestException("Invalid request", "image is required");
        }
        byte[] imageInBytes = Base64.getMimeDecoder().decode(base64Image);
        String imageMimeType = StringUtils.substringBefore(imageTypeDesc, ";");
        ImageType imageType = ImageType.fromString(imageMimeType);
        ImageEntity image = new ImageEntity(employeeId, imageInBytes, imageType);
        image.setSourceSystem(sourceSystem);
        imageService.saveProfileImageAndScale(image);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, path = "/{employeeId}")
    public Metadata getImageMetadata(@PathVariable("employeeId") String employeeId) {
        if (StringUtils.isEmpty(employeeId)) {
            throw new BadRequestException("Invalid request", "employeeId is required");
        }
        ImageEntity originalImage = imageService.getImageMetadata(employeeId);
        if (originalImage == null) {
            throw new NotFoundException("Not found", "Image metadata not found");
        }
        return imageMetadataAdapter.makeFromModel(originalImage);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, path = "/{employeeId}/{size}")
    @ResponseBody
    public ResponseEntity<byte[]> getImage(@PathVariable(name = "size") String imageSize, @PathVariable("employeeId") String employeeId) {

        ImageSize imageSizeType;
        if (imageSize == null) {
            imageSizeType = ImageSize.DEFAULT;
        } else {
            imageSizeType = ImageSize.fromString(imageSize);
        }
        if (StringUtils.isEmpty(employeeId)) {
            throw new BadRequestException("Invalid request", "employee and image size are required");
        }

        ImageEntity image = imageService.getImage(employeeId, imageSizeType);
        if (image == null) {
            throw new NotFoundException("Not found", "Image with size " + imageSize + " not found");
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getImageType().getContentType()))
                .body(image.getPic());
    }


    @Override
    @RequestMapping(method = RequestMethod.GET)
    public List<Metadata> search(@RequestParam("createdAfter") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime createdTime,
                                 @RequestParam("notCreatedBy") String sourceSystem) {

        if (createdTime == null || StringUtils.isEmpty(sourceSystem)) {
            throw new BadRequestException("Invalid request", "createdAfter and notCreatedBy are required");
        }
        List<ImageEntity> images = imageService.getImageMetadata(createdTime, sourceSystem);
        if (images == null || images.isEmpty()) {
            return Collections.emptyList();
        }
        return imageMetadataAdapter.makeFromModels(images);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, path = "/iterator")
    public PageIterator<Metadata> getMetadata(@RequestParam("createdAfter") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime createdTime,
                                              @RequestParam(name = "notCreatedBy", required = false) String sourceSystem, @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "25") int size) {
        if (createdTime == null) {
            throw new BadRequestException("Invalid request", "created time is required");
        }
        if (size > 100) {
            throw new BadRequestException("Invalid request", "size per page is limited to 100");
        }
        PageRequest pageReq = PageRequest.of(page, size);
        Page<ImageEntity> pageOfMetadata;
        if (StringUtils.isNotEmpty(sourceSystem)) {
            pageOfMetadata = imageService.getImageMetadata(createdTime, sourceSystem, pageReq);
        } else {
            pageOfMetadata = imageService.getImageMetadata(createdTime, pageReq);
        }
        if (pageOfMetadata.hasContent()) {
            List<Metadata> metadata = pageOfMetadata.map(imgMetadata -> imageMetadataAdapter.makeFromModel(imgMetadata)).toList();
            return new PageIterator<Metadata>(metadata, pageOfMetadata.getTotalElements(), page, size);
        }
        return PageIterator.empty();
    }
}
