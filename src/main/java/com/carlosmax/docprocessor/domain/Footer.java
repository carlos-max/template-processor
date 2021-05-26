package com.carlosmax.docprocessor.domain;

import com.carlosmax.docprocessor.domain.enumeration.HeaderFooterType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "footer")
public class Footer implements Serializable {

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

    @OneToMany(mappedBy = "footer", cascade = CascadeType.ALL)
    List<FooterParagraph> paragraphList;

    public List<FooterParagraph> getSortedParagraphs() {
        return paragraphList.stream().sorted(Comparator.comparing(fp -> fp.getParagraph().getPrintOrder())).collect(Collectors.toList());
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

    public List<FooterParagraph> getParagraphList() {
        return paragraphList;
    }

    public void setParagraphList(List<FooterParagraph> paragraphList) {
        this.paragraphList = paragraphList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Footer footer = (Footer) o;

        if (id != null ? !id.equals(footer.id) : footer.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
