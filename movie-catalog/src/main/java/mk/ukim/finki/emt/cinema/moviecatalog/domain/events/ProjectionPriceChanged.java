package mk.ukim.finki.emt.cinema.moviecatalog.domain.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import mk.ukim.finki.emt.cinema.moviecatalog.domain.model.MovieId;
import mk.ukim.finki.emt.cinema.moviecatalog.domain.model.ProjectionId;
import mk.ukim.finki.emt.cinema.sharedkernel.domain.base.DomainEvent;
import mk.ukim.finki.emt.cinema.sharedkernel.domain.financial.Money;
import org.springframework.lang.NonNull;

import java.time.Instant;
import java.util.Objects;

public class ProjectionPriceChanged implements DomainEvent {

    @JsonProperty("occurredOn")
    private final Instant occurredOn;

    @JsonProperty("movieId")
    private final MovieId movieId;

    @JsonProperty("projectionId")
    private final ProjectionId projectionId;

    @JsonProperty("newPricePerSeat")
    private final Money newPricePerSeat;

    @JsonCreator
    public ProjectionPriceChanged(@NonNull Instant occurredOn, @NonNull MovieId movieId,
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
