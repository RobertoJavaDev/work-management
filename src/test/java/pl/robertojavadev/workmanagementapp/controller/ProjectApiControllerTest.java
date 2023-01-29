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
import pl.robertojavadev.workmanagementapp.exception.ResourceConstraintViolationException;
import pl.robertojavadev.workmanagementapp.exception.ResourceNotFoundException;
import pl.robertojavadev.workmanagementapp.model.Project;
import pl.robertojavadev.workmanagementapp.service.ProjectService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProjectApiControllerTest {

    public static final String DESCRIPTION_IS_TOO_LONG = "Name of description is too long - more than 255 letters" +
            " Name of description is too long - more than 255 letters." +
            " Name of description is too long - more than 255 letters" +
            " Name of description is too long - more than 255 letters" +
            " Name of description is too long - more than 255 letters" +
            " Name of description is too long - more than 255 letters" +
            " Name of description is too long - more than 255 letters";

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
    void shouldReturnStatusCreatedWhenProjectCreatedCorrectly() throws Exception {
        //given
        ProjectDto project = new ProjectDto("New Project", "New project should be make");
        given(projectService.createProject(project)).willReturn(project);

        //when
        ResultActions result = mockMvc.perform(post("/api/v1/projects/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(project))));

        //then
        result.andExpect(status().isCreated());
    }

    @Test
    void shouldReturnThrownAnExceptionWhenProjectNameIsEmpty() throws Exception {
        //given
        ProjectDto project = new ProjectDto("", "New project should be make");
        given(projectService.createProject(project)).willReturn(project);

        //when
        ResultActions result = mockMvc.perform(post("/api/v1/projects/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(project))));

        //then
        result.andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnThrownAnExceptionWhenProjectNameHasOnlyWhiteSpaces() throws Exception {
        //given
        ProjectDto project = new ProjectDto("     ", "New project should be make");
        given(projectService.createProject(project)).willReturn(project);

        //when
        ResultActions result = mockMvc.perform(post("/api/v1/projects/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(project))));

        //then
        result.andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnThrownAnExceptionWhenProjectNameHasIllegalSize() throws Exception {
        //given
        ProjectDto project = new ProjectDto("NewProjectNameWithName's62signsNewProjectNameWithName's62signs", "New project should be make");
        given(projectService.createProject(project)).willReturn(project);

        //when
        ResultActions result = mockMvc.perform(post("/api/v1/projects/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(project))));

        //then
        result.andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnStatusOkWhenProjectDeletedCorrectly() throws Exception {
        //given
        ProjectDto projectDto = new ProjectDto("Test project", "Description");

        //when
        ResultActions result = mockMvc.perform(delete("/api/v1/projects/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(projectDto))));

        //then
        result.andExpect(status().isOk());
    }

    @Test
    void shouldReturnStatusOkWhenProjectUpdatedCorrectly() throws Exception {
        //given
        ProjectDto oldProject = new ProjectDto("Old name", "Old description");
        ProjectDto changeProject = new ProjectDto("New name", "New description");
        UUID id = UUID.randomUUID();
        given(projectService.updateProject(id, oldProject)).willReturn(changeProject);

        //when
        ResultActions result = mockMvc.perform(patch("/api/v1/projects/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(changeProject))));

        //then
        result.andExpect(status().isOk());
    }

    @Test
    void shouldReturnStatusNotFoundWhenProjectDoesNotExist() throws Exception {
        //given
        ProjectDto updateProject = new ProjectDto("Old name", "Old description");
        UUID id = UUID.randomUUID();
        when(projectService.updateProject(id, updateProject)).thenThrow(ResourceNotFoundException.class);

        //when
        ResultActions result = mockMvc.perform(patch("/api/v1/projects/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(updateProject))));

        //then
        result.andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnStatusBadRequestWhenUpdatedProjectHasEmptyName() throws Exception {
        //given
        ProjectDto oldProject = new ProjectDto("Old name", "Old description");
        ProjectDto updateProject = new ProjectDto("", "Old description");
        UUID id = UUID.randomUUID();
        given(projectService.updateProject(id, oldProject)).willReturn(updateProject);

        //when
        ResultActions result = mockMvc.perform(patch("/api/v1/projects/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(updateProject))));

        //then
        result.andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnStatusBadRequestWhenUpdatedProjectDescriptionIsTooLong() throws Exception{
        //given
        ProjectDto oldProject = new ProjectDto("Name", "Old description");
        ProjectDto updateProject = new ProjectDto("Name", DESCRIPTION_IS_TOO_LONG);
        UUID id = UUID.randomUUID();
        given(projectService.updateProject(id, oldProject))
                .willReturn(updateProject);

        //when
        ResultActions result = mockMvc.perform(patch("/api/v1/projects/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(updateProject))));

        //then
        result.andExpect(status().isBadRequest());
    }
}
