package api.findev.dto;

import lombok.Data;

@Data
public class UserDto {
    private String id;
    private String email;
    private String role;
    private String avatar;
    private String firstName;
    private String lastName;
}

