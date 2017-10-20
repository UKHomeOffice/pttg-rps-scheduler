package uk.gov.digital.ho.pttg;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AccessCodeTriggerServiceTest {

    private static final String BASE_ACCESS_CODE_URL = "localhost:8888";
    private static final String ACCESS_CODE_URL = "localhost:8888/refresh";
    private static final String HMRC_ACCESS_BASIC_AUTH = "abc:123";

    @Mock
    private RestTemplate restTemplate;

    private AccessCodeTriggerService service;

    @Before
    public void setUp() throws Exception {
        service = new AccessCodeTriggerService(restTemplate, BASE_ACCESS_CODE_URL, HMRC_ACCESS_BASIC_AUTH);
    }

    @Test
    public void shouldCallAccessCodeRefreshUrl() {
        HttpHeaders basicAuthHeaders = new HttpHeaders();
        basicAuthHeaders.set("AUTHENTICATION", "Basic YWJjOjEyMw==");
        HttpEntity entity = new HttpEntity<String>(basicAuthHeaders);

        service.triggerNewAccessCode();
        verify(restTemplate).postForEntity(eq(ACCESS_CODE_URL), eq(entity), eq(ResponseEntity.class));
    }
}