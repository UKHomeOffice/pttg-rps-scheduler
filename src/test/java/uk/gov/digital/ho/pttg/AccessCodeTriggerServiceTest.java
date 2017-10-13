package uk.gov.digital.ho.pttg;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AccessCodeTriggerServiceTest {

    public static final String BASE_ACCESS_CODE_URL = "localhost:8888";
    public static final String ACCESS_CODE_URL = "localhost:8888/refresh";

    @Mock
    private RestTemplate restTemplate;

    private AccessCodeTriggerService service;

    @Before
    public void setUp() throws Exception {
        service = new AccessCodeTriggerService(restTemplate, BASE_ACCESS_CODE_URL);
    }

    @Test
    public void shouldCallAccessCodeRefreshUrl() {
        service.triggerNewAccessCode();
        verify(restTemplate).postForEntity(eq(ACCESS_CODE_URL), Matchers.isNull(), eq(ResponseEntity.class));
    }
}