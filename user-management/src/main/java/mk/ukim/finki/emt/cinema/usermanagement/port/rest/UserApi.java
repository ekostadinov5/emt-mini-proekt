package mk.ukim.finki.emt.cinema.usermanagement.port.rest;

import mk.ukim.finki.emt.cinema.sharedkernel.domain.financial.Money;
import mk.ukim.finki.emt.cinema.sharedkernel.domain.financial.enumeration.Currency;
import mk.ukim.finki.emt.cinema.usermanagement.application.service.UserService;
import mk.ukim.finki.emt.cinema.usermanagement.domain.model.*;
import mk.ukim.finki.emt.cinema.usermanagement.domain.model.enumeration.Gender;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping(path = "/api/users", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
public class UserApi {

    private final UserService userService;

    public UserApi(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public UserId createUser(@RequestParam String username,
                             @RequestParam String password,
                             @RequestParam String email,
                             @RequestParam String firstName,
                             @RequestParam String lastName,
                             @RequestParam Gender gender,
                             @RequestParam String dateOfBirth) {
        LocalDate dateOfBirthLocalDate = LocalDate.parse(dateOfBirth, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        return this.userService.createUser(new Username(username), new Password(password), new Email(email),
                new FullName(firstName, lastName), gender, dateOfBirthLocalDate);
    }

    @PostMapping("/reservations/create")
    public ReservationId createReservation(@RequestParam String userId,
                                           @RequestParam String movieId,
                                           @RequestParam String projectionId,
                                           @RequestParam String currency,
                                           @RequestParam Integer amount,
                                           @RequestParam Short numberOfSeats) {
        return this.userService.createReservation(new UserId(userId), new MovieId(movieId),
                new ProjectionId(projectionId), Money.valueOf(Currency.valueOf(currency), amount), numberOfSeats);
    }

    @PostMapping("/reservations/cancel")
    public ReservationId cancelReservation(@RequestParam String userId,
                                           @RequestParam String reservationId,
                                           @RequestParam String movieId) {
        return this.userService.cancelReservation(new UserId(userId), new MovieId(movieId),
                new ReservationId(reservationId));
    }

}
