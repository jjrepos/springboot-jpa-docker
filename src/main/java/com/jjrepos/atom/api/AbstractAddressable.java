package com.jjrepos.atom.api;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractAddressable implements Addressable {

    private static final long serialVersionUID = 6839178965814583722L;

    private List<Link> links;

    @Override
    public List<Link> getLinks() {
        return links;
    }

    @Override
    public void setLinks(List<Link> links) {
        this.links = links;
    }

    @Override
    public void addLink(Link link) {
        this.links = links == null ? new ArrayList<Link>() : links;
        this.links.add(link);
    }

    /**
     * Adds a 'self' ref'ed link.
     *
     * @param path path to the resource
     */
    public void addSelfLink(String path) {
        addLink("self", path);
    }

    /**
     * Adds a link with the relation.
     *
     * @param rel  relationship
     * @param path path to the resource
     */
    public void addLink(String rel, String path) {
        Link link = new Link(rel, path);
        addLink(link);
    }

}
