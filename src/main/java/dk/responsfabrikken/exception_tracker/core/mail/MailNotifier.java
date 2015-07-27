package dk.responsfabrikken.exception_tracker.core.mail;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import dk.responsfabrikken.exception_tracker.core.model.client.LoggingEventDto;
import dk.responsfabrikken.exception_tracker.core.model.server.*;
import dk.responsfabrikken.exception_tracker.core.service.exceptiongroup.NotificationListener;
import dk.responsfabrikken.exception_tracker.core.service.exceptiongroup.NotificationListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.List;

@Component
public class MailNotifier extends NotificationListenerAdapter {

    @Autowired TemplateEngine templateEngine;
    @Autowired SettingsRepository settingsRepository;

    @Override
    public void onNewExceptionGroupAdded(ExceptionGroup exceptionGroup) {
        Project project = exceptionGroup.getProject();
        List<User> watchers = project.getWatchers();
        for (User watcher : watchers) {
            mail(watcher, exceptionGroup);
        }
    }

    @Override
    public void onNewExceptionGroupLog(ExceptionGroup exceptionGroup, ExceptionGroup log) {
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
                if (settings.getPort() != 0) {
                    javaMailSender.setPort(settings.getPort());
                    javaMailSender.setProtocol("smtps");
                }
                javaMailSender.setUsername(settings.getUsername());
                javaMailSender.setPassword(settings.getPassword());

                MimeMessage message = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message);

                helper.setFrom("noreply@example.com");
                helper.setTo(watcher.getEmail());
                helper.setSubject(exceptionGroup.getProject().getName()  + ": New exception group: " + exceptionGroup.getMessage());
                helper.setText(createMailText(exceptionGroup), true);
                javaMailSender.send(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String createMailText(ExceptionGroup exceptionGroup) {
        Context context = new Context();
        context.setVariable("exceptionGroup", exceptionGroup);
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        try {
            List<LoggingEventDto> value = new ObjectMapper().readValue(exceptionGroup.getLogs().get(0).getContext(), typeFactory.constructCollectionType(List.class, LoggingEventDto.class));
            context.setVariable("logs", value);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return templateEngine.process("new-exception-group", context);
    }
}
