package com.phong.testsec.dto.request;

import com.phong.testsec.entity.TaskPriority;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreateTaskRequest
{
    @NotBlank(message = "title không được để trống")
    @Size(max=100, message = "title tối đa 100 ký tự")
    private String title;

    private String description;

    private TaskPriority priority;

    @FutureOrPresent(message = "Due date phải là hôm nay hoặc trong tương lai")
    private LocalDate dueDate;

//    @NotNull(message = "userId không được để trống")
//    private Long userId;
}
