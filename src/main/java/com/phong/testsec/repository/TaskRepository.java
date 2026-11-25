package com.phong.testsec.repository;

import com.phong.testsec.entity.Task;
import com.phong.testsec.entity.TaskPriority;
import com.phong.testsec.entity.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>
{
    @EntityGraph(attributePaths = {"user"})
    Optional<Task> findByIdAndUserId(Long id, Long userId);

    @EntityGraph(attributePaths = {"user"})
    Page<Task> findByUserId(Long userId, Pageable pageable);

    @EntityGraph(attributePaths = {"user"})
    Page<Task> findByUserIdAndStatus(Long userId, TaskStatus status, Pageable pageable);

    @EntityGraph(attributePaths = {"user"})
    Page<Task> findByUserIdAndPriority(Long userId, TaskPriority priority, Pageable pageable);

    @EntityGraph(attributePaths = {"user"})
    Page<Task> findByUserIdAndStatusAndPriority(Long userId, TaskStatus status, TaskPriority priority, Pageable pageable);

    @EntityGraph(attributePaths = {"user"})
    List<Task> findByUserIdAndDueDateBeforeAndStatusNot(Long userId, LocalDate date, TaskStatus status);

    @Query("SELECT t.status, COUNT(t) FROM Task t WHERE t.user.id = :userId GROUP BY t.status")
    List<Object[]> countTasksByStatusForUser(Long userId);

    @Query("SELECT t FROM Task t JOIN FETCH t.user WHERE t.user.id = :userId")
    List<Task> findTasksWithUser(@Param("userId") Long userId);
}
