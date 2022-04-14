package com.jjrepos.profileimage.api.adpater.representation;

import java.util.Collection;
import java.util.List;

/**
 * Representation Adapter Adapts from a Model to a Representation.
 *
 * @param <Model>          The Model Object to adapt from
 * @param <Representation> The representation object to adapt to
 * @author Justin Jesudass
 */
public interface RepresentationAdapter<Model, Representation> {

    /**
     * Makes a list of representations from models.
     *
     * @param models  models.
     * @param uriInfo base uri
     * @return list of Representation objects.
     */
    List<Representation> makeFromModels(Collection<Model> models);

    /**
     * Creates an representation from a model.
     *
     * @param model   employee
     * @param uriInfo base uri
     * @return Representation object
     */
    Representation makeFromModel(Model model);

}
