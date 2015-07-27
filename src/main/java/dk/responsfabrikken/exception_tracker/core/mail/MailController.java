package dk.responsfabrikken.exception_tracker.core.mail;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import dk.responsfabrikken.exception_tracker.core.model.client.LoggingEventDto;
import dk.responsfabrikken.exception_tracker.core.model.server.ExceptionGroup;
import dk.responsfabrikken.exception_tracker.core.model.server.ExceptionGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/test/mail")
public class MailController {
    @Autowired ExceptionGroupRepository repository;

    @RequestMapping
    public String testMailSender(Model model) {
        List<ExceptionGroup> all = repository.findAll();
        ExceptionGroup exceptionGroup = all.get(all.size() - 1);
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        try {
            List<LoggingEventDto> value = new ObjectMapper().readValue(exceptionGroup.getLogs().get(0).getContext(), typeFactory.constructCollectionType(List.class, LoggingEventDto.class));
            model.addAttribute("logs", value);
        } catch (IOException e) {
            e.printStackTrace();
        }

        model.addAttribute("exceptionGroup", exceptionGroup);
        return "new-exception-group";
    }
}
