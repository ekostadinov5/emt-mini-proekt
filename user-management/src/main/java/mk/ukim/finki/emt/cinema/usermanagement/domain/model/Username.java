package mk.ukim.finki.emt.cinema.usermanagement.domain.model;

import lombok.Getter;
import mk.ukim.finki.emt.cinema.sharedkernel.domain.base.ValueObject;
import org.springframework.lang.NonNull;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
@Getter
public class Username implements ValueObject {

    private String username;

    protected Username() {}

    public Username(@NonNull String username) {
        Objects.requireNonNull(username, "username must not be null");

        if (username.length() < 3) {
            throw new IllegalArgumentException("The username must be at least three characters long");
        }

        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Username username1 = (Username) o;
        return username.equals(username1.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public String toString() {
        return "Username{" +
                "username='" + username + '\'' +
                '}';
    }

}
