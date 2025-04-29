package edu.tcu.cs.frogcrew.creweduser;

import edu.tcu.cs.frogcrew.game.Game;
import edu.tcu.cs.frogcrew.game.GameRepository;
import edu.tcu.cs.frogcrew.system.exception.ObjectNotFoundException;
import edu.tcu.cs.frogcrew.user.FrogCrewUser;
import edu.tcu.cs.frogcrew.user.FrogCrewUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class CrewedUserService {

    private final FrogCrewUserRepository frogCrewUserRepository;
    private final GameRepository gameRepository;
    private final CrewedUserRepository crewedUserRepository;

    public CrewedUserService(CrewedUserRepository crewedUserRepository, GameRepository gameRepository, FrogCrewUserRepository frogCrewUserRepository) {
        this.crewedUserRepository = crewedUserRepository;
        this.gameRepository = gameRepository;
        this.frogCrewUserRepository = frogCrewUserRepository;
    }

    public List<FrogCrewUser> findCrewedUsersByAvailabilityAndPosition(Integer gameId, String positionName) {
        Game game = this.gameRepository.findById(gameId).orElseThrow(() -> new ObjectNotFoundException("game", gameId));

        LocalDateTime gameDate = game.getGameDateTime();

        List<FrogCrewUser> qualifiedUsers = this.frogCrewUserRepository.findFrogCrewUsersByQualifiedPositionsContainingIgnoreCase(positionName);

        return qualifiedUsers.stream()
                .filter(user -> isAvailable(user, gameDate))
                .toList();
    }

    private boolean isAvailable(FrogCrewUser frogCrewUser, LocalDateTime gameTime) {
        List<CrewedUser> assignments = crewedUserRepository.findByUser(frogCrewUser);

        for (CrewedUser crewedUser : assignments) {
            if (crewedUser.getGame().getGameDateTime().isEqual(gameTime)) {
                return false;
            }
        }
        return true;
    }
}
