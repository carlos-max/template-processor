package com.carlosmax.docprocessor.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "header_paragraph")
public class HeaderParagraph implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "header_id")
    private Header header;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "paragraph_id")
    private Paragraph paragraph;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Paragraph getParagraph() {
        return paragraph;
    }

    public void setParagraph(Paragraph paragraph) {
        this.paragraph = paragraph;
    }
}
