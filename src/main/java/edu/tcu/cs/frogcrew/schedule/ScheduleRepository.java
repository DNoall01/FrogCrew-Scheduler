package edu.tcu.cs.frogcrew.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {

    List<Schedule> findSchedulesBySeason(String season);

    @Query("SELECT DISTINCT s.sport FROM Schedule s")
    List<String> findDistinctSports();

    Schedule findScheduleBySeasonAndSport(String season, String sport);
}
