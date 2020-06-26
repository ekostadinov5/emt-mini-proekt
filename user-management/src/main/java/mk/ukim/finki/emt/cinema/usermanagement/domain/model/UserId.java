package mk.ukim.finki.emt.cinema.usermanagement.domain.model;

import mk.ukim.finki.emt.cinema.sharedkernel.domain.base.DomainObjectId;

public class UserId extends DomainObjectId {

    public UserId() {
        super(DomainObjectId.randomId(UserId.class).getId());
    }

    public UserId(String id) {
        super(id);
    }

}
