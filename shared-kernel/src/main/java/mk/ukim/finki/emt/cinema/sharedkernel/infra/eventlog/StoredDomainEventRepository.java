package mk.ukim.finki.emt.cinema.sharedkernel.infra.eventlog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoredDomainEventRepository extends JpaRepository<StoredDomainEvent, Long> {

    @Query("SELECT MAX(sde.id) FROM StoredDomainEvent sde")
    Long findHighestDomainEventId();

    @Query("SELECT sde FROM StoredDomainEvent sde WHERE sde.id >= :low AND sde.id <= :high ORDER BY sde.id")
    List<StoredDomainEvent> findEventsBetween(@Param("low") Long low, @Param("high") Long high);

}
