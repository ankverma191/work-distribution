package com.freedom.financial.work.distribution.request;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotEmpty;

@Data
public class TaskUpdateRequest {
    @NonNull
    @NotEmpty(message = "task ID cannot be empty")
    private String taskId = null;
}
