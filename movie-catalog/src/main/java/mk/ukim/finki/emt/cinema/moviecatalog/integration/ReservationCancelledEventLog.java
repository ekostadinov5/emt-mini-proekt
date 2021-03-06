package mk.ukim.finki.emt.cinema.moviecatalog.integration;

import mk.ukim.finki.emt.cinema.sharedkernel.domain.base.RemoteEventLog;
import mk.ukim.finki.emt.cinema.sharedkernel.infra.eventlog.StoredDomainEvent;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ReservationCancelledEventLog implements RemoteEventLog {

    private final List<StoredDomainEvent> events;

    public ReservationCancelledEventLog(ResponseEntity<List<StoredDomainEvent>> response) {
        events = response.getBody();
    }

    @Override
    public List<StoredDomainEvent> events() {
        return events;
    }

}
