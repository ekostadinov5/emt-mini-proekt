package mk.ukim.finki.emt.cinema.moviecatalog.port.client;

import mk.ukim.finki.emt.cinema.moviecatalog.integration.ReservationCreatedEventLog;
import mk.ukim.finki.emt.cinema.sharedkernel.domain.base.RemoteEventLog;
import mk.ukim.finki.emt.cinema.sharedkernel.infra.eventlog.RemoteEventLogService;
import mk.ukim.finki.emt.cinema.sharedkernel.infra.eventlog.StoredDomainEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@Service
public class ReservationCreatedRemoteEventLogServiceClient implements RemoteEventLogService {

    private final String source;
    private final RestTemplate restTemplate;

    public ReservationCreatedRemoteEventLogServiceClient(@Value("${app.user-management.url}") String source,
                                                         @Value("${app.user-management.connect-timeout-ms}") int connectTimeout,
                                                         @Value("${app.user-management.read-timeout-ms}") int readTimeout) {
        this.source = Objects.requireNonNull(source, "source must not be null");
        this.restTemplate = new RestTemplate();
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(connectTimeout);
        requestFactory.setReadTimeout(readTimeout);
        restTemplate.setRequestFactory(requestFactory);
    }

    @Override
    public String source() {
        return source;
    }

    @Override
    public RemoteEventLog currentLog(Long lastProcessedEventId) {
        URI currentLogUri = UriComponentsBuilder.fromUriString(source)
                .path(String.format("/api/event-log/%d", lastProcessedEventId)).build().toUri();
        return this.retrieveLog(currentLogUri);
    }

    private RemoteEventLog retrieveLog(@NonNull URI uri) {
        Objects.requireNonNull(uri, "uri must not be null");

        ResponseEntity<List<StoredDomainEvent>> response = restTemplate.exchange(uri, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<StoredDomainEvent>>() {});
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException("Could not retrieve log from URI " + uri);
        }
        return new ReservationCreatedEventLog(response);
    }

}
