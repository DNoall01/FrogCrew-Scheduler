package edu.tcu.cs.frogcrew.template.dto;

import jakarta.validation.constraints.NotEmpty;

public record TemplateDto(Integer templateId,
                          @NotEmpty(message = "Template name is required") String templateName) {
}
