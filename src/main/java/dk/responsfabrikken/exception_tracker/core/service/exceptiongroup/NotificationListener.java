package dk.responsfabrikken.exception_tracker.core.service.exceptiongroup;

import dk.responsfabrikken.exception_tracker.core.model.server.Comment;
import dk.responsfabrikken.exception_tracker.core.model.server.ExceptionGroup;
import dk.responsfabrikken.exception_tracker.core.model.server.User;

public interface NotificationListener {
    void onNewExceptionGroupAdded(ExceptionGroup exceptionGroup);

    void onExceptionGroupAssigneeChanged(ExceptionGroup exceptionGroup, User oldAssignee, User newAssignee);

    void onExceptionGroupRegression(ExceptionGroup exceptionGroup);

    void onNewExceptionGroupLog(ExceptionGroup exceptionGroup, ExceptionGroup log);

    void onExceptionGroupCommented(ExceptionGroup exceptionGroup, Comment comment);

    void onExceptionGroupResolved(ExceptionGroup exceptionGroup);
}
