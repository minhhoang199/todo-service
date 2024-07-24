package com.example.toDo.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class GetTaskDetailRequest {
    @NotBlank
    private String taskId;
}
