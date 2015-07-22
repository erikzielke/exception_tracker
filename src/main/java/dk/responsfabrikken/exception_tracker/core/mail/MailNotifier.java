package dk.responsfabrikken.exception_tracker.core.mail;

import dk.responsfabrikken.exception_tracker.core.model.server.*;
import dk.responsfabrikken.exception_tracker.core.service.exceptiongroup.NewExceptionGroupListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MailNotifier implements NewExceptionGroupListener {

    @Autowired SettingsRepository settingsRepository;

    @Override
    public void onNewExceptionGroupAdded(ExceptionGroup exceptionGroup) {
        Project project = exceptionGroup.getProject();
        List<User> watchers = project.getWatchers();
        for (User watcher : watchers) {
            mail(watcher, exceptionGroup);
        }
    }

    private void mail(User watcher, ExceptionGroup exceptionGroup) {
        List<Settings> settingsList = settingsRepository.findAll();
        if (!settingsList.isEmpty()) {
            try {
                Settings settings = settingsList.get(0);
                JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
                javaMailSender.setHost(settings.getHost());
                javaMailSender.setUsername(settings.getUsername());
                javaMailSender.setPassword(settings.getPassword());
                SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
                simpleMailMessage.setFrom("noreply@example.com");
                simpleMailMessage.setTo(watcher.getEmail());
                simpleMailMessage.setSubject(exceptionGroup.getProject() + ": " + exceptionGroup.getMessage());
                simpleMailMessage.setText(exceptionGroup.getStackTrace());
                javaMailSender.send(simpleMailMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
