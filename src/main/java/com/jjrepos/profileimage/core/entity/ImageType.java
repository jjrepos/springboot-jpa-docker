package com.jjrepos.profileimage.core.entity;

import org.apache.commons.lang3.StringUtils;

/**
 * Image Types (file extension) enumeration.
 *
 * @author Justin Jesudass
 */
public enum ImageType {

    PNG("image/png"), JPEG("image/jpeg"), JPG("image/jpg");

    private static final String ALL_IMAGE_TYPE_DESC;

    /**
     * Static block to initialize ALL_IMAGE_TYPE_DESC.
     */
    static {
        StringBuilder allContentTypes = new StringBuilder();
        for (ImageType imgType : ImageType.values()) {
            allContentTypes.append(imgType.getContentType());
            allContentTypes.append(",");
        }
        ALL_IMAGE_TYPE_DESC = allContentTypes.substring(0, allContentTypes.length() - 1);
    }

    private final String contentType;

    ImageType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * Constructs an {@link ImageType} from the String content type parameter passed.
     *
     * @param contentType The image content type.
     * @return {@link ImageType}
     * @throws IllegalArgumentException if no matching {@link ImageType} found.
     */
    public static ImageType fromString(final String contentType) {
        if (StringUtils.isNotEmpty(contentType)) {
            for (ImageType imgType : ImageType.values()) {
                if (contentType.equalsIgnoreCase(imgType.getContentType())) {
                    return imgType;
                }
            }
        }
        throw new IllegalArgumentException("Invalid image content type: " + contentType + ", must be one of : " + ALL_IMAGE_TYPE_DESC);
    }

    public String getContentType() {
        return contentType;
    }

    public String getFileType() {
        return StringUtils.substringAfter(contentType, "/");
    }
}
