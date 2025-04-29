package edu.tcu.cs.frogcrew.config;

import edu.tcu.cs.frogcrew.user.FrogCrewUserService;
import edu.tcu.cs.frogcrew.user.FrogCrewUser;
import edu.tcu.cs.frogcrew.system.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class DataLoader implements CommandLineRunner {

    private final FrogCrewUserService userService;

    public DataLoader(FrogCrewUserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        // only add if not already present
        try {
            userService.findByUsername("admin@example.com");
        } catch (Exception e) {
            FrogCrewUser admin = new FrogCrewUser();
            admin.setUsername("admin@example.com");
            admin.setEmail("admin@example.com");
            admin.setPassword("admin");               // will be BCrypt-encoded
            admin.setFirstName("Site");
            admin.setLastName("Admin");
            admin.setPhoneNumber("000-000-0000");
            admin.setRole(Role.ADMIN);
            admin.setQualifiedPositions(Collections.emptyList());
            userService.addCrewMember(admin);
            System.out.println("==> Seeded Admin: admin@example.com / admin");
        }
    }
}
