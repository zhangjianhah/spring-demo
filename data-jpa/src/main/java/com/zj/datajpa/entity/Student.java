package com.zj.datajpa.entity;


import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "zj_student")
@Data
public class Student {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String studentId;

    @Column(name = "student_name", unique = true, nullable = false, length = 64)
    private String studentName;

    @Column(name = "password", nullable = false, length = 64)
    private String password;

    @Column(name = "email", length = 64)
    private String email;
//
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)

//    name与referencedColumnName名称不应当相同，否则不会自动创建
//    @JoinColumn(name = "student_card_id",referencedColumnName = "student_card_id")
    @JoinColumn(name = "student_card_id")
    private StudentCard studentCard;


    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "class_room_id")
    private ClassRoom classRoom;


    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinTable(name = "teacher_student", joinColumns = { @JoinColumn(name = "student_id") }, inverseJoinColumns = {@JoinColumn(name = "teacher_id") })
    private List<Teacher> teachers;
}

