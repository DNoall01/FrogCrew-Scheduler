package edu.tcu.cs.frogcrew.template;

import edu.tcu.cs.frogcrew.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@Transactional
public class TemplateService {

    private TemplateRepository templateRepository;

    public TemplateService(TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    public Template findById(Integer templateId) {
        return this.templateRepository.findById(templateId).orElseThrow(() -> new ObjectNotFoundException("template", templateId));
    }

    public List<Template> findAll() {
        return templateRepository.findAll();
    }

    public Template save(Template template) {
        return templateRepository.save(template);
    }

    public Template update(Integer templateId, Template update) {
        return this.templateRepository.findById(templateId)
                .map(template -> {
                    template.setTemplateName(update.getTemplateName());
                    template.setGameType(update.getGameType());
                    return this.templateRepository.save(template);
                }).orElseThrow(() -> new ObjectNotFoundException("template", templateId));
    }

    public void delete(Integer templateId) {
        this.templateRepository.findById(templateId)
                .orElseThrow(() -> new ObjectNotFoundException("template", templateId));
        this.templateRepository.deleteById(templateId);
    }


}
