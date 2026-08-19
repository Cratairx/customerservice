package org.example.customerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CustomerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }
//
    //
}
/*Kundtjänsten ska vara ett helt nytt Spring Bootprojekt (en egen applikation)
Kundtjänsten ska ha en EGEN databasv(OBS SQLITE/H2 EJ TILLÅTET!!)
Kundtjänsten ansvarar för ALL kundhantering: registrera, ändra och ta bort kunder
Kundtjänsten ska ha ett REST API som svarar med JSON, minst:
hämta en kund
hämta alla kunder (ENBART OM NI HAR EN ADMIN SIDA, ANNARS BORTSE FRÅN DENNA PUNKT!)
registrera en kund
ändra en kunds uppgifter
ta bort en kund
Regeln från Backend 1 gäller fortfarande: en kund får bara tas bort om kunden inte har några aktiva bokningar
Men nu ligger bokningarna i en annan tjänst! Kundtjänsten måste alltså FRÅGA bokningstjänsten om kunden har aktiva bokningar innan kunden tas bort
 */