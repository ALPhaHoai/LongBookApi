package com.longbook.model;

import com.mysql.cj.util.StringUtils;
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

    public int getIdInt() {
        return (StringUtils.isStrictlyNumeric(id)) ? Integer.valueOf(id) : -1;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookId() {
        return bookId;
    }
    public int getBookIdInt() {
        return (StringUtils.isStrictlyNumeric(bookId)) ? Integer.valueOf(bookId) : -1;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getCategoryId() {
        return categoryId;
    }
    public int getCategoryIdInt() {
        return (StringUtils.isStrictlyNumeric(categoryId)) ? Integer.valueOf(categoryId) : -1;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public JSONObject toJSON() {
        return new JSONObject()
                .appendField("id", getIdInt())
                .appendField("book_id", getBookIdInt())
                .appendField("category_id", getCategoryIdInt());
    }

}
