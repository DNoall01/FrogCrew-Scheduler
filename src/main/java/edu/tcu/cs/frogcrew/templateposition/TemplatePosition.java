package edu.tcu.cs.frogcrew.templateposition;

import edu.tcu.cs.frogcrew.position.Position;
import edu.tcu.cs.frogcrew.template.Template;
import jakarta.persistence.*;

@Entity
public class TemplatePosition {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "template_id", nullable = false)
    private Template template;

    @ManyToOne
    @JoinColumn(name = "position_id", nullable = false)
    private Position position;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
