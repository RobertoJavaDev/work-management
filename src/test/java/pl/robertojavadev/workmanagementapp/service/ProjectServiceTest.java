package pl.robertojavadev.workmanagementapp.service;

import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import pl.robertojavadev.workmanagementapp.dto.ProjectDto;
import pl.robertojavadev.workmanagementapp.dto.ProjectMapper;
import pl.robertojavadev.workmanagementapp.exception.ResourceNotDeletedException;
import pl.robertojavadev.workmanagementapp.exception.ResourceNotFoundException;
import pl.robertojavadev.workmanagementapp.model.Project;
import pl.robertojavadev.workmanagementapp.repository.ProjectRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
public class ProjectServiceTest {

    public static final UUID ID_1 = UUID.fromString("0175650a-a076-11ed-a8fc-0242ac120002");
    private static final String PROJECT_NAME_1 = "Project Java";
    private static final String PROJECT_NAME_2 = "Project sport";
    private static final String PROJECT_NAME_3 = "Project english";
    private static final String EMPTY_PROJECT_NAME = "";
    private static final String WHITE_SPACES_PROJECT_NAME = "       ";
    public static final String DESCRIPTION_1 = "Learn programming";
    public static final String DESCRIPTION_2 = "Learn english vocabulary";
    public static final String EMPTY_DESCRIPTION = "";
    public static final String DESCRIPTION_IS_TOO_LONG = "Name of description is too long - more than 255 letters" +
            " Name of description is too long - more than 255 letters." +
            " Name of description is too long - more than 255 letters" +
            " Name of description is too long - more than 255 letters" +
            " Name of description is too long - more than 255 letters" +
            " Name of description is too long - more than 255 letters" +
            " Name of description is too long - more than 255 letters";

    @Autowired
    private ProjectService projectService;

    @MockBean
    private ProjectRepository projectRepository;

    @MockBean
    private ProjectMapper projectMapper;

    @Test
    void shouldReturnAllProjects() {
        // given
        List<Project> projects = new ArrayList<>(List.of(
                new Project(PROJECT_NAME_1, DESCRIPTION_1, Instant.now()),
                new Project(PROJECT_NAME_2, EMPTY_DESCRIPTION, Instant.now()),
                new Project(PROJECT_NAME_3, DESCRIPTION_2, Instant.now())
        ));

        // when
        when(projectRepository.findAll()).thenReturn(projects);
        List<ProjectDto> allProjects = projectService.getAllProjects();

        // then
        assertThat(allProjects, hasSize(projects.size()));
    }

    @Test
    void shouldCreatedProjectCorrectly() {
        // given
        Project project = new Project(PROJECT_NAME_1, DESCRIPTION_1, Instant.now());
        ProjectDto createdProject = new ProjectDto(PROJECT_NAME_1, DESCRIPTION_1);
        ProjectDto projectDto = new ProjectDto(PROJECT_NAME_1, DESCRIPTION_1);

        // when
        when(projectRepository.save(any(Project.class))).thenReturn(project);
        when(projectMapper.mapProjectEntityToProjectDto(project)).thenReturn(projectDto);
        ProjectDto returnProject = projectService.createProject(createdProject);

        // then
        assertEquals(project.getName(), returnProject.getName());
        assertEquals(project.getDescription(), returnProject.getDescription());
    }

    @Test
    void shouldThrowAnExceptionWhenNameIsEmpty() {
        // given
        Project project = new Project(EMPTY_PROJECT_NAME, DESCRIPTION_1, Instant.now());
        ProjectDto createdProject = new ProjectDto(EMPTY_PROJECT_NAME, DESCRIPTION_1);
        ProjectDto projectDto = new ProjectDto(EMPTY_PROJECT_NAME, DESCRIPTION_1);

        // when
        when(projectRepository.save(any(Project.class))).thenReturn(project);
        when(projectMapper.mapProjectEntityToProjectDto(project)).thenReturn(projectDto);

        // then
        assertThrows(ConstraintViolationException.class, () -> projectService.createProject(createdProject));
    }

    @Test
    void shouldThrowAnExceptionWhenNameHasWhiteSpaces() {
        // given
        Project project = new Project(WHITE_SPACES_PROJECT_NAME, DESCRIPTION_1, Instant.now());
        ProjectDto createdProject = new ProjectDto(WHITE_SPACES_PROJECT_NAME, DESCRIPTION_1);
        ProjectDto projectDto = new ProjectDto(WHITE_SPACES_PROJECT_NAME, DESCRIPTION_1);

        // when
        when(projectRepository.save(any(Project.class))).thenReturn(project);
        when(projectMapper.mapProjectEntityToProjectDto(project)).thenReturn(projectDto);

        // then
        assertThrows(ConstraintViolationException.class, () -> projectService.createProject(createdProject));
    }

    @Test
    void shouldDeletedProjectCorrectly() throws ResourceNotDeletedException {
        // given
        Project project = new Project(PROJECT_NAME_1, DESCRIPTION_1, Instant.now());
        project.setId(ID_1);

        // when
        when(projectRepository.existsById(ID_1)).thenReturn(true);
        projectService.deleteProject(ID_1);

        // then
        verify(projectRepository).deleteById(project.getId());
    }

    @Test
    void shouldThrowExceptionWhenProjectWithIdDoesNotExist() {
        // given
        Project project = new Project(PROJECT_NAME_1, DESCRIPTION_1, Instant.now());
        project.setId(ID_1);

        // when
        given(projectRepository.findById(Mockito.any())).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> projectService.deleteProject(UUID.randomUUID()))
                .isInstanceOf(ResourceNotDeletedException.class);
        assertThatExceptionOfType(ResourceNotDeletedException.class)
                .isThrownBy(() -> projectService.deleteProject(UUID.randomUUID()))
                .withMessageContaining("does not exist");
    }

    @Test
    void shouldUpdatedProjectCorrectly() {
        // given
        ProjectDto updatedProjectDto = new ProjectDto(PROJECT_NAME_2, DESCRIPTION_2);
        Project project = new Project(PROJECT_NAME_1, DESCRIPTION_1, Instant.now());
        project.setId(ID_1);
        ProjectDto newProjectDto = new ProjectDto(PROJECT_NAME_2, DESCRIPTION_2);
        newProjectDto.setId(ID_1);

        // when
        when(projectRepository.findById(ID_1))
                .thenReturn(Optional.of(project));
        when(projectMapper.mapProjectEntityToProjectDto(project))
                .thenReturn(newProjectDto);
        ProjectDto projectDto = projectService.updateProject(ID_1, updatedProjectDto);

        // then
        Assertions.assertThat(projectDto.getName()).isEqualTo(updatedProjectDto.getName());
        Assertions.assertThat(projectDto.getDescription()).isEqualTo(updatedProjectDto.getDescription());
    }

    @Test
    void shouldThrowAnExceptionWhenUpdatedProjectWithIdDoesNotExist() {
        // given
        ProjectDto updatedProjectDto = new ProjectDto(PROJECT_NAME_2, DESCRIPTION_2);
        updatedProjectDto.setId(ID_1);

        // when
        given(projectRepository.findById(Mockito.any())).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> projectService.updateProject(UUID.randomUUID(), updatedProjectDto))
                .isInstanceOf(ResourceNotFoundException.class);
        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> projectService.updateProject(UUID.randomUUID(), updatedProjectDto))
                .withMessageContaining("does not exist");
    }

    @Test
    void shouldThrowAnExceptionWhenUpdatedNameIsEmpty() {
        // given
        ProjectDto updatedProjectDto = new ProjectDto(EMPTY_PROJECT_NAME, DESCRIPTION_2);

        // when

        // then
        assertThrows(ConstraintViolationException.class, () -> projectService.updateProject(ID_1, updatedProjectDto));
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(()->projectService.updateProject(UUID.randomUUID(), updatedProjectDto))
                .withMessageContaining("Project's name must not be empty");
    }

    @Test
    void shouldThrowAnExceptionWhenUpdatedNameHasWhiteSpaces() {
        // given
        ProjectDto updatedProjectDto = new ProjectDto(WHITE_SPACES_PROJECT_NAME, DESCRIPTION_2);

        // when

        // then
        assertThrows(ConstraintViolationException.class, () -> projectService.updateProject(ID_1, updatedProjectDto));
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(()->projectService.updateProject(UUID.randomUUID(), updatedProjectDto))
                .withMessageContaining("Project's name must not be empty");
    }

    @Test
    void shouldThrowAnExceptionWhenUpdatedProjectDescriptionIsTooLong() {
        // given
        ProjectDto updatedProjectDto = new ProjectDto(PROJECT_NAME_1, DESCRIPTION_IS_TOO_LONG);

        // when

        // then
        assertThrows(ConstraintViolationException.class, () -> projectService.updateProject(ID_1, updatedProjectDto));
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(()->projectService.updateProject(UUID.randomUUID(), updatedProjectDto))
                .withMessageContaining("Description's size must be between 0 and 255");
    }
}
