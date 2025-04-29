package edu.tcu.cs.frogcrew.invite.domain;

import lombok.Data;                 // <-- Lombok generates getters/setters
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Data
public class Invite {

    private final String token = UUID.randomUUID().toString();
    private String name;
    private String email;
    private Set<String> roles;
    private final Instant createdAt = Instant.now();
    private boolean accepted = false;
}
