package mk.ukim.finki.emt.cinema.moviecatalog.domain.model;

import lombok.Getter;
import mk.ukim.finki.emt.cinema.sharedkernel.domain.base.AbstractEntity;
import mk.ukim.finki.emt.cinema.sharedkernel.domain.base.DomainObjectId;
import mk.ukim.finki.emt.cinema.sharedkernel.domain.financial.Money;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "projection")
@Getter
public class Projection extends AbstractEntity<ProjectionId> {

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "time", nullable = false)
    private LocalTime time;

    @Column(name = "number_of_free_seats", nullable = false)
    private Short numberOfFreeSeats;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "currency", column = @Column(name = "currency")),
            @AttributeOverride(name = "amount", column = @Column(name = "price_per_seat"))
    })
    private Money pricePerSeat;

    protected Projection() {}

    public Projection(@NonNull LocalDate date, @NonNull LocalTime time, @NonNull Short numberOfFreeSeats,
                      @NonNull Money pricePerSeat) {
        super(DomainObjectId.randomId(ProjectionId.class));

        Objects.requireNonNull(date, "date must not be null");
        Objects.requireNonNull(time, "time must not be null");
        Objects.requireNonNull(numberOfFreeSeats, "numberOfFreeSeats must not be null");
        Objects.requireNonNull(pricePerSeat, "pricePerSeat must not be null");

        if (date.isBefore(LocalDate.now()) || (date.isEqual(LocalDate.now()) && time.isBefore(LocalTime.now()))) {
            throw new IllegalArgumentException("The date and time must not be in the past");
        }
        if (numberOfFreeSeats < 0) {
            throw new IllegalArgumentException("The number of free seats must not be negative");
        }

        this.date = date;
        this.time = time;
        this.numberOfFreeSeats = numberOfFreeSeats;
        this.pricePerSeat = pricePerSeat;
    }

    public void changePricePerSeat(Money newPricePerSeat) {
        this.pricePerSeat = newPricePerSeat;
    }

    public void reserveSeats(Short numberOfSeats) {
        if (numberOfSeats > this.numberOfFreeSeats) {
            throw new RuntimeException("Not enough free seats");
        }

        this.numberOfFreeSeats = (short)(this.numberOfFreeSeats - numberOfSeats);
    }

    public void cancelSeats(Short numberOfSeats) {
        this.numberOfFreeSeats = (short)(this.numberOfFreeSeats + numberOfSeats);
    }

}
