package com.jjrepos.profileimage.api.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom 404 exception.
 *
 * @author Seth Jordan
 */
//@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
    private static final long serialVersionUID = 3993913199222755711L;

    private List<String> messages;
    private String title;

    /**
     * Constructor.
     */
    public NotFoundException() {
        super();
    }

    /**
     * Constructor.
     *
     * @param title   title
     * @param message descriptive message
     */
    public NotFoundException(String title, String message) {
        super();
        this.messages = new ArrayList<>(1);
        this.messages.add(message);
        this.title = title;
    }

    /**
     * Constructor.
     *
     * @param title    title
     * @param messages list of descriptive message;
     */
    public NotFoundException(String title, List<String> messages) {
        super();
        this.messages = messages;
        this.title = title;

    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}