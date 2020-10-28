package com.zj.datajpa.dao;

import com.zj.datajpa.entity.Teacher;
import org.hibernate.annotations.SQLInsert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * JpaRepository : 常用接口
 * JpaSpecificationExecutor ： 提供了一些拓展功能，完成多条件查询，并且支持分页与排序。
 * 但是不能单独使用，需要配合其他接口使用，如JpaRepository
 */

@Repository
public interface TeacherRepository extends JpaRepository<Teacher,Long>, JpaSpecificationExecutor<Teacher> {


    /**
     * 根据教师名称查询单个教师，这是单个，注意方法中teacher和teachers的区别
     * 这里find|get|read都一样
     * @param name
     * @return
     */
    Teacher findTeacherByTeacherName(String name);
    Teacher readTeacherByTeacherName(String name);
    Teacher getTeacherByTeacherName(String name);

    /**
     * 根据教师名称和状态码查询单个教师
     * @param name
     * @param status
     * @return
     */
    Teacher findTeacherByTeacherNameAndStatus(String name,Short status);

    /**
     * 根据教师状态查询教师列表
     * @param status
     * @return
     */
    List<Teacher> findTeachersByStatus(Short status);

    /**
     * 根据教师状态查询教师，并且按照教师ID排序，默认正序（即ASC）
     * @param status
     * @return
     */
    List<Teacher> findTeachersByStatusOrderByTeacherId(Short status);

    /**
     * 根据教师状态查询教师，并且按照教师ID倒序排序
     * @param status
     * @return
     */
    List<Teacher> findTeachersByStatusOrderByTeacherIdDesc(Short status);

    /**
     * 根据教师状态查询教师，并且教师状态码小于指定参数
     * @param status
     * @return
     */
    List<Teacher> findTeachersByStatusLessThan(Short status);

    /**
     * 根据教师名称和状态码查询教师列表，其中教师名称为模糊查询、
     * 需要注意的是，这里需要自己给参数添加运算符%，例如
     * findTeachersByTeacherNameLikeAndStatus("%zhang%",0)
     * @param teacherName
     * @param status
     * @return
     */
    List<Teacher> findTeachersByTeacherNameLikeAndStatus(String teacherName,Short status);


    /**
     * 根据教师状态查询，这里采用了参数排序，而不是利用方法名称规则
     * @param status
     * @param sort
     * @return
     */
    List<Teacher> findTeachersByStatusLessThan(Short status,Sort sort);


    @Override
    Page<Teacher> findAll(Pageable pageable);


    /**
     * 使用sql查询获取教师信息，这里采用HQL，如果想要使用原生SQL，那么@Query中nativeQuery设置为true
     * 这里的占位符用的是?+参数下标
     * 注意：：参数下标从1开始
     * @param status
     * @param name
     * @return
     */
    @Query(value="from Teacher teacher where teacher.status = ?1 and teacher.teacherName like  concat('%',?2,'%') ")
    List<Teacher> listTeacherBystatusAndName1(Short status,String name);
    /**
     * z这里与方法一中基本一致，采用了:+自定义参数名称的方式，与MyBatis类似
     * @param status
     * @param name
     * @return
     */
    @Query(value="from Teacher teacher where teacher.status = :status and teacher.teacherName like  concat('%',:name,'%') ")
    List<Teacher> listTeacherBystatusAndName2(@Param("status") Short status, @Param("name") String name);


    /**
     * 使用原生SQL获取教师名称，其实就是nativeQuery = true,默认值为false
     * @param status
     * @return
     */
    @Query(value="select teacher_name from teacher where status = ?1",nativeQuery = true)
    String listNameByStatus(Short status);


    @Modifying
    @Query(value="update teacher set teacher_name = ?1 where teacher_id = ?2",nativeQuery = true)
    void updaetTeacher(String name,Long id);


}
