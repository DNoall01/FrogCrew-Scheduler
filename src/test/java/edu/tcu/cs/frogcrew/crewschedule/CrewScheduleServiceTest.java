package edu.tcu.cs.frogcrew.crewschedule;

import edu.tcu.cs.frogcrew.creweduser.CrewedUser;
import edu.tcu.cs.frogcrew.creweduser.CrewedUserRepository;
import edu.tcu.cs.frogcrew.creweduser.converter.CrewedUserToCrewMemberDtoConverter;
import edu.tcu.cs.frogcrew.creweduser.dto.CrewedMemberDto;
import edu.tcu.cs.frogcrew.game.Game;
import edu.tcu.cs.frogcrew.game.GameRepository;
import edu.tcu.cs.frogcrew.position.Position;
import edu.tcu.cs.frogcrew.position.PositionRepository;
import edu.tcu.cs.frogcrew.system.exception.ObjectNotFoundException;
import edu.tcu.cs.frogcrew.user.FrogCrewUser;
import edu.tcu.cs.frogcrew.user.FrogCrewUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CrewScheduleServiceTest {

    @Mock
    GameRepository gameRepository;

    @Mock
    FrogCrewUserRepository frogCrewUserRepository;

    @Mock
    PositionRepository positionRepository;

    @Mock
    CrewedUserRepository crewedUserRepository;

    @Mock
    CrewedUserToCrewMemberDtoConverter crewedUserToCrewMemberDtoConverter;

    @InjectMocks
    CrewScheduleService crewScheduleService;

    Game mockGame;
    FrogCrewUser mockUser;
    Position mockPosition;

    @BeforeEach
    void setUp() {
        mockGame = new Game();
        mockGame.setGameId(1);

        mockUser = new FrogCrewUser();
        mockUser.setId(10);

        mockPosition = new Position();
        mockPosition.setPositionName("Camera");
    }

    @Test
    void testFindGameByIdSuccess() {
        given(this.gameRepository.findById(1)).willReturn(Optional.of(mockGame));

        Game found = crewScheduleService.findGameById(1);

        assertThat(found).isEqualTo(mockGame);
        verify(gameRepository, times(1)).findById(1);
    }

    @Test
    void testFindGameByIdNotFound() {
        given(gameRepository.findById(1)).willReturn(Optional.empty());

        Throwable thrown = catchThrowable(() -> crewScheduleService.findGameById(1));

       assertThat(thrown).isInstanceOf(ObjectNotFoundException.class);
       verify(gameRepository, times(1)).findById(1);
    }

    @Test
    void testAddCrewScheduleSuccess() {
        CrewedMemberDto dto = new CrewedMemberDto(null, 10, 1, "John Smith", "Camera", LocalTime.of(12, 30), "Sideline");
        List<CrewedMemberDto> dtos = List.of(dto);

        given(this.gameRepository.findById(1)).willReturn(Optional.of(mockGame));
        given(this.frogCrewUserRepository.findById(10)).willReturn(Optional.of(mockUser));
        given(this.positionRepository.findByPositionName("Camera")).willReturn(Optional.of(mockPosition));

        CrewedUser savedUser = new CrewedUser();
        given(crewedUserRepository.save(any(CrewedUser.class))).willReturn(savedUser);

        List<CrewedUser> result = crewScheduleService.addCrewSchedule(1, dtos);

        assertThat(result).hasSize(1);
        verify(crewedUserRepository).save(any(CrewedUser.class));
    }

    @Test
    void testUpdateCrewScheduleSuccess() {
        CrewedMemberDto dto = new CrewedMemberDto(100, 10, 1, "John Smith", "Camera", LocalTime.of(12, 30), "Sideline");

        CrewedUser existingCrewedUser = new CrewedUser();
        existingCrewedUser.setCrewedUserId(100);

        given(this.crewedUserRepository.findById(100)).willReturn(Optional.of(existingCrewedUser));
        given(this.frogCrewUserRepository.findById(10)).willReturn(Optional.of(mockUser));
        given(this.positionRepository.findByPositionName("Camera")).willReturn(Optional.of(mockPosition));
        given(this.crewedUserRepository.saveAll(anyList())).willAnswer(invocation -> invocation.getArgument(0));

        List<CrewedUser> result = crewScheduleService.updateCrewSchedule(List.of(dto));

        assertThat(result).hasSize(1);
        verify(crewedUserRepository).saveAll(anyList());
    }

    @Test
    void testDeleteCrewScheduleByGameIdSuccess() {
        given(this.gameRepository.findById(1)).willReturn(Optional.of(mockGame));
        given(this.crewedUserRepository.findByGame(mockGame)).willReturn(List.of(new CrewedUser()));

        crewScheduleService.deleteCrewScheduleByGameId(1);

        verify(this.crewedUserRepository, Mockito.never()).delete(any());
    }
}
