package com.terzo;

import java.util.Date;

public class WorkflowModel {
    private String tenant;
    private Long workflowId;
    private String workflowName;
    private String pipelineName;
    private String status;
    private Date startDate;
    private Date endDate;

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public Long getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(Long workflowId) {
        this.workflowId = workflowId;
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
    }

    public String getPipelineName() {
        return pipelineName;
    }

    public void setPipelineName(String pipelineName) {
        this.pipelineName = pipelineName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public WorkflowModel() {
    }

    public WorkflowModel(String tenant, Long workflowId, String workflowName, String pipelineName, String status, Date startDate, Date endDate) {
        this.tenant = tenant;
        this.workflowId = workflowId;
        this.workflowName = workflowName;
        this.pipelineName = pipelineName;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "WorkflowModel{" +
                "tenant='" + tenant + '\'' +
                ", workflowId=" + workflowId +
                ", workflowName='" + workflowName + '\'' +
                ", pipelineName='" + pipelineName + '\'' +
                ", status='" + status + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                '}';
    }
}
