package dk.responsfabrikken.exception_tracker.core.model.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import dk.responsfabrikken.exception_tracker.core.model.server.ExceptionGroup;
import dk.responsfabrikken.exception_tracker.core.model.server.ExceptionLog;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class ExceptionLogDto {
    private long id;
    private long timestamp;
    private String stackTrace;
    private boolean hasCode;
    private List<LoggingEventDto> context;


    public static ExceptionLogDto fromExceptionLog(ExceptionGroup one, ExceptionLog exceptionLog) {
        ExceptionLogDto exceptionLogDto = new ExceptionLogDto();
        exceptionLogDto.setStackTrace(one.getStackTrace());
        exceptionLogDto.setTimestamp(exceptionLog.getTimestamp());
        exceptionLogDto.setId(exceptionLog.getId());
        if (exceptionLog.getContext() != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            TypeFactory typeFactory = TypeFactory.defaultInstance();
            try {
                exceptionLogDto.context = objectMapper.readValue(exceptionLog.getContext(), typeFactory.constructCollectionType(List.class, LoggingEventDto.class));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        exceptionLogDto.setHasCode(one.getProject().getRepositoryUrl() != null);
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

    public List<LoggingEventDto> getContext() {
        return context;
    }

    public void setContext(List<LoggingEventDto> context) {
        this.context = context;
    }

    public boolean isHasCode() {
        return hasCode;
    }

    public void setHasCode(boolean hasCode) {
        this.hasCode = hasCode;
    }
}
