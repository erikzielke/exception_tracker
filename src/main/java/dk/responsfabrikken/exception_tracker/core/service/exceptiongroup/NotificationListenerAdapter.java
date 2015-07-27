package dk.responsfabrikken.exception_tracker.core.service.exceptiongroup;

import dk.responsfabrikken.exception_tracker.core.model.server.Comment;
import dk.responsfabrikken.exception_tracker.core.model.server.ExceptionGroup;
import dk.responsfabrikken.exception_tracker.core.model.server.User;

/**
 * Default listener that does nothing
 */
public abstract class NotificationListenerAdapter implements NotificationListener {
    @Override
    public void onNewExceptionGroupAdded(ExceptionGroup exceptionGroup) {

    }

    @Override
    public void onExceptionGroupAssigneeChanged(ExceptionGroup exceptionGroup, User oldAssignee, User newAssignee) {

    }

    @Override
    public void onExceptionGroupRegression(ExceptionGroup exceptionGroup) {

    }

    @Override
    public void onNewExceptionGroupLog(ExceptionGroup exceptionGroup, ExceptionGroup log) {

    }

    @Override
    public void onExceptionGroupCommented(ExceptionGroup exceptionGroup, Comment comment) {

    }

    @Override
    public void onExceptionGroupResolved(ExceptionGroup exceptionGroup) {

    }
}
