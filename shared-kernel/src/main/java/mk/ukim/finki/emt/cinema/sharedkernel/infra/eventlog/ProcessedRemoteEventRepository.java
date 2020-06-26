package mk.ukim.finki.emt.cinema.sharedkernel.infra.eventlog;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedRemoteEventRepository extends JpaRepository<ProcessedRemoteEvent, String> {
}
