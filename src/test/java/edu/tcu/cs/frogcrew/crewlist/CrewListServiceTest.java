package edu.tcu.cs.frogcrew.crewlist;

import edu.tcu.cs.frogcrew.creweduser.CrewedUserRepository;
import edu.tcu.cs.frogcrew.crewlist.converter.GameToCrewListDtoConverter;
import edu.tcu.cs.frogcrew.crewlist.dto.CrewListDto;
import edu.tcu.cs.frogcrew.game.Game;
import edu.tcu.cs.frogcrew.game.GameRepository;
import edu.tcu.cs.frogcrew.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CrewListServiceTest {

    @Mock
    GameRepository gameRepository;

    @Mock
    GameToCrewListDtoConverter gameToCrewListDtoConverter;

    @Mock
    CrewedUserRepository crewedUserRepository;

    @InjectMocks
    CrewListService crewListService;

    Game game;

    @BeforeEach
    void setup() {
        game = new Game();
        game.setGameId(1);
        // Set other fields as needed if required
    }

    @Test
    void testFindCrewListByGameIdSuccess() {
        // Arrange
        CrewListDto expectedDto = new CrewListDto(
                1,
                null,
                null,
                "Venue",
                "Opponent",
                List.of()
        );

        given(gameRepository.findById(1)).willReturn(Optional.of(game));
        given(gameToCrewListDtoConverter.convert(game)).willReturn(expectedDto);
        CrewListDto actualDto = crewListService.findCrewListByGameId(1);

        // Assert
        assertThat(actualDto).isNotNull();
        assertThat(actualDto.gameId()).isEqualTo(1);
        assertThat(actualDto.venue()).isEqualTo("Venue");
        verify(this.gameRepository, times(1)).findById(1);
        verify(this.gameToCrewListDtoConverter, times(1)).convert(game);
    }

    @Test
    void testFindCrewListByGameIdNotFound() {
        // Arrange
        given(this.gameRepository.findById(99)).willReturn(Optional.empty());

        Throwable thrown = catchThrowable(() -> {
            CrewListDto actualDto = crewListService.findCrewListByGameId(99);
        });

        assertThat(thrown).isInstanceOf(ObjectNotFoundException.class).hasMessage("Could not find game with id 99");
    }


}
