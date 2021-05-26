package com.carlosmax.docprocessor.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "footer_paragraph")
public class FooterParagraph implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "footer_id")
    private Footer footer;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "paragraph_id")
    private Paragraph paragraph;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Footer getFooter() {
        return footer;
    }

    public void setFooter(Footer footer) {
        this.footer = footer;
    }

    public Paragraph getParagraph() {
        return paragraph;
    }

    public void setParagraph(Paragraph paragraph) {
        this.paragraph = paragraph;
    }
}
