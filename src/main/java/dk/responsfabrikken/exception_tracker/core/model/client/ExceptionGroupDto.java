package dk.responsfabrikken.exception_tracker.core.model.client;

import dk.responsfabrikken.exception_tracker.core.model.server.ExceptionGroup;
import dk.responsfabrikken.exception_tracker.core.model.server.ExceptionGroupStatus;
import dk.responsfabrikken.exception_tracker.core.model.server.ExceptionLog;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class ExceptionGroupDto {
    private String message;
    private String project;
    private String className;
    private String methodName;
    private String lineNumber;
    private String stackTrace;
    private int count;
    private long id;
    private long lastSeen;
    private String fileName;
    private boolean resolved;
    private String assigneeName;
    private long assigneeId;
    private List<ExceptionLogDto> logs;
    private List<CommentDto> comments;

    public static ExceptionGroupDto fromExceptionGroup(ExceptionGroup exceptionGroup) {
        ExceptionGroupDto exceptionGroupDto = new ExceptionGroupDto();
        exceptionGroupDto.id = exceptionGroup.getId();
        exceptionGroupDto.message = exceptionGroup.getMessage();
        exceptionGroupDto.project = exceptionGroup.getProject().getName();
        List<ExceptionLog> logs = exceptionGroup.getLogs();
        exceptionGroupDto.count = logs.size();
        exceptionGroupDto.lastSeen = logs.get(logs.size() - 1).getTimestamp();
        exceptionGroupDto.resolved = exceptionGroup.getExceptionGroupStatus() == ExceptionGroupStatus.RESOLVED;
        if (exceptionGroup.getAssignee() != null) {
            exceptionGroupDto.assigneeName = exceptionGroup.getAssignee().getFullName();
            exceptionGroupDto.assigneeId = exceptionGroup.getAssignee().getId();
        }
        exceptionGroup.getComments();
        return exceptionGroupDto;
    }

    public static ExceptionGroupDto fromExceptionFullGroup(ExceptionGroup exceptionGroup) {
        ExceptionGroupDto exceptionGroupDto = new ExceptionGroupDto();
        exceptionGroupDto.id = exceptionGroup.getId();
        exceptionGroupDto.message = exceptionGroup.getMessage();
        exceptionGroupDto.project = exceptionGroup.getProject().getName();
        List<ExceptionLog> logs = exceptionGroup.getLogs();
        exceptionGroupDto.count = logs.size();
        exceptionGroupDto.lastSeen = logs.get(logs.size() - 1).getTimestamp();
        exceptionGroupDto.className = exceptionGroup.getClassName();
        exceptionGroupDto.fileName = exceptionGroup.getFileName();
        exceptionGroupDto.lineNumber = exceptionGroup.getLineNumber();
        exceptionGroupDto.methodName = exceptionGroup.getMethodName();
        exceptionGroupDto.stackTrace = exceptionGroup.getStackTrace();
        exceptionGroupDto.resolved = exceptionGroup.getExceptionGroupStatus() == ExceptionGroupStatus.RESOLVED;
        exceptionGroupDto.comments =  exceptionGroup.getComments().stream().map(CommentDto::fromComment).collect(toList());
        if (exceptionGroup.getAssignee() != null) {
            exceptionGroupDto.assigneeName = exceptionGroup.getAssignee().getFullName();
            exceptionGroupDto.assigneeId = exceptionGroup.getAssignee().getId();
        }
        return exceptionGroupDto;
    }

    public static ExceptionGroupDto fromExceptionFullGroupWithLogs(ExceptionGroup one) {
        ExceptionGroupDto exceptionGroupDto = fromExceptionGroup(one);
        exceptionGroupDto.logs = one.getLogs().stream().map((exceptionLog) -> ExceptionLogDto.fromExceptionLog(one, exceptionLog)).collect(toList());
        return exceptionGroupDto;
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

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public long getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(long lastSeen) {
        this.lastSeen = lastSeen;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<ExceptionLogDto> getLogs() {
        return logs;
    }

    public void setLogs(List<ExceptionLogDto> logs) {
        this.logs = logs;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    public String getAssigneeName() {
        return assigneeName;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }

    public long getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(long assigneeId) {
        this.assigneeId = assigneeId;
    }

    public List<CommentDto> getComments() {
        return comments;
    }

    public void setComments(List<CommentDto> comments) {
        this.comments = comments;
    }
}
