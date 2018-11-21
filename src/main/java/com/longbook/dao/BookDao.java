package com.longbook.dao;

import com.longbook.model.Book;
import com.longbook.model.BookCategories;
import com.longbook.model.BookCategory;
import com.longbook.model.Books;
import com.mysql.cj.util.StringUtils;
import lib.DB;

import java.util.ArrayList;

/**
 * Created by Long
 * Date: 11/15/2018
 * Time: 1:25 PM
 */
public class BookDao {
    private static DB db = new DB();

    public static Book insert(Book book) {
        if (book != null) {
            if (get(book.getTitle(), book.getContent()) != null) {
                return null;
            } else if (db.insert("book", new String[]{"title", "content"}, new String[]{book.getTitle(), book.getContent()})) {
                return get(book.getTitle(), book.getContent());
            }
        }
        return null;
    }

    public static Book update(Book book) {
        //Update when this id exist and new book title, content is not duplicate with books in database
        if (get(book.getId()) != null && getNotId(book.getId(), book.getTitle(), book.getContent()) == null) {
            if (db.update("book", new String[]{"title", "content"}, new String[]{book.getTitle(), book.getContent()}, "id = " + book.getId())) {
                return get(book.getId());
            } else return null;
        } else return null;
    }

    public static boolean delete(Book book) {
        if (book != null && book.getIdInt() > 0) {
            //get book category
            BookCategories bookCategoryList = BookCategoryDao.getFromBook(book.getId());

            //Can't delete book category
            if (bookCategoryList != null && !db.execute("DELETE FROM book_category WHERE book_id = " + book.getId())) {
                return false;
            }

            //Can't delete book
            if (!db.execute("DELETE FROM book WHERE id = " + book.getId())) {
                //restore book category data
                BookCategoryDao.insert(bookCategoryList);
                return false;
            }
            return true;
        }
        return true;
    }

    public static boolean deleteAllCategory(Book book) {
        if (book != null && book.getIdInt() > 0) {
            //get all category of this book
            BookCategories bookCategoryList = BookCategoryDao.getFromBook(book.getId());

            //Can't delete all category
            return bookCategoryList == null || db.execute("DELETE FROM book_category WHERE book_id = " + book.getId());
        }
        return true;
    }

    /**
     * Convert sql data to Book object
     * result: id, title, content
     */
    public static Book getfromResult(ArrayList<String> result) {
        Book book = new Book(result.get(0));
        book.setTitle(result.get(1));
        book.setContent(result.get(2));

        /*
        Categories categories = CategoryDao.getAllhasBook(book.getId());
        if (categories != null) {
            book.setCategories(categories);
        }
        */

        return book;

    }

    public static Book get(String title, String content) {
        if (title != null && content != null && db.select("id, title, content", "book", "title = '" + DB.validSql(title) + "' AND content = '" + DB.validSql(content) + "' LIMIT 1")) {
            return getfromResult(db.getResult().get(0));
        }
        return null;
    }

    //Get a book has title like @title, content like @content but id != @id
    public static Book getNotId(String id, String title, String content) {
        if (title != null && content != null && db.select("id, title, content", "book", "title = '" + DB.validSql(title) + "' AND content = '" + DB.validSql(content) + "' AND id != " + id + " LIMIT 1")) {
            return getfromResult(db.getResult().get(0));
        }
        return null;
    }

    public static Book get(String id) {
        if (StringUtils.isStrictlyNumeric(id) && Integer.valueOf(id) > 0 && db.select("id, title, content", "book", "id = '" + id + "' LIMIT 1")) {
            return getfromResult(db.getResult().get(0));
        }
        return null;
    }

    /**
     * start: [0 - ~]
     * limit: [1 - 100]
     */
    public static Books getAll(String start, String limit) {
        if (!StringUtils.isStrictlyNumeric(start) || Integer.valueOf(start) < 0
                || !StringUtils.isStrictlyNumeric(limit) || Integer.valueOf(limit) < 1 || Integer.valueOf(limit) > 100)
            return null;
        if (db.select("id, title, content", "book", "1 ORDER BY id ASC LIMIT " + start + "," + limit)) {
            Books result = new Books();
            for (ArrayList<String> record : db.getResult()) {
                result.add(getfromResult(record));
            }
            return result;
        }
        return null;
    }

    /**
     * start: [0 - ~]
     * limit: [1 - 100]
     */
    public static Books search(String title, String content, String[] categories, String start, String limit) {
        String condition = "1";
        String table = "book";
        if (title != null && !"".equals(title)) condition += " AND book.title like '%" + DB.validSql(title) + "%'";
        if (content != null && !"".equals(content)) condition += " AND book.content like '%" + DB.validSql(content) + "%'";
        if (categories != null && categories.length > 0) {
            table += ", book_category";
            condition += " AND book.id = book_category.book_id";
            for (String category : categories) {
                condition += " AND book_category.category_id = '" + category + "'";
            }

        }

        if (db.select("book.id, book.title, book.content", table, condition + " ORDER BY book.id ASC LIMIT " + start + "," + limit)) {
            Books result = new Books();
            for (ArrayList<String> record : db.getResult()) {
                result.add(getfromResult(record));
            }
            return result;
        }
        return null;
    }

    /**
     * start: [0 - ~]
     * limit: [1 - 100]
     */
    public static Books getAllhasCategory(String categoryId, String start, String limit) {
        if (!StringUtils.isStrictlyNumeric(categoryId) || Integer.valueOf(categoryId) < 0
                || !StringUtils.isStrictlyNumeric(start) || Integer.valueOf(start) < 0
                || !StringUtils.isStrictlyNumeric(limit) || Integer.valueOf(limit) < 1 || Integer.valueOf(limit) > 100)
            return null;
        if (db.select("book.id, book.title, book.content", "book, book_category",
                "book.id = book_category.book_id AND book_category.category_id = " + categoryId + " ORDER BY book.id ASC LIMIT " + start + "," + limit)) {
            Books result = new Books();
            for (ArrayList<String> record : db.getResult()) {
                result.add(getfromResult(record));
            }
            return result;
        }
        return null;
    }

    //Check if this book has this category categoryId
    public static boolean ishasCategory(Book book, String categoryId) {
        if (book == null || !StringUtils.isStrictlyNumeric(book.getId()) || book.getIdInt() < 1) return false;
        if (!StringUtils.isStrictlyNumeric(categoryId) || Integer.valueOf(categoryId) < 1) return false;
        return db.select("*", "book_category", "book_id = " + book.getId() + " AND category_id = " + categoryId);
    }

    public static BookCategory insertCategory(String bookId, String categoryId) {
        if (!StringUtils.isStrictlyNumeric(bookId) || Integer.valueOf(bookId) < 1) return null;
        if (!StringUtils.isStrictlyNumeric(categoryId) || Integer.valueOf(categoryId) < 1) return null;
        if (db.insert("book_category", new String[]{"book_id", "category_id"}, new String[]{bookId, categoryId})) {
            return BookCategoryDao.get(bookId, categoryId);
        } else return null;
    }

    public static boolean deleteCategory(String bookId, String categoryId) {
        if (!StringUtils.isStrictlyNumeric(bookId) || Integer.valueOf(bookId) < 1) return false;
        if (!StringUtils.isStrictlyNumeric(categoryId) || Integer.valueOf(categoryId) < 1) return false;
        return db.execute("DELETE FROM book_category WHERE book_id = " + bookId + " AND category_id = " + categoryId);
    }
}
