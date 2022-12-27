package pl.robertojavadev.workmanagementapp.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.robertojavadev.workmanagementapp.dto.TaskDto;
import pl.robertojavadev.workmanagementapp.dto.TaskMapper;
import pl.robertojavadev.workmanagementapp.exception.ResourceNotFoundException;
import pl.robertojavadev.workmanagementapp.model.Task;
import pl.robertojavadev.workmanagementapp.repository.TaskRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
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
}
