package dk.responsfabrikken.exception_tracker.core.model.server;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncomingExceptionRepository extends JpaRepository<IncomingException, Long> {
    List<IncomingException> findByProcessedIsNull();
}
