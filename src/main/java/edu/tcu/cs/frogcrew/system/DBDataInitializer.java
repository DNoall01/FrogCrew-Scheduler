package edu.tcu.cs.frogcrew.system;

import edu.tcu.cs.frogcrew.game.Game;
import edu.tcu.cs.frogcrew.game.GameRepository;
import edu.tcu.cs.frogcrew.schedule.Schedule;
import edu.tcu.cs.frogcrew.schedule.ScheduleRepository;
import edu.tcu.cs.frogcrew.user.FrogCrewUser;
import edu.tcu.cs.frogcrew.user.FrogCrewUserService;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Component
public class DBDataInitializer implements CommandLineRunner {

    private final FrogCrewUserService userService;

    private final GameRepository gameRepository;

    private final ScheduleRepository scheduleRepository;

    public DBDataInitializer(FrogCrewUserService userService, GameRepository gameRepository, ScheduleRepository scheduleRepository) {
        this.userService = userService;
        this.gameRepository = gameRepository;
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        FrogCrewUser u1 = new FrogCrewUser();
        u1.setUsername("dnoall@gmail.com");
        u1.setPassword("password");
        u1.setFirstName("Dylan");
        u1.setLastName("Noall");
        u1.setEmail("dnoall@gmail.com");
        u1.setPhoneNumber("1234567890");
        u1.setRole(Role.ADMIN);
        u1.setEnabled(true);
        u1.setQualifiedPositions(List.of("Director"));

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

        Game game = new Game();
        game.setGameDateTime(LocalDateTime.now());
        game.setVenue("Home");
        game.setOpponent("Opponent 1");
        game.setSport("Football");
        game.setCrewMembers(List.of());
        gameRepository.save(game);

        Schedule s1 = new Schedule();
        s1.setSeason("Spring 2025");
        s1.setSport("Football");
        s1.setGames(List.of(game));

        scheduleRepository.save(s1);


        userService.addCrewMember(u1);
    }
}
