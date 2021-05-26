package com.carlosmax.docprocessor.domain;

import com.carlosmax.docprocessor.domain.enumeration.HeaderFooterType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "header")
public class Header implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "print_order")
    private int printOrder;

    @ManyToOne
    @JoinColumn(name = "template_id")
    private Template template;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "type")
    private HeaderFooterType type;

    @OneToMany(mappedBy = "header", cascade = CascadeType.ALL)
    List<HeaderParagraph> paragraphList;

    public List<HeaderParagraph> getSorterdParagraphs() {
        return paragraphList.stream().sorted(Comparator.comparing(hp -> hp.getParagraph().getPrintOrder())).collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPrintOrder() {
        return printOrder;
    }

    public void setPrintOrder(int printOrder) {
        this.printOrder = printOrder;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public HeaderFooterType getType() {
        return type;
    }

    public void setType(HeaderFooterType type) {
        this.type = type;
    }

    public List<HeaderParagraph> getParagraphList() {
        return paragraphList;
    }

    public void setParagraphList(List<HeaderParagraph> paragraphList) {
        this.paragraphList = paragraphList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Header header = (Header) o;

        if (id != null ? !id.equals(header.id) : header.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
