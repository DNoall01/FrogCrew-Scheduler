package edu.tcu.cs.frogcrew.report;

import edu.tcu.cs.frogcrew.creweduser.CrewedUserRepository;
import edu.tcu.cs.frogcrew.game.Game;
import edu.tcu.cs.frogcrew.schedule.Schedule;
import edu.tcu.cs.frogcrew.schedule.ScheduleRepository;
import edu.tcu.cs.frogcrew.system.exception.ObjectNotFoundException;
import edu.tcu.cs.frogcrew.user.FrogCrewUser;
import edu.tcu.cs.frogcrew.user.FrogCrewUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Transactional
public class ReportService {

    private final ExcelReportGenerator excelReportGenerator;
    private final CrewedUserRepository crewedUserRepository;
    private final FrogCrewUserRepository frogCrewUserRepository;
    private final ScheduleRepository scheduleRepository;

    public ReportService(ExcelReportGenerator excelReportGenerator, CrewedUserRepository crewedUserRepository, FrogCrewUserRepository frogCrewUserRepository, ScheduleRepository scheduleRepository) {
        this.excelReportGenerator = excelReportGenerator;
        this.crewedUserRepository = crewedUserRepository;
        this.frogCrewUserRepository = frogCrewUserRepository;
        this.scheduleRepository = scheduleRepository;
    }

    public byte[] generateCrewMemberReport(Integer userId, String season) throws IOException {
        this.frogCrewUserRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("user", userId));
        return excelReportGenerator.generateCrewMemberReport(userId, season);
    }

    public byte[] generateFinancialReport(String season, String sport) throws IOException {
        return excelReportGenerator.generateFinancialReport(season, sport);
    }

    public byte[] generatePositionReport(Integer positionId, String season) throws IOException {
        return excelReportGenerator.generatePositionReport(positionId, season);
    }

    public byte[] generatePositionReport(Integer positionId, String season, String sport) throws IOException {
        return excelReportGenerator.generatePositionReportForSport(positionId, season, sport);
    }
}
