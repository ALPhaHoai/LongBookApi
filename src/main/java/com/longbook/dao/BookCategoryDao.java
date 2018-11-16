package com.longbook.dao;

import com.longbook.model.BookCategories;
import com.longbook.model.BookCategory;
import com.mysql.cj.util.StringUtils;
import lib.DB;

import java.util.ArrayList;

/**
 * Created by Long
 * Date: 11/15/2018
 * Time: 2:00 PM
 */
public class BookCategoryDao {
    static DB db = new DB();

    /**
     * Convert sql data to Book object
     * result: id, book_id, category_id
     */
    public static BookCategory getfromResult(ArrayList<String> result) {
        return new BookCategory(result.get(0), result.get(1), result.get(2));
    }

    /**
     * start: [0 - ~]
     * limit: [1 - 100]
     */
    public static BookCategories getAll(String start, String limit) {
        if (!StringUtils.isStrictlyNumeric(start) || Integer.valueOf(start) < 0
                || !StringUtils.isStrictlyNumeric(limit) || Integer.valueOf(limit) < 1 || Integer.valueOf(limit) > 100)
            return null;
        if (db.select("id, book_id, category_id", "book_category", "1 ORDER BY id ASC LIMIT " + start + "," + limit)) {
            BookCategories bookCategoryList = new BookCategories();
            for (ArrayList<String> record : db.getResult()) {
                bookCategoryList.add(getfromResult(record));
            }
            return bookCategoryList.size() == 0 ? null : bookCategoryList;
        }
        return null;
    }

    public static BookCategory get(String id) {
        if (StringUtils.isStrictlyNumeric(id) && Integer.valueOf(id) > 0 && db.select("id, book_id, category_id", "book_category", "id = '" + id + "' LIMIT 1")) {
            return getfromResult(db.getResult().get(0));
        }
        return null;
    }

    public static BookCategory get(String bookId, String categoryId) {
        if (StringUtils.isStrictlyNumeric(bookId) && Integer.valueOf(bookId) > 0 && StringUtils.isStrictlyNumeric(categoryId) && Integer.valueOf(categoryId) > 0 && db.select("id, book_id, category_id", "book_category", "book_id = '" + bookId + "' AND category_id = '" + categoryId + "' LIMIT 1")) {
            return getfromResult(db.getResult().get(0));
        }
        return null;
    }

    public static BookCategory getNotId(String id, String bookId, String categoryId) {
        if (StringUtils.isStrictlyNumeric(id) && Integer.valueOf(id) > 0
                && StringUtils.isStrictlyNumeric(bookId) && Integer.valueOf(bookId) > 0
                && StringUtils.isStrictlyNumeric(categoryId) && Integer.valueOf(categoryId) > 0
                && db.select("id, book_id, category_id", "book_category", "book_id = '" + bookId + "' AND category_id = '" + categoryId + "' AND id != " + id + " LIMIT 1")) {
            return getfromResult(db.getResult().get(0));
        }
        return null;
    }

    public static BookCategories getFromBook(String bookId) {
        if (StringUtils.isStrictlyNumeric(bookId) && Integer.valueOf(bookId) > 0 && db.select("id, book_id, category_id", "book_category", "book_id = '" + bookId + "'")) {
            BookCategories bookCategoryList = new BookCategories();
            for (ArrayList<String> record : db.getResult()) {
                bookCategoryList.add(getfromResult(record));
            }
            return bookCategoryList.size() == 0 ? null : bookCategoryList;
        }
        return null;
    }

    public static BookCategories getFromCategory(String categoryId) {
        if (StringUtils.isStrictlyNumeric(categoryId) && Integer.valueOf(categoryId) > 0 && db.select("id, book_id, category_id", "book_category", "category_id = '" + categoryId + "'")) {
            BookCategories bookCategoryList = new BookCategories();
            for (ArrayList<String> record : db.getResult()) {
                bookCategoryList.add(getfromResult(record));
            }
            return bookCategoryList.size() == 0 ? null : bookCategoryList;
        }
        return null;
    }

    public static BookCategory insert(BookCategory bookcategory) {
        if (bookcategory != null && StringUtils.isStrictlyNumeric(bookcategory.getBookId()) && Integer.valueOf(bookcategory.getBookId()) > 0
                && StringUtils.isStrictlyNumeric(bookcategory.getCategoryId()) && Integer.valueOf(bookcategory.getCategoryId()) > 0
                && get(bookcategory.getBookId(), bookcategory.getCategoryId()) == null) {
            if (db.insert("book_category", new String[]{"book_id", "category_id"}, new String[]{bookcategory.getBookId(), bookcategory.getCategoryId()})) {
                return get(bookcategory.getBookId(), bookcategory.getCategoryId());
            }
        }
        return null;
    }

    public static boolean delete(BookCategory bookcategory) {
        if (bookcategory != null && StringUtils.isStrictlyNumeric(bookcategory.getId()))
            return db.execute("DELETE FROM book_category WHERE id = " + bookcategory.getId());
        else return false;
    }

    /*
     * insert a list of BookCategory
     * if one of them fail: remove successfull record and return false
     * else return true
     * */
    public static boolean insert(BookCategories bookcategoryList) {
        if (bookcategoryList != null && bookcategoryList.size() > 0) {
            BookCategories bookcategorySuccessList = new BookCategories();
            for (BookCategory bookcategory : bookcategoryList) {
                if (insert(bookcategory) != null) {
                    bookcategorySuccessList.add(bookcategory);
                } else {
                    if (bookcategorySuccessList.size() > 0)
                        for (BookCategory bookcategorySuccess : bookcategorySuccessList) {
                            delete(bookcategorySuccess);
                        }
                    return false;
                }
            }
            return true;
        } else return true;
    }


    public static BookCategory update(BookCategory book) {
        if (get(book.getId()) != null && getNotId(book.getId(), book.getBookId(), book.getCategoryId()) == null){
            if (db.update("book_category", new String[]{"book_id", "category_id"}, new String[]{book.getBookId(), book.getCategoryId()}, "id = " + book.getId())) {
                return get(book.getId());
            } else return null;
        } else return null;
    }
}
