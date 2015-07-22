package dk.responsfabrikken.exception_tracker.core.model.server;

import dk.responsfabrikken.exception_tracker.core.model.client.IncomingExceptionDto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.util.Date;

@Entity
public class IncomingException {
    @Id
    @GeneratedValue
    private Long id;
    private String project;
    private String message;
    @Lob
    private String stackTrace;
    private long timestamp;
    private String className;
    private String methodName;
    private String lineNumber;
    private String fileName;
    private Date processed;
    private String revision;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
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

    public Date getProcessed() {
        return processed;
    }

    public void setProcessed(Date processed) {
        this.processed = processed;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public static IncomingException fromDto(IncomingExceptionDto incomingExceptionDto) {
        IncomingException incomingException = new IncomingException();
        incomingException.message = incomingExceptionDto.getMessage();
        incomingException.stackTrace = fromStackTraceArray(incomingExceptionDto.getStackTrace());
        incomingException.timestamp = incomingExceptionDto.getTimestamp();
        incomingException.className = incomingExceptionDto.getClassName();
        incomingException.methodName = incomingExceptionDto.getMethodName();
        incomingException.lineNumber = incomingExceptionDto.getLineNumber();
        incomingException.fileName = incomingExceptionDto.getFileName();
        incomingException.revision = incomingExceptionDto.getRevision();
        return incomingException;
    }

    public static String fromStackTraceArray(String[] strings) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : strings) {
            stringBuilder.append(string).append("\n");
        }
        return stringBuilder.toString();
    }


}
