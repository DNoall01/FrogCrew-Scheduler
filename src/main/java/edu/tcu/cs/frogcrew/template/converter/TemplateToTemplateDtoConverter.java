package edu.tcu.cs.frogcrew.template.converter;

import edu.tcu.cs.frogcrew.template.Template;
import edu.tcu.cs.frogcrew.template.dto.TemplateDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TemplateToTemplateDtoConverter implements Converter<Template, TemplateDto> {

    @Override
    public TemplateDto convert(Template source) {
        return new TemplateDto(source.getTemplateId(), source.getTemplateName());
    }
}
