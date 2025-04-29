package edu.tcu.cs.frogcrew.report;


import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("${api.endpoint.base-url}/report")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/crewMember/{userId}/{season}")
    public ResponseEntity<byte[]> generateCrewMemberReport(@PathVariable Integer userId, @PathVariable String season) throws Exception {
        byte[] excelData = this.reportService.generateCrewMemberReport(userId, season);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDisposition(ContentDisposition.attachment().filename("crewMemberReport.xlsx").build());

        return ResponseEntity.ok().headers(headers).body(excelData);
    }

    @GetMapping("/financial/{season}/{sport}")
    public ResponseEntity<byte[]> generateFinancialReport(@PathVariable String season, @PathVariable String sport) throws Exception {
        byte[] excelData = this.reportService.generateFinancialReport(season, sport);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDisposition(ContentDisposition.attachment().filename("financialReport.xlsx").build());

        return ResponseEntity.ok().headers(headers).body(excelData);
    }

    @GetMapping("/position/{positionId}/{season}")
    public ResponseEntity<byte[]> generatePositionReport(@PathVariable Integer positionId, @PathVariable String season) throws Exception {
        byte[] excelData = this.reportService.generatePositionReport(positionId, season);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDisposition(ContentDisposition.attachment().filename("positionReport.xlsx").build());
        return ResponseEntity.ok().headers(headers).body(excelData);
    }

    @GetMapping("/position/{positionId}/{season}/{sport}")
    public ResponseEntity<byte[]> generatePositionReport(@PathVariable Integer positionId, @PathVariable String season, @PathVariable String sport) throws Exception {
        byte[] excelData = this.reportService.generatePositionReport(positionId, season, sport);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.presentationml.presentation"));
        headers.setContentDisposition(ContentDisposition.attachment().filename("positionReport.xlsx").build());
        return ResponseEntity.ok().headers(headers).body(excelData);
    }


}
