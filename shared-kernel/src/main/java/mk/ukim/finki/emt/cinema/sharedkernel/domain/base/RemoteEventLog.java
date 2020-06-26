package mk.ukim.finki.emt.cinema.sharedkernel.domain.base;

import mk.ukim.finki.emt.cinema.sharedkernel.infra.eventlog.StoredDomainEvent;

import java.util.List;

public interface RemoteEventLog {

    List<StoredDomainEvent> events();

}
