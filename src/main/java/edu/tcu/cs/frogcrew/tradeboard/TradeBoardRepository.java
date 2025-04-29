package edu.tcu.cs.frogcrew.tradeboard;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeBoardRepository extends JpaRepository<TradeBoard, Integer> {
}
