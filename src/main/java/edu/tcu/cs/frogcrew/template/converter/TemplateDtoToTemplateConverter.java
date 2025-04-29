package edu.tcu.cs.frogcrew.template.converter;

import edu.tcu.cs.frogcrew.template.Template;
import edu.tcu.cs.frogcrew.template.dto.TemplateDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TemplateDtoToTemplateConverter implements Converter<TemplateDto, Template> {

    @Override
    public Template convert(TemplateDto source) {
        Template template = new Template();
        template.setTemplateId(source.templateId());
        template.setTemplateName(source.templateName());
        return template;
    }
}
