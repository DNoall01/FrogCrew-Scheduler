package edu.tcu.cs.frogcrew.invite;

import edu.tcu.cs.frogcrew.invite.domain.Invite;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InviteService {

    // in-memory store: token ➜ Invite
    private final Map<String, Invite> store = new ConcurrentHashMap<>();

    public Invite createInvite(String name, String email, Set<String> roles) {

        /* business rule – e-mail unique among invites */
        boolean exists = store.values().stream()
                .anyMatch(i -> i.getEmail().equalsIgnoreCase(email));
        if (exists) {
            throw new IllegalArgumentException("Email already invited.");
        }

        Invite invite = new Invite();
        invite.setName(name);
        invite.setEmail(email);
        invite.setRoles(roles);

        store.put(invite.getToken(), invite);

        System.out.printf(
            "📧  Sending invite to %s   link: http://localhost:5173/register?token=%s%n",
            email, invite.getToken()
        );

        return invite;
    }

    public Invite getByToken(String token) {
        return store.get(token);
    }

    public void markAccepted(String token) {
        Invite i = store.get(token);
        if (i != null) i.setAccepted(true);
    }
}
