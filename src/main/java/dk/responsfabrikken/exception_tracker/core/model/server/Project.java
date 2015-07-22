package dk.responsfabrikken.exception_tracker.core.model.server;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Project {
    @GeneratedValue
    @Id
    private Long id;
    private String name;
    @OneToMany(mappedBy = "project")
    private List<ExceptionGroup> exceptionGroups;
    @ManyToMany(mappedBy = "watchedProjects")
    private List<User> watchers;

    private String repositoryUrl;
    private String repositoryUsername;
    private String repositoryPassword;


    public Project() {
        exceptionGroups = new ArrayList<>();
        watchers = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ExceptionGroup> getExceptionGroups() {
        return exceptionGroups;
    }

    public void setExceptionGroups(List<ExceptionGroup> exceptionGroups) {
        this.exceptionGroups = exceptionGroups;
    }

    public List<User> getWatchers() {
        return watchers;
    }

    public void setWatchers(List<User> watchers) {
        this.watchers = watchers;
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
