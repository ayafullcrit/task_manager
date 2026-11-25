package com.phong.testsec.service;

import com.phong.testsec.dto.request.*;
import com.phong.testsec.dto.response.*;
import com.phong.testsec.entity.*;
import com.phong.testsec.exception.*;
import com.phong.testsec.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final NotificationProducer notificationProducer;

    public TaskService(TaskRepository taskRepository, NotificationProducer notificationProducer)
    {
        this.taskRepository = taskRepository;
        this.notificationProducer = notificationProducer;
    }

    // Helper: Trích xuất User từ SecurityContext
    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private Task findTaskOrThrow(Long id, User currentUser) {
        return taskRepository.findByIdAndUserId(id, currentUser.getId())
                .orElseThrow(() -> new TaskNotFoundException("Không tìm thấy task hoặc bạn không có quyền!"));
    }

    public TaskResponse createTask(CreateTaskRequest request) {
        User currentUser = getCurrentUser();

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());
        task.setUser(currentUser); // Gán cho user hiện tại

        notificationProducer.sendTaskNotification(task.getTitle());

        return toTaskResponse(taskRepository.save(task));
    }

    public TaskResponse getTaskById(Long id) {
        return toTaskResponse(findTaskOrThrow(id, getCurrentUser()));
    }

    public Page<TaskResponse> getTasks(TaskStatus status, TaskPriority priority, Pageable pageable) {
        User currentUser = getCurrentUser();
        Page<Task> taskPage;

        if (status != null && priority != null) {
            taskPage = taskRepository.findByUserIdAndStatusAndPriority(currentUser.getId(), status, priority, pageable);
        } else if (status != null) {
            taskPage = taskRepository.findByUserIdAndStatus(currentUser.getId(), status, pageable);
        } else if (priority != null) {
            taskPage = taskRepository.findByUserIdAndPriority(currentUser.getId(), priority, pageable);
        } else {
            taskPage = taskRepository.findByUserId(currentUser.getId(), pageable);
        }
        return taskPage.map(this::toTaskResponse);
    }

    public TaskResponse updateTaskStatus(Long id, UpdateTaskStatusRequest request) {
        Task task = findTaskOrThrow(id, getCurrentUser());
        task.setStatus(request.getStatus());
        return toTaskResponse(taskRepository.save(task));
    }

    public TaskResponse updateTask(Long id, UpdateTaskRequest request) {
        Task task = findTaskOrThrow(id, getCurrentUser());

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        if (request.getPriority() != null) task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());

        return toTaskResponse(taskRepository.save(task));
    }

    public void deleteTask(Long id) {
        Task task = findTaskOrThrow(id, getCurrentUser());
        taskRepository.delete(task);
    }

    public List<TaskResponse> getOverdueTasks() {
        User currentUser = getCurrentUser();
        return taskRepository.findByUserIdAndDueDateBeforeAndStatusNot(currentUser.getId(), LocalDate.now(), TaskStatus.DONE)
                .stream().map(this::toTaskResponse).toList();
    }

    public TaskSummaryResponse getTaskSummary() {
        User currentUser = getCurrentUser();
        List<Object[]> rawResults = taskRepository.countTasksByStatusForUser(currentUser.getId());

        long todoCount = 0, inProgressCount = 0, doneCount = 0;
        for (Object[] row : rawResults) {
            TaskStatus status = (TaskStatus) row[0];
            Long count = (Long) row[1];
            switch (status) {
                case TODO -> todoCount = count;
                case IN_PROGRESS -> inProgressCount = count;
                case DONE -> doneCount = count;
            }
        }
        return new TaskSummaryResponse(todoCount, inProgressCount, doneCount);
    }

    private TaskResponse toTaskResponse(Task task) {
        return new TaskResponse(
                task.getId(), task.getTitle(), task.getDescription(),
                task.getStatus(), task.getPriority(), task.getDueDate(),
                task.getCreatedAt(), task.getUser().getId(), task.getUser().getName()
        );
    }
}
