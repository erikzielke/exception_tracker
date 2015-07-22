package dk.responsfabrikken.exception_tracker.core.model.client;

import dk.responsfabrikken.exception_tracker.core.model.server.Comment;

public class CommentDto {
    private String username;
    private long commentDate;
    private String commentText;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(long commentDate) {
        this.commentDate = commentDate;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public static CommentDto fromComment(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setUsername(comment.getUser().getFullName());
        commentDto.setCommentDate(comment.getCommentDate());
        commentDto.setCommentText(comment.getText());
        return commentDto;
    }
}
