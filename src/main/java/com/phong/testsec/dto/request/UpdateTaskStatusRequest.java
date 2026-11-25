package com.phong.testsec.dto.request;

import com.phong.testsec.entity.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTaskStatusRequest {
    @NotNull(message = "Status không được để trống")
    private TaskStatus status;
}
