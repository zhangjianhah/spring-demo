package com.zj.datajpa.entity;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table
@Data
public class StudentCard {

    @Id
    @GeneratedValue(strategy= GenerationType.TABLE)
    private Long studentCardId;
//    private Long id;

    @Column
    private String studentCardNumber;

}
