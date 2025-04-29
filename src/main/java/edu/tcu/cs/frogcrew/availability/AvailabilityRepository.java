package edu.tcu.cs.frogcrew.availability;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, AvailabilityId> {

    List<Availability> findByFrogCrewUserIdAndGame_Schedule_Id(Integer userId, Integer scheduleId);

    List<Availability> findByFrogCrewUserIdAndGame_Schedule_Season(Integer userId, String season);

}
