package mk.ukim.finki.emt.cinema.usermanagement.application.service;

import mk.ukim.finki.emt.cinema.sharedkernel.domain.financial.Money;
import mk.ukim.finki.emt.cinema.usermanagement.domain.events.ReservationCancelled;
import mk.ukim.finki.emt.cinema.usermanagement.domain.events.ReservationCreated;
import mk.ukim.finki.emt.cinema.usermanagement.domain.model.*;
import mk.ukim.finki.emt.cinema.usermanagement.domain.model.enumeration.Gender;
import mk.ukim.finki.emt.cinema.usermanagement.domain.repository.UserRepository;
import mk.ukim.finki.emt.cinema.usermanagement.integration.ProjectionPriceChangedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public UserService(UserRepository userRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.userRepository = userRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public UserId createUser(Username username, Password password, Email email, FullName fullName, Gender gender,
                             LocalDate dateOfBirth) {
        return this.userRepository
                .saveAndFlush(new User(username, password, email, fullName, gender, dateOfBirth)).id();
    }

    public ReservationId createReservation(UserId userId, MovieId movieId, ProjectionId projectionId,
                                           Money pricePerSeat, Short numberOfSeats) {
        User user = this.userRepository.findById(userId).orElseThrow(RuntimeException::new);
        Reservation reservation = user.createReservation(movieId, projectionId, pricePerSeat, numberOfSeats);
        this.userRepository.saveAndFlush(user);
        applicationEventPublisher
                .publishEvent(new ReservationCreated(Instant.now(), reservation.id(), movieId, projectionId,
                        numberOfSeats));
        return reservation.id();
    }

    public ReservationId cancelReservation(UserId userId, MovieId movieId, ReservationId reservationId) {
        User user = this.userRepository.findById(userId).orElseThrow(RuntimeException::new);
        Reservation reservation = user.cancelReservation(reservationId);
        this.userRepository.saveAndFlush(user);
        applicationEventPublisher
                .publishEvent(new ReservationCancelled(Instant.now(), reservation.id(), movieId,
                        reservation.getProjectionId(), reservation.getNumberOfSeats()));
        return reservation.id();
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onProjectionPriceChangedEvent(ProjectionPriceChangedEvent event) {
        List<User> users = this.userRepository.findAll().stream()
                .filter(u -> u.hasReservationForProjection(event.getProjectionId()))
                .collect(Collectors.toList());
        users.forEach(u -> u.updateReservationsTotalPrice(event.getProjectionId(), event.getNewPricePerSeat()));
        this.userRepository.saveAll(users);
    }

}
