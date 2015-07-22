package dk.responsfabrikken.exception_tracker.core.service;

import dk.responsfabrikken.exception_tracker.core.model.server.*;
import dk.responsfabrikken.exception_tracker.core.service.exceptiongroup.ExceptionGroupListeners;
import dk.responsfabrikken.exception_tracker.core.service.exceptiongroup.NewExceptionGroupListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class IncomingExceptionProcessor {
    @Autowired IncomingExceptionRepository incomingExceptionRepository;
    @Autowired ProjectRepository projectRepository;
    @Autowired ExceptionGroupRepository exceptionGroupRepository;
    @Autowired ExceptionLogRepository exceptionLogRepository;
    @Autowired ExceptionGroupListeners exceptionGroupListeners;

    //    @Scheduled(fixedRate = 1000 * 60)
    public void processExceptions() {
        List<IncomingException> incomingExceptions = incomingExceptionRepository.findByProcessedIsNull();
        for (IncomingException incomingException : incomingExceptions) {
            processException(incomingException);
            incomingException.setProcessed(new Date());
            incomingExceptionRepository.save(incomingException);
        }
    }

    private void processException(IncomingException incomingException) {
        String projectName = incomingException.getProject();
        Project project = projectRepository.findByName(projectName);
        if (project == null) {
            project = new Project();
            project.setName(projectName);
            projectRepository.save(project);
        }

        ExceptionGroup exceptionGroup = exceptionGroupRepository.findByProjectAndStackTrace(project, incomingException.getStackTrace());
        if (exceptionGroup == null) {
            exceptionGroup = new ExceptionGroup();
            exceptionGroup.setProject(project);
            exceptionGroup.setMessage(incomingException.getMessage());
            exceptionGroup.setStackTrace(incomingException.getStackTrace());
            exceptionGroup.setLineNumber(incomingException.getLineNumber());
            exceptionGroup.setMethodName(incomingException.getMethodName());
            exceptionGroup.setFileName(incomingException.getFileName());
            exceptionGroup.setClassName(incomingException.getClassName());
            exceptionGroup.setExceptionGroupStatus(ExceptionGroupStatus.UNRESOLVED);
            exceptionGroup.setRevision(incomingException.getRevision());
            exceptionGroupRepository.save(exceptionGroup);
            for (NewExceptionGroupListener newExceptionGroupListener : exceptionGroupListeners.getListeners()) {
                newExceptionGroupListener.onNewExceptionGroupAdded(exceptionGroup);
            }
        } else {
            exceptionGroup.setRevision(incomingException.getRevision());
            exceptionGroup.setExceptionGroupStatus(ExceptionGroupStatus.UNRESOLVED);
            exceptionGroupRepository.save(exceptionGroup);
        }

        ExceptionLog exceptionLog = new ExceptionLog();
        exceptionLog.setExceptionGroup(exceptionGroup);
        exceptionLog.setTimestamp(incomingException.getTimestamp());
        exceptionLogRepository.save(exceptionLog);
    }
}
