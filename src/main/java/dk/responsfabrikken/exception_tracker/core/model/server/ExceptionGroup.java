package dk.responsfabrikken.exception_tracker.core.model.server;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ExceptionGroup {
    @Id
    @GeneratedValue
    private Long id;
    private String message;
    @ManyToOne
    private Project project;
    @Lob
    private String stackTrace;
    private String className;
    private String methodName;
    private String lineNumber;
    private String fileName;
    private ExceptionGroupStatus exceptionGroupStatus;
    @OneToMany(mappedBy = "exceptionGroup")
    private List<ExceptionLog> logs;
    @ManyToOne
    private User assignee;
    @OneToMany(mappedBy = "exceptionGroup")
    private List<Comment> comments;
    private String revision;

    public ExceptionGroup() {
        logs = new ArrayList<>();
        comments = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<ExceptionLog> getLogs() {
        return logs;
    }

    public void setLogs(List<ExceptionLog> logs) {
        this.logs = logs;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public ExceptionGroupStatus getExceptionGroupStatus() {
        return exceptionGroupStatus;
    }

    public void setExceptionGroupStatus(ExceptionGroupStatus exceptionGroupStatus) {
        this.exceptionGroupStatus = exceptionGroupStatus;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }
}
