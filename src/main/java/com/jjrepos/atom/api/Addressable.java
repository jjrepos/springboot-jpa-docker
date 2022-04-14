package com.jjrepos.atom.api;

import java.io.Serializable;
import java.util.List;

public interface Addressable extends Serializable {

    /**
     * Gets a list of links.
     *
     * @return links
     */
    List<Link> getLinks();

    /**
     * Sets the Links associated with the representation.
     *
     * @param links List of Links
     */
    void setLinks(List<Link> links);

    /**
     * Adds the link to the list of link representations.
     *
     * @param link Link
     */
    void addLink(Link link);
}
