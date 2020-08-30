package com.zj.dataredis.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Student extends BaseEntity {

    private Long studentId;
    private String studentName;
    private String addr;
}
