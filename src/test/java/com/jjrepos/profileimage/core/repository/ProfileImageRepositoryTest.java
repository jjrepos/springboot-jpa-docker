package com.jjrepos.profileimage.core.repository;

import com.jjrepos.profileimage.core.entity.ImageEntity;
import com.jjrepos.profileimage.core.entity.ImageSize;
import com.jjrepos.profileimage.core.entity.ImageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ProfileImageRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private ProfileImageRepository imageRepo;

    @Value("classpath:images/sjordanProfile.png")
    private org.springframework.core.io.Resource image;

    @PersistenceContext
    private EntityManager em;

    private ImageEntity sethImage;
    private OffsetDateTime createdTime = OffsetDateTime.now();
    private String employeeId = "520610";

    @BeforeEach
    public void setup() throws IOException {
        Path path = Paths.get(image.getFile().getAbsolutePath());
        byte[] imageBytes = Files.readAllBytes(path);
        ImageEntity image = new ImageEntity(employeeId, imageBytes, ImageType.PNG);

        image.setSourceSystem("Zone");
        image.setImageSize(ImageSize.ORIGINAL);
        image.setCreatedTime(OffsetDateTime.now());
        image.setScalingComplete(false);
        ImageEntity persistedImage = imageRepo.save(image);
        em.flush();

        assertThat(persistedImage).isNotNull();
        assertThat(persistedImage.getId()).isNotNull();
        assertThat(persistedImage.getEmployeeId()).isEqualTo(employeeId);

    }


    @Test
    public void testDeleteByEmployeeId() {
        ImageEntity persistedImage = imageRepo.findByEmployeeIdAndImageSize(employeeId, ImageSize.ORIGINAL);
        assertThat(persistedImage.getId()).isNotNull();
        assertThat(persistedImage.getEmployeeId()).isEqualTo(employeeId);

        int deleteCount = imageRepo.deleteByEmployeeId(employeeId);
        em.flush();
        assertThat(deleteCount).isEqualTo(1);
    }

    @Test
    public void testFindByImageSizeCreatedTimeGreaterThanEqualSourceSystemNot() {
        List<ImageEntity> images = imageRepo.findByImageSizeCreatedTimeGreaterThanEqualSourceSystemNot(ImageSize.ORIGINAL, createdTime, "test");
        assertThat(images).isNotEmpty();
        assertThat(images.size()).isGreaterThan(0);
        assertThat(images).extracting("employeeId").contains(employeeId);
    }

    @Test
    public void testFindByEmployeeAndSize() {
        ImageEntity persistedImage = imageRepo.findByEmployeeIdAndImageSize(employeeId, ImageSize.ORIGINAL);
        assertThat(persistedImage).isNotNull();
        assertThat(persistedImage.getId()).isNotNull();
        assertThat(persistedImage.getEmployeeId()).isEqualTo(employeeId);
    }


    @Test
    public void testFindWithPictureByEmployeeIdImageSize() {
        ImageEntity foundImage = imageRepo.findWithPictureByEmployeeIdImageSize(employeeId, ImageSize.ORIGINAL);
        assertThat(foundImage).isNotNull();
        assertThat(foundImage.getPic()).isNotEmpty();
        assertThat(foundImage.getEmployeeId()).isEqualTo(employeeId);
    }


    @Test
    public void testFindByEmployeeIdAndImageSize() {
        ImageEntity foundImage = imageRepo.findByEmployeeIdAndImageSize(employeeId, ImageSize.ORIGINAL);
        assertThat(foundImage).isNotNull();
        assertThat(foundImage.getId()).isNotNull();
        assertThat(foundImage.getPic()).isNotEmpty();
    }

    @Test
    public void testFindByCreatedTimeGreaterThanEqual() {
        Page<ImageEntity> page = imageRepo.findByImageSizeAndCreatedTimeGreaterThanEqualOrderByCreatedTime(ImageSize.ORIGINAL, createdTime, PageRequest.of(0, 10));
        assertThat(page.getTotalPages()).isEqualTo(1);
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getContent()).extracting("employeeId").contains(employeeId);
    }

    @Test
    public void testFindByCreatedTimeGreaterThanAndSourceSystemNotOrderByCreatedTime() {
        Page<ImageEntity> page = imageRepo.findByImageSizeAndCreatedTimeGreaterThanEqualAndSourceSystemNotIgnoreCaseOrderByCreatedTime(ImageSize.ORIGINAL, createdTime, "test", PageRequest.of(0, 10));
        assertThat(page.getTotalPages()).isEqualTo(1);
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getContent()).extracting("employeeId").contains(employeeId);
    }


}
