package mk.ukim.finki.emt.cinema.sharedkernel.infra.eventlog;

import mk.ukim.finki.emt.cinema.sharedkernel.domain.base.RemoteEventLog;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Map;

@Service
public class RemoteEventProcessor {

    private final ProcessedRemoteEventRepository processedRemoteEventRepository;
    private final Map<String, RemoteEventLogService> remoteEventLogs;
    private final Map<String, RemoteEventTranslator> remoteEventTranslators;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final TransactionTemplate transactionTemplate;

    public RemoteEventProcessor(ProcessedRemoteEventRepository processedRemoteEventRepository,
                                Map<String, RemoteEventLogService> remoteEventLogs,
                                Map<String, RemoteEventTranslator> remoteEventTranslators,
                                ApplicationEventPublisher applicationEventPublisher,
                                TransactionTemplate transactionTemplate) {
        this.processedRemoteEventRepository = processedRemoteEventRepository;
        this.remoteEventLogs = remoteEventLogs;
        this.remoteEventTranslators = remoteEventTranslators;
        this.applicationEventPublisher = applicationEventPublisher;
        this.transactionTemplate = transactionTemplate;
    }

    @Scheduled(fixedDelay = 15000)
    public void processEvents() {
        remoteEventLogs.values().forEach(this::processEvents);
    }

    private void processEvents(@NonNull RemoteEventLogService remoteEventLogService) {
        RemoteEventLog log = remoteEventLogService.currentLog(this.getLastProcessedId(remoteEventLogService));
        processEvents(remoteEventLogService, log.events());
    }

    private Long getLastProcessedId(@NonNull RemoteEventLogService remoteEventLogService) {
        return processedRemoteEventRepository.findById(remoteEventLogService.source())
                .map(ProcessedRemoteEvent::getLastProcessedEventId)
                .orElse(0L);
    }

    private void processEvents(@NonNull RemoteEventLogService remoteEventLogService,
                               @NonNull List<StoredDomainEvent> events) {
        events.forEach(e -> {
            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                    publishEvent(e);
                    setLastProcessedId(remoteEventLogService, e.id());
                }
            });
        });
    }

    private void setLastProcessedId(@NonNull RemoteEventLogService remoteEventLogService, long lastProcessedId) {
        processedRemoteEventRepository
                .saveAndFlush(new ProcessedRemoteEvent(remoteEventLogService.source(), lastProcessedId));
    }

    private void publishEvent(@NonNull StoredDomainEvent event) {
        remoteEventTranslators.values().stream()
                .filter(t -> t.supports(event))
                .findFirst()
                .flatMap(t -> t.translate(event))
                .ifPresent(applicationEventPublisher::publishEvent);
    }

}
