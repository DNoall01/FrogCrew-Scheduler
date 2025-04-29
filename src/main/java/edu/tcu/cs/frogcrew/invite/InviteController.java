package edu.tcu.cs.frogcrew.invite;

import edu.tcu.cs.frogcrew.invite.dto.InviteRequestDto;
import edu.tcu.cs.frogcrew.invite.domain.Invite;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("${api.endpoint.base-url}/invites")
public class InviteController {

    private final InviteService service;

    public InviteController(InviteService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> invite(@RequestBody @Valid InviteRequestDto dto) {

        Invite invite = service.createInvite(dto.name(), dto.email(), dto.roles());

        return ResponseEntity
                .created(URI.create("/api/invites/" + invite.getToken()))
                .body(Map.of("token", invite.getToken()));
    }

    @GetMapping("/{token}")
    public Invite get(@PathVariable String token) {
        return service.getByToken(token);
    }
}
