package mk.ukim.finki.emt.cinema.usermanagement.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import mk.ukim.finki.emt.cinema.sharedkernel.domain.base.DomainEvent;
import mk.ukim.finki.emt.cinema.sharedkernel.infra.eventlog.RemoteEventTranslator;
import mk.ukim.finki.emt.cinema.sharedkernel.infra.eventlog.StoredDomainEvent;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProjectionPriceChangedEventTranslator implements RemoteEventTranslator {

    private final ObjectMapper objectMapper;

    public ProjectionPriceChangedEventTranslator(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Boolean supports(StoredDomainEvent storedDomainEvent) {
        return storedDomainEvent.domainEventClassName()
                .equals("mk.ukim.finki.emt.cinema.moviecatalog.domain.events.ProjectionPriceChanged");
    }

    @Override
    public Optional<DomainEvent> translate(StoredDomainEvent remoteEvent) {
        return Optional.of(remoteEvent.toDomainEvent(objectMapper, ProjectionPriceChangedEvent.class));
    }

}
