package edu.tcu.cs.frogcrew.template;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.frogcrew.system.StatusCode;
import edu.tcu.cs.frogcrew.system.exception.ObjectNotFoundException;
import edu.tcu.cs.frogcrew.template.dto.TemplateDto;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class TemplateControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    TemplateService templateService;

    @Autowired
    ObjectMapper objectMapper;

    List<Template> templates;

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    Template t1;
    Template t2;

    @BeforeEach
    void setUp() {
        templates = new ArrayList<>();

        t1 = new Template();
        t1.setTemplateId(1);
        t1.setTemplateName("Template1");
        t1.setGameType("Football");

        t2 = new Template();
        t2.setTemplateId(2);
        t2.setTemplateName("Template 2");
        t2.setGameType("Basketball");

        templates.add(t1);
        templates.add(t2);
    }

    @Test
    void testFindAllTemplatesSuccess() throws Exception {
        given(this.templateService.findAll()).willReturn(List.of(t1, t2));

        this.mockMvc.perform(get(this.baseUrl + "/template").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find Success"))
                .andExpect(jsonPath("$.data[0].templateName").value(t1.getTemplateName()))
                .andExpect(jsonPath("$.data[1].templateName").value(t2.getTemplateName()));
    }

    @Test
    void testAddTemplateSuccess() throws Exception {
        TemplateDto newTemplateDto = new TemplateDto(null, "Soccer");

        String json = this.objectMapper.writeValueAsString(newTemplateDto);

        Template savedTemplate = new Template();
        savedTemplate.setTemplateId(3);
        savedTemplate.setTemplateName("Soccer");
        savedTemplate.setGameType("Sport");

        given(this.templateService.save(Mockito.any(Template.class))).willReturn(savedTemplate);

        this.mockMvc.perform(post(this.baseUrl + "/template").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.templateId").value(savedTemplate.getTemplateId()))
                .andExpect(jsonPath("$.data.templateName").value(savedTemplate.getTemplateName()));
    }

    @Test
    void testUpdateTemplateSuccess() throws Exception {
        TemplateDto updateDto = new TemplateDto(t1.getTemplateId(), "Updated Football");

        Template updated = new Template();
        updated.setTemplateId(t1.getTemplateId());
        updated.setTemplateName("Updated Football");
        updated.setGameType("Sport");

        given(this.templateService.update(eq(t1.getTemplateId()), Mockito.any(Template.class))).willReturn(updated);

        String json = this.objectMapper.writeValueAsString(updateDto);

        this.mockMvc.perform(put(this.baseUrl + "/template/" + t1.getTemplateId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.templateId").value(t1.getTemplateId()))
                .andExpect(jsonPath("$.data.templateName").value("Updated Football"));
    }

    @Test
    void testUpdateTemplateNotFound() throws Exception {
        TemplateDto updateDto = new TemplateDto(99, "Ghost Template");

        given(this.templateService.update(eq(99), Mockito.any(Template.class)))
                .willThrow(new ObjectNotFoundException("template", 99));

        String json = this.objectMapper.writeValueAsString(updateDto);

        this.mockMvc.perform(put(this.baseUrl + "/template/99").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find template with id 99"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteTemplateSuccess() throws Exception {
        doNothing().when(this.templateService).delete(1);

        this.mockMvc.perform(delete(this.baseUrl + "/template/" + t1.getTemplateId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"));
    }

    @Test
    void testDeleteTemplateNotFound() throws Exception {
        doThrow(new ObjectNotFoundException("template", 99)).when(this.templateService).delete(99);

        this.mockMvc.perform(delete(this.baseUrl + "/template/99").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find template with id 99"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}
