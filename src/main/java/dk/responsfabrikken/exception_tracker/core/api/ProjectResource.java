package dk.responsfabrikken.exception_tracker.core.api;

import dk.responsfabrikken.exception_tracker.core.model.client.ProjectDto;
import dk.responsfabrikken.exception_tracker.core.model.client.UserDto;
import dk.responsfabrikken.exception_tracker.core.model.server.Project;
import dk.responsfabrikken.exception_tracker.core.model.server.ProjectRepository;
import dk.responsfabrikken.exception_tracker.core.model.server.User;
import dk.responsfabrikken.exception_tracker.core.model.server.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api/projects")
public class ProjectResource {

    @Autowired ProjectRepository projectRepository;
    @Autowired UserRepository userRepository;

    @RequestMapping
    public List<ProjectDto> list(HttpServletRequest request) {
        UserDto userDto = (UserDto) request.getSession().getAttribute("user");
        User user = userRepository.findOne(userDto.getId());
        List<Project> projectList = projectRepository.findAll();
        return projectList.stream().map((project) -> ProjectDto.fromProject(project, user)).collect(toList());
    }


    @RequestMapping("/{projectId}/watch")
    public ProjectDto watch(@PathVariable("projectId") long projectId, HttpServletRequest request) {
        Project project = projectRepository.findOne(projectId);
        UserDto userDto = (UserDto) request.getSession().getAttribute("user");
        User user = userRepository.findOne(userDto.getId());
        user.getWatchedProjects().add(project);
        userRepository.save(user);
        return ProjectDto.fromProject(project, user);
    }

    @RequestMapping("/{projectId}/unwatch")
    public ProjectDto unwatch(@PathVariable("projectId") long projectId, HttpServletRequest request) {
        Project project = projectRepository.findOne(projectId);
        UserDto userDto = (UserDto) request.getSession().getAttribute("user");
        User user = userRepository.findOne(userDto.getId());
        user.getWatchedProjects().remove(project);
        userRepository.save(user);
        return ProjectDto.fromProject(project, user);
    }

    @RequestMapping("/{projectId}")
    public ProjectDto get(@PathVariable("projectId") long projectId, HttpServletRequest request) {
        UserDto userDto = (UserDto) request.getSession().getAttribute("user");
        User user = userRepository.findOne(userDto.getId());
        Project project = projectRepository.findOne(projectId);
        return ProjectDto.fromProjectFull(project, user);
    }

    @RequestMapping(value = "/{projectId}", method = RequestMethod.POST)
    public ProjectDto save(@PathVariable("projectId") long projectId, @RequestBody ProjectDto projectDto, HttpServletRequest request) {
        UserDto userDto = (UserDto) request.getSession().getAttribute("user");
        User user = userRepository.findOne(userDto.getId());

        Project project = projectRepository.findOne(projectDto.getId());
        project.setRepositoryUrl(projectDto.getRepositoryUrl());
        project.setRepositoryUsername(projectDto.getRepositoryUsername());
        project.setRepositoryPassword(projectDto.getRepositoryPassword());
        projectRepository.save(project);
        return ProjectDto.fromProjectFull(project, user);
    }
}
