package com.gabriel.apitask.repository;

import com.gabriel.apitask.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer>, JpaSpecificationExecutor<Task> {

    @Query(value = "select * from tb_task where date = :date", nativeQuery = true)
    List<Task> findAllByDate(@Param("date") LocalDate date);

}
