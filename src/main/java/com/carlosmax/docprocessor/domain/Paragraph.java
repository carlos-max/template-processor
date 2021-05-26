package com.carlosmax.docprocessor.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "paragraph")
public class Paragraph implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "print_order")
    private int printOrder;

    @Column(name = "alignment")
    private String alignment;

    @Column(name = "border_between")
    private String borderBetween;

    @Column(name = "border_top")
    private String borderTop;

    @Column(name = "border_bottom")
    private String borderBottom;

    @Column(name = "border_left")
    private String borderLeft;

    @Column(name = "border_right")
    private String borderRight;

    @Column(name = "spacing_line_rule")
    private String spacingLineRule;

    @Column(name = "spacing_between")
    private Double spacingBetween;

    @Column(name = "spacing_before")
    private int spacingBefore;

    @Column(name = "spacing_before_lines")
    private int spacingBeforeLines;

    @Column(name = "spacing_after")
    private int spacingAfter;

    @Column(name = "spacing_after_lines")
    private int spacingAfterLines;

    @Column(name = "vertical_alignment")
    private String verticalAlignment;

    @OneToMany(mappedBy = "paragraph", cascade = CascadeType.ALL)
    private List<Content> contents;

    public List<Content> getSortedContents() {
        return contents.stream().sorted(Comparator.comparing(Content::getPrintOrder)).collect(Collectors.toList());
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

    public String getAlignment() {
        return alignment;
    }

    public void setAlignment(String alignment) {
        this.alignment = alignment;
    }

    public String getBorderBetween() {
        return borderBetween;
    }

    public void setBorderBetween(String borderBetween) {
        this.borderBetween = borderBetween;
    }

    public String getBorderTop() {
        return borderTop;
    }

    public void setBorderTop(String borderTop) {
        this.borderTop = borderTop;
    }

    public String getBorderBottom() {
        return borderBottom;
    }

    public void setBorderBottom(String borderBottom) {
        this.borderBottom = borderBottom;
    }

    public String getBorderLeft() {
        return borderLeft;
    }

    public void setBorderLeft(String borderLeft) {
        this.borderLeft = borderLeft;
    }

    public String getBorderRight() {
        return borderRight;
    }

    public void setBorderRight(String borderRight) {
        this.borderRight = borderRight;
    }

    public String getSpacingLineRule() {
        return spacingLineRule;
    }

    public void setSpacingLineRule(String spacingLineRule) {
        this.spacingLineRule = spacingLineRule;
    }

    public Double getSpacingBetween() {
        return spacingBetween;
    }

    public void setSpacingBetween(Double spacingBetween) {
        this.spacingBetween = spacingBetween;
    }

    public int getSpacingBefore() {
        return spacingBefore;
    }

    public void setSpacingBefore(int spacingBefore) {
        this.spacingBefore = spacingBefore;
    }

    public int getSpacingBeforeLines() {
        return spacingBeforeLines;
    }

    public void setSpacingBeforeLines(int spacingBeforeLines) {
        this.spacingBeforeLines = spacingBeforeLines;
    }

    public int getSpacingAfter() {
        return spacingAfter;
    }

    public void setSpacingAfter(int spacingAfter) {
        this.spacingAfter = spacingAfter;
    }

    public int getSpacingAfterLines() {
        return spacingAfterLines;
    }

    public void setSpacingAfterLines(int spacingAfterLines) {
        this.spacingAfterLines = spacingAfterLines;
    }

    public String getVerticalAlignment() {
        return verticalAlignment;
    }

    public void setVerticalAlignment(String verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
    }

    public List<Content> getContents() {
        return contents;
    }

    public void setContents(List<Content> contents) {
        this.contents = contents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Paragraph paragraph = (Paragraph) o;

        if (id != null ? !id.equals(paragraph.id) : paragraph.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
