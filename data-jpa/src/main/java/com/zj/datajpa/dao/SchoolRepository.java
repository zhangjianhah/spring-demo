package com.zj.datajpa.dao;

import com.zj.datajpa.entity.School;
import com.zj.datajpa.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SchoolRepository extends JpaRepository<School,String>, JpaSpecificationExecutor<School> {
}
