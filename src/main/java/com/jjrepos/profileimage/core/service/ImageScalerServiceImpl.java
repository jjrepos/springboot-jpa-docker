package com.jjrepos.profileimage.core.service;

import com.jjrepos.profileimage.core.entity.ImageEntity;
import com.jjrepos.profileimage.core.entity.ImageSize;
import com.jjrepos.profileimage.core.repository.ProfileImageRepository;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.OffsetDateTime;

/**
 * {@link ImageScalerService} implementation.
 *
 * @author Justin Jesudass
 */
@Transactional
@Service
@Component
public class ImageScalerServiceImpl implements ImageScalerService {

    public static final int MAX_IMAGE_SIZE_IN_MB = 10;
    private static final Logger LOG = LoggerFactory.getLogger(ImageScalerServiceImpl.class);
    private static final String SOURCE_SYSTEM = "profile-image-api";

    @Autowired
    private ProfileImageRepository imageRepo;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ImageEntity saveOriginalImage(final ImageEntity image) {
        if (image == null || StringUtils.isEmpty(image.getEmployeeId())) {
            throw new IllegalArgumentException("Invalid image, metadata");
        }

        validateImage(image.getPic());

        // delete all existing images
        LOG.debug("Deleting all images for employee: {}", image.getEmployeeId());
        imageRepo.deleteByEmployeeId(image.getEmployeeId());

        // Create ORIGINAL image
        image.setImageSize(ImageSize.ORIGINAL);
        image.setCreatedTime(OffsetDateTime.now());
        image.setScalingComplete(false);
        return imageRepo.save(image);
    }

    @Override
    @Async()
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void scaleProfileImageToAllSizesAndSave(final ImageEntity originalImage) {
        if (originalImage == null || originalImage.getImageSize() != ImageSize.ORIGINAL
                || ArrayUtils.isEmpty(originalImage.getPic())) {
            throw new IllegalArgumentException("Invalid Image, cannot be scaled.");
        }
        final String employeeId = originalImage.getEmployeeId();
        InputStream in = new ByteArrayInputStream(originalImage.getPic());

        try {
            BufferedImage original = ImageIO.read(in);
            for (ImageSize imageSize : ImageSize.values()) {
                if (imageSize.allowsScaling()) {
                    LOG.debug("Scaling to employee {} image size width: {}, height {}", employeeId,
                            imageSize.getWidth(), imageSize.getHeight());

                    try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                        Thumbnails.of(original)
                                .size(imageSize.getWidth(), imageSize.getHeight())
                                .outputFormat(originalImage.getImageType().getFileType())
                                .toOutputStream(out);
                        byte[] scaledImage = out.toByteArray();
                        out.flush();
                        ImageEntity image = new ImageEntity(employeeId, scaledImage, originalImage.getImageType(), imageSize);
                        image.setScalingComplete(true);
                        image.setCreatedTime(OffsetDateTime.now());
                        image.setSourceSystem(SOURCE_SYSTEM);
                        LOG.trace("Scaled image {}", image);
                        imageRepo.save(image);
                    }
                }
            }
            // set scaling complete on original image
            originalImage.setScalingComplete(true);
            imageRepo.save(originalImage);
        } catch (IOException e) {
            LOG.error("Profile Image for employee: {} could not be scaled", employeeId, e);
            System.out.println("Profile Image for employee: {} could not be scaled - sys" + employeeId);
        }
    }

    /**
     * Gets the size of the image byte array in Mbs.
     *
     * @param image the image byte array.
     * @return size in Mbs.
     */
    protected double getImageSizeInMbs(final byte[] image) {
        return image == null ? 0 : (double) image.length / (1024 * 1024);
    }

    /**
     * Validates if the data in the byte array is with in the proper size constraints. Also validates if the data can be
     */
    private void validateImage(final byte[] bytes) {
        final double imageSize = getImageSizeInMbs(bytes);
        LOG.debug("Original image size: {} MB", imageSize);
        if (imageSize <= 0 || imageSize > MAX_IMAGE_SIZE_IN_MB) {
            throw new IllegalArgumentException("Image size is not valid : " + imageSize + " MB");
        }
        InputStream in = new ByteArrayInputStream(bytes);
        BufferedImage original = null;
        try {
            original = ImageIO.read(in);
        } catch (IOException ignored) {
        }
        if (original == null) {
            throw new IllegalArgumentException("No image reader for image binary - invalid image binary");
        }
    }

}
