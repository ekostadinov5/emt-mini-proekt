package mk.ukim.finki.emt.cinema.sharedkernel.infra.eventlog;

import lombok.Getter;
import mk.ukim.finki.emt.cinema.sharedkernel.domain.base.IdentifiableDomainObject;
import org.springframework.lang.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "processed_remote_event")
@Getter
public class ProcessedRemoteEvent implements IdentifiableDomainObject<String> {

    @Id
    @Column(name = "source", nullable = false)
    private String source;

    @Column(name = "last_processed_event_id", nullable = false)
    private Long lastProcessedEventId;

    protected ProcessedRemoteEvent() {}

    public ProcessedRemoteEvent(@NonNull String source, @NonNull Long lastProcessedEventId) {
        this.source = Objects.requireNonNull(source, "source must not be null");
        this.lastProcessedEventId = Objects.requireNonNull(lastProcessedEventId,
                "lastProcessedEventId must not be null");
    }

    @Override
    public String id() {
        return source;
    }

}
