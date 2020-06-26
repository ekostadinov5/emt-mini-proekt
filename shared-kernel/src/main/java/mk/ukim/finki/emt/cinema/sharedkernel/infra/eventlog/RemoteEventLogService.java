package mk.ukim.finki.emt.cinema.sharedkernel.infra.eventlog;

import mk.ukim.finki.emt.cinema.sharedkernel.domain.base.RemoteEventLog;

public interface RemoteEventLogService {

    String source();

    RemoteEventLog currentLog(Long lastProcessedEventId);

}
