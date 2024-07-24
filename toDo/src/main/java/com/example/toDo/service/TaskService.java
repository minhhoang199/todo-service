package com.example.toDo.service;

import com.example.toDo.model.ResponseObject;
import com.example.toDo.model.Task;
import com.example.toDo.model.dto.TaskDto;
import com.example.toDo.model.enums.State;
import com.example.toDo.model.request.ChangeStatusRequest;
import com.example.toDo.model.request.GetTaskDetailRequest;
import com.example.toDo.repeository.TaskRepository;
import com.example.toDo.utils.ResponseUtil;
import com.example.toDo.utils.TaskTranslator;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TaskService {
    private TaskRepository taskRepository;
    private TaskTranslator taskTranslator;
    private ResponseUtil responseUtil;

    public List<TaskDto> getAll(Integer pageNo, Integer pageSize) {
        //pageNo can be 0
        if (pageNo < 0) {
            throw new RuntimeException("This parameter can not be negative");
        }
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        Page<Task> pageTask = taskRepository.findAll(pageable);
        Page<TaskDto> pageResult = pageTask.map(task -> this.taskTranslator.transferToDto(task));
        return pageResult.toList();
    }

    //Search by Title and date
    public List<TaskDto> getByTitleAndDate(String title, String strDate, Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(strDate, formatter);
        Page<Task> pageTask = taskRepository.findByContentContainingAndDate(title, date, pageable);
        Page<TaskDto> pageResult = pageTask.map(task -> this.taskTranslator.transferToDto(task));
        return pageResult.toList();
    }

    //Search by Title
    public List<TaskDto> getByTitle(String title, Integer pageNo, Integer pageSize) {
        //pageNo can be 0
        if (pageNo < 0) {
            throw new RuntimeException("This parameter can not be negative");
        }
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Task> pageTask = taskRepository.findByContentContaining(title, pageable);
        Page<TaskDto> pageResult = pageTask.map(task -> this.taskTranslator.transferToDto(task));
        return pageResult.toList();
    }


    //Search by date
    public List<TaskDto> getByDate(String strDate, Integer pageNo, Integer pageSize) {
        //pageNo can be 0
        if (pageNo < 0) {
            throw new RuntimeException("This parameter can not be negative");
        }
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(strDate, formatter);
        Page<Task> pageTask = taskRepository.findByDate(date, pageable);
        Page<TaskDto> pageResult = pageTask.map(task -> this.taskTranslator.transferToDto(task));
        return pageResult.toList();
    }

    public boolean addNewTask(TaskDto newTaskDto) {
        newTaskDto.setTaskState(State.ACTIVE);
        taskRepository.save(this.taskTranslator.transferToTask(newTaskDto));
        return true;
    }

    public boolean updateTask(TaskDto updateTask) {
        if (StringUtils.isNotBlank(updateTask.getId())){
            Optional<Task> opTask = taskRepository.findById(updateTask.getId());
            if (opTask.isPresent()) {
                Task oldTask = opTask.get();
                if (StringUtils.isNotBlank(updateTask.getContent())) oldTask.setContent(updateTask.getContent());
                if (updateTask.getDate() != null) oldTask.setDate(updateTask.getDate());
                if (updateTask.getTaskState() != null) oldTask.setTaskState(updateTask.getTaskState());
                taskRepository.save(this.taskTranslator.transferToTask(updateTask));
                return true;
            } else {
                throw new RuntimeException("Not found task");
            }
        } else {
            throw new RuntimeException("Id must not be empty");
        }
    }

    public boolean deleteTask(String id) {
        Optional<Task> opTask = taskRepository.findById(id);
        if (opTask.isPresent()) {
            taskRepository.deleteById(id);
            return true;
        } else {
            throw new RuntimeException("Not found task");
        }
    }

    public boolean changeStatus(ChangeStatusRequest statusRequest) {
        if (CollectionUtils.isEmpty(statusRequest.getListIdsAndState())) {
            throw new RuntimeException("List of id empty !");
        }
        List<Task> taskList = this.taskRepository.findByIds(statusRequest.getListIdsAndState().keySet());
        if (CollectionUtils.isEmpty(taskList)) {
            throw new RuntimeException("Not found any task!");
        }
        for (Task task: taskList) {
            task.setTaskState(statusRequest.getListIdsAndState().get(task.getId()));
        }
        this.taskRepository.saveAll(taskList);
        return true;
    }

    public ResponseObject getById(GetTaskDetailRequest getTaskDetailRequest) {
        Optional<Task> optionalTask = taskRepository.findById(getTaskDetailRequest.getTaskId());
        if (optionalTask.isEmpty()){
            return new ResponseObject("404", "Not found task by Id!", null);
        }
        return new ResponseObject("200", "Success",  this.taskTranslator.transferToDto(optionalTask.get()));
    }
}
