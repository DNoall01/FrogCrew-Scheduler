package edu.tcu.cs.frogcrew.invite.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.Set;

public record InviteRequestDto(
        @NotBlank String name,
        @Email   String email,
        Set<String> roles
) {}
