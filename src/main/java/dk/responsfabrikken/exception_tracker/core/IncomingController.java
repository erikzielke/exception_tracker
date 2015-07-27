package dk.responsfabrikken.exception_tracker.core;

import dk.responsfabrikken.exception_tracker.core.model.client.ExceptionTrackStatusDto;
import dk.responsfabrikken.exception_tracker.core.model.client.IncomingExceptionDto;
import dk.responsfabrikken.exception_tracker.core.model.server.ExceptionGroup;
import dk.responsfabrikken.exception_tracker.core.model.server.ExceptionGroupRepository;
import dk.responsfabrikken.exception_tracker.core.model.server.IncomingException;
import dk.responsfabrikken.exception_tracker.core.model.server.IncomingExceptionRepository;
import dk.responsfabrikken.exception_tracker.core.service.GitFetchService;
import dk.responsfabrikken.exception_tracker.core.service.IncomingExceptionProcessor;
import dk.responsfabrikken.exception_tracker.core.service.exceptiongroup.ExceptionGroupListeners;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class IncomingController {
    @Autowired IncomingExceptionRepository incomingExceptionRepository;
    @Autowired IncomingExceptionProcessor incomingExceptionProcessor;
    @Autowired GitFetchService gitFetchService;
    @Autowired ExceptionGroupListeners exceptionGroupListeners;
    @Autowired ExceptionGroupRepository repository;

    @RequestMapping("/incoming")
    public ExceptionTrackStatusDto incoming(@RequestBody IncomingExceptionDto incomingExceptionDto) {
        IncomingException incomingException = IncomingException.fromDto(incomingExceptionDto);
        IncomingException save = incomingExceptionRepository.save(incomingException);
        return new ExceptionTrackStatusDto();
    }

    @RequestMapping("/process")
    public void process() {
        incomingExceptionProcessor.processExceptions();
    }

    @RequestMapping("/reps")
    public void processRepositories() {
        gitFetchService.fetchRepositories();
    }


}
