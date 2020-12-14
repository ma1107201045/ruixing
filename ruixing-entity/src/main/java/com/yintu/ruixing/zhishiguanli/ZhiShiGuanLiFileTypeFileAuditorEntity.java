package com.yintu.ruixing.zhishiguanli;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZhiShiGuanLiFileTypeFileAuditorEntity {
    private Integer id;

    private Integer fileId;

    private Integer auditorId;

    private Integer sort;

    private Short activate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public Integer getAuditorId() {
        return auditorId;
    }

    public void setAuditorId(Integer auditorId) {
        this.auditorId = auditorId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Short getActivate() {
        return activate;
    }

    public void setActivate(Short activate) {
        this.activate = activate;
    }
}