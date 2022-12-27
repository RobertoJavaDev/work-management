package pl.robertojavadev.workmanagementapp.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.robertojavadev.workmanagementapp.dto.TaskDto;
import pl.robertojavadev.workmanagementapp.model.Task;
import pl.robertojavadev.workmanagementapp.service.TaskService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(final TaskService taskService) {
        this.taskService = taskService;
    }

    @Transactional
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {

        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @Transactional
    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTaskById(@NotNull @PathVariable UUID id) {

        TaskDto taskDto = taskService.getTaskById(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("message", "The task has been successfully retrieved");

        return new ResponseEntity<>(taskDto, headers, HttpStatus.OK);
    }

    @Transactional
    @PostMapping
    public ResponseEntity<TaskDto> createTask(@Valid @RequestBody TaskDto taskRequest) {

        TaskDto taskDto = taskService.createTask(taskRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.add("message", "The task has been successfully created");

        return new ResponseEntity<>(taskDto, headers, HttpStatus.CREATED);
    }

    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<TaskDto> updateTask(@Valid @PathVariable UUID id, @RequestBody TaskDto taskRequest) {

        TaskDto taskDto = taskService.updateTask(id, taskRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.add("message", "The task has been successfully updated");

        return new ResponseEntity<>(taskDto, headers, HttpStatus.OK);
    }
}
