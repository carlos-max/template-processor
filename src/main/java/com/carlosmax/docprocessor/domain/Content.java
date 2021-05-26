package com.carlosmax.docprocessor.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Blob;

@Entity
@Table(name = "content")
public class Content implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "print_order")
    private int printOrder;

    @Column(name = "font_name")
    private String fontName;

    @Column(name = "font_size")
    private int fontSize;

    @Column(name = "color")
    private String color;

    @Column(name = "bold")
    private boolean bold;

    @Column(name = "underline")
    private String underline;

    @Column(name = "capitalized")
    private boolean capitalized;

    @Column(name = "strike_through")
    private boolean strikeThrough;

    @Column(name = "italic")
    private boolean italic;

    @Column(name = "vertical_alignment")
    private String verticalAlignment;

    @Column(name = "value")
    private String value;

    @Column(name = "image_value")
    private Blob imageValue;

    @Column(name = "image_width_emu")
    private Long imageWidthEmu;

    @Column(name = "image_height_emu")
    private Long imageHeightEmu;

    @ManyToOne
    @JoinColumn(name = "paragraph_id")
    private Paragraph paragraph;

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

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public String getUnderline() {
        return underline;
    }

    public void setUnderline(String underline) {
        this.underline = underline;
    }

    public boolean isCapitalized() {
        return capitalized;
    }

    public void setCapitalized(boolean capitalized) {
        this.capitalized = capitalized;
    }

    public boolean isStrikeThrough() {
        return strikeThrough;
    }

    public void setStrikeThrough(boolean strikeThrough) {
        this.strikeThrough = strikeThrough;
    }

    public boolean isItalic() {
        return italic;
    }

    public void setItalic(boolean italic) {
        this.italic = italic;
    }

    public String getVerticalAlignment() {
        return verticalAlignment;
    }

    public void setVerticalAlignment(String verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Blob getImageValue() {
        return imageValue;
    }

    public void setImageValue(Blob imageValue) {
        this.imageValue = imageValue;
    }

    public Long getImageWidthEmu() {
        return imageWidthEmu;
    }

    public void setImageWidthEmu(Long imageWidthEmu) {
        this.imageWidthEmu = imageWidthEmu;
    }

    public Long getImageHeightEmu() {
        return imageHeightEmu;
    }

    public void setImageHeightEmu(Long imageHeightEmu) {
        this.imageHeightEmu = imageHeightEmu;
    }

    public Paragraph getParagraph() {
        return paragraph;
    }

    public void setParagraph(Paragraph paragraph) {
        this.paragraph = paragraph;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Content content = (Content) o;

        if (id != null ? !id.equals(content.id) : content.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
