package com.longbook.model;

import com.mysql.cj.util.StringUtils;
import net.minidev.json.JSONObject;

/**
 * Created by Long
 * Date: 11/15/2018
 * Time: 11:39 AM
 */
public class Book extends JSONExport {
    private String id;
    private String title;
    private String content;

    private Categories categories;

    public Book() {
    }

    public Book(String id) {
        this.id = id;
    }

    public Book(int id) {
        this.id = String.valueOf(id);
    }

    public String getId() {
        return id;
    }

    public int getIdInt() {
        return (StringUtils.isStrictlyNumeric(id)) ? Integer.valueOf(id) : -1;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Categories getCategories() {
        return categories;
    }

    public void setCategories(Categories categories) {
        this.categories = categories;
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject()
                .appendField("id", getIdInt())
                .appendField("title", getTitle())
                .appendField("content", getContent());
        if (categories != null) jsonObject.put("category", categories.toJSON());
        return jsonObject;
    }

}
