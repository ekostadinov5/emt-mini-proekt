package mk.ukim.finki.emt.cinema.usermanagement.domain.model;

import lombok.Getter;
import mk.ukim.finki.emt.cinema.sharedkernel.domain.base.AbstractEntity;
import mk.ukim.finki.emt.cinema.sharedkernel.domain.base.DomainObjectId;
import mk.ukim.finki.emt.cinema.sharedkernel.domain.financial.Money;
import mk.ukim.finki.emt.cinema.usermanagement.domain.model.enumeration.Gender;
import mk.ukim.finki.emt.cinema.usermanagement.domain.model.enumeration.ReservationState;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "application_user")
@Getter
public class User extends AbstractEntity<UserId> {

    @Version
    private Long version;

    @Embedded
    @AttributeOverride(name = "username", column = @Column(name = "username", unique = true))
    private Username username;

    @Embedded
    @AttributeOverride(name = "password", column = @Column(name = "password"))
    private Password password;

    @Embedded
    @AttributeOverride(name = "email", column = @Column(name = "email", unique = true))
    private Email email;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "firstName", column = @Column(name = "first_name")),
            @AttributeOverride(name = "lastName", column = @Column(name = "last_name"))
    })
    private FullName fullName;

    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Set<Reservation> reservations;

    protected User() {}

    public User(@NonNull Username username, @NonNull Password password, @NonNull Email email,
                @NonNull FullName fullName, @NonNull Gender gender, @NonNull LocalDate dateOfBirth) {
        super(DomainObjectId.randomId(UserId.class));

        Objects.requireNonNull(username, "username must not be null");
        Objects.requireNonNull(password, "password must not be null");
        Objects.requireNonNull(email, "email must not be null");
        Objects.requireNonNull(fullName, "fullName must not be null");
        Objects.requireNonNull(gender, "gender must not be null");
        Objects.requireNonNull(dateOfBirth, "dateOfBirth must not be null");

        if (dateOfBirth.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("The date of birth must not be in the future");
        }

        this.username = username;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;

        this.reservations = new HashSet<>();
    }

    public Reservation createReservation(MovieId movieId, ProjectionId projectionId, Money pricePerSeat,
                                         Short numberOfSeats) {
        if (this.reservations.stream().filter(r -> r.getState().equals(ReservationState.CREATED)).count() >= 3) {
            throw new RuntimeException("A user must not have more than 3 active reservations");
        }
        if (this.reservations.stream().anyMatch(r -> r.getProjectionId() == projectionId)) {
            throw new IllegalArgumentException("A user must not have more than 1 reservation for a single projection");
        }

        Reservation reservation = new Reservation(movieId, projectionId, pricePerSeat, numberOfSeats);
        this.reservations.add(reservation);
        return reservation;
    }

    public Reservation cancelReservation(ReservationId reservationId) {
        Reservation reservation = this.reservations.stream()
                .filter(r -> r.id().equals(reservationId))
                .findFirst()
                .orElseThrow(RuntimeException::new);
        this.reservations.remove(reservation);
        return reservation;
    }

    public Boolean hasReservationForProjection(ProjectionId projectionId) {
        return this.reservations.stream()
                .anyMatch(r -> r.getProjectionId().equals(projectionId));
    }

    public void updateReservationsTotalPrice(ProjectionId projectionId, Money newPricePerSeat) {
        this.reservations.stream()
                .filter(r -> r.getProjectionId().equals(projectionId))
                .findFirst()  // Because maximum 1 reservation can be made for a single projection
                .ifPresent(r -> r.updateTotalPrice(newPricePerSeat));
    }

}
