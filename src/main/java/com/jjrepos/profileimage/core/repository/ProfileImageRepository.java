package com.jjrepos.profileimage.core.repository;

import com.jjrepos.profileimage.core.entity.ImageEntity;
import com.jjrepos.profileimage.core.entity.ImageSize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * {@link ImageEntity} repository defenetions.
 *
 * @author Justin Jesudass
 */
@RepositoryDefinition(domainClass = ImageEntity.class, idClass = Long.class)
public interface ProfileImageRepository {

    /**
     * Saves an {@link ImageEntity}.
     *
     * @param image {@link ImageEntity}.
     * @return {@link ImageEntity}.
     */
    ImageEntity save(ImageEntity image);

    /**
     * Gets an byte array of the picture, along with image type only. All other
     * properties are not fetched.
     *
     * @param employeeId employee id
     * @param imageSize  {@link ImageSize}
     * @return {@link ImageEntity} of the picture if present, null otherwise.
     */
    @Query("SELECT NEW  com.bah.is.profileimage.core.entity.ImageEntity(img.employeeId, img.pic, img.imageType) FROM #{#entityName} img  WHERE img.employeeId = :employeeId AND img.imageSize = :imageSize")
    ImageEntity findWithPictureByEmployeeIdImageSize(@Param("employeeId") String employeeId, @Param("imageSize") ImageSize imageSize);

    /**
     * Deletes all {@link ImageEntity} associated with the associated
     * {@link ImageEntity}..
     *
     * @param employeeId employeeId id.
     */
    @Modifying
    @Query("DELETE FROM #{#entityName} img where img.employeeId = :employeeId")
    int deleteByEmployeeId(@Param("employeeId") String employeeId);

    /**
     * Gets {@link ImageEntity} for the employee by {@link ImageSize}. This call
     * does not bring the picture as byte array.
     *
     * @param employeeId employee id
     * @param imageSize  {@link ImageSize}
     * @return {@link ImageEntity}
     */
    ImageEntity findByEmployeeIdAndImageSize(String employeeId, ImageSize imageSize);

    /**
     * Gets all {@link ImageEntity} created on or after createdDate and
     *
     * @param imageSize    {@link ImageSize}
     * @param createdTime  {@link OffsetDateTime}
     * @param sourceSystem The source system which created the image.
     * @return List of {@link ImageEntity} if found, empty List otherwise.
     */
    @Query("SELECT img FROM #{#entityName} img WHERE img.imageSize = :imageSize AND img.createdTime >= :createdTime AND upper(img.sourceSystem) <> upper(:sourceSystem)")
    List<ImageEntity> findByImageSizeCreatedTimeGreaterThanEqualSourceSystemNot(@Param("imageSize") ImageSize imageSize,
                                                                                @Param("createdTime") OffsetDateTime createdTime, @Param("sourceSystem") String sourceSystem);

    @Query("SELECT img FROM #{#entityName} img WHERE img.imageSize = :imageSize AND img.createdTime >= :createdTime")
    List<ImageEntity> findByImageSizeCreatedTimeGreaterThan(@Param("imageSize") ImageSize imageSize,
                                                            @Param("createdTime") OffsetDateTime createdTime);

    Page<ImageEntity> findByImageSizeAndCreatedTimeGreaterThanEqualOrderByCreatedTime(@Param("imageSize") ImageSize imageSize,
                                                                                      @Param("createdTime") OffsetDateTime createdTime, Pageable pageable);

    Page<ImageEntity> findByImageSizeAndCreatedTimeGreaterThanEqualAndSourceSystemNotIgnoreCaseOrderByCreatedTime(@Param("imageSize") ImageSize imageSize,
                                                                                                                  @Param("createdTime") OffsetDateTime createdTime,
                                                                                                                  @Param("sourceSystem") String sourceSystem,
                                                                                                                  Pageable pageable);

}
