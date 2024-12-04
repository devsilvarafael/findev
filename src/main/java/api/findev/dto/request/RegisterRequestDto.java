package api.findev.dto.request;

import api.findev.enums.UserType;

public record RegisterRequestDto(String email, String password, UserType userType) {}
