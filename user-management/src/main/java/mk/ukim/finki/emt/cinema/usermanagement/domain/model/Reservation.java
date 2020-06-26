package mk.ukim.finki.emt.cinema.usermanagement.domain.model;

import lombok.Getter;
import mk.ukim.finki.emt.cinema.sharedkernel.domain.base.AbstractEntity;
import mk.ukim.finki.emt.cinema.sharedkernel.domain.base.DomainObjectId;
import mk.ukim.finki.emt.cinema.sharedkernel.domain.financial.Money;
import mk.ukim.finki.emt.cinema.usermanagement.domain.model.enumeration.ReservationState;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "reservation")
@Getter
public class Reservation extends AbstractEntity<ReservationId> {

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "movie_id"))
    private MovieId movieId;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "projection_id"))
    private ProjectionId projectionId;

    @Column(name = "reservation_state", nullable = false)
    @Enumerated(EnumType.STRING)
    private ReservationState state;

    @Column(name = "number_of_seats", nullable = false)
    private Short numberOfSeats;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "currency", column = @Column(name = "currency")),
            @AttributeOverride(name = "amount", column = @Column(name = "total_price"))
    })
    private Money totalPrice;

    protected Reservation() {}

    public Reservation(MovieId movieId, ProjectionId projectionId, Money pricePerSeat, Short numberOfSeats) {
        super(DomainObjectId.randomId(ReservationId.class));

        Objects.requireNonNull(movieId, "movieId must not be null");
        Objects.requireNonNull(projectionId, "projectionId must not be null");
        Objects.requireNonNull(pricePerSeat, "pricePerSeat must not be null");
        Objects.requireNonNull(numberOfSeats, "numberOfSeats must not be null");

        if (numberOfSeats <= 0 || numberOfSeats > 6) {
            throw new IllegalArgumentException("The number of seats must be between 1 and 6");
        }

        this.movieId = movieId;
        this.projectionId = projectionId;
        this.state = ReservationState.CREATED;
        this.numberOfSeats = numberOfSeats;
        this.totalPrice = pricePerSeat.multiply((int)numberOfSeats);
    }

    public void updateTotalPrice(Money newPricePerSeat) {
        Money newTotalPrice = newPricePerSeat.multiply((int)numberOfSeats);
        if (newTotalPrice.isSmallerThan(this.totalPrice)) {
            this.totalPrice = newTotalPrice;
        }
    }

}
