package com.feportoa.todolist.task;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;
import java.util.Optional;


public interface ITaskRepository extends JpaRepository<TaskModel, UUID>{ //Sempre estender JpaRepository nas interfaces do Spring, contendo o model e o UUID
    //List<TaskModel> findByid(UUID id);
    Optional<TaskModel> findById(UUID id);
    List<TaskModel> findByUserId(UUID userId);
}
