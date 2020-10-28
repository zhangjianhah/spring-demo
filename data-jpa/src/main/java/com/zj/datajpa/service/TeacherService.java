package com.zj.datajpa.service;


import com.zj.datajpa.dao.TeacherRepository;
import com.zj.datajpa.entity.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;


    public List<Teacher> listTeacher(){
        List<Teacher> list = teacherRepository.findAll(Sort.by(Sort.Direction.DESC, "teacherId"));
        return list;
    }


    public Teacher findTeacherByTeacherName(String name){
        Teacher teacher = teacherRepository.findTeacherByTeacherName(name);
        return teacher;
    }

    public Teacher findTeacherByTeacherNameAndStatus(String name,Short status){
        Teacher teacher = teacherRepository.findTeacherByTeacherNameAndStatus(name,status);
        return teacher;
    }

    public  List<Teacher> findTeachersByStatus(Short status){
        List<Teacher> list = teacherRepository.findTeachersByStatus(status);
        return list;
    }




    public void updaetTeacher(String name,Long id){
        teacherRepository.updaetTeacher(name,id);
    }
}
