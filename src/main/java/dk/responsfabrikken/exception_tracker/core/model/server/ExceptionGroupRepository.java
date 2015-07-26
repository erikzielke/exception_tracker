package dk.responsfabrikken.exception_tracker.core.model.server;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExceptionGroupRepository extends JpaRepository<ExceptionGroup, Long> {
    ExceptionGroup findByProjectAndStackTrace(Project project, String stackTrace);

    List<ExceptionGroup> findByProject(Project project);
}
