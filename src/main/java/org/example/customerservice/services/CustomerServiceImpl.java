package org.example.customerservice.services;
import org.example.customerservice.dto.CustomerDTO;
import org.example.customerservice.dto.DetailedCustomerDTO;
import org.example.customerservice.model.Customer;
import org.example.customerservice.repositories.CustomerRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service

public class CustomerServiceImpl implements CustomerService {

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;

    }

    private final CustomerRepository customerRepository;

    @Override
    public CustomerDTO customerToCustomerDTO(Customer c){
        return new  CustomerDTO(c.getId(),c.getFirstName(),c.getLastName());
    }
    @Override
    public DetailedCustomerDTO customerToDetailedCustomerDTO(Customer c){
        return new DetailedCustomerDTO(c.getId(),c.getFirstName(),c.getLastName(),c.getEmail());
    }

    @Override
    public List<DetailedCustomerDTO> getAllDetailedCustomersDto() {
        return customerRepository.findAll().stream()
                .map(this::customerToDetailedCustomerDTO)
                .toList();
    }

    @Override
    public boolean register(Long id, String firstName, String lastName, String email) {
        if( firstName == null || lastName == null || email == null ){
            return false;
        }
        if(firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()){
            return false;
        }

        if (customerRepository.findByEmail(email).isPresent()){
            return false;
        }
        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setEmail(email);
        customerRepository.save(customer);

        return true;
    }


    // SKA EJ TAS BORT. SKA FIXAS med någon typ av fråga till order-service/orderService
    // om customer id har booking = true ta inte bort kunden!!!!
    @Override
    public boolean deleteCustomer(Long id) {
        return false;
    }
    /* @Override
    public boolean deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id).orElse(null);

        if (customer == null) {
            return false;
        }
        if (customer.getBookings() != null && !customer.getBookings().isEmpty()) {
            return false;
        }

        customerRepository.deleteById(id);
        return true;

    }*/

    @Override
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    @Override
    public boolean updateCustomer(Long id, String firstname, String lastname, String email) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if (customer != null) {
            customer.setFirstName(firstname);
            customer.setLastName(lastname);
            customer.setEmail(email);
            customerRepository.save(customer);
        }
        return false;
    }

}
