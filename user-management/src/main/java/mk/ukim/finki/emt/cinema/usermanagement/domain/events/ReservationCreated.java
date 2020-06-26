package mk.ukim.finki.emt.cinema.usermanagement.domain.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import mk.ukim.finki.emt.cinema.sharedkernel.domain.base.DomainEvent;
import mk.ukim.finki.emt.cinema.usermanagement.domain.model.MovieId;
import mk.ukim.finki.emt.cinema.usermanagement.domain.model.ProjectionId;
import mk.ukim.finki.emt.cinema.usermanagement.domain.model.ReservationId;
import org.springframework.lang.NonNull;

import java.time.Instant;
import java.util.Objects;

public class ReservationCreated implements DomainEvent {

    @JsonProperty("occurredOn")
    private final Instant occurredOn;

    @JsonProperty("reservationId")
    private final ReservationId reservationId;

    @JsonProperty("movieId")
    private final MovieId movieId;

    @JsonProperty("projectionId")
    private final ProjectionId projectionId;

    @JsonProperty("numberOfSeats")
    private final Short numberOfSeats;

    @JsonCreator
    public ReservationCreated(@NonNull Instant occurredOn, @NonNull ReservationId reservationId,
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
