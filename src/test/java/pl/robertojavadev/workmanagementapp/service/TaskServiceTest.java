package pl.robertojavadev.workmanagementapp.service;

import org.junit.jupiter.api.Assertions;
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

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
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
        //given
        List<Task> taskList = List.of(
                new Task(ID_OF_TASK_1, description1, null),
                new Task(ID_OF_TASK_2, description2, null),
                new Task(ID_OF_TASK_3, description3, null)
        );

        //when
        when(taskRepository.findAll()).thenReturn(taskList);
        List<Task> allTasks = taskService.getAllTasks();

        //then
        assertThat(allTasks, hasSize(taskList.size()));
    }

    @Test
    void shouldReturnTaskByIdCorrectly() {
        //given
        Task task = new Task(ID_OF_TASK_3, description3, null);
        TaskDto expectedTask = new TaskDto(ID_OF_TASK_3, description3, null);

        //when
        when(taskRepository.findById(ID_OF_TASK_3)).thenReturn(Optional.of(task));
        when(taskMapper.mapTaskEntityToTaskDto(task)).thenReturn(expectedTask);
        TaskDto actualTask = taskService.getTaskById(ID_OF_TASK_3);

        //then
        Assertions.assertEquals(expectedTask, actualTask);
    }

    @Test
    void shouldThrowAnExceptionWhenTaskDoesNotExist() {
        //given
        Task task = new Task(ID_OF_TASK_3, description3, null);

        //when
        given(taskRepository.findById(Mockito.any())).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> taskService.getTaskById(ID_OF_TASK_1))
                .isInstanceOf(ResourceNotFoundException.class);
        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> taskService.getTaskById(ID_OF_TASK_1))
                .withMessageContaining("doesn't exist");
    }
}
