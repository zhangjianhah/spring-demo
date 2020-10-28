package com.zj.datajpa.controller;


import com.zj.datajpa.dao.SchoolRepository;
import com.zj.datajpa.dao.TeacherRepository;
import com.zj.datajpa.entity.School;
import com.zj.datajpa.entity.Student;
import com.zj.datajpa.entity.Teacher;
import com.zj.datajpa.service.TeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.*;

@RestController
@Slf4j

@RequestMapping(value = "/test")
public class TestController {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private SchoolRepository schoolRepository;


    @GetMapping(value = "/save")
    public void test(){

        Teacher teacher = new Teacher();;
        teacher.setTeacherName(UUID.randomUUID().toString().replaceAll("-",""));
        teacher.setStatus((short)0);

        teacherRepository.save(teacher);

    }



    @GetMapping(value = "/saveAll")
    public void saveAll(){

        long startTime = System.currentTimeMillis();


        List<School> list = new ArrayList<>();
        for(int i=0;i<100000;i++){
            School school = new School();
//            school.setSchoolId(UUID.randomUUID().toString().replaceAll("-",""));
            school.setDetail("asdasdsdfasdfasdfasdfasd");
            school.setAddress("sdafasdfasdfasdfasd");
            school.setCreateTime(new Date());
            school.setUpdateTime(new Date());


            school.setStatus((short)0);
            list.add(school);
        }

        long middleTime = System.currentTimeMillis();

        System.out.println("生成时间"+ (middleTime - startTime) + "----------------------");

        schoolRepository.saveAll(list);

        long endTime = System.currentTimeMillis();

        System.out.println("插入时间"+ (endTime - middleTime) + "----------------------");

    }


    @GetMapping(value = "/get/{id}")
    public Teacher findById(@PathVariable Long id){

        List <Teacher> teachers = teacherService.listTeacher();

        Optional<Teacher> teacher = teacherRepository.findById(id);
        return teacher.get();


    }

//    @GetMapping(value = "/list")
//    public List<Teacher> findById(){
//
//        List<Teacher> list = null;
//        try {
//            list = teacherRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return list;
//    }






//    @PostConstruct
//    public void test(){
//        log.info("info");
//        log.error("error");
//    }
}
