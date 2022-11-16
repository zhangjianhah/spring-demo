package com.zj.datajpa.entity.test;


import lombok.Data;

import javax.persistence.*;

@Entity(name = "book")
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "color")
public class Book {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;
}
