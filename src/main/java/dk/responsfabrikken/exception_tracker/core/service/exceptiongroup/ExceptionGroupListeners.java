package dk.responsfabrikken.exception_tracker.core.service.exceptiongroup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import dk.responsfabrikken.exception_tracker.core.mail.MailNotifier;

import java.util.Collections;
import java.util.List;

@Component
public class ExceptionGroupListeners {
    @Autowired MailNotifier mailNotifier;

    public List<NotificationListener> getListeners() {
        return Collections.singletonList(mailNotifier);
    }
}
