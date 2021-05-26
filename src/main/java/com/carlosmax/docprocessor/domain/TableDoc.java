package com.carlosmax.docprocessor.domain;

import org.apache.xmlbeans.XmlCursor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "table_doc")
public class TableDoc implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cursor_pos")
    private String cursorPos;

    @OneToMany(mappedBy = "tableDoc", cascade = CascadeType.ALL)
    private List<TableRow> rows;

    @ManyToOne
    @JoinColumn(name = "template_id")
    private Template template;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCursorPos() {
        return cursorPos;
    }

    public void setCursorPos(String cursorPos) {
        this.cursorPos = cursorPos;
    }

    public List<TableRow> getRows() {
        return rows;
    }

    public void setRows(List<TableRow> rows) {
        this.rows = rows;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }
}
