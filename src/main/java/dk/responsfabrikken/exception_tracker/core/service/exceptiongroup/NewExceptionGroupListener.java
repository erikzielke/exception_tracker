package dk.responsfabrikken.exception_tracker.core.service.exceptiongroup;

import dk.responsfabrikken.exception_tracker.core.model.server.ExceptionGroup;

public interface NewExceptionGroupListener {
    void onNewExceptionGroupAdded(ExceptionGroup exceptionGroup);
}
