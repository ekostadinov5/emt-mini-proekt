package mk.ukim.finki.emt.cinema.moviecatalog.domain.model;

import lombok.Getter;
import mk.ukim.finki.emt.cinema.moviecatalog.domain.model.enumeration.Genre;
import mk.ukim.finki.emt.cinema.sharedkernel.domain.base.AbstractEntity;
import mk.ukim.finki.emt.cinema.sharedkernel.domain.base.DomainObjectId;
import mk.ukim.finki.emt.cinema.sharedkernel.domain.financial.Money;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "movie")
@Getter
public class Movie extends AbstractEntity<MovieId> {

    @Version
    private Long version;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "genre", nullable = false)
    @Enumerated(EnumType.STRING)
    private Genre genre;

    @Column(name = "duration", nullable = false)
    private Short duration;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "movie_id")
    private Set<Projection> projections;

    protected Movie() {}

    public Movie(@NonNull String name, @NonNull Genre genre, @NonNull Short duration) {
        super(DomainObjectId.randomId(MovieId.class));

        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(genre, "genre must not be null");
        Objects.requireNonNull(duration, "duration must not be null");

        if (name.isEmpty()) {
            throw new IllegalArgumentException("The name must not be empty");
        }
        if (duration < 0) {
            throw new IllegalArgumentException("The duration must not be negative");
        }

        this.name = name;
        this.genre = genre;
        this.duration = duration;

        this.projections = new HashSet<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public void setDuration(Short duration) {
        this.duration = duration;
    }

    public Projection addProjection(LocalDate date, LocalTime time, Short numberOfFreeSeats, Money pricePerSeat){
        Projection projection = new Projection(date, time, numberOfFreeSeats, pricePerSeat);
        projections.add(projection);
        return projection;
    }

    public void changePricePerSeatForProjection(ProjectionId projectionId, Money newPricePerSeat) {
        this.projections.stream()
                .filter(p -> p.id().equals(projectionId))
                .findFirst()
                .ifPresent(p -> p.changePricePerSeat(newPricePerSeat));
    }

    public void reserveSeatsForProjection(ProjectionId projectionId, Short numberOfSeats) {
        this.projections.stream()
                .filter(p -> p.id().equals(projectionId))
                .findFirst()
                .ifPresent(p -> p.reserveSeats(numberOfSeats));
    }

    public void cancelSeatsForProjection(ProjectionId projectionId, Short numberOfSeats) {
        this.projections.stream()
                .filter(p -> p.id().equals(projectionId))
                .findFirst()
                .ifPresent(p -> p.cancelSeats(numberOfSeats));
    }

}
