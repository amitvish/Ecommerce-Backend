package com.amit.ecommerce.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
public class UserResponseDTO {

    private List<UserDTO> users;

    public List<UserDTO> getUsers() {
        return users;
    }
}
