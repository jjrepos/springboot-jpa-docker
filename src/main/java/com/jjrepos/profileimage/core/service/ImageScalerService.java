package com.jjrepos.profileimage.core.service;

import com.jjrepos.profileimage.core.entity.ImageEntity;
import com.jjrepos.profileimage.core.entity.ImageSize;

/**
 * Service to scale and save the rezied images to the repository.
 *
 * @author Justin Jesudass
 */
public interface ImageScalerService {
    /**
     * Scales (Resizes) an employee's profile image to all sizes in {@link ImageSize} enum and saves it to the
     * repository.
     *
     * @param originalImage The {@link ImageEntity} whose image size is {@link ImageSize#ORIGINAL}
     */
    void scaleProfileImageToAllSizesAndSave(final ImageEntity originalImage);

    /**
     * Saves an {@link ImageEntity} whose size is {@link ImageSize#ORIGINAL} to the repository.
     *
     * @param image {@link ImageEntity}
     */
    ImageEntity saveOriginalImage(ImageEntity image);
}
