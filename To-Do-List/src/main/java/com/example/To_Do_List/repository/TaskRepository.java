package com.example.To_Do_List.repository;

import com.example.To_Do_List.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {


    Task findByNameTask(String nameTask);
    Task findByTextOfTask(String textOfTask);
    @Query("SELECT t FROM Task AS t JOIN FETCH t.users AS u WHERE u.id =:id")
    List<Task> findTaskByUserId(Long id);

    //JOIN FETCH все зависимые сущности подключаем
    Page<Task> findByNameTaskContainingIgnoreCaseOrTextOfTaskContainingIgnoreCase(
            String nameTask, String textOfTask, Pageable pageable);
}
