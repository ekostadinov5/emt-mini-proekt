package mk.ukim.finki.emt.cinema.usermanagement.integration;

import mk.ukim.finki.emt.cinema.sharedkernel.domain.base.RemoteEventLog;
import mk.ukim.finki.emt.cinema.sharedkernel.infra.eventlog.StoredDomainEvent;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ProjectionPriceChangedEventLog implements RemoteEventLog {

    private final List<StoredDomainEvent> events;

    public ProjectionPriceChangedEventLog(ResponseEntity<List<StoredDomainEvent>> response) {
        events = response.getBody();
    }

    @Override
    public List<StoredDomainEvent> events() {
        return events;
    }

}
