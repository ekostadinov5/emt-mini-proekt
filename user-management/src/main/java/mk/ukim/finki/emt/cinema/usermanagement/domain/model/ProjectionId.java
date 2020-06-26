package mk.ukim.finki.emt.cinema.usermanagement.domain.model;

import mk.ukim.finki.emt.cinema.sharedkernel.domain.base.DomainObjectId;

import javax.persistence.Embeddable;

@Embeddable
public class ProjectionId extends DomainObjectId {

    protected ProjectionId() {
        super(DomainObjectId.randomId(ProjectionId.class).getId());
    }

    public ProjectionId(String id) {
        super(id);
    }

}
