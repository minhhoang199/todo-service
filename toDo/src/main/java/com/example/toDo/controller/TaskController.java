package com.example.toDo.controller;

import com.example.toDo.model.ResponseObject;
import com.example.toDo.model.dto.TaskDto;
import com.example.toDo.model.request.ChangeStatusRequest;
import com.example.toDo.model.request.GetTaskDetailRequest;
import com.example.toDo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

//    @GetMapping("")
//    public ResponseEntity<Object> getAll(
//            @RequestParam(value = "pageNo") Optional<Integer> pageNo,
//            @RequestParam(value = "pageSize") Optional<Integer> pageSize
//    ){
//        try {
//            List<Task> taskList = taskService.getAll(pageNo.orElse(0), pageSize.orElse(10));
//            return ResponseEntity.ok(taskList);
//        } catch (RuntimeException e){
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//
//    }
    @PutMapping("/detail")
    public ResponseEntity<ResponseObject> getDetail(@RequestBody GetTaskDetailRequest getTaskDetailRequest){
        return ResponseEntity.ok(this.taskService.getById(getTaskDetailRequest));
    }

    @GetMapping("")
    public ResponseEntity<ResponseObject> getByConditions(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "date", required = false) String date,
            @RequestParam(value = "pageNo") Optional<Integer> pageNo,
            @RequestParam(value = "pageSize") Optional<Integer> pageSize
    ){
        try {
            //Not input title condition search
            if (title == null && date != null){
                List<TaskDto> taskList = taskService.getByDate(date, pageNo.orElse(0), pageSize.orElse(10));
                return ResponseEntity.ok(new ResponseObject("201", "Success", taskList));
            }
            //Not input date condition search
            else if (title != null && date == null){
                List<TaskDto> taskList = taskService.getByTitle(title, pageNo.orElse(0), pageSize.orElse(10));
                return ResponseEntity.ok(new ResponseObject("201", "Success", taskList));
            }  // Input both search conditions
            else if (title != null && date != null){
                List<TaskDto> taskList = taskService.getByTitleAndDate(title, date, pageNo.orElse(0), pageSize.orElse(10));
                return ResponseEntity.ok(new ResponseObject("201", "Success", taskList));
            }//Not input both search conditions
            else {
                List<TaskDto> taskList = taskService.getAll(pageNo.orElse(0), pageSize.orElse(10));
                return ResponseEntity.ok(new ResponseObject("201", "Success", taskList));
            }
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(new ResponseObject("400", e.getMessage(), null));
        }
    }

    //Define 202 is "Create success"
    @PostMapping
    public ResponseEntity<ResponseObject> addNewTask(@Valid @RequestBody TaskDto newTask){
        try {
            var isCreated = taskService.addNewTask(newTask);
            if (isCreated) {
                return ResponseEntity.ok().body(new ResponseObject("202", "Create success", null));
            } else {
                return ResponseEntity.internalServerError().body(new ResponseObject("500", "Internal server error", null));
            }
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(new ResponseObject("400", e.getMessage(), null));
        }
    }


    //Define 203 is "Update success"
    @PatchMapping()
    public ResponseEntity<ResponseObject> updateTask(
            @Valid @RequestBody TaskDto newTask){
        try {
            var isUpdated = taskService.updateTask(newTask);
            if (isUpdated) {
                return ResponseEntity.ok().body(new ResponseObject("203", "Update success", null));
            } else {
                return ResponseEntity.internalServerError().body(new ResponseObject("500", "Internal server error", null));
            }
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(new ResponseObject("400", e.getMessage(), null));
        }
    }

    //Define 204 is "Delete success"
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteTask(@PathVariable("id") String id){
        try {
            var isDeleted = this.taskService.deleteTask(id);
            if (isDeleted) {
                return ResponseEntity.ok().body(new ResponseObject("204", "Delete success", null));
            } else {
                return ResponseEntity.internalServerError().body(new ResponseObject("500", "Internal server error", null));
            }
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(new ResponseObject("400", e.getMessage(), null));
        }
    }


    @PatchMapping("/changeStatus")
    public ResponseEntity<ResponseObject> changeStatusTasks(
            @RequestBody ChangeStatusRequest statusRequest){
        try {
            var isUpdated = taskService.changeStatus(statusRequest);
            if (isUpdated) {
                return ResponseEntity.ok().body(new ResponseObject("205", "Change status success", null));
            } else {
                return ResponseEntity.internalServerError().body(new ResponseObject("500", "Internal server error", null));
            }
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(new ResponseObject("400", e.getMessage(), null));
        }
    }
}
