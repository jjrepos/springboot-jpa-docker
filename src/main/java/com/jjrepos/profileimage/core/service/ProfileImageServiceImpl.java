package com.jjrepos.profileimage.core.service;

import com.jjrepos.profileimage.core.entity.ImageEntity;
import com.jjrepos.profileimage.core.entity.ImageSize;
import com.jjrepos.profileimage.core.repository.ProfileImageRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Implementation of {@link ProfileImageService}
 *
 * @author Justin Jesudass
 */
@Transactional
@Service
@Component
public class ProfileImageServiceImpl implements ProfileImageService {

    @Autowired
    private ProfileImageRepository imageRepo;

    @Autowired
    private ImageScalerService imageScaler;

    @Override
    public void saveProfileImageAndScale(ImageEntity image) {
        ImageEntity originalImage = imageScaler.saveOriginalImage(image);
        imageScaler.scaleProfileImageToAllSizesAndSave(originalImage);
    }

    @Override
    public ImageEntity getImageMetadata(String employeeId) {
        if (StringUtils.isEmpty(employeeId)) {
            throw new IllegalArgumentException("employee id is required");
        }
        return imageRepo.findByEmployeeIdAndImageSize(employeeId, ImageSize.ORIGINAL);
    }

    @Override
    public ImageEntity getImage(String employeeId, ImageSize imageSize) {
        if (StringUtils.isEmpty(employeeId) || imageSize == null) {
            throw new IllegalArgumentException("employee id, image size are required");
        }
        return imageRepo.findWithPictureByEmployeeIdImageSize(employeeId, imageSize);
    }

    @Override
    public List<ImageEntity> getImageMetadata(OffsetDateTime createdTime, String sourceSystem) {
        if (createdTime == null) {
            throw new IllegalArgumentException("Created time and source system are required");
        }
        return imageRepo.findByImageSizeCreatedTimeGreaterThanEqualSourceSystemNot(ImageSize.ORIGINAL, createdTime, sourceSystem);
    }


    @Override
    public Page<ImageEntity> getImageMetadata(OffsetDateTime createdTime, PageRequest pageReq) {
        if (createdTime == null || pageReq == null) {
            throw new IllegalArgumentException("Created time and Page info are required");
        }
        return imageRepo.findByImageSizeAndCreatedTimeGreaterThanEqualOrderByCreatedTime(ImageSize.ORIGINAL, createdTime, pageReq);
    }

    @Override
    public Page<ImageEntity> getImageMetadata(OffsetDateTime createdTime, String sourceSystem, PageRequest pageReq) {
        if (createdTime == null || sourceSystem == null || pageReq == null) {
            throw new IllegalArgumentException("Created time, source system and Page info are required");
        }
        return imageRepo.findByImageSizeAndCreatedTimeGreaterThanEqualAndSourceSystemNotIgnoreCaseOrderByCreatedTime(ImageSize.ORIGINAL, createdTime, sourceSystem, pageReq);
    }

}
