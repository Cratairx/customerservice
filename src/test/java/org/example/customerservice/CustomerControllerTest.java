package org.example.customerservice;

import org.example.customerservice.controllers.CustomerController;
import org.example.customerservice.dto.DetailedCustomerDTO;
import org.example.customerservice.model.Customer;
import org.example.customerservice.repositories.CustomerRepository;
import org.example.customerservice.services.CustomerClient;
import org.example.customerservice.services.CustomerService;
import org.example.customerservice.services.CustomerServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.client.MockRestServiceServer;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


//@SpringBootTest
//@AutoConfigureMockMvc
@WebMvcTest(controllers = CustomerController.class)
public class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;


    @MockitoBean
    private CustomerService customerService;
    @MockitoBean
    private CustomerRepository customerRepository;
    @MockitoBean
    private CustomerClient customerClient;
    @MockitoBean
    private RestTemplate restTemplate;
    @MockitoBean
    private CustomerServiceImpl  customerServiceImpl;

    private Customer customer (Long id, String firstName, String lastName, String email) {
        Customer customer = new Customer();
        customer.setId(id);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setEmail(email);
        return customer;
    }
    @Test
    void getCustomerById() throws Exception {
        Customer found = customer(1L,"Daniel","Inserte","Daniel@mail.com");
        when(customerService.getCustomerById(1L)).thenReturn(found);
        when(customerService.customerToDetailedCustomerDTO(found))
                .thenReturn(new DetailedCustomerDTO(1L,"Daniel","Inserte","Daniel@mail.com"));

        mockMvc.perform(get("/api/Customer/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Anna"))
                .andExpect(jsonPath("$.lastName").value("Andersson"))
                .andExpect(jsonPath("$.email").value("anna@example.com"));
    }

   /* @Test
    void getCustomerById(){
       CustomerClient customerClient = new CustomerClient();
        Customer customer = customerClient.getCustomerById(1L);
        assertNotNull(customer);
        assertEquals(1L,customer.getId());

    }*/

    @Test
    void registerShouldReturnFalseWhenEmailAlreadyInUse() {
        when(customerRepository.findByEmail("john@example.com"))
                .thenReturn(Optional.of(new Customer()));

       Customer result = customerServiceImpl.register("John", "Doe", "john@example.com");

        assertNull(result);
        verify(customerRepository, never()).save(any());
    }



    @Test
    void registerShouldReturnTrueWhenEmailNotInUse() {
        when(customerRepository.findByEmail("john@example.com")).thenReturn(Optional.empty());

        Customer result = customerServiceImpl.register("John", "Doe", "john@example.com");

        assertNotNull(result);
        verify(customerRepository, times(1)).save(any(Customer.class));

    }
   /* @Test
    void deleteCustomerShouldReturnFalseWhenHasBooking() {
        Customer customer = new Customer();
        customer.setBookings(List.of(new Booking()));

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        boolean result = customerServiceImpl.deleteCustomer(1L);

        assertFalse(result);
        verify(customerRepository, never()).deleteById(any());
    }*/

    @Test
    void updateCustomerShouldReturnTrue() {
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("john@example.com");
        customer.setId(1L);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        customerServiceImpl.updateCustomer(1L, "Emil", "D", "emil@example.com");

        assertEquals("Emil", customer.getFirstName());
        assertEquals("D", customer.getLastName());
        assertEquals("emil@example.com", customer.getEmail());

        verify(customerRepository, times(1)).save(any(Customer.class));
    }
    @Test
    void getCustomerByIdShouldReturnCustomer() {
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setId(1L);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        assertEquals(customer, customerServiceImpl.getCustomerById(1L));

    }

    /*@Test
    void getAllDetailedCustomersDTOShouldReturnCustomers() {

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setBookings(List.of(new Booking()));

        when(customerRepository.findAll()).thenReturn(List.of(customer));

        List<DetailedCustomerDTO> result = customerServiceImpl.getAllDetailedCustomersDto();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());

    }*/
    @Test
    void getCustomerByFirstName(){
        RestTemplate restTemplate = new RestTemplate();
        MockRestServiceServer server = MockRestServiceServer.createServer(restTemplate);

        server.expect(requestTo("http://customer-service/customer"))
                .andRespond(withSuccess(
                        "{\"id\":1,\"firstName\":\"Daniel\",\"lastName\":\"Inserte\"}",


                        MediaType.APPLICATION_JSON));
        Customer customer = restTemplate.getForObject("http://customer-service/customer", Customer.class);
        assertNotNull(customer);
        assertEquals("Daniel",customer.getFirstName());


    }



}
