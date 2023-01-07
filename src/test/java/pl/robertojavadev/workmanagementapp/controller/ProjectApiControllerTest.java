package pl.robertojavadev.workmanagementapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.robertojavadev.workmanagementapp.dto.ProjectDto;
import pl.robertojavadev.workmanagementapp.model.Project;
import pl.robertojavadev.workmanagementapp.service.ProjectService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProjectApiControllerTest {

    @MockBean
    private ProjectService projectService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnStatusOkWhenListOfAllProjectsIsEmpty() throws Exception {
        //given

        //when
        MockHttpServletResponse result = mockMvc.perform(get("/api/v1/projects")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse();

        //then
        assertThat(result.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getHeader("message")).contains("no available projects");
    }

    @Test
    void shouldReturnStatusOkAndReturnListOfProjectsCorrectly() throws Exception {
        //given
        List<Project> projects = new ArrayList<>(List.of(
                new Project("Project Java", "Learn programming", Instant.now()),
                new Project("Project sport", "", Instant.now()),
                new Project("Project english", "Learn english vocabulary", Instant.now())
        ));
        given(projectService.getAllProjects()).willReturn(projects);

        //when
        MockHttpServletResponse result = mockMvc.perform(get("/api/v1/projects")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse();

        //then
        assertThat(result.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getHeader("message")).contains("successfully");
    }

    @Test
    void shouldReturnStatusCreatedWhenProjectCreatedCorrectly() throws Exception{
        //given
        ProjectDto project = new ProjectDto("New Project","New project should be make");
        given(projectService.createProject(project)).willReturn(project);

        //when
        ResultActions result = mockMvc.perform(post("/api/v1/projects/")
                .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(objectMapper.writeValueAsString(project))));

        //then
        result.andExpect(status().isCreated());
    }

    @Test
    void shouldReturnThrownAnExceptionWhenProjectNameIsEmpty() throws Exception{
        //given
        ProjectDto project = new ProjectDto("","New project should be make");
        given(projectService.createProject(project)).willReturn(project);

        //when
        ResultActions result = mockMvc.perform(post("/api/v1/projects/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(project))));

        //then
        result.andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnThrownAnExceptionWhenProjectNameHasOnlyWhiteSpaces() throws Exception{
        //given
        ProjectDto project = new ProjectDto("     ","New project should be make");
        given(projectService.createProject(project)).willReturn(project);

        //when
        ResultActions result = mockMvc.perform(post("/api/v1/projects/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(project))));

        //then
        result.andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnThrownAnExceptionWhenProjectNameHasIllegalSize() throws Exception{
        //given
        ProjectDto project = new ProjectDto("NewProjectNameWithName's62signsNewProjectNameWithName's62signs","New project should be make");
        given(projectService.createProject(project)).willReturn(project);

        //when
        ResultActions result = mockMvc.perform(post("/api/v1/projects/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(project))));

        //then
        result.andExpect(status().isBadRequest());
    }
}
