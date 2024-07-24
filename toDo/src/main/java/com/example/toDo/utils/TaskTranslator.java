package com.example.toDo.utils;

import com.example.toDo.model.Task;
import com.example.toDo.model.dto.TaskDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskTranslator {
    @Autowired
    private ModelMapper mapper;

    public TaskDto transferToDto(Task task) {
        return this.mapper.map(task, TaskDto.class);
    }

    public Task transferToTask(TaskDto dto) {
        return this.mapper.map(dto, Task.class);
    }

}
