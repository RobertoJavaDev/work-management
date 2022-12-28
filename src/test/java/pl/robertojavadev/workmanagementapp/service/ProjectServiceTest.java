package pl.robertojavadev.workmanagementapp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import pl.robertojavadev.workmanagementapp.model.Project;
import pl.robertojavadev.workmanagementapp.repository.ProjectRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
public class ProjectServiceTest {

    @Autowired
    private ProjectService projectService;

    @MockBean
    private ProjectRepository projectRepository;

    @Test
    void shouldReturnAllProjects() {
        //given
        List<Project> projects = new ArrayList<>(List.of(
                new Project("Project Java", Instant.now()),
                new Project("Project sport", Instant.now()),
                new Project("Project english", Instant.now())
        ));

        //when
        when(projectRepository.findAll()).thenReturn(projects);
        List<Project> allProjects = projectService.getAllProjects();

        //then
        assertThat(allProjects, hasSize(projects.size()));
    }
}
