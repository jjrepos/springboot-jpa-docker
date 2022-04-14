package com.jjrepos.profileimage.core.entity;

import org.apache.commons.lang3.StringUtils;

/**
 * Image Size Enumeration.
 *
 * @author Justin Jesudass
 */

public enum ImageSize {
    THUMBNAIL("48X48", 48, 48),
    SMALL("64X64", 64, 64),
    DEFAULT("96X96", 96, 96),
    MEDIUM("120X120", 120, 120),
    LARGE("240X240", 240, 240),
    X_LARGE("360X360", 360, 360),
    XX_LARGE("432X432", 432, 432),
    XX_LARGE2("504X504", 504, 504),

    // The image size is not predefined for original, hence using -1 and -1, and no auto generation is required.
    ORIGINAL("original", -1, -1);

    private int width;
    private int height;
    private String description;

    ImageSize(String description, int width, int height) {
        this.description = description;
        this.width = width;
        this.height = height;
    }

    /**
     * Gets the {@link ImageSize} for the size description.
     *
     * @param size description of size like "64X64"
     * @return {@link ImageSize}
     */
    public static ImageSize fromString(String size) {
        if (StringUtils.isNotEmpty(size)) {
            for (ImageSize imgSize : ImageSize.values()) {
                if (imgSize.getDescription().equalsIgnoreCase(size) || imgSize.name().equalsIgnoreCase(size)) {
                    return imgSize;
                }
            }
        }
        throw new IllegalArgumentException("Invalid image size: " + size);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Tests whether the ImageSize allows scaling.
     *
     * @return true if the ImageSize is scalable based on the width and the length of the imgge.
     */
    public boolean allowsScaling() {
        return width >= 0 && height >= 0;
    }
}
