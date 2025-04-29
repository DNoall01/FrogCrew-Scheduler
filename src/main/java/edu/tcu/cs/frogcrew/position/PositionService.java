package edu.tcu.cs.frogcrew.position;

import edu.tcu.cs.frogcrew.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class PositionService {

    private final PositionRepository positionRepository;

    public PositionService(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    public List<String> findAllPositionNames() {
        return this.positionRepository.findAll().stream()
                .map(Position::getPositionName)
                .toList();
    }

    public Position save(Position position) {
        return this.positionRepository.save(position);
    }

    // TODO: Fix error handler
    public Position update(Integer positionId, Position update) {
        return this.positionRepository.findById(positionId)
                .map(oldPosition -> {
                    oldPosition.setPositionName(update.getPositionName());
                    oldPosition.setPositionLocation(update.getPositionLocation());
                    return positionRepository.save(oldPosition);
                }).orElseThrow(() -> new ObjectNotFoundException("position", positionId));
    }

    public Position findByPositionName(String positionName) {
        return this.positionRepository.findByPositionName(positionName).orElseThrow(() -> new RuntimeException("Position not found"));
    }
}
