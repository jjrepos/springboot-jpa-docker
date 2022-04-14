package com.jjrepos.profileimage.api;

import com.jjrepos.profileimage.api.representation.Metadata;
import com.jjrepos.profileimage.api.representation.PageIterator;
import com.jjrepos.profileimage.core.entity.ImageSize;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * Employee's Profile Image resource.
 *
 * @author Justin Jesudass
 */

public interface ProfileImageResource extends Resource {

    String SOURCE_SYSTEM = "Source-System";

    /**
     * Searches across all images and returns a list of {@link Metadata} matching the parameters.
     * Only use when the size of the result set is small. large sizes will result in indefinite wait times.
     *
     * @param createdTime  {@link OffsetDateTime} searches for all images created on or after the created time.
     * @param sourceSystem searches for all images NOT created by the specified source system.
     * @return List of {@link Metadata} matching the parameters if found, 404 otherwise.
     */

    List<Metadata> search(OffsetDateTime createdTime, String sourceSystem);


    /**
     * Searches across all images and returns a Page of {@link Metadata} matching the parameters.
     * This returns the page object which can help iterate though result set of larger sizes.
     *
     * @param createdTime  {@link OffsetDateTime} searches for all images created on or after the created time.
     * @param sourceSystem searches for all images NOT created by the specified source system.
     * @param page         page number of the results
     * @param size         number of metadata to fetch
     * @return Page of {@link Metadata} matching the parameters if found, empty {@link PageIterator#empty()} otherwise.
     */

    PageIterator<Metadata> getMetadata(OffsetDateTime createdTime, String sourceSystem, int page, int size);

    /**
     * Uploads a new employee's profile image.
     *
     * @param imagePayload {@link HttpEntity}
     * @param employeeId   employee's Id
     * @param imageType    The type of image uploaded, obtained from the header parameter "Content-Type"
     * @param sourceSystem The source system through which the user uploaded the image.
     * @return HTTP Response code 201 if successful.
     * @throws IOException Exception thrown if image could not be uploaded.
     */

    ResponseEntity<Void> uploadImage(HttpEntity<byte[]> imagePayload, String employeeId, String imageType, String sourceSystem)
            throws IOException;

    /**
     * Uploads a new employee's profile image.
     *
     * @param base64Image   Image as Base64 encoded String
     * @param employeeId    employee's Id
     * @param imageTypeDesc The type of image uploaded, obtained from the header parameter "Content-Type"
     * @param sourceSystem  The source system through which the user uploaded the image.
     * @return HTTP Response code 201 if successful.
     * @throws IOException Exception thrown if image could not be uploaded.
     */


    ResponseEntity<Void> uploadBase64EncodedImage(String base64Image, String employeeId, String imageTypeDesc, String sourceSystem);

    /**
     * Gets an employee's profile image metadata.
     *
     * @param employeeId employee's Id
     * @return {@link Metadata} if found, 404 otherwise.
     */


    Metadata getImageMetadata(String employeeId);

    /**
     * Gets an employee's profile image.
     *
     * @param imageSize  {@link ImageSize}. size parameter can be null. if
     * @param employeeId employee's Id
     * @return {@link ResponseEntity} containing the employee's profile image.
     */


    ResponseEntity<byte[]> getImage(String imageSize, String employeeId);


}
