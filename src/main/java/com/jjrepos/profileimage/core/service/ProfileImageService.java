package com.jjrepos.profileimage.core.service;

import com.jjrepos.profileimage.core.entity.ImageEntity;
import com.jjrepos.profileimage.core.entity.ImageSize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Employee's Profile Image Service.
 *
 * @author Justin Jesudass
 */
public interface ProfileImageService {
    /**
     * Saves a profile image, and scales the saved image to all sizes as specified by {@link ImageSize#allowsScaling()}
     *
     * @param image {@link ImageEntity}
     */
    void saveProfileImageAndScale(ImageEntity image);

    /**
     * Gets an employee's Image by {@link ImageSize}
     *
     * @param employeeId employee id
     * @param imageSize  {@link ImageSize}
     * @return {@link ImageEntity} containing the image, employee Id and image type if found, null otherwise.
     */
    ImageEntity getImage(String employeeId, ImageSize imageSize);

    /**
     * Gets only the metadata of the image size {@link ImageSize#ORIGINAL}. Note: The image itself is not returned with
     * this call @see {@link #getImage(String, ImageSize)}
     *
     * @param employeeId employee id
     * @return {@link ImageEntity} with metadata ONLY if found, null otherwise.
     */
    ImageEntity getImageMetadata(String employeeId);

    /**
     * Gets all image metadata whose  created time is greater or equal to passed created time and source system is not the passed sourceSystem parameter.
     *
     * @param createdTime  created time of the image
     * @param sourceSystem the source system source system that created the image
     * @return List<ImageEntity>
     */
    List<ImageEntity> getImageMetadata(OffsetDateTime createdTime, String sourceSystem);

    /**
     * Gets all image metadata whose  created time is greater or equal to passed created time.
     *
     * @param createdTime created time of the image
     * @param pageReq     {@link PageRequest}
     * @return Page<ImageEntity>
     */
    Page<ImageEntity> getImageMetadata(OffsetDateTime createdTime, PageRequest pageReq);

    /**
     * Gets all image metadata whose  created time is greater or equal to passed created time and source system is not the passed sourceSystem parameter.
     *
     * @param createdTime  created time of the image
     * @param sourceSystem the source system source system that created the image
     * @param pageReq      {@link PageRequest}
     * @return Page<ImageEntity>
     */
    Page<ImageEntity> getImageMetadata(OffsetDateTime createdTime, String sourceSystem, PageRequest pageReq);
}
