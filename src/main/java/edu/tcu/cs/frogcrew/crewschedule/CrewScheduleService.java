package edu.tcu.cs.frogcrew.crewschedule;

import edu.tcu.cs.frogcrew.creweduser.CrewedUser;
import edu.tcu.cs.frogcrew.creweduser.CrewedUserRepository;
import edu.tcu.cs.frogcrew.creweduser.converter.CrewedUserToCrewMemberDtoConverter;
import edu.tcu.cs.frogcrew.creweduser.dto.CrewedMemberDto;
import edu.tcu.cs.frogcrew.crewschedule.dto.CrewScheduleDto;
import edu.tcu.cs.frogcrew.game.Game;
import edu.tcu.cs.frogcrew.game.GameRepository;
import edu.tcu.cs.frogcrew.position.Position;
import edu.tcu.cs.frogcrew.position.PositionRepository;
import edu.tcu.cs.frogcrew.system.exception.ObjectNotFoundException;
import edu.tcu.cs.frogcrew.user.FrogCrewUser;
import edu.tcu.cs.frogcrew.user.FrogCrewUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CrewScheduleService {

    private final GameRepository gameRepository;
    private final FrogCrewUserRepository frogCrewUserRepository;
    private final PositionRepository positionRepository;
    private final CrewedUserRepository crewedUserRepository;
    private final CrewedUserToCrewMemberDtoConverter crewedUserToCrewMemberDtoConverter;

    public CrewScheduleService(GameRepository gameRepository, FrogCrewUserRepository frogCrewUserRepository, PositionRepository positionRepository, CrewedUserRepository crewedUserRepository, CrewedUserToCrewMemberDtoConverter crewedUserToCrewMemberDtoConverter) {
        this.gameRepository = gameRepository;
        this.frogCrewUserRepository = frogCrewUserRepository;
        this.positionRepository = positionRepository;
        this.crewedUserRepository = crewedUserRepository;
        this.crewedUserToCrewMemberDtoConverter = crewedUserToCrewMemberDtoConverter;
    }

    public Game findGameById(Integer gameId) {
        return this.gameRepository.findById(gameId).orElseThrow(() -> new ObjectNotFoundException("game", gameId));
    }

    public List<CrewedUser> addCrewSchedule(Integer gameId, List<CrewedMemberDto> crewScheduleDtos) {
        Game game = this.gameRepository.findById(gameId).orElseThrow(() -> new ObjectNotFoundException("game", gameId));

        List<CrewedUser> savedCrewedUsers = new ArrayList<>();

        for (CrewedMemberDto dto : crewScheduleDtos) {
            FrogCrewUser user = this.frogCrewUserRepository.findById(dto.userId()).orElseThrow(() -> new ObjectNotFoundException("user", dto.userId()));
            Position position = this.positionRepository.findByPositionName(dto.position()).orElseThrow(() -> new ObjectNotFoundException("position", dto.position()));

            CrewedUser crewedUser = new CrewedUser();
            crewedUser.setGame(game);
            crewedUser.setUser(user);
            crewedUser.setPosition(position);

            CrewedUser saved = this.crewedUserRepository.save(crewedUser);
            savedCrewedUsers.add(saved);
        }
        return savedCrewedUsers;
    }

    public List<CrewedUser> updateCrewSchedule(List<CrewedMemberDto> crewMemberDtos) {
        List<CrewedUser> updatedCrewedUsers = new ArrayList<>();

        for (CrewedMemberDto dto : crewMemberDtos) {
            CrewedUser crewedUser = this.crewedUserRepository.findById(dto.crewMemberId()).orElseThrow(() -> new ObjectNotFoundException("crewed user", dto.crewMemberId()));
            FrogCrewUser user = this.frogCrewUserRepository.findById(dto.userId()).orElseThrow(() -> new ObjectNotFoundException("user", dto.userId()));
            Position position = this.positionRepository.findByPositionName(dto.position()).orElseThrow(() -> new ObjectNotFoundException("position", dto.position()));

            crewedUser.setUser(user);
            crewedUser.setPosition(position);
            updatedCrewedUsers.add(crewedUser);
        }

        return this.crewedUserRepository.saveAll(updatedCrewedUsers);
    }

    public void deleteCrewScheduleByGameId(Integer gameId) {
        Game game = this.gameRepository.findById(gameId).orElseThrow(() -> new ObjectNotFoundException("game", gameId));

        List<CrewedUser> crewedUsers = this.crewedUserRepository.findByGame(game);

        if (!crewedUsers.isEmpty()) {
            this.crewedUserRepository.deleteAll(crewedUsers);
        }
    }
}
