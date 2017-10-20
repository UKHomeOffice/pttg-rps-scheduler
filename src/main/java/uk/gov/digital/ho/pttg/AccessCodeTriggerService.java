package uk.gov.digital.ho.pttg;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@Slf4j
public class AccessCodeTriggerService {

    private final RestTemplate restTemplate;
    private final String url;
    private String hmrcAccessBasicAuth;

    @Autowired
    public AccessCodeTriggerService(RestTemplate restTemplate,
                                    @Value("${base.hmrc.access.code.url}") String url,
                                    @Value("${hmrc.access.service.auth}") String hmrcAccessBasicAuth) {
        this.restTemplate = restTemplate;
        this.url = url;
        this.hmrcAccessBasicAuth = hmrcAccessBasicAuth;
    }

    @Scheduled(fixedRateString = "${refresh.interval}")
    void triggerNewAccessCode() {
        log.info("Calling HMRC access code refresh at {}", LocalDateTime.now());
        try {
            restTemplate.postForEntity(url + "/refresh", basicAuthRequest(), ResponseEntity.class);
        } catch (RestClientException e) {
            throw new SchedulerException("Error triggering access code refresh", e);
        }
    }

    private HttpEntity basicAuthRequest() {

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();

        headers.set("AUTHENTICATION",
                    String.format("Basic %s", Base64.getEncoder().encodeToString(hmrcAccessBasicAuth.getBytes(Charset.forName("utf-8")))));

        return new HttpEntity(headers);
    }
}
