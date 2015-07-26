package dk.responsfabrikken.exception_tracker.core.model.server;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class ExceptionLog {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private ExceptionGroup exceptionGroup;
    private long timestamp;
    private String context;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ExceptionGroup getExceptionGroup() {
        return exceptionGroup;
    }

    public void setExceptionGroup(ExceptionGroup exceptionGroup) {
        this.exceptionGroup = exceptionGroup;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
