package com.jjrepos.profileimage.core.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * Employee's profile image.
 *
 * @author Justin Jesudass
 */
@Table(name = "IMAGE", uniqueConstraints = {@UniqueConstraint(name = "image-size-employeeid", columnNames = {"employeeId", "imageSize"})})
@Entity
public class ImageEntity implements Serializable {
    private static final long serialVersionUID = 5625940412416496992L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "image_id_seq")
    @SequenceGenerator(name = "image_id_seq", sequenceName = "IMAGE_ID_SEQ", allocationSize = 25)

    private Long id;

    @Lob
    @Basic(optional = false, fetch = FetchType.LAZY)
    private byte[] pic;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ImageType imageType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ImageSize imageSize;

    @Column(nullable = false)
    private String employeeId;

    @Column(nullable = false)
    private OffsetDateTime createdTime;

    @Column(nullable = false)
    private String sourceSystem;

    @Column(nullable = false)
    private boolean scalingComplete;

    /**
     * Default Constructor required for JPA.
     */
    public ImageEntity() {
    }

    /**
     * Initialize ImageEntity.
     *
     * @param employeeId employee Id.
     * @param pic        image as array of bytes.
     * @param imageType  {@link ImageType}
     */
    public ImageEntity(String employeeId, byte[] pic, ImageType imageType) {
        super();
        this.employeeId = employeeId;
        this.pic = pic;
        this.imageType = imageType;
    }

    /**
     * Initialize ImageEntity.
     *
     * @param employeeId employee Id.
     * @param pic        image as array of bytes.
     * @param imageType  {@link ImageType}
     * @param imageSize  {@link ImageSize}
     */
    public ImageEntity(String employeeId, byte[] pic, ImageType imageType, ImageSize imageSize) {
        this(employeeId, pic, imageType);
        this.imageSize = imageSize;
    }

    public Long getId() {
        return id;
    }

    public byte[] getPic() {
        return pic;
    }

    public void setPic(byte[] pic) {
        this.pic = pic;
    }

    public ImageType getImageType() {
        return imageType;
    }

    public void setImageType(ImageType imageType) {
        this.imageType = imageType;
    }

    public ImageSize getImageSize() {
        return imageSize;
    }

    public void setImageSize(ImageSize imageSize) {
        this.imageSize = imageSize;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public OffsetDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(OffsetDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public String getSourceSystem() {
        return sourceSystem;
    }

    public void setSourceSystem(String sourceSystem) {
        this.sourceSystem = sourceSystem;
    }

    public boolean isScalingComplete() {
        return scalingComplete;
    }

    public void setScalingComplete(boolean scalingComplete) {
        this.scalingComplete = scalingComplete;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ImageEntity [id=").append(id).append(", imageType=").append(imageType).append(", imageSize=").append(imageSize).append(", employeeId=")
                .append(employeeId).append(", createdTime=").append(createdTime).append("]");
        return builder.toString();
    }

}
