package com.gabriel.apitask.repository;

import com.gabriel.apitask.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query(value = "select * from TB_CATEGORY where name = :name", nativeQuery = true)
    Optional<Category> findByName(@Param("name") String name);

}
