package edu.tcu.cs.frogcrew.template;

import edu.tcu.cs.frogcrew.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TemplateServiceTest {

    @Mock
    TemplateRepository templateRepository;

    @InjectMocks
    TemplateService templateService;

    Template t1;
    Template t2;
    List<Template> templates;

    @BeforeEach
    void setUp() {
        templates = new ArrayList<>();

        t1 = new Template();
        t1.setTemplateId(1);
        t1.setTemplateName("Football Template");
        t1.setGameType("Football");

        t2 = new Template();
        t2.setTemplateId(2);
        t2.setTemplateName("Basketball Template");
        t2.setGameType("Basketball");

        templates.add(t1);
        templates.add(t2);
    }

    @Test
    void testFindByIdSuccess() {
        given(this.templateRepository.findById(1)).willReturn(Optional.of(t1));

        Template foundTemplate = this.templateService.findById(1);

        assertThat(foundTemplate.getTemplateId()).isEqualTo(1);
        assertThat(foundTemplate.getTemplateName()).isEqualTo("Football Template");
        assertThat(foundTemplate.getGameType()).isEqualTo("Football");
        verify(templateRepository, times(1)).findById(1);
    }

    @Test
    void testFindByIdNotFound() {
        given(this.templateRepository.findById(99)).willReturn(Optional.empty());

        Throwable thrown = catchThrowable(() -> this.templateService.findById(99));

        assertThat(thrown).isInstanceOf(ObjectNotFoundException.class);
        verify(templateRepository, times(1)).findById(99);
    }

    @Test
    void testFindAllSuccess() {
        given(this.templateRepository.findAll()).willReturn(templates);

        List<Template> foundTemplates = this.templateService.findAll();

        assertThat(foundTemplates).hasSize(2);
        verify(templateRepository, times(1)).findAll();
    }

    @Test
    void testSaveSuccess() {
        Template newTemplate = new Template();
        newTemplate.setTemplateName("Soccer Template");
        newTemplate.setGameType("Soccer");

        given(this.templateRepository.save(newTemplate)).willReturn(newTemplate);

        Template savedTemplate = this.templateService.save(newTemplate);

        assertThat(savedTemplate.getTemplateName()).isEqualTo("Soccer Template");
        assertThat(savedTemplate.getGameType()).isEqualTo("Soccer");
        verify(templateRepository, times(1)).save(newTemplate);
    }

    @Test
    void testUpdateSuccess() {
        Template existingTemplate = new Template();
        existingTemplate.setTemplateId(1);
        existingTemplate.setTemplateName("Old Template");
        existingTemplate.setGameType("Old Game");

        Template updatedTemplate = new Template();
        updatedTemplate.setTemplateName("Updated Template");
        updatedTemplate.setGameType("Updated Game");

        given(this.templateRepository.findById(1)).willReturn(Optional.of(existingTemplate));
        given(this.templateRepository.save(existingTemplate)).willReturn(existingTemplate);

        Template result = this.templateService.update(1, updatedTemplate);

        assertThat(result.getTemplateId()).isEqualTo(1);
        assertThat(result.getTemplateName()).isEqualTo("Updated Template");
        assertThat(result.getGameType()).isEqualTo("Updated Game");
        verify(this.templateRepository, times(1)).findById(1);
        verify(this.templateRepository, times(1)).save(existingTemplate);
    }

    @Test
    void testUpdateNotFound() {
        Integer nonExistentId = 99;
        Template updatedTemplate = new Template();
        updatedTemplate.setTemplateName("New Template");
        updatedTemplate.setGameType("New Game");

        given(this.templateRepository.findById(nonExistentId)).willReturn(Optional.empty());

        Throwable thrown = catchThrowable(() -> this.templateService.update(nonExistentId, updatedTemplate));

        assertThat(thrown).isInstanceOf(ObjectNotFoundException.class);
        verify(this.templateRepository, times(1)).findById(nonExistentId);
    }

    @Test
    void testDeleteSuccess() {
        given(this.templateRepository.findById(1)).willReturn(Optional.of(t1));

        this.templateService.delete(1);

        verify(this.templateRepository, times(1)).findById(1);
        verify(this.templateRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteNotFound() {
        given(this.templateRepository.findById(99)).willReturn(Optional.empty());

        Throwable thrown = catchThrowable(() -> this.templateService.delete(99));

        assertThat(thrown).isInstanceOf(ObjectNotFoundException.class);
        verify(this.templateRepository, times(1)).findById(99);
    }

}
