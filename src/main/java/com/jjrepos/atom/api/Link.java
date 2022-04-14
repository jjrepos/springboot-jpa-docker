package com.jjrepos.atom.api;

import java.io.Serializable;
import java.util.Objects;


public class Link implements Serializable {
    private static final long serialVersionUID = -5870667135641971497L;
    private String rel;
    private String path;

    Link() {
    }

    Link(String rel, String path) {
        this.rel = rel;
        this.path = path;
    }

    public static Link of(String rel, String path) {
        return new Link(rel, path);
    }

    public static Link selfLinkOf(String path) {
        return new Link("self", path);
    }

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Link [rel=").append(rel).append(", path=").append(path).append("]");
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Link link = (Link) o;
        return rel.equals(link.rel) &&
                path.equals(link.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rel, path);
    }
}
