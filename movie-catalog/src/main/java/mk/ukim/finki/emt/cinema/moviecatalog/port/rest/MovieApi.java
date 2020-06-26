package mk.ukim.finki.emt.cinema.moviecatalog.port.rest;

import mk.ukim.finki.emt.cinema.moviecatalog.application.service.MovieService;
import mk.ukim.finki.emt.cinema.moviecatalog.domain.model.MovieId;
import mk.ukim.finki.emt.cinema.moviecatalog.domain.model.ProjectionId;
import mk.ukim.finki.emt.cinema.moviecatalog.domain.model.enumeration.Genre;
import mk.ukim.finki.emt.cinema.sharedkernel.domain.financial.Money;
import mk.ukim.finki.emt.cinema.sharedkernel.domain.financial.enumeration.Currency;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping(value = "/api/movies", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
public class MovieApi {

    private final MovieService movieService;

    public MovieApi(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping("/create")
    public MovieId createMovie(@RequestParam String name,
                               @RequestParam Genre genre,
                               @RequestParam Short duration) {
        return this.movieService.createMovie(name, genre, duration);
    }

    @PostMapping("/projections/add")
    public ProjectionId addProjection(@RequestParam String movieId,
                                      @RequestParam String date,
                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time,
                                      @RequestParam Short numberOfFreeSeats,
                                      @RequestParam String currency,
                                      @RequestParam Integer amount) {
        LocalDate dateLocalDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        return this.movieService.addProjection(new MovieId(movieId), dateLocalDate, time, numberOfFreeSeats,
                Money.valueOf(Currency.valueOf(currency), amount));
    }

    @PostMapping("/projections/change-price-per-seat")
    public void changePricePerSeatForProjection(@RequestParam String movieId,
                                                @RequestParam String projectionId,
                                                @RequestParam String currency,
                                                @RequestParam Integer amount) {
        this.movieService.changePricePerSeatForProjection(new MovieId(movieId), new ProjectionId(projectionId),
                Money.valueOf(Currency.valueOf(currency), amount));
    }

}
