package org.example.customerservice.controllers;
import org.example.customerservice.dto.CustomerDTO;
import org.example.customerservice.dto.DetailedCustomerDTO;
import org.example.customerservice.model.Customer;
import org.example.customerservice.repositories.CustomerRepository;
import org.example.customerservice.services.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RestController
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerRepository repo;

    public CustomerController(CustomerService customerService, CustomerRepository repo, ResponseEntityExceptionHandler responseEntityExceptionHandler) {
        this.customerService = customerService;
        this.repo = repo;
    }

    @GetMapping("/index")
    public String index() {
        return "Index";
    }

    @GetMapping("/")
    public String home() {
        return "Index";
    }

    @GetMapping("/customer")
    public String customer() {
        return repo.findById(1L).map(Customer::getFirstName).orElse("Unknown customer");
    }

    @RequestMapping("/deletecustomer/{id}")
    public String deleteCustomer(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        boolean result = customerService.deleteCustomer(id);

        if (!result) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete customer. Customer may have active bookings.");
        }

        return "redirect:/allcustomers";

    }

    @GetMapping("/api/customers")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(repo.findAll());
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/api/customer")
    public ResponseEntity<CustomerDTO> registerCustomer(@RequestBody CustomerDTO customer) {
        boolean success = customerService.register(customer.getFirstName(),customer.getLastName(),customer.getEmail());
        if (!success) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/api/editcustomer")
    public ResponseEntity<CustomerDTO> editCustomer(@RequestBody CustomerDTO customer, @RequestBody Long id) {
        boolean success = customerService.getCustomerById(id).equals(customer);
        if (!success) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/api/editcustomer")
    public ResponseEntity<CustomerDTO> updateCustomer(@RequestBody CustomerDTO customer) {
        boolean success = customerService.updateCustomer(customer.getId(),customer.getFirstName(),customer.getLastName(),customer.getEmail());
        if (!success) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/api/customer")
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable Long id) {
        boolean success = customerService.getCustomerById(id) == null;
        if (!success) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/api/customer")
    public ResponseEntity<CustomerDTO> getPostCustomer(@RequestBody CustomerDTO customer) {
        boolean success = customerService.getCustomerById(customer.getId()) == null;
        if (!success) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}



