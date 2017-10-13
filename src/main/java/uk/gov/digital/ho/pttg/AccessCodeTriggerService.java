package uk.gov.digital.ho.pttg;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
@Slf4j
public class AccessCodeTriggerService {

    private final RestTemplate restTemplate;
    private final String url;

    @Autowired
    public AccessCodeTriggerService(RestTemplate restTemplate, @Value("${base.hmrc.access.code.url}") String url) {
        this.restTemplate = restTemplate;
        this.url = url;
    }

    @Scheduled(fixedRateString = "${refresh.interval}")
    void triggerNewAccessCode() {
        log.info("Calling HMRC access code refresh at {}", LocalDateTime.now());
        try {
            restTemplate.postForEntity(url + "/refresh", null, ResponseEntity.class);
        } catch (RestClientException e) {
            throw new SchedulerException("Error triggering access code refresh", e);
        }
    }

}
