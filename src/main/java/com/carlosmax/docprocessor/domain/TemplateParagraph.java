package com.carlosmax.docprocessor.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "template_paragraph")
public class TemplateParagraph implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "template_id")
    private Template template;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "paragraph_id")
    private Paragraph paragraph;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public Paragraph getParagraph() {
        return paragraph;
    }

    public void setParagraph(Paragraph paragraph) {
        this.paragraph = paragraph;
    }
}
