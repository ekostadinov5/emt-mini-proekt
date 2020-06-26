package mk.ukim.finki.emt.cinema.moviecatalog.integration;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import mk.ukim.finki.emt.cinema.moviecatalog.domain.model.MovieId;
import mk.ukim.finki.emt.cinema.moviecatalog.domain.model.ProjectionId;
import mk.ukim.finki.emt.cinema.moviecatalog.domain.model.ReservationId;
import mk.ukim.finki.emt.cinema.sharedkernel.domain.base.DomainEvent;
import org.springframework.lang.NonNull;

import java.time.Instant;
import java.util.Objects;

@Getter
public class ReservationCancelledEvent implements DomainEvent {

    private final Instant occurredOn;

    private final ReservationId reservationId;

    private final MovieId movieId;

    private final ProjectionId projectionId;

    private final Short numberOfSeats;

    @JsonCreator
    public ReservationCancelledEvent(@NonNull Instant occurredOn, @NonNull ReservationId reservationId,
                                     @NonNull MovieId movieId, @NonNull ProjectionId projectionId,
                                     @NonNull Short numberOfSeats) {
        this.occurredOn = Objects.requireNonNull(occurredOn, "occurredOn must not be null");
        this.reservationId = Objects.requireNonNull(reservationId, "reservationId must not be null");
        this.movieId = Objects.requireNonNull(movieId, "movieId must not be null");
        this.projectionId = Objects.requireNonNull(projectionId, "projectionId must not be null");
        this.numberOfSeats = Objects.requireNonNull(numberOfSeats, "numberOfSeats must not be null");
    }

    @Override
    public Instant occurredOn() {
        return occurredOn;
    }

}
