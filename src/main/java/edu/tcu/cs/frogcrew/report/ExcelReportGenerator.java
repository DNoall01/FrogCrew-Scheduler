package edu.tcu.cs.frogcrew.report;

import edu.tcu.cs.frogcrew.creweduser.CrewedUser;
import edu.tcu.cs.frogcrew.creweduser.CrewedUserRepository;
import edu.tcu.cs.frogcrew.system.exception.ObjectNotFoundException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
public class ExcelReportGenerator {

    private final CrewedUserRepository crewedUserRepository;

    public ExcelReportGenerator(CrewedUserRepository crewedUserRepository) {
        this.crewedUserRepository = crewedUserRepository;
    }

    public byte[] generateCrewMemberReport(Integer userId, String season) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Crew Member Report");

        // Header Row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("User ID");
        headerRow.createCell(1).setCellValue("Full Name");
        headerRow.createCell(2).setCellValue("Position");
        headerRow.createCell(3).setCellValue("Report Time");
        headerRow.createCell(4).setCellValue("Report Location");

        CrewedUser user = this.crewedUserRepository.findByUser_Id(userId);
        // Add crew member data (Here you should retrieve data from DB or other source)
        Row dataRow = sheet.createRow(1);
        dataRow.createCell(0).setCellValue(userId); // Example data
        dataRow.createCell(1).setCellValue(user.getUser().getFullName());
        dataRow.createCell(2).setCellValue(user.getPosition().getPositionName());
        dataRow.createCell(3).setCellValue(user.getGame().getGameTime().toString());
        dataRow.createCell(4).setCellValue(user.getPosition().getPositionLocation());

        // Writing to ByteArrayOutputStream to send as response
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            workbook.write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } finally {
            workbook.close();
        }
    }

    public byte[] generateFinancialReport(String season, String sport) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Financial Report");

        // Header Row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Season");
        headerRow.createCell(1).setCellValue("Sport");
        headerRow.createCell(2).setCellValue("Revenue");
        headerRow.createCell(3).setCellValue("Expense");
        headerRow.createCell(4).setCellValue("Profit");

        // Example data (this should be dynamically retrieved)
        Row dataRow = sheet.createRow(1);
        dataRow.createCell(0).setCellValue(season);
        dataRow.createCell(1).setCellValue(sport);
        dataRow.createCell(2).setCellValue(10000);  // Example revenue
        dataRow.createCell(3).setCellValue(5000);   // Example expense
        dataRow.createCell(4).setCellValue(5000);   // Example profit

        // Writing to ByteArrayOutputStream to send as response
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            workbook.write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } finally {
            workbook.close();
        }
    }

    public byte[] generatePositionReport(Integer positionId, String season) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Position Report");

        // Header Row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Position");
        headerRow.createCell(1).setCellValue("Season");

        Row dataRow = sheet.createRow(1);
        dataRow.createCell(0).setCellValue(positionId);
        dataRow.createCell(1).setCellValue(season);

        // Writing to ByteArrayOutputStream to send as response
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            workbook.write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } finally {
            workbook.close();
        }
    }

    public byte[] generatePositionReportForSport(Integer positionId, String season, String sport) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Position Report for Sport");

        // Header Row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Position");
        headerRow.createCell(1).setCellValue("Season");
        headerRow.createCell(2).setCellValue("Sport");

        // Example data (this should be dynamically retrieved)
        Row dataRow = sheet.createRow(1);
        dataRow.createCell(0).setCellValue(positionId);
        dataRow.createCell(1).setCellValue(season);
        dataRow.createCell(2).setCellValue(sport);

        // Writing to ByteArrayOutputStream to send as response
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            workbook.write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } finally {
            workbook.close();
        }
    }
}
