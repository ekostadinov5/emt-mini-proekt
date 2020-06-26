package mk.ukim.finki.emt.cinema.moviecatalog.domain.model;

import mk.ukim.finki.emt.cinema.sharedkernel.domain.base.DomainObjectId;

import javax.persistence.Embeddable;

@Embeddable
public class ReservationId extends DomainObjectId {

    protected ReservationId() {
        super(DomainObjectId.randomId(ReservationId.class).getId());
    }

    public ReservationId(String id) {
        super(id);
    }

}
