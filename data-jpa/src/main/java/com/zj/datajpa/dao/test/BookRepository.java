package com.zj.datajpa.dao.test;

import com.zj.datajpa.entity.Teacher;
import com.zj.datajpa.entity.test.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface BookRepository extends JpaRepository<Book,Integer>, JpaSpecificationExecutor<Book> {


}
