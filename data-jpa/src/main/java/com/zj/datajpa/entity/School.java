package com.zj.datajpa.entity;


import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
@Data
public class School {

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String schoolId;


    @Column
    private String schoolName;
    @Column
    private String address;
    @Column
    private Short status;
    @Column
    private String detail;
    @Column
    private Date createTime;
    @Column
    private Date updateTime;
}
