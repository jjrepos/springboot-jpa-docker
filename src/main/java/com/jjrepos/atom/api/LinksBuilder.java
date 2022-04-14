package com.jjrepos.atom.api;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder class for assisting in building of links.
 *
 * @author sjordan
 */

public class LinksBuilder {
    private List<Link> links;

    /**
     * Default Constructor
     */
    public LinksBuilder() {
        this.links = new ArrayList<Link>();
    }

    /**
     * Adds a 'self' ref'ed link.
     *
     * @param path path from the base uri
     * @return this builder.
     */
    public LinksBuilder addSelfLink(String path) {
        addLink("self", path);
        return this;
    }

    /**
     * Adds a link.
     *
     * @param rel  relationship
     * @param path path from the base uri
     * @return this builder.
     */
    public LinksBuilder addLink(String rel, String path) {
        Link link = new Link();
        link.setRel(rel);
        link.setPath(path);
        this.links.add(link);
        return this;
    }

    /**
     * Adds a link.
     *
     * @param rel relationship
     * @param url the full url
     * @return this builder.
     */
    public LinksBuilder addLinkWithAbsoluteUrl(String rel, String url) {
        Link link = new Link();
        link.setRel(rel);
        link.setPath(url);
        this.links.add(link);
        return this;
    }

    /**
     * Builds an array of links.
     *
     * @return links array.
     */
    public List<Link> toLinks() {
        return links;
    }

}
