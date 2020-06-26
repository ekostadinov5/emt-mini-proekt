package mk.ukim.finki.emt.cinema.sharedkernel.domain.base;

import java.time.Instant;

public interface DomainEvent extends DomainObject {

    Instant occurredOn();

}
