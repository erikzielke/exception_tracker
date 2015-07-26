package dk.responsfabrikken.exception_tracker.core.model.server;

import dk.responsfabrikken.exception_tracker.core.model.client.UserDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {
    @GeneratedValue
    @Id
    private Long id;
    private String shortName;
    private String fullName;
    private String email;
    private String password;
    @ManyToMany
    private List<Project> watchedProjects;
    @OneToMany(mappedBy = "assignee")
    private List<ExceptionGroup> exceptionGroups;
    @OneToMany(mappedBy = "user")
    private List<Comment> comments;

    public User() {
        watchedProjects = new ArrayList<>();
    }

    public static User fromUserDto(UserDto userDto) {
        User user = new User();
        user.setShortName(userDto.getShortName());
        user.setFullName(userDto.getFullName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        return user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Project> getWatchedProjects() {
        return watchedProjects;
    }

    public void setWatchedProjects(List<Project> watchedProjects) {
        this.watchedProjects = watchedProjects;
    }

    public List<ExceptionGroup> getExceptionGroups() {
        return exceptionGroups;
    }

    public void setExceptionGroups(List<ExceptionGroup> exceptionGroups) {
        this.exceptionGroups = exceptionGroups;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
