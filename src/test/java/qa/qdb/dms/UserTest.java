//package qa.qdb.dms;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.http.*;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringRunner;
//import qa.qdb.dms.response.UserDTO;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = DocumentManagementSystemApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ActiveProfiles("default")
//public class UserTest {
//
//    private static Logger LOG = LoggerFactory.getLogger(UserTest.class);
//
//    TestRestTemplate restTemplate = new TestRestTemplate();
//
//    HttpHeaders headers = new HttpHeaders();
//
//    @Test
//    public void addUsers() {
//
//        String url = "http://localhost:8080/credit/dms/users/add";
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<String> requestEntity = new HttpEntity<>(new HttpHeaders());
//        Map<String, String> params = new HashMap<>();
//        params.put("firstName", "Mandla");
//
//        ResponseEntity<String> response = this.restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class, params);
//
//        assertThat(response.getBody()).isNotNull();
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(response.getBody()).isEqualTo("User created successfully");
//
//        params.replace("firstName", "Mandla", "John");
//
//        ResponseEntity<String> secondResponse = this.restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class, params);
//
//        assertThat(secondResponse.getBody()).isNotNull();
//        assertThat(secondResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(secondResponse.getBody()).isEqualTo("User created successfully");
//    }
//
//    @Test
//    public void findUsers() {
//        String url = "http://localhost:8080/credit/dms/users/findone";
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<String> requestEntity = new HttpEntity<>(new HttpHeaders());
//        Map<String, Integer> params = new HashMap<>();
//        params.put("userId", 1);
//
//        ResponseEntity<String> response = this.restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class, params);
//        assertThat(response).isNotNull();
//
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        try {
//            UserDTO userResponse = objectMapper.readValue(response.getBody(), UserDTO.class);
//            assertThat(userResponse.getFirstName()).isEqualTo("Mandla");
//        } catch(JsonProcessingException ex) {
//            LOG.error("Exception handling response from {}", url);
//        } catch (IOException e) {
//            LOG.error("Exception handling response from {}", url);
//        }
//    }
//}
