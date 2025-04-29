package edu.tcu.cs.frogcrew.position;

import edu.tcu.cs.frogcrew.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PositionServiceTest {

    @Mock
    PositionRepository positionRepository;

    @InjectMocks
    PositionService positionService;

    Position p1;
    Position p2;

    @BeforeEach
    void setUp() {
        p1 = new Position();
        p1.setPositionId(1);
        p1.setPositionName("Team Lead");
        p1.setPositionLocation("HQ");

        p2 = new Position();
        p2.setPositionId(2);
        p2.setPositionName("Referee");
        p2.setPositionLocation("End Zone");
    }

    @Test
    void testFindAllPositionNamesSuccess() {
        List<Position> positions = List.of(p1, p2);
        given(positionRepository.findAll()).willReturn(positions);

        List<String> result = positionService.findAllPositionNames();

        assertThat(result).containsExactly("Team Lead", "Referee");
    }

    @Test
    void testFindAllPositionNamesNotFound() {
        given(positionRepository.findAll()).willReturn(List.of());

        List<String> result = positionService.findAllPositionNames();

        assertThat(result).isEmpty();
    }

    @Test
    void testSaveSuccess() {
        given(positionRepository.save(p1)).willReturn(p1);

        Position savedPosition = positionService.save(p1);

        assertThat(savedPosition.getPositionId()).isEqualTo(1);
        assertThat(savedPosition.getPositionName()).isEqualTo("Team Lead");
    }

    @Test
    void testUpdateSuccess() {
        Position updated = new Position();
        updated.setPositionName("Captain");
        updated.setPositionLocation("Field");

        given(positionRepository.findById(1)).willReturn(Optional.of(p1));
        given(positionRepository.save(any(Position.class))).willAnswer(inv -> inv.getArgument(0));

        Position result = positionService.update(1, updated);

        assertThat(result.getPositionName()).isEqualTo("Captain");
        assertThat(result.getPositionLocation()).isEqualTo("Field");
    }


    @Test
    void testUpdateNotFound() {
        Position update = new Position();
        update.setPositionName("Captain");

        given(positionRepository.findById(99)).willReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> positionService.update(99, update));
    }

    @Test
    void testFindByPositionName() {
        given(this.positionRepository.findByPositionName("Team Lead")).willReturn(Optional.of(p1));

        Position result = this.positionService.findByPositionName("Team Lead");

        assertThat(result.getPositionId()).isEqualTo(1);
    }

    @Test
    void testFindByPositionNameNotFound() {
        given(this.positionRepository.findByPositionName("Team Lead")).willReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> this.positionService.findByPositionName("Team Lead"));
    }
}
