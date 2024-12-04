package api.findev.dto;

import java.util.UUID;

public record AuthResponseDto (String name, String token, UUID id) { }
