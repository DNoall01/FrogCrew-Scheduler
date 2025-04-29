package edu.tcu.cs.frogcrew.gametypeproperties;

import jakarta.validation.constraints.NotEmpty;

import java.time.LocalTime;

public class Properties {
        @NotEmpty(message = "Pay rate is required") private String payRate;
        private LocalTime reportTime;

        public Properties(String payRate, LocalTime reportTime) {
            this.payRate = payRate;
            this.reportTime = reportTime;
        }

        public String getPayRateProperty() {
            return payRate;
        }

        public void setPayRateProperty(String payRate) {
            this.payRate = payRate;
        }

        public LocalTime getReportTimeProperty() {
            return reportTime;
        }

        public void setReportTimeProperty(LocalTime reportTime) {
            this.reportTime = reportTime;
        }
    }
