package com.amit.ecommerce.model;

import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
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
// @AllArgsConstructor
public class User {

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private Integer age;
    private String gender;
    private String email;
    private String phone;
    private String birthDate;
    private String role;
    private String ssn;

    public User(Long id, String firstName, String lastName, Integer age, String gender, String email, String phone,
            String birthDate, String role, String ssn) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
        this.birthDate = birthDate;
        this.role = role;
        this.ssn = ssn;
    }
}
