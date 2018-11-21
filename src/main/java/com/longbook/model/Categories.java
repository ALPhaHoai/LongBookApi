package com.longbook.model;

import net.minidev.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Long
 * Date: 11/15/2018
 * Time: 4:39 PM
 */
public class Categories extends ArrayList<Category> {
    public JSONArray toJSON() {
        if (this.size() == 0) return null;
        else {
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < this.size(); i++) {
                if (this.get(i) != null) jsonArray.add(this.get(i).toJSON());
            }
            return jsonArray;
        }
    }

    public String toJSONString() {
        if (this.toJSON() == null) return "";
        else return this.toJSON().toJSONString();
    }

    public boolean isContain(Category category) {
        try{
            if(category == null) return false;
            for (int i = 0; i < this.size(); i++) {
                if (this.get(i) != null && this.get(i).getId().equals(category.getId())) {
                    return true;
                }
            }
            return false;
        } catch (Exception ex){
            return false;
        }
    }

    //Lọc những category không nằm trong categories
    public Categories sub(Categories categories) {
        if(this.size() == 0 || categories == null || categories.size() == 0) return this;
        Categories c = new Categories();
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i) != null && !this.isContain(categories.get(i))) {
                c.add(categories.get(i));
            }
        }
        return c.size() == 0 ? null : c;
    }

    public BookCategories toBookCategories(String bookId) {
        if(this.size() == 0) return null;
        BookCategories bookCategories = new BookCategories();
        for (int i = 0; i < this.size(); i++) {
            Category category = this.get(i);
            if(category != null) {
                bookCategories.add(new BookCategory(bookId, category.getId()));
            }
        }
        return bookCategories.size() == 0 ? null : bookCategories;
    }
}
