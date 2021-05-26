package com.carlosmax.docprocessor.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Blob;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "template")
public class Template implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "name")
    private String name;

    @Column(name = "margin_top")
    private Integer marginTop;

    @Column(name = "margin_bottom")
    private Integer marginBottom;

    @Column(name = "margin_left")
    private Integer marginLeft;

    @Column(name = "margin_right")
    private Integer marginRight;

    @Column(name = "watermark_blob")
    private Blob watermarkBlob;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL)
    List<Header> headerList;

    public List<Header> getSortedHeaders() {
        return headerList.stream().sorted(Comparator.comparing(Header::getPrintOrder)).collect(Collectors.toList());
    }

    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL)
    List<Footer> footerList;

    public List<Footer> getSortedFooters() {
        return footerList.stream().sorted(Comparator.comparing(Footer::getPrintOrder)).collect(Collectors.toList());
    }

    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL)
    List<TemplateParagraph> paragraphList;

    public List<TemplateParagraph> getSortedParagraphs() {
        return paragraphList.stream().sorted(Comparator.comparing(tp -> tp.getParagraph().getPrintOrder())).collect(Collectors.toList());
    }

    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL)
    List<TableDoc> tables;

    public List<TableDoc> getTables() {
        return tables;
    }

    public void setTables(List<TableDoc> tables) {
        this.tables = tables;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMarginTop() {
        return marginTop;
    }

    public void setMarginTop(Integer marginTop) {
        this.marginTop = marginTop;
    }

    public Integer getMarginBottom() {
        return marginBottom;
    }

    public void setMarginBottom(Integer marginBottom) {
        this.marginBottom = marginBottom;
    }

    public Integer getMarginLeft() {
        return marginLeft;
    }

    public void setMarginLeft(Integer marginLeft) {
        this.marginLeft = marginLeft;
    }

    public Integer getMarginRight() {
        return marginRight;
    }

    public void setMarginRight(Integer marginRight) {
        this.marginRight = marginRight;
    }

    public Blob getWatermarkBlob() {
        return watermarkBlob;
    }

    public void setWatermarkBlob(Blob watermarkBlob) {
        this.watermarkBlob = watermarkBlob;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public List<Header> getHeaderList() {
        return headerList;
    }

    public void setHeaderList(List<Header> headerList) {
        this.headerList = headerList;
    }

    public List<Footer> getFooterList() {
        return footerList;
    }

    public void setFooterList(List<Footer> footerList) {
        this.footerList = footerList;
    }

    public List<TemplateParagraph> getParagraphList() {
        return paragraphList;
    }

    public void setParagraphList(List<TemplateParagraph> paragraphList) {
        this.paragraphList = paragraphList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Template template = (Template) o;

        if (id != null ? !id.equals(template.id) : template.id != null) return false;
        if (name != null ? !name.equals(template.name) : template.name != null) return false;
        if (uuid != null ? !uuid.equals(template.uuid) : template.uuid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (uuid != null ? uuid.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
