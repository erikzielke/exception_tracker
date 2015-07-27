package dk.responsfabrikken.exception_tracker.core.model.server;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExceptionLogRepository extends JpaRepository<ExceptionLog, Long> {

}

