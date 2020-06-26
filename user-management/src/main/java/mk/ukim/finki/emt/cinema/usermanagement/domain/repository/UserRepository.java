package mk.ukim.finki.emt.cinema.usermanagement.domain.repository;

import mk.ukim.finki.emt.cinema.usermanagement.domain.model.User;
import mk.ukim.finki.emt.cinema.usermanagement.domain.model.UserId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UserId> {
}
