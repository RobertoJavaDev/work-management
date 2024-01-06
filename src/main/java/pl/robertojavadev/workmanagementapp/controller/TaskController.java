package pl.robertojavadev.workmanagementapp.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.robertojavadev.workmanagementapp.dto.TaskDto;
import pl.robertojavadev.workmanagementapp.service.TaskService;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<TaskDto>> getAllTasks() {

        List<TaskDto> tasksDtoList = taskService.getAllTasks();
        HttpHeaders headers = new HttpHeaders();

        if (tasksDtoList.isEmpty()) {
            headers.add("message", "There are no available tasks to view");
        } else {
            headers.add("message", "The all tasks has been successfully retrieved");
        }

        return new ResponseEntity<>(tasksDtoList, headers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTaskById(@NotNull @PathVariable UUID id) {

        TaskDto taskDto = taskService.getTaskById(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("message", "The task has been successfully retrieved");

        return new ResponseEntity<>(taskDto, headers, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TaskDto> createTask(@Valid @RequestBody TaskDto taskRequest) {

        TaskDto taskDto = taskService.createTask(taskRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.add("message", "The task has been successfully created");

        return new ResponseEntity<>(taskDto, headers, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> updateTask(@Valid @PathVariable UUID id, @RequestBody TaskDto taskRequest) {

        TaskDto taskDto = taskService.updateTask(id, taskRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.add("message", "The task has been successfully updated");

        return new ResponseEntity<>(taskDto, headers, HttpStatus.OK);
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<TaskDto> toggleTask(@Valid @PathVariable UUID id){

        TaskDto taskDto = taskService.toggleTask(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("message", "The task has been successfully toggled");

        return new ResponseEntity<>(taskDto,headers,HttpStatus.OK);
    }
}
