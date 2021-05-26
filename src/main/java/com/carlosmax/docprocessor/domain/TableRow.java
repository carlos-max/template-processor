package com.carlosmax.docprocessor.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "table_row")
public class TableRow implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "print_order")
    private int printOrder;

    @OneToMany(mappedBy = "tableRow", cascade = CascadeType.ALL)
    private List<TableCell> cells;

    @ManyToOne
    @JoinColumn(name = "table_doc_id")
    private TableDoc tableDoc;

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

    public List<TableCell> getCells() {
        return cells;
    }

    public void setCells(List<TableCell> cells) {
        this.cells = cells;
    }

    public TableDoc getTableDoc() {
        return tableDoc;
    }

    public void setTableDoc(TableDoc tableDoc) {
        this.tableDoc = tableDoc;
    }
}
