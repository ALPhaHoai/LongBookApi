package com.longbook.dao;

import com.longbook.model.BookCategories;
import com.longbook.model.Categories;
import com.longbook.model.Category;
import com.mysql.cj.util.StringUtils;
import lib.DB;

import java.util.ArrayList;

/**
 * Created by Long
 * Date: 11/15/2018
 * Time: 4:38 PM
 */
public class CategoryDao {
    static DB db = new DB();

    public static Category get(String id) {
        if (StringUtils.isStrictlyNumeric(id) && Integer.valueOf(id) > 0 && db.select("id, name", "category", "id = " + id)) {
            return new Category(db.getResult().get(0).get(0), db.getResult().get(0).get(1));
        }
        return null;
    }

    public static Category gethasName(String name) {
        if (name != null && db.select("id, name", "category", "name = '" + DB.validSql(name) + "' LIMIT 1")) {
            return new Category(db.getResult().get(0).get(0), db.getResult().get(0).get(1));
        }
        return null;
    }

    public static Category gethasNamenotId(String id, String name) {
        if (name != null && db.select("id, name", "category", "name = '" + DB.validSql(name) + "' AND id != " + id + " LIMIT 1")) {
            return new Category(db.getResult().get(0).get(0), db.getResult().get(0).get(1));
        }
        return null;
    }

    /**
     * Get all category of this book id
     * start: [0 - ~]
     * limit: [1 - 100]
     */
    public static Categories getAllhasBook(String bookId, String start, String limit) {
        if (!StringUtils.isStrictlyNumeric(bookId) || Integer.valueOf(bookId) < 0
                || !StringUtils.isStrictlyNumeric(start) || Integer.valueOf(start) < 0
                || !StringUtils.isStrictlyNumeric(limit) || Integer.valueOf(limit) < 1 || Integer.valueOf(limit) > 100)
            return null;
        if (db.select("category.id, category.name", "category, book_category", "category.id = book_category.category_id AND book_category.book_id = " + bookId + " ORDER BY category.id ASC LIMIT " + start + "," + limit)) {
            Categories categories = new Categories();
            for (ArrayList<String> record : db.getResult()) {
                categories.add(new Category(record.get(0), record.get(1)));
            }
            return categories.size() == 0 ? null : categories;
        }
        return null;
    }


    /**
     * start: [0 - ~]
     * limit: [1 - 100]
     * */
    public static Categories getAll(String start, String limit) {
        if(!StringUtils.isStrictlyNumeric(start) || Integer.valueOf(start) < 0
                || !StringUtils.isStrictlyNumeric(limit) || Integer.valueOf(limit) < 1 || Integer.valueOf(limit) > 100) return null;
        if (db.select("id, name", "category", "1 ORDER BY id ASC LIMIT " + start + "," + limit)) {
            Categories categories = new Categories();
            for (ArrayList<String> record : db.getResult()) {
                categories.add(new Category(record.get(0), record.get(1)));
            }
            return categories.size() == 0 ? null : categories;
        }
        return null;
    }

    public static Category insert(Category category) {
        if (category != null) {
            if (gethasName(category.getName()) != null) {
                return null;
            } else if (db.insert("category", new String[]{"name"}, new String[]{category.getName()})) {
                return gethasName(category.getName());
            }
        }
        return null;
    }

    public static boolean delete(Category category) {
        if (category != null && category.getId() != null) {
            //get book category
            BookCategories bookCategoryList = BookCategoryDao.getFromCategory(category.getId());

            //Can't delete book category
            if (bookCategoryList != null && !db.execute("DELETE FROM book_category WHERE category_id = " + category.getId())) {
                return false;
            }

            //Can't delete category
            if (!db.execute("DELETE FROM category WHERE id = " + category.getId())) {
                //restore book category data
                BookCategoryDao.insert(bookCategoryList);
                return false;
            }
            return true;
        }
        return true;
    }


    public static Category update(Category category) {
        //Update when this id exist and new category title is not duplicate with categories in database
        if (get(category.getId()) != null && gethasNamenotId(category.getId(), category.getName()) == null) {
            if (db.update("category", new String[]{"name"}, new String[]{category.getName()}, "id = " + category.getId())) {
                return get(category.getId());
            } else return null;
        } else return null;
    }
}
