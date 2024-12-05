package api.findev.dto;

import api.findev.enums.UserType;

import java.util.UUID;

public record AuthResponseDto (String name, String token, UUID id, UserType role, String avatar) { }
