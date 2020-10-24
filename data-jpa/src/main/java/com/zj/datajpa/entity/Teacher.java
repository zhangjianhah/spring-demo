package com.zj.datajpa.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
@Data
public class Teacher {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long teacherId;

    @Column
    private String teacherName;


    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "zj_teacher_student", joinColumns = { @JoinColumn(name = "teacher_id") }, inverseJoinColumns = {@JoinColumn(name = "student_id") })
    private List<Student> students;


    @Column
    private Short status;
}
