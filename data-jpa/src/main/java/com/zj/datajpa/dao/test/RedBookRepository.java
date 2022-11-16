package com.zj.datajpa.dao.test;

import com.zj.datajpa.entity.test.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RedBookRepository extends JpaRepository<Book,Integer>, JpaSpecificationExecutor<Book> {


}
