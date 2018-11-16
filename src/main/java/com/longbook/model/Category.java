package com.longbook.model;

import net.minidev.json.JSONObject;

/**
 * Created by Long
 * Date: 11/15/2018
 * Time: 4:34 PM
 */
public class Category extends JSONExport {
    private String id;
    private String name;

    public Category() {
    }

    public Category(String id) {
        this.id = id;
    }

    public Category(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JSONObject toJSON() {
        return new JSONObject()
                .appendField("id", getId())
                .appendField("name", getName());
    }
}
