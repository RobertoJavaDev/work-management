package pl.robertojavadev.workmanagementapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.robertojavadev.workmanagementapp.model.Task;
import pl.robertojavadev.workmanagementapp.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(final TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(){

        return ResponseEntity.ok(taskService.getAllTasks());
    }
}
