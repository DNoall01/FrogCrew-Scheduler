package edu.tcu.cs.frogcrew.availability;

import edu.tcu.cs.frogcrew.game.Game;
import edu.tcu.cs.frogcrew.schedule.Schedule;
import edu.tcu.cs.frogcrew.schedule.ScheduleRepository;
import edu.tcu.cs.frogcrew.system.Role;
import edu.tcu.cs.frogcrew.system.exception.ObjectNotFoundException;
import edu.tcu.cs.frogcrew.user.FrogCrewUser;
import edu.tcu.cs.frogcrew.user.FrogCrewUserRepository;
import edu.tcu.cs.frogcrew.user.FrogCrewUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AvailabilityServiceTest {

    @Mock
    AvailabilityRepository availabilityRepository;

    @Mock
    ScheduleRepository scheduleRepository;

    @Mock
    FrogCrewUserRepository frogCrewUserRepository;

    @Mock
    FrogCrewUserService userService;

    @InjectMocks
    AvailabilityService availabilityService;

    private FrogCrewUser user;
    private Schedule schedule;
    private Game game;
    private Availability availability;
    private AvailabilityId availabilityId;

    @BeforeEach
    void setUp() {

        user = new FrogCrewUser();
        user.setId(1);
        user.setFirstName("John");
        user.setLastName("Smith");
        user.setEmail("john.smith@gmail.com");
        user.setPassword("password");
        user.setRole(Role.CREW);
        user.setPhoneNumber("123456789");
        user.setPayRate("10.00");

        schedule = new Schedule();
        schedule.setId(1);
        schedule.setSeason("Spring 2025");

        game = new Game();
        game.setGameId(1);
        game.setSchedule(schedule);
        game.setSport("Basketball");
        game.setGameDateTime(LocalDateTime.of(LocalDate.of(2025, 5, 15), LocalTime.of(12, 0)));
        game.setVenue("Sports Arena");
        game.setOpponent("Team A");
        game.setPositions(Arrays.asList("Coach", "Ref"));

        availability = new Availability();
        availabilityId = new AvailabilityId();
        availabilityId.setUserId(user.getId());
        availabilityId.setGameId(game.getGameId());
        availability.setId(availabilityId);
        availability.setUser(user);
        availability.setGame(game);
        availability.setAvailable(true);
        availability.setComment("Available for the game.");
    }


    @Test
    void testSaveSuccess() {
        Availability availability = new Availability();
        availability.setAvailable(false);

        given(this.availabilityRepository.save(availability)).willReturn(availability);

        Availability returnedAvailability = availabilityService.save(availability);

        assertThat(returnedAvailability.isAvailable()).isEqualTo(availability.isAvailable());
        verify(this.availabilityRepository, times(1)).save(availability);
    }

    @Test
    void testUpdateSuccess() {
        Availability availability = new Availability();
        AvailabilityId availabilityId = new AvailabilityId();
        availabilityId.setUserId(1);
        availabilityId.setGameId(1);
        availability.setId(availabilityId);
        availability.setComment("Original comment");

        Availability update = new Availability();
        update.setComment("Updated comment");

        given(this.availabilityRepository.findById(availabilityId)).willReturn(Optional.of(availability));
        given(this.availabilityRepository.save(availability)).willReturn(availability);

        Availability updatedAvailability = this.availabilityService.update(update, availabilityId);

        assertThat(updatedAvailability.getId()).isEqualTo(availabilityId);
        assertThat(updatedAvailability.getComment()).isEqualTo(update.getComment());
        verify(this.availabilityRepository, times(1)).findById(availabilityId);
        verify(this.availabilityRepository, times(1)).save(availability);
    }

    @Test
    void testFindByUserIdAndScheduleIdSuccess() {
        FrogCrewUser user = new FrogCrewUser();
        user.setId(1);
        user.setFirstName("John");
        user.setLastName("Smith");
        user.setEmail("john.smith@gmail.com");
        user.setPassword("password");
        user.setRole(Role.CREW);
        user.setPhoneNumber("123456789");
        user.setPayRate("10.00");

        Schedule schedule = new Schedule();
        schedule.setId(1);
        schedule.setSeason("Spring 2025");

        Game game = new Game();
        game.setGameId(1);
        game.setSchedule(schedule);
        game.setSport("Basketball");
        game.setGameDateTime(LocalDateTime.of(LocalDate.of(2025, 5, 15), LocalTime.of(12, 0)));
        game.setVenue("Sports Arena");
        game.setOpponent("Team A");
        game.setPositions(Arrays.asList("Coach", "Ref"));

        Availability availability = new Availability();
        AvailabilityId availabilityId = new AvailabilityId();
        availabilityId.setUserId(user.getId());
        availabilityId.setGameId(game.getGameId());
        availability.setId(availabilityId);
        availability.setUser(user);
        availability.setGame(game);
        availability.setAvailable(true);
        availability.setComment("Available for the game.");

        // Mock repository methods
        given(this.frogCrewUserRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(this.scheduleRepository.findById(schedule.getId())).willReturn(Optional.of(schedule));
        given(this.availabilityRepository.findByFrogCrewUserIdAndGame_Schedule_Id(user.getId(), schedule.getId()))
                .willReturn(List.of(availability)); // Ensure the repository returns a list with the availability

        // Call service method
        List<Availability> returnedAvailability = this.availabilityService.findByUserIdAndScheduleId(user.getId(), schedule.getId());

        // Assertions
        assertThat(returnedAvailability.size()).isEqualTo(1); // Expect 1 result
        verify(this.frogCrewUserRepository, times(1)).findById(user.getId());
        verify(this.scheduleRepository, times(1)).findById(schedule.getId());
        verify(this.availabilityRepository, times(1)).findByFrogCrewUserIdAndGame_Schedule_Id(user.getId(), schedule.getId());
    }

    @Test
    void testFindByUserIdAndScheduleIdNotFound() {

        given(this.frogCrewUserRepository.findById(Mockito.any(Integer.class))).willReturn(Optional.empty());

        Throwable thrown = catchThrowable(() -> {
            List<Availability> returnedAvailabilities = this.availabilityService.findByUserIdAndScheduleId(1, Mockito.any(Integer.class));
        });

        assertThat(thrown).isInstanceOf(ObjectNotFoundException.class).hasMessage("Could not find user with id 1");
        verify(this.frogCrewUserRepository, times(1)).findById(1);
    }

    @Test
    void testFindByUserIdAndSeasonSuccess() {

        given(this.frogCrewUserRepository.findById(1)).willReturn(Optional.of(new FrogCrewUser()));
        given(this.availabilityRepository.findByFrogCrewUserIdAndGame_Schedule_Season(user.getId(), schedule.getSeason())).willReturn(List.of(new Availability()));

        List<Availability> availability = this.availabilityService.findByUserIdAndSeason(1, "Spring 2025");

        assertThat(availability.size()).isEqualTo(1);
        verify(this.frogCrewUserRepository, times(1)).findById(1);

    }

    @Test
    void testFindByUserIdAndSeasonNotFound() {
        given(this.frogCrewUserRepository.findById(1)).willReturn(Optional.empty());

        Throwable thrown = catchThrowable(() -> {
            List<Availability> returnedAvailabilities = this.availabilityService.findByUserIdAndSeason(1, "Spring 2025");
        });

        assertThat(thrown).isInstanceOf(ObjectNotFoundException.class).hasMessage("Could not find user with id 1");
        verify(this.frogCrewUserRepository, times(1)).findById(1);
    }

}
