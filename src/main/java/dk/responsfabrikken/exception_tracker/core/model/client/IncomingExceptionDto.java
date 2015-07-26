package dk.responsfabrikken.exception_tracker.core.model.client;

import java.util.ArrayList;
import java.util.List;

public class IncomingExceptionDto {
    private String message;
    private String[] stackTrace;
    private long timestamp;
    private String className;
    private String methodName;
    private String lineNumber;
    private String fileName;
    private String revision;
    private String project;
    private List<LoggingEventDto> loggingEvents;

    public IncomingExceptionDto() {
        loggingEvents = new ArrayList<>();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String[] getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String[] stackTrace) {
        this.stackTrace = stackTrace;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
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

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public List<LoggingEventDto> getLoggingEvents() {
        return loggingEvents;
    }

    public void setLoggingEvents(List<LoggingEventDto> loggingEvents) {
        this.loggingEvents = loggingEvents;
    }
}
