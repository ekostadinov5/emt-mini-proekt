package mk.ukim.finki.emt.cinema.moviecatalog.application.service;

import mk.ukim.finki.emt.cinema.moviecatalog.domain.events.ProjectionPriceChanged;
import mk.ukim.finki.emt.cinema.moviecatalog.domain.model.Movie;
import mk.ukim.finki.emt.cinema.moviecatalog.domain.model.MovieId;
import mk.ukim.finki.emt.cinema.moviecatalog.domain.model.Projection;
import mk.ukim.finki.emt.cinema.moviecatalog.domain.model.ProjectionId;
import mk.ukim.finki.emt.cinema.moviecatalog.domain.model.enumeration.Genre;
import mk.ukim.finki.emt.cinema.moviecatalog.domain.repository.MovieRepository;
import mk.ukim.finki.emt.cinema.moviecatalog.integration.ReservationCancelledEvent;
import mk.ukim.finki.emt.cinema.moviecatalog.integration.ReservationCreatedEvent;
import mk.ukim.finki.emt.cinema.sharedkernel.domain.financial.Money;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
@Transactional
public class MovieService {

    private final MovieRepository movieRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public MovieService(MovieRepository movieRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.movieRepository = movieRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public MovieId createMovie(String name, Genre genre, Short duration) {
        return this.movieRepository.saveAndFlush(new Movie(name, genre, duration)).id();
    }

    public ProjectionId addProjection(MovieId movieId, LocalDate date, LocalTime time, Short numberOfFreeSeats,
                                      Money pricePerSeat) {
        Movie movie = this.movieRepository.findById(movieId).orElseThrow(RuntimeException::new);
        Projection projection = movie.addProjection(date, time, numberOfFreeSeats, pricePerSeat);
        this.movieRepository.saveAndFlush(movie);
        return projection.id();
    }

    public void changePricePerSeatForProjection(MovieId movieId, ProjectionId projectionId, Money newPricePerSeat) {
        Movie movie = this.movieRepository.findById(movieId).orElseThrow(RuntimeException::new);
        movie.changePricePerSeatForProjection(projectionId, newPricePerSeat);
        this.movieRepository.saveAndFlush(movie);
        applicationEventPublisher
                .publishEvent(new ProjectionPriceChanged(Instant.now(), movieId, projectionId, newPricePerSeat));
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onReservationCreatedEvent(ReservationCreatedEvent event) {
        Movie movie = movieRepository.findById(event.getMovieId()).orElseThrow(RuntimeException::new);
        movie.reserveSeatsForProjection(event.getProjectionId(), event.getNumberOfSeats());
        movieRepository.saveAndFlush(movie);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onReservationCancelledEvent(ReservationCancelledEvent event) {
        Movie movie = movieRepository.findById(event.getMovieId()).orElseThrow(RuntimeException::new);
        movie.cancelSeatsForProjection(event.getProjectionId(), event.getNumberOfSeats());
        movieRepository.saveAndFlush(movie);
    }

}
