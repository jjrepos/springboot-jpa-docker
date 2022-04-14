package com.jjrepos.profileimage.api.adpater.representation;

import com.jjrepos.atom.api.LinksBuilder;
import com.jjrepos.profileimage.api.representation.Metadata;
import com.jjrepos.profileimage.core.entity.ImageEntity;
import com.jjrepos.profileimage.core.entity.ImageSize;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Adapts {@link ImageEntity} to {@link Metadata}.
 *
 * @author Justin Jesudass
 */
@Component
public class MetadataAdapter implements RepresentationAdapter<ImageEntity, Metadata> {

    @Override
    public List<Metadata> makeFromModels(Collection<ImageEntity> images) {
        if (images == null) {
            throw new IllegalArgumentException("Images are required");
        }
        List<Metadata> metadata = images.stream()
                .map(image -> makeFromModel(image))
                .collect(Collectors.toList());
        return metadata;
    }

    @Override
    public Metadata makeFromModel(ImageEntity image) {
        if (image == null || image.getImageSize() != ImageSize.ORIGINAL) {
            throw new IllegalArgumentException("Image with size" + ImageSize.ORIGINAL + "is required");
        }
        Metadata metadata = new Metadata();
        metadata.setEmployeeId(image.getEmployeeId());
        metadata.setCreatedTime(image.getCreatedTime());
        metadata.setCreatedByClient(image.getSourceSystem());

        LinksBuilder builder = new LinksBuilder();
        builder.addLink("metadata", "/image/" + image.getEmployeeId());
        builder.addLink("image-original", "/image/" + image.getEmployeeId() + "/" + ImageSize.ORIGINAL.getDescription());

        // image scaling is complete? create image links
        if (image.isScalingComplete()) {
            for (ImageSize imgSize : ImageSize.values()) {
                if (imgSize.allowsScaling()) {
                    builder.addLink("image-" + imgSize.getDescription(), "/image/" + image.getEmployeeId() + "/" + imgSize.getDescription());
                }
            }
        }
        metadata.setLinks(builder.toLinks());
        return metadata;
    }

}
