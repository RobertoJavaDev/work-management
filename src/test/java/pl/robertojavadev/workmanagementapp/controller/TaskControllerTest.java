package pl.robertojavadev.workmanagementapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.robertojavadev.workmanagementapp.dto.TaskDto;
import pl.robertojavadev.workmanagementapp.exception.ResourceNotFoundException;
import pl.robertojavadev.workmanagementapp.service.TaskService;

import java.util.Objects;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    private static final UUID ID_OF_TASK_1 = UUID.fromString("ebb006a8-5266-45cc-947e-c436024336a1");

    @MockBean
    private TaskService taskService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnStatusOkWhenGetTaskByIdIsCorrectly() throws Exception {
        // given
        TaskDto task = new TaskDto(ID_OF_TASK_1, "Learn Spring Boot", null);
        when(taskService.getTaskById(task.getId())).thenReturn(task);

        // when
        ResultActions result = mockMvc.perform(get("/api/v1/tasks/{id}", ID_OF_TASK_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(task))));

        // then
        result.andExpect(status().isOk());
    }

    @Test
    void shouldReturnStatusNotFoundWhenTaskDoesNotExist() throws Exception {
        // given
        TaskDto task = new TaskDto(ID_OF_TASK_1, "Learn Spring Boot", null);
        when(taskService.getTaskById(any())).thenThrow(ResourceNotFoundException.class);

        // when
        ResultActions result = mockMvc.perform(get("/api/v1/tasks/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(task))));

        // then
        result.andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnStatusCreatedWhenTaskCreatedCorrectly() throws Exception {
        // given
        TaskDto task = new TaskDto(ID_OF_TASK_1, "Learn Spring Boot", null);
        when(taskService.createTask(task)).thenReturn(task);

        // when
        ResultActions result = mockMvc.perform(post("/api/v1/tasks/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(task))));

        // then
        result.andExpect(status().isCreated());
    }

    @Test
    void shouldThrowAnExceptionWhenTaskDescriptionIsEmpty() throws Exception {
        // given
        TaskDto task = new TaskDto(ID_OF_TASK_1, "", null);
        given(taskService.createTask(task)).willReturn(new TaskDto(ID_OF_TASK_1, "", null));

        // when
        ResultActions result = mockMvc.perform(post("/api/v1/tasks/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(task))));

        // then
        result.andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowAnExceptionWhenTaskDescriptionHasWhiteSpaces() throws Exception {
        // given
        TaskDto task = new TaskDto(ID_OF_TASK_1, "    ", null);
        given(taskService.createTask(task)).willReturn(new TaskDto(ID_OF_TASK_1, "    ", null));

        // when
        ResultActions result = mockMvc.perform(post("/api/v1/tasks/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(task))));

        // then
        result.andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnStatusOkWhenTaskUpdatedCorrectly() throws Exception {
        // given
        TaskDto task = new TaskDto(ID_OF_TASK_1, "Learn Spring Boot", null);
        TaskDto updateTask = new TaskDto(ID_OF_TASK_1, "Learn Java", null);

        when(taskService.updateTask(ID_OF_TASK_1, task)).thenReturn(updateTask);

        // when
        ResultActions result = mockMvc.perform(put("/api/v1/tasks/" + ID_OF_TASK_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(updateTask))));

        // then
        result.andExpect(status().isOk());
    }

    @Test
    void shouldReturnStatusNotFoundWhenTaskUpdatedDoesNotExist() throws Exception {
        // given
        TaskDto updateTask = new TaskDto(ID_OF_TASK_1, "Learn Java", null);

        when(taskService.updateTask(any(), any())).thenThrow(ResourceNotFoundException.class);

        // when
        ResultActions result = mockMvc.perform(put("/api/v1/tasks/" + ID_OF_TASK_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(updateTask))));

        // then
        result.andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnStatusOkWhenToggleTaskCorrectly() throws Exception {
        // given
        TaskDto task = new TaskDto(ID_OF_TASK_1, "Learn Spring Boot", null);
        TaskDto toggleTask = new TaskDto(ID_OF_TASK_1, "Learn Java Boot", true, null);

        when(taskService.toggleTask(task.getId())).thenReturn(toggleTask);

        // when
        ResultActions result = mockMvc.perform(patch("/api/v1/tasks/" + ID_OF_TASK_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(toggleTask))));

        // then
        result.andExpect(status().isOk());
    }

    @Test
    void shouldReturnStatusNotFoundWhenToggleTaskDoesNotExist() throws Exception {
        // given
        TaskDto toggleTask = new TaskDto(ID_OF_TASK_1, "Learn Java", null);

        when(taskService.toggleTask(any())).thenThrow(ResourceNotFoundException.class);

        // when
        ResultActions result = mockMvc.perform(patch("/api/v1/tasks/" + ID_OF_TASK_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(toggleTask))));

        // then
        result.andExpect(status().isNotFound());
    }
}
