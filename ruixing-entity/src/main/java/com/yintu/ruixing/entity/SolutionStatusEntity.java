package com.yintu.ruixing.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolutionStatusEntity implements Serializable {
    private static final long serialVersionUID = 6430089144140626351L;
    private Integer id;

    private Integer yearId;

    private Integer projectId;

    private Integer fileTypeId;

    private Short taskStatus;

    private Date taskFinishDate;

    private Short projectStatus;

    private String filePath;

    private String fileName;

    private Integer auditorId;

    private Integer bidderId;

    private Short releaseStatus;

    private Short type;

    private String yearName;

    private String projectName;

    private String fileTypeName;

}