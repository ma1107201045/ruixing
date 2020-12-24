package com.yintu.ruixing.danganguanli;

import java.util.Date;

public class LineBaseInformationStationAccessoryEntity {
    private Integer id;

    private String createBy;

    private Date createTime;

    private String modifiedBy;

    private Date modifiedTime;

    private String accessoryName;

    private String accessoryPath;

    private Integer lineBaseInformationStationId;

    private String hardwareMaterialCodeId;

    private String softwareMaterialCodeId;

    private String configurationFileId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy == null ? null : modifiedBy.trim();
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public String getAccessoryName() {
        return accessoryName;
    }

    public void setAccessoryName(String accessoryName) {
        this.accessoryName = accessoryName == null ? null : accessoryName.trim();
    }

    public String getAccessoryPath() {
        return accessoryPath;
    }

    public void setAccessoryPath(String accessoryPath) {
        this.accessoryPath = accessoryPath == null ? null : accessoryPath.trim();
    }

    public Integer getLineBaseInformationStationId() {
        return lineBaseInformationStationId;
    }

    public void setLineBaseInformationStationId(Integer lineBaseInformationStationId) {
        this.lineBaseInformationStationId = lineBaseInformationStationId;
    }

    public String getHardwareMaterialCodeId() {
        return hardwareMaterialCodeId;
    }

    public void setHardwareMaterialCodeId(String hardwareMaterialCodeId) {
        this.hardwareMaterialCodeId = hardwareMaterialCodeId == null ? null : hardwareMaterialCodeId.trim();
    }

    public String getSoftwareMaterialCodeId() {
        return softwareMaterialCodeId;
    }

    public void setSoftwareMaterialCodeId(String softwareMaterialCodeId) {
        this.softwareMaterialCodeId = softwareMaterialCodeId == null ? null : softwareMaterialCodeId.trim();
    }

    public String getConfigurationFileId() {
        return configurationFileId;
    }

    public void setConfigurationFileId(String configurationFileId) {
        this.configurationFileId = configurationFileId == null ? null : configurationFileId.trim();
    }
}