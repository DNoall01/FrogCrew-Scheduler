package edu.tcu.cs.frogcrew.template;

import edu.tcu.cs.frogcrew.system.Result;
import edu.tcu.cs.frogcrew.system.StatusCode;
import edu.tcu.cs.frogcrew.template.converter.TemplateDtoToTemplateConverter;
import edu.tcu.cs.frogcrew.template.converter.TemplateToTemplateDtoConverter;
import edu.tcu.cs.frogcrew.template.dto.TemplateDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.endpoint.base-url}/template")
public class TemplateController {

    private final TemplateService templateService;
    private final TemplateToTemplateDtoConverter templateToTemplateDtoConverter;
    private final TemplateDtoToTemplateConverter templateDtoToTemplateConverter;

    public TemplateController(TemplateService templateService, TemplateToTemplateDtoConverter templateToTemplateDtoConverter, TemplateDtoToTemplateConverter templateDtoToTemplateConverter) {
        this.templateService = templateService;
        this.templateToTemplateDtoConverter = templateToTemplateDtoConverter;
        this.templateDtoToTemplateConverter = templateDtoToTemplateConverter;
    }

    @GetMapping("")
    public Result findAllTemplates() {
        List<Template> foundTemplates = this.templateService.findAll();
        List<TemplateDto> templateDtos = foundTemplates.stream()
                .map(templateToTemplateDtoConverter::convert)
                .toList();
        return new Result(true, StatusCode.SUCCESS, "Find Success", templateDtos);
    }

    @PostMapping("")
    public Result addTemplate(@RequestBody TemplateDto templateDto) {
        Template newTemplate = this.templateDtoToTemplateConverter.convert(templateDto);
        Template savedTemplate = this.templateService.save(newTemplate);
        TemplateDto savedTemplateDto = this.templateToTemplateDtoConverter.convert(savedTemplate);
        return new Result(true, StatusCode.SUCCESS, "Add Success", savedTemplateDto);
    }

    @GetMapping("/{templateId}")
    public Result findTemplateById(@PathVariable Integer templateId) {
        Template foundTemplate = this.templateService.findById(templateId);
        TemplateDto savedTemplateDto = this.templateToTemplateDtoConverter.convert(foundTemplate);
        return new Result(true, StatusCode.SUCCESS, "Find Success", savedTemplateDto);
    }

    @PutMapping("/{templateId}")
    public Result updateTemplate(@PathVariable Integer templateId, @RequestBody TemplateDto templateDto) {
        Template update = this.templateDtoToTemplateConverter.convert(templateDto);
        Template updatedTemplate = this.templateService.update(templateId, update);
        TemplateDto updatedTemplateDto = this.templateToTemplateDtoConverter.convert(updatedTemplate);
        return new Result(true, StatusCode.SUCCESS, "Update Success", updatedTemplateDto);
    }

    @DeleteMapping("/{templateId}")
    public Result deleteTemplate(@PathVariable Integer templateId) {
        this.templateService.delete(templateId);
        return new Result(true, StatusCode.SUCCESS, "Delete Success");
    }
}
