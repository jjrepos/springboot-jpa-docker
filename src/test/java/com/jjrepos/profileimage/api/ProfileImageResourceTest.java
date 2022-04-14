package com.jjrepos.profileimage.api;

import com.jjrepos.profileimage.core.entity.ImageEntity;
import com.jjrepos.profileimage.core.entity.ImageSize;
import com.jjrepos.profileimage.core.entity.ImageType;
import com.jjrepos.profileimage.core.service.ProfileImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.Base64;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProfileImageResourceTest extends AbstractResourceTest {
    private static final Logger LOG = LoggerFactory.getLogger(ProfileImageResourceTest.class);
    private final OffsetDateTime createdTime = OffsetDateTime.now();
    private final String employeeId = "520610";
    @Value("classpath:images/sjordanProfile.png")
    private org.springframework.core.io.Resource image;
    @Autowired
    private ProfileImageService imageService;

    @BeforeEach
    public void setUp() throws IOException {
        Path path = Paths.get(image.getFile().getAbsolutePath());
        byte[] imageBytes = Files.readAllBytes(path);
        ImageEntity imageEntity = new ImageEntity("520610", imageBytes, ImageType.PNG);
        imageEntity.setImageSize(ImageSize.ORIGINAL);
        imageEntity.setSourceSystem("Zone");
        imageService.saveProfileImageAndScale(imageEntity);
    }


    @Test
    public void testGetImageMetadataByEmployee() throws Exception {
        this.mockMvc.perform(get("/image/" + employeeId).header(GATEWAY_HEADER, getGateWayHeader(employeeId)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("employeeId", is(employeeId)))
                .andExpect(jsonPath("$.employeeId", is(employeeId)))
                .andExpect(jsonPath("$.links").isArray())
                .andExpect(jsonPath("$.links", hasSize(10)));
    }

    @Test
    public void get_image_should_return_ok() throws Exception {
        ImageSize size = ImageSize.MEDIUM;
        this.mockMvc.perform(get("/image/" + employeeId + "/" + size).header(GATEWAY_HEADER, getGateWayHeader(employeeId)))
                .andExpect(status().isOk());
    }

    @Test
    public void get_image_should_return_bad_request() throws Exception {
        String imageSize = "test";
        this.mockMvc.perform(get("/image/" + employeeId + "/" + imageSize).header(GATEWAY_HEADER, getGateWayHeader(employeeId)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages").isArray())
                .andExpect(jsonPath("$.messages", hasItem("Invalid image size: " + imageSize)));
    }

    @Test
    public void testSearchImageByCreatedTimeAndSourceSystem() throws Exception {
        this.mockMvc.perform(get("/image").param("createdAfter", createdTime.toString()).param("notCreatedBy", "test").header(GATEWAY_HEADER, getGateWayHeader(employeeId)))
                .andExpect(status().isOk());
    }

    @Test
    public void create_base64_image_should_return_created() throws Exception {
        Path path = Paths.get(image.getFile().getAbsolutePath());
        byte[] imageBytes = Files.readAllBytes(path);
        imageBytes = Base64.getMimeEncoder().encode(imageBytes);
        this.mockMvc.perform(post("/image/520610/base64")
                        .content(imageBytes)
                        .contentType(MediaType.IMAGE_PNG_VALUE)
                        .header(GATEWAY_HEADER, getGateWayHeader(employeeId))
                        .header(ProfileImageResource.SOURCE_SYSTEM, "Zone"))
                .andExpect(status().isCreated());
    }

    @Test
    public void create_image_missing_header_should_return_bad_request() throws Exception {
        Path path = Paths.get(image.getFile().getAbsolutePath());
        byte[] imageBytes = Files.readAllBytes(path);
        this.mockMvc.perform(post("/image/520610")
                        .content(imageBytes)
                        .contentType(MediaType.IMAGE_PNG_VALUE)
                        .header(GATEWAY_HEADER, getGateWayHeader(employeeId)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void create_image_missing_image_should_return_bad_request() throws Exception {
        this.mockMvc.perform(post("/image/520610")
                        .contentType(MediaType.IMAGE_PNG_VALUE)
                        .header(GATEWAY_HEADER, getGateWayHeader(employeeId))
                        .header(ProfileImageResource.SOURCE_SYSTEM, "Zone"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void create_base64_image_missing_header_should_return_bad_request() throws Exception {
        Path path = Paths.get(image.getFile().getAbsolutePath());
        byte[] imageBytes = Files.readAllBytes(path);
        imageBytes = Base64.getMimeEncoder().encode(imageBytes);
        this.mockMvc.perform(post("/image/520610/base64")
                        .content(imageBytes)
                        .contentType(MediaType.IMAGE_PNG_VALUE)
                        .header(GATEWAY_HEADER, getGateWayHeader(employeeId)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void create_base64_image_no_image_should_return_bad_request() throws Exception {
        this.mockMvc.perform(post("/image/520610/base64")
                        .contentType(MediaType.IMAGE_PNG_VALUE)
                        .header(GATEWAY_HEADER, getGateWayHeader(employeeId))
                        .header(ProfileImageResource.SOURCE_SYSTEM, "Zone"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void create_image_should_return_created() throws Exception {
        Path path = Paths.get(image.getFile().getAbsolutePath());
        byte[] imageBytes = Files.readAllBytes(path);
        this.mockMvc.perform(post("/image/520610")
                        .content(imageBytes)
                        .contentType(MediaType.IMAGE_PNG_VALUE)
                        .header(GATEWAY_HEADER, getGateWayHeader(employeeId))
                        .header(ProfileImageResource.SOURCE_SYSTEM, "Zone"))
                .andExpect(status().isCreated());
    }

    @Test
    public void get_invalid_url_should_return_not_found() throws Exception {
        ImageSize size = ImageSize.MEDIUM;
        this.mockMvc.perform(get("/image/image/" + employeeId + "/" + size).header(GATEWAY_HEADER, getGateWayHeader(employeeId)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void test_get_metadata_by_created_time_source_system_should_return_paged_results() throws Exception {
        this.mockMvc.perform(get("/image/iterator")
                        .param("createdAfter", createdTime.toString())
                        .param("notCreatedBy", "test")
                        .header(GATEWAY_HEADER, getGateWayHeader(employeeId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].employeeId", is(employeeId)))
                .andExpect(jsonPath("$.content[0].createdByClient", not("test")))
                .andExpect(jsonPath("$.content[0].links").isArray())
                .andExpect(jsonPath("$.content[0].links", hasSize(10)));
    }

    @Test
    public void test_get_metadata_by_created_time_should_return_paged_results() throws Exception {
        this.mockMvc.perform(get("/image/iterator")
                        .param("createdAfter", createdTime.toString())
                        .header(GATEWAY_HEADER, getGateWayHeader(employeeId)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].employeeId", is(employeeId)))
                .andExpect(jsonPath("$.content[0].links").isArray())
                .andExpect(jsonPath("$.content[0].links", hasSize(10)));
    }

    @Test
    public void test_get_metadata_by_created_time_should_return_bad_request() throws Exception {
        this.mockMvc.perform(get("/image/iterator")
                        .param("createdAfter", "2018-01-01")
                        .param("notCreatedBy", "junit")
                        .header(GATEWAY_HEADER, getGateWayHeader(employeeId)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void test_search_by_created_time_not_created_by_should_return_ok() throws Exception {
        this.mockMvc.perform(get("/image")
                        .param("createdAfter", createdTime.toString())
                        .param("notCreatedBy", "test")
                        .header(GATEWAY_HEADER, getGateWayHeader(employeeId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].employeeId", is(employeeId)))
                .andExpect(jsonPath("$[0].createdByClient", not("test")))
                .andExpect(jsonPath("$[0].links").isArray())
                .andExpect(jsonPath("$[0].links", hasSize(10)));
    }


    @Test
    public void test_search_by_created_time_not_created_by_should_return_bad_request() throws Exception {
        this.mockMvc.perform(get("/image")
                        .param("createdAfter", "2018-01-01")
                        .param("notCreatedBy", "junit")
                        .header(GATEWAY_HEADER, getGateWayHeader(employeeId)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void test_search_by_created_time_should_return_bad_request() throws Exception {
        this.mockMvc.perform(get("/image")
                        .param("createdAfter", createdTime.toString())
                        .header(GATEWAY_HEADER, getGateWayHeader(employeeId)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}