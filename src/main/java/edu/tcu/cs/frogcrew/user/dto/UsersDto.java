package edu.tcu.cs.frogcrew.user.dto;

import jakarta.validation.constraints.NotEmpty;

public record UsersDto(
        Integer userId,
        @NotEmpty(message = "Full name is required") String fullName,
        @NotEmpty(message = "Email is required") String email,
        @NotEmpty(message = "Phone number is required") String phoneNumber
) {
}
