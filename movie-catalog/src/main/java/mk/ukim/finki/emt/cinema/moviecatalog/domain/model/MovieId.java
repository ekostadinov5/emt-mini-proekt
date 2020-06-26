package mk.ukim.finki.emt.cinema.moviecatalog.domain.model;

import lombok.Getter;
import mk.ukim.finki.emt.cinema.sharedkernel.domain.base.DomainObjectId;

import javax.persistence.Embeddable;

@Embeddable
public class MovieId extends DomainObjectId {

    protected MovieId() {
        super(DomainObjectId.randomId(MovieId.class).getId());
    }

    public MovieId(String id) {
        super(id);
    }

}
