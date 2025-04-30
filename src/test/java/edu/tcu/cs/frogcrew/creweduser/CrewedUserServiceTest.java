package edu.tcu.cs.frogcrew.creweduser;

import edu.tcu.cs.frogcrew.game.Game;
import edu.tcu.cs.frogcrew.game.GameRepository;
import edu.tcu.cs.frogcrew.position.Position;
import edu.tcu.cs.frogcrew.user.FrogCrewUser;
import edu.tcu.cs.frogcrew.user.FrogCrewUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CrewedUserServiceTest {

    @Mock
    CrewedUserRepository crewedUserRepository;

    @Mock
    GameRepository gameRepository;

    @Mock
    FrogCrewUserRepository frogCrewUserRepository;

    @InjectMocks
    CrewedUserService crewedUserService;

    FrogCrewUser frogUser;
    Position position;
    Game game;
    CrewedUser u1;

    @BeforeEach
    void setUp() {
        frogUser = new FrogCrewUser();
        frogUser.setFirstName("John");
        frogUser.setLastName("Smith");
        frogUser.setEmail("john.smith@gmail.com");

        position = new Position();
        position.setPositionName("Coach");
        position.setPositionLocation("Sideline");

        game = new Game();
        game.setGameId(1);
        game.setVenue("Home");
        game.setGameDateTime(LocalDateTime.of(LocalDate.of(2025, 5, 15), LocalTime.of(12, 30)));
        game.setOpponent("OU");
        game.setSport("Football");
        game.setPositions(List.of("Director", "Producer"));

        u1 = new CrewedUser();
        u1.setUser(frogUser);
        u1.setPosition(position);
        u1.setGame(game);

    }

    @Test
    void testFindCrewMembersByAvailabilityAndPosition() {
        given(this.gameRepository.findById(game.getGameId()))
                .willReturn(Optional.of(game));

        given(this.frogCrewUserRepository.findFrogCrewUsersByQualifiedPositionsContainingIgnoreCase(position.getPositionName()))
                .willReturn(List.of(frogUser));

        given(this.crewedUserRepository.findByUser(frogUser))
                .willReturn(List.of());

        List<FrogCrewUser> foundUsers = this.crewedUserService.findCrewedUsersByAvailabilityAndPosition(
                game.getGameId(),
                position.getPositionName()
        );

        assertThat(foundUsers.size()).isEqualTo(1);
        assertThat(foundUsers.get(0).getFirstName()).isEqualTo(frogUser.getFirstName());
        assertThat(foundUsers.get(0).getLastName()).isEqualTo(frogUser.getLastName());
        assertThat(foundUsers.get(0).getEmail()).isEqualTo(frogUser.getEmail());

        verify(this.gameRepository, times(1)).findById(game.getGameId());
        verify(this.frogCrewUserRepository, times(1)).findFrogCrewUsersByQualifiedPositionsContainingIgnoreCase(position.getPositionName());
        verify(this.crewedUserRepository, times(1)).findByUser(frogUser);
    }

    @Test
    void testFindCrewMembersByAvailabilityAndPositionUserNotAvailable() {
        given(this.gameRepository.findById(game.getGameId()))
                .willReturn(Optional.of(game));

        given(this.frogCrewUserRepository.findFrogCrewUsersByQualifiedPositionsContainingIgnoreCase(position.getPositionName()))
                .willReturn(List.of(frogUser));

        given(this.crewedUserRepository.findByUser(frogUser))
                .willReturn(List.of(u1));

        List<FrogCrewUser> foundUsers = this.crewedUserService.findCrewedUsersByAvailabilityAndPosition(
                game.getGameId(),
                position.getPositionName()
        );

        assertThat(foundUsers).isEmpty();

        verify(this.gameRepository, times(1)).findById(game.getGameId());
        verify(this.frogCrewUserRepository, times(1)).findFrogCrewUsersByQualifiedPositionsContainingIgnoreCase(position.getPositionName());
        verify(this.crewedUserRepository, times(1)).findByUser(frogUser);
    }

}
