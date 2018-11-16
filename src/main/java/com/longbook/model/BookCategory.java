package com.longbook.model;

import net.minidev.json.JSONObject;

/**
 * Created by Long
 * Date: 11/15/2018
 * Time: 1:42 PM
 */
public class BookCategory extends JSONExport {
    private String id;
    private String bookId;
    private String categoryId;

    public BookCategory() {
    }

    public BookCategory(String id) {
        this.id = id;
    }

    public BookCategory(String id, String bookId, String categoryId) {
        this.id = id;
        this.bookId = bookId;
        this.categoryId = categoryId;
    }

    public BookCategory(String bookId, String categoryId) {
        this.bookId = bookId;
        this.categoryId = categoryId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public JSONObject toJSON() {
        return new JSONObject()
                .appendField("id", id)
                .appendField("book_id", bookId)
                .appendField("category_id", categoryId);
    }

}
