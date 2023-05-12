package pl.robertojavadev.workmanagementapp.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pl.robertojavadev.workmanagementapp.dto.TaskDto;
import pl.robertojavadev.workmanagementapp.dto.TaskMapper;
import pl.robertojavadev.workmanagementapp.exception.ResourceNotFoundException;
import pl.robertojavadev.workmanagementapp.model.Task;
import pl.robertojavadev.workmanagementapp.repository.TaskRepository;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Validated
public class TaskService {

    private final TaskRepository taskRepository;

    private final TaskMapper taskMapper;

    @Transactional(readOnly = true)
    public List<TaskDto> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(taskMapper::mapTaskEntityToTaskDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public TaskDto getTaskById(@NotNull UUID id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> {
            throw new ResourceNotFoundException("Task with Id: " + id + " does not exist.");
        });
        return taskMapper.mapTaskEntityToTaskDto(task);
    }

    @Transactional
    public TaskDto createTask(@Valid TaskDto taskRequest) {

        Task task = new Task();
        task.setDescription(taskRequest.getDescription());
        task.setDeadline(taskRequest.getDeadline());

        return taskMapper.mapTaskEntityToTaskDto(task);
    }

    @Transactional
    public TaskDto updateTask(@NotNull UUID id, @Valid TaskDto taskRequest) {

        Task task = taskRepository.findById(id).orElseThrow(() -> {

            throw new ResourceNotFoundException("Task with Id: " + id + " does not exist.");
        });

        task.setDescription(taskRequest.getDescription());
        task.setDeadline(taskRequest.getDeadline());

        return taskMapper.mapTaskEntityToTaskDto(task);
    }

    @Transactional
    public TaskDto toggleTask(@NotNull UUID id) {

        Task task = taskRepository.findById(id).orElseThrow(() -> {
            throw new ResourceNotFoundException("Task with Id: " + id + " does not exist.");
        });

        task.setDone(!task.isDone());

        return taskMapper.mapTaskEntityToTaskDto(task);
    }
}
