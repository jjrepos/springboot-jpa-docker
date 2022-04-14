package com.jjrepos.profileimage.api.representation;

import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PageIterator<T> {
    private final Collection<T> content;
    private final long totalElements;
    private final int size;
    private final int page;

    public PageIterator(Collection<T> content, long totalElements, int page, int size) {
        Assert.notNull(content, "Content must not be null!");
        Assert.isTrue(size >= 0, "size should be greater than or equal 0");
        Assert.isTrue(page >= 0, "page should be greater than or equal to 0");
        this.content = content;
        this.totalElements = totalElements;
        this.size = size;
        this.page = page;
    }

    public static <T> PageIterator<T> empty() {
        List<T> content = Collections.emptyList();
        return new PageIterator<T>(content, 0, 0, 0);
    }

    public int getTotalPages() {
        return this.size == 0 ? 0 : (int) Math.ceil((double) this.totalElements / (double) this.size);
    }

    public long getTotalElements() {
        return this.totalElements;
    }

    public Collection<T> getContent() {
        return this.content;
    }

    public boolean hasNext() {
        return this.page + 1 < this.getTotalPages();
    }

    public boolean isLast() {
        return !this.hasNext();
    }

    public boolean hasPrevious() {
        return this.page > 0;
    }

    public boolean isFirst() {
        return !this.hasPrevious();
    }

    public int getNumberOfElements() {
        return this.content.size();
    }
}
