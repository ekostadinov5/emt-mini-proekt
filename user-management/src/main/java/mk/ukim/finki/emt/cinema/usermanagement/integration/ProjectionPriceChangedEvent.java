package mk.ukim.finki.emt.cinema.usermanagement.integration;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import mk.ukim.finki.emt.cinema.sharedkernel.domain.base.DomainEvent;
import mk.ukim.finki.emt.cinema.sharedkernel.domain.financial.Money;
import mk.ukim.finki.emt.cinema.usermanagement.domain.model.MovieId;
import mk.ukim.finki.emt.cinema.usermanagement.domain.model.ProjectionId;
import org.springframework.lang.NonNull;

import java.time.Instant;
import java.util.Objects;

@Getter
public class ProjectionPriceChangedEvent implements DomainEvent {

    private final Instant occurredOn;

    private final MovieId movieId;

    private final ProjectionId projectionId;

    private final Money newPricePerSeat;

    @JsonCreator
    public ProjectionPriceChangedEvent(@NonNull Instant occurredOn, @NonNull MovieId movieId,
                                  @NonNull ProjectionId projectionId, @NonNull Money newPricePerSeat) {
        this.occurredOn = Objects.requireNonNull(occurredOn, "occurredOn must not be null");
        this.movieId = Objects.requireNonNull(movieId, "movieId must not be null");
        this.projectionId = Objects.requireNonNull(projectionId, "projectionId must not be null");
        this.newPricePerSeat = Objects.requireNonNull(newPricePerSeat, "newPricePerSeat must not be null");
    }

    @Override
    public Instant occurredOn() {
        return occurredOn;
    }

}
