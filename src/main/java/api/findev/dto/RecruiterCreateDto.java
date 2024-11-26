package api.findev.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class RecruiterCreateDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private UUID company;
}
