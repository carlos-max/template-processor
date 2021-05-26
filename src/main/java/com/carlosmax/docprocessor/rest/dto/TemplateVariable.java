package com.carlosmax.docprocessor.rest.dto;

import java.io.Serializable;

public class TemplateVariable implements Serializable {

    private String name;
    private String value;

    public String getName() {
        return "[".concat(name.toUpperCase().trim()).concat("]");
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
