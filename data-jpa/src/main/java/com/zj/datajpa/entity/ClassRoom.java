package com.zj.datajpa.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Table
@Data
public class ClassRoom {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long classRoomId;

    @Column
    private String classRoomName;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "class_room_id")
    private List<Student> students;



}
