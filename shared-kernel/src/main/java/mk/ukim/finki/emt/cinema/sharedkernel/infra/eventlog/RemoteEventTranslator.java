package mk.ukim.finki.emt.cinema.sharedkernel.infra.eventlog;

import mk.ukim.finki.emt.cinema.sharedkernel.domain.base.DomainEvent;

import java.util.Optional;

public interface RemoteEventTranslator {

    Boolean supports(StoredDomainEvent storedDomainEvent);

    Optional<DomainEvent> translate(StoredDomainEvent remoteEvent);

}
