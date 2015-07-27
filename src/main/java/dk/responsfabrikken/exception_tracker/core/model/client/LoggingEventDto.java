package dk.responsfabrikken.exception_tracker.core.model.client;

import java.util.Date;

public class LoggingEventDto {
    private String message;
    private long timestamp;
    private String level;

    public Date getDate() {
        return new Date(timestamp);
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
