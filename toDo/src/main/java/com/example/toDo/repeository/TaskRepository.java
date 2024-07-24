package com.example.toDo.repeository;

import com.example.toDo.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {
    Page<Task> findByContentContainingAndDate(String content, LocalDate date, Pageable pageable);
    Page<Task> findByContentContaining(String title, Pageable pageable);
    Page<Task> findByDate(LocalDate date, Pageable pageable);
    @Query(value="select * from task t where t.id in :ids", nativeQuery=true)
    List<Task> findByIds(Set<String> ids);
}
