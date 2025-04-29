package edu.tcu.cs.frogcrew.user.dto;

import edu.tcu.cs.frogcrew.system.Role;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UserDto(
        Integer id,
        @NotEmpty(message = "First name is required") String firstName,
        @NotEmpty(message = "Last name is required") String lastName,
        @NotEmpty(message = "Email is required") String email,
        @NotEmpty(message = "Phone number is required") String phoneNumber,
        @NotNull(message = "Role is required") Role role,
        @NotNull(message = "Positions is required") List<String> positions
) {
}
