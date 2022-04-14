package com.jjrepos.profileimage.api.representation;

import com.jjrepos.atom.api.AbstractAddressable;

import java.time.OffsetDateTime;

/**
 * Employee profile image's metadata.
 *
 * @author Justin Jesudass
 */
public class Metadata extends AbstractAddressable {
    private static final long serialVersionUID = 1353175728434727087L;

    private String employeeId;
    private OffsetDateTime createdTime;
    private String createdBy;
    private String createdByClient;

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public OffsetDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(OffsetDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedByClient() {
        return createdByClient;
    }

    public void setCreatedByClient(String createdByClient) {
        this.createdByClient = createdByClient;
    }

}
