package ua.opnu.labwork2.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI hotelBookingOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Система бронювання готелів")
                        .version("1.0.0")
                        .description("""
                                REST API для управління готельним бізнесом: готелі, номери, \
                                зручності, гості та бронювання. \

                                Можливості системи: \
                                ведення довідника готелів та їхніх номерів; \
                                управління каталогом зручностей і прив'язка їх до номерів; \
                                реєстрація гостей та створення бронювань з контролем перетину дат; \
                                перегляд аналітики (кількість готелів, активні бронювання, розподіл за типами номерів); \
                                повнотекстовий пошук готелів, номерів і гостей. \

                                Технологічний стек: Java 17, Spring Boot 3, Spring Data JPA, \
                                Hibernate, PostgreSQL, Bean Validation, Springdoc OpenAPI.""")
                        .contact(new Contact()
                                .name("Кафедра комп'ютерних наук, ОНПУ")
                                .email("variant06.student@op.edu.ua")));
    }
}
