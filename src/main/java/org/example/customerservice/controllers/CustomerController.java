package org.example.customerservice.controllers;
import org.example.customerservice.dto.CustomerDTO;
import org.example.customerservice.dto.DetailedCustomerDTO;
import org.example.customerservice.model.Customer;
import org.example.customerservice.repositories.CustomerRepository;
import org.example.customerservice.services.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RestController
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerRepository repo;

    public CustomerController(CustomerService customerService, CustomerRepository repo) {
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
        return repo.findById(1L).map(Customer::getFirstName).orElse("Unknow customer");
    }

    @RequestMapping("/deletecustomer/{id}")
    public String deleteCustomer(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        boolean result = customerService.deleteCustomer(id);

        if (!result) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete customer. Customer may have active bookings.");
        }

        return "redirect:/allcustomers";

    }

    /*@GetMapping("/allcustomers")
    public String allcustomers(Model model) {
        model.addAttribute("customers", customerService.getAllDetailedCustomersDto());

        return "allCustomers";
    }*/
    @GetMapping("/allcustomers")
    public List<DetailedCustomerDTO> getAllCustomers(){
        return customerService.getAllDetailedCustomersDto();
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute CustomerDTO customer, Model model) {
        boolean sucess = customerService.register(customer.getId(),customer.getFirstName(), customer.getLastName(), customer.getEmail());
        if (!sucess) {
            model.addAttribute("error","Failed to register customer");
            return "register";
        }
        return "redirect:/allcustomers";
    }

    @GetMapping("/editcustomer/{id}")
    public String editCustomer(@PathVariable Long id, Model model) {
        model.addAttribute("customer", customerService.getCustomerById(id));
        return "editCustomer";
    }

    @PostMapping("/editcustomer")
    public String updateCustomer(@ModelAttribute CustomerDTO customer, Model model) {
      boolean sucess=customerService.updateCustomer(customer.getId(), customer.getFirstName(), customer.getLastName(), customer.getEmail());
      if (!sucess) {
          model.addAttribute("error","Failed to update customer");
          return "redirect:/allcustomers";
      }
      model.addAttribute("sucess", "Successfully updated customer details");
        return "redirect:/allcustomers";
    }


}
