package pl.robertojavadev.workmanagementapp.service;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import pl.robertojavadev.workmanagementapp.dto.TaskDto;
import pl.robertojavadev.workmanagementapp.dto.TaskMapper;
import pl.robertojavadev.workmanagementapp.exception.ResourceNotFoundException;
import pl.robertojavadev.workmanagementapp.model.Task;
import pl.robertojavadev.workmanagementapp.repository.TaskRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
public class TaskServiceTest {

    private static final UUID ID_OF_TASK_1 = UUID.fromString("ebb006a8-5266-45cc-947e-c436024336a1");
    private static final UUID ID_OF_TASK_2 = UUID.fromString("bf111be8-2c68-47fb-a8a6-1833106a6cb0");
    private static final UUID ID_OF_TASK_3 = UUID.fromString("beb95a1b-3ac8-401d-869d-ba8fa73c090e");
    private static final String description1 = "Learn Java programming";
    private static final String description2 = "Write a simple application";
    private static final String description3 = "Read a book about programing";

    @Autowired
    private TaskService taskService;

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private TaskMapper taskMapper;

    @Test
    void shouldReturnAllTasks() {
        // given
        List<Task> taskList = List.of(
                new Task(ID_OF_TASK_1, description1, null),
                new Task(ID_OF_TASK_2, description2, null),
                new Task(ID_OF_TASK_3, description3, null)
        );

        // when
        when(taskRepository.findAll()).thenReturn(taskList);
        List<TaskDto> allTasks = taskService.getAllTasks();

        // then
        assertThat(allTasks, hasSize(taskList.size()));
    }

    @Test
    void shouldReturnTaskByIdCorrectly() {
        // given
        Task task = new Task(ID_OF_TASK_3, description3, null);
        TaskDto expectedTask = new TaskDto(ID_OF_TASK_3, description3, null);

        // when
        when(taskRepository.findById(ID_OF_TASK_3)).thenReturn(Optional.of(task));
        when(taskMapper.mapTaskEntityToTaskDto(task)).thenReturn(expectedTask);
        TaskDto actualTask = taskService.getTaskById(ID_OF_TASK_3);

        // then
        assertEquals(expectedTask, actualTask);
    }

    @Test
    void shouldThrowAnExceptionWhenTaskDoesNotExist() {
        // given
        Task task = new Task(ID_OF_TASK_3, description3, null);

        // when
        given(taskRepository.findById(any())).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> taskService.getTaskById(ID_OF_TASK_1))
                .isInstanceOf(ResourceNotFoundException.class);
        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> taskService.getTaskById(ID_OF_TASK_1))
                .withMessageContaining("doesn't exist");
    }

    @Test
    void shouldCreatedTaskCorrectly() {
        // given
        Task task = new Task(ID_OF_TASK_1, description1, null);
        TaskDto createdTask = new TaskDto(ID_OF_TASK_1, description1, null);
        TaskDto taskDto = new TaskDto(ID_OF_TASK_1, description1, null);

        // when
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(taskMapper.mapTaskEntityToTaskDto(task)).thenReturn(taskDto);
        TaskDto returnTask = taskService.createTask(createdTask);

        // then
        assertEquals(task.getId(), returnTask.getId());
        assertEquals(task.getDescription(), returnTask.getDescription());
    }

    @Test
    void shouldThrowAnExceptionWhenDescriptionIsEmpty() {
        // given
        Task task = new Task(ID_OF_TASK_1, "", null);
        TaskDto createdTask = new TaskDto(ID_OF_TASK_1, "", null);
        TaskDto taskDto = new TaskDto(ID_OF_TASK_1, "", null);

        // when
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(taskMapper.mapTaskEntityToTaskDto(task)).thenReturn(taskDto);

        // then
        assertThrows(ConstraintViolationException.class, () -> taskService.createTask(createdTask));
    }

    @Test
    void shouldThrowAnExceptionWhenDescriptionHasWhiteSpaces() {
        // given
        Task task = new Task(ID_OF_TASK_1, "    ", null);
        TaskDto createdTask = new TaskDto(ID_OF_TASK_1, "    ", null);
        TaskDto taskDto = new TaskDto(ID_OF_TASK_1, "    ", null);

        // when
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(taskMapper.mapTaskEntityToTaskDto(task)).thenReturn(taskDto);

        // then
        assertThrows(ConstraintViolationException.class, () -> taskService.createTask(createdTask));
    }

    @Test
    void shouldReturnUpdatedTaskCorrectly() {
        // given
        Task task = new Task(ID_OF_TASK_1, description1, null);
        TaskDto updatedTask = new TaskDto(ID_OF_TASK_1, "New description", null);

        // when
        when(taskRepository.findById(ID_OF_TASK_1)).thenReturn(Optional.of(task));
        when(taskMapper.mapTaskEntityToTaskDto(Mockito.any(Task.class))).thenReturn(updatedTask);
        TaskDto taskDto = taskService.updateTask(ID_OF_TASK_1, updatedTask);

        // then
        assertEquals(task.getDescription(), updatedTask.getDescription());
    }

    @Test
    void shouldThrowAnExceptionWhenUpdatedTaskDoesNotExist() {
        // given
        TaskDto task = new TaskDto(ID_OF_TASK_1, description1, null);

        // when
        given(taskRepository.findById(any())).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> taskService.updateTask(ID_OF_TASK_1, task))
                .isInstanceOf(ResourceNotFoundException.class);
        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> taskService.updateTask(ID_OF_TASK_1, task))
                .withMessageContaining("doesn't exist");
    }

    @Test
    void shouldThrowAnExceptionWhenToggleTaskDoesNotExist() {
        // given
        TaskDto task = new TaskDto(ID_OF_TASK_1, description1, null);

        // when
        given(taskRepository.findById(any())).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> taskService.toggleTask(ID_OF_TASK_1))
                .isInstanceOf(ResourceNotFoundException.class);
        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> taskService.toggleTask(ID_OF_TASK_1))
                .withMessageContaining("doesn't exist");
    }
}
