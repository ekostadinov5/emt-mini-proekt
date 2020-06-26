package mk.ukim.finki.emt.cinema.moviecatalog.domain.repository;

import mk.ukim.finki.emt.cinema.moviecatalog.domain.model.Movie;
import mk.ukim.finki.emt.cinema.moviecatalog.domain.model.MovieId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, MovieId> {
}
