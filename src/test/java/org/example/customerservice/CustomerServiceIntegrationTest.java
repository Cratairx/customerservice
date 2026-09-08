package org.example.customerservice;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.example.customerservice.model.Customer;
import org.example.customerservice.repositories.CustomerRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mysql.MySQLContainer;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
class CustomerServiceIntegrationTest {

    @Container
    static final MySQLContainer MYSQL = new MySQLContainer("mysql:8.0")
            .withDatabaseName("customerdb")
            .withUsername("root")
            .withPassword("root");

    static MockWebServer bookingServiceMock;

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MYSQL::getJdbcUrl);
        registry.add("spring.datasource.username", MYSQL::getUsername);
        registry.add("spring.datasource.password", MYSQL::getPassword);
        registry.add("booking-service.base-url", () -> bookingServiceMock.url("/").toString());
    }
    @Autowired
    private CustomerRepository customerRepository;
    @BeforeAll
    static void startMockCustomerService() throws IOException {
        bookingServiceMock = new MockWebServer();
        bookingServiceMock.start();
    }

    @AfterAll
    static void stopMockCustomerService() throws IOException {
        bookingServiceMock.shutdown();
    }
    Customer customer;
    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
        customerRepository.deleteAll();
        customer = customerRepository.save(new Customer("Daniel","Inserte","daniel.idsas@sds.com"));


    }
    //  room = roomRepository.save(new Room(null, "101", RoomType.SINGLE));

    @AfterEach
    void cleanUp() {
        customerRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Autowired
    private TestRestTemplate restTemplate;


   @Test
    void customerNotDeletedWhenBookingExists() throws IOException {
        bookingServiceMock.enqueue(new MockResponse().setResponseCode(200));
       Long customerId = 1L;
        ResponseEntity<Void> response = restTemplate.postForEntity("http://localhost:8081/api/bookings/exists?customerId={customerId}",null, Void.class, customerId);

       // metoden är inte allowed för att kunden har en bookninig.
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
        assertThat(customerRepository.count()).isEqualTo(1);



    }

}
