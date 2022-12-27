package pl.robertojavadev.workmanagementapp.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import pl.robertojavadev.workmanagementapp.dto.TaskDto;
import pl.robertojavadev.workmanagementapp.dto.TaskMapper;
import pl.robertojavadev.workmanagementapp.exception.ResourceNotFoundException;
import pl.robertojavadev.workmanagementapp.model.Task;
import pl.robertojavadev.workmanagementapp.repository.TaskRepository;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Validated
public class TaskService {

    private final TaskRepository taskRepository;

    private final TaskMapper taskMapper;

    public List<Task> getAllTasks() {

        return taskRepository.findAll();
    }

    public TaskDto getTaskById(UUID id) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent()) {
            return taskMapper.mapTaskEntityToTaskDto(task.get());
        } else {
            throw new ResourceNotFoundException("Task with that id doesn't exist");
        }
    }

    public TaskDto createTask(@Valid TaskDto taskRequest) {

        Task task = new Task();
        task.setDescription(taskRequest.getDescription());
        task.setDeadline(taskRequest.getDeadline());

        return taskMapper.mapTaskEntityToTaskDto(taskRepository.save(task));
    }

    public TaskDto updateTask(@Valid UUID id, TaskDto taskRequest) {

        Task task = taskRepository.findById(id).orElseThrow(() -> {

            throw new ResourceNotFoundException("Task with that id doesn't exist");
        });

        task.setDescription(taskRequest.getDescription());
        task.setDeadline(taskRequest.getDeadline());

        return taskMapper.mapTaskEntityToTaskDto(taskRepository.save(task));
    }
}