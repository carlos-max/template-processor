package com.carlosmax.docprocessor.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "table_cell")
public class TableCell implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "print_order")
    private int printOrder;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "paragraph_id")
    private Paragraph paragraph;

    @ManyToOne
    @JoinColumn(name = "table_row_id")
    private TableRow tableRow;

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

    public Paragraph getParagraph() {
        return paragraph;
    }

    public void setParagraph(Paragraph paragraph) {
        this.paragraph = paragraph;
    }

    public TableRow getTableRow() {
        return tableRow;
    }

    public void setTableRow(TableRow tableRow) {
        this.tableRow = tableRow;
    }
}
