package com.amit.ecommerce.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private Long id;

    private String firstName;
    private String lastName;
    private Integer age;
    private String gender;
    private String email;
    private String phone;
    private String birthDate;
    private String image;
    private String role;
    private String ssn;
}
