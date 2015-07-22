package dk.responsfabrikken.exception_tracker.core.model.client;

import dk.responsfabrikken.exception_tracker.core.model.server.Project;
import dk.responsfabrikken.exception_tracker.core.model.server.User;

public class ProjectDto {
    private long id;
    private String name;
    private boolean isWatched;
    private String repositoryUrl;
    private String repositoryUsername;
    private String repositoryPassword;


    public static ProjectDto fromProject(Project project, User user) {
        ProjectDto projectDto = new ProjectDto();
        projectDto.id = project.getId();
        projectDto.name = project.getName();
        projectDto.isWatched = project.getWatchers().contains(user);
        return projectDto;
    }

    public static ProjectDto fromProjectFull(Project project, User user) {
        ProjectDto projectDto = new ProjectDto();
        projectDto.id = project.getId();
        projectDto.name = project.getName();
        projectDto.isWatched = project.getWatchers().contains(user);
        projectDto.repositoryUrl = project.getRepositoryUrl();
        projectDto.repositoryUsername = project.getRepositoryUsername();
        projectDto.repositoryPassword = project.getRepositoryPassword();
        return projectDto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isWatched() {
        return isWatched;
    }

    public void setIsWatched(boolean isWatched) {
        this.isWatched = isWatched;
    }

    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    public void setRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }

    public String getRepositoryUsername() {
        return repositoryUsername;
    }

    public void setRepositoryUsername(String repositoryUsername) {
        this.repositoryUsername = repositoryUsername;
    }

    public String getRepositoryPassword() {
        return repositoryPassword;
    }

    public void setRepositoryPassword(String repositoryPassword) {
        this.repositoryPassword = repositoryPassword;
    }
}
