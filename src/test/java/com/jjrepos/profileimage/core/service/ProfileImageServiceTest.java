package com.jjrepos.profileimage.core.service;

import com.jjrepos.profileimage.core.entity.ImageEntity;
import com.jjrepos.profileimage.core.entity.ImageSize;
import com.jjrepos.profileimage.core.entity.ImageType;
import com.jjrepos.profileimage.core.repository.ProfileImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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


public class ProfileImageServiceTest extends AbstractServiceTest {
    private static final Logger LOG = LoggerFactory.getLogger(ProfileImageServiceTest.class);

    @Value("classpath:images/sjordanProfile.png")
    private org.springframework.core.io.Resource image;

    @PersistenceContext
    private EntityManager em;


    @Autowired
    private ProfileImageService profileImageService;

    @Autowired
    private ImageScalerService imageScalerService;

    @Autowired
    private ProfileImageRepository profileImageRepository;

    private OffsetDateTime createdTime = OffsetDateTime.now();
    private String employeeId = "542129";

    @BeforeEach
    public void setUp() throws IOException {
        Path path = Paths.get(image.getFile().getAbsolutePath());
        byte[] imageBytes = Files.readAllBytes(path);
        ImageEntity imageEntity = new ImageEntity(employeeId, imageBytes, ImageType.PNG);
        imageEntity.setImageSize(ImageSize.ORIGINAL);
        imageEntity.setSourceSystem("Zone");
        profileImageService.saveProfileImageAndScale(imageEntity);
    }


    @Test
    public void testGetImageMetadata() {
        ImageEntity e = profileImageService.getImageMetadata(employeeId);
        assertThat(e).isNotNull();
        assertThat(e.getId()).isNotNull();
        assertThat(e.getEmployeeId()).isEqualTo(employeeId);
    }

    @Test
    public void testGetImage() {
        ImageEntity ie = profileImageService.getImage(employeeId, ImageSize.ORIGINAL);
        assertThat(ie).isNotNull();
        assertThat(ie.getEmployeeId()).isEqualTo(employeeId);
        assertThat(ie.getPic()).isNotEmpty();
    }

    @Test
    public void testGetImageMetadataList() {
        List<ImageEntity> images = profileImageService.getImageMetadata(createdTime, "test");
        assertThat(images).isNotEmpty();
        assertThat(images).hasSize(1);
        assertThat(images).extracting("employeeId").contains(employeeId);
    }

    @Test
    public void testGetImageMetadataPageByCreatedDate() {
        Page<ImageEntity> page = profileImageService.getImageMetadata(createdTime, PageRequest.of(0, 25));
        assertThat(page.getTotalPages()).isEqualTo(1);
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getContent()).extracting("employeeId").contains(employeeId);
    }

    @Test
    public void testGetImageMetadataPageByCreatedDateSourceSystem() {
        Page<ImageEntity> page = profileImageService.getImageMetadata(createdTime, "test", PageRequest.of(0, 25));
        assertThat(page.getTotalPages()).isEqualTo(1);
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getContent()).extracting("employeeId").contains(employeeId);
    }

}
