package com.terzo;

import java.util.Date;

public class AuditModel {
    private String action;
    private Long workflowId;
    private Long propertyId;
    private String propertyName;
    private String propertyAction;
    private String stageName;
    private String oldValue;
    private String newValue;
    private Date timeStamp;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Long getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(Long workflowId) {
        this.workflowId = workflowId;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyAction() {
        return propertyAction;
    }

    public void setPropertyAction(String propertyAction) {
        this.propertyAction = propertyAction;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public AuditModel() {
    }

    public AuditModel(String action, Long workflowId, Long propertyId, String propertyName, String propertyAction, String stageName, String oldValue, String newValue, Date timeStamp) {
        this.action = action;
        this.workflowId = workflowId;
        this.propertyId = propertyId;
        this.propertyName = propertyName;
        this.propertyAction = propertyAction;
        this.stageName = stageName;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "AuditModel{" +
                "action='" + action + '\'' +
                ", workflowId=" + workflowId +
                ", propertyId=" + propertyId +
                ", propertyName='" + propertyName + '\'' +
                ", propertyAction='" + propertyAction + '\'' +
                ", stageName='" + stageName + '\'' +
                ", oldValue='" + oldValue + '\'' +
                ", newValue='" + newValue + '\'' +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
