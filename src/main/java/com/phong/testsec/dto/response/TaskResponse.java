package com.phong.testsec.dto.response;

import com.phong.testsec.entity.TaskPriority;
import com.phong.testsec.entity.TaskStatus;
import com.phong.testsec.entity.TaskPriority;
import com.phong.testsec.entity.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse
{
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private LocalDate dueDate;
    private LocalDateTime createdAt;
    private Long userId;
    private String userName;

}
