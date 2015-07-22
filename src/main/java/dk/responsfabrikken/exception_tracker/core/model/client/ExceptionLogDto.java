package dk.responsfabrikken.exception_tracker.core.model.client;

import dk.responsfabrikken.exception_tracker.core.model.server.ExceptionGroup;
import dk.responsfabrikken.exception_tracker.core.model.server.ExceptionLog;

public class ExceptionLogDto {
    private long id;
    private long timestamp;
    private String stackTrace;

    public static ExceptionLogDto fromExceptionLog(ExceptionGroup one, ExceptionLog exceptionLog) {
        ExceptionLogDto exceptionLogDto = new ExceptionLogDto();
        exceptionLogDto.setStackTrace(one.getStackTrace());
        exceptionLogDto.setTimestamp(exceptionLog.getTimestamp());
        exceptionLogDto.setId(exceptionLog.getId());
        return exceptionLogDto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }
}
