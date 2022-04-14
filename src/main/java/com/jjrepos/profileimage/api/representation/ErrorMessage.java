package com.jjrepos.profileimage.api.representation;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Representation of a generic error message.
 *
 * @author sjordan
 */
@XmlRootElement(name = "error")
public class ErrorMessage {
    private String title;
    private List<String> messages = new ArrayList<String>();

    /**
     * Default constructor.
     */
    public ErrorMessage() {
    }

    /**
     * Convenience constructor.
     *
     * @param title   title
     * @param message message
     */
    public ErrorMessage(String title, String message) {
        this.title = title;
        this.messages.add(message);
    }

    /**
     * Convenience constructor.
     *
     * @param title    title
     * @param messages messages
     */
    public ErrorMessage(String title, List<String> messages) {
        this.title = title;
        this.messages = messages;
    }

    /**
     * Gets the title of the message.
     *
     * @return the title.
     */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets a list of messages describing the error.
     *
     * @return list of messages
     */
    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }
}