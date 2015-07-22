package dk.responsfabrikken.exception_tracker.core.model.server;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Comment {
    @Id
    @GeneratedValue
    private long id;
    private long commentDate;
    private String text;
    @ManyToOne
    private User user;
    @ManyToOne
    private ExceptionGroup exceptionGroup;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(long commentDate) {
        this.commentDate = commentDate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ExceptionGroup getExceptionGroup() {
        return exceptionGroup;
    }

    public void setExceptionGroup(ExceptionGroup exceptionGroup) {
        this.exceptionGroup = exceptionGroup;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
