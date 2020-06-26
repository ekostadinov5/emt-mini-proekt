package mk.ukim.finki.emt.cinema.sharedkernel.infra.eventlog;

import com.fasterxml.jackson.databind.ObjectMapper;
import mk.ukim.finki.emt.cinema.sharedkernel.domain.base.DomainEvent;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DomainEventLogService {

    private final ObjectMapper objectMapper;
    private final StoredDomainEventRepository storedDomainEventRepository;

    public DomainEventLogService(ObjectMapper objectMapper, StoredDomainEventRepository storedDomainEventRepository) {
        this.objectMapper = objectMapper;
        this.storedDomainEventRepository = storedDomainEventRepository;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void append(DomainEvent domainEvent) {
        StoredDomainEvent storedDomainEvent = new StoredDomainEvent(domainEvent, objectMapper);
        storedDomainEventRepository.saveAndFlush(storedDomainEvent);
    }

    @NonNull
    @Transactional(propagation = Propagation.REQUIRED)
    public List<StoredDomainEvent> retrieveLog(Long lastProcessedEventId) {
        Long max = storedDomainEventRepository.findHighestDomainEventId();
        max = (max == null) ? 0 : max;
        return storedDomainEventRepository.findEventsBetween(lastProcessedEventId + 1, max);
    }

}
