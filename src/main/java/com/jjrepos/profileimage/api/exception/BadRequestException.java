package com.jjrepos.profileimage.api.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom bad request exception.
 *
 * @author Seth Jordan
 */
public class BadRequestException extends RuntimeException {
    private static final long serialVersionUID = -4144803856570626263L;
    private List<String> messages;
    private String title;

    /**
     * Constructor.
     *
     * @param title   title
     * @param message descriptive message;
     */
    public BadRequestException(String title, String message) {
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
    public BadRequestException(String title, List<String> messages) {
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
