package com.longbook.service;

import com.longbook.dao.BookCategoryDao;
import com.longbook.dao.BookDao;
import com.longbook.dao.CategoryDao;
import com.longbook.model.*;
import com.mysql.cj.util.StringUtils;
import lib.MyLib;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static lib.MyLib.echoErrorMessage;
import static lib.MyLib.echoSuccessMessage;

/**
 * Created by Long
 * Date: 11/15/2018
 * Time: 10:33 AM
 */
@Path("/book")
public class BookService {
    //Book Message
    public static final String NOT_EXIST = "This book doesn't exist";
    public static final String EMPTY_DATA = "Empty data";
    public static final String CANNOT_INSERT = "Can't insert this book";
    public static final String CANNOT_DELETE = "Can't delete this book";
    public static final String CANNOT_UPDATE = "Can't update this book";
    public static final String DELETE_SUCCESSFUL = "Delete this book successful";
    public static final String DUPPLICATE_CONTENT = "Duplicate content with book ";
    public static final String INVALID_INPUT_DATA = "Invalid input data";

    //Category of book
    public static final String ALREADY_HAS_CATEGORY = "This book is already has this category";
    public static final String NOT_HAS_CATEGORY = "This book is not has this category";
    public static final String HAS_NO_CATEGORY = "This book has no category";
    public static final String DELETE_ALL_CATEGORY_SUCCESSFUL = "Delete all category of this book successful";
    public static final String CANNOT_DELETE_ALL_CATEGORY = "Can't delete all category of this book";
    public static final String CANNOT_INSERT_CATEGORY = "Can't insert this category for this book";
    public static final String INSERT_CATEGORY_SUCCESSFUL = "Insert this category for this book successful";
    public static final String CANNOT_DELETE_CATEGORY = "Can't delete this category for this book";
    public static final String DELETE_CATEGORY_SUCCESSFUL = "Delete this category of this book successful";

    /**
     * start: [0 - ~]
     * limit: [1 - 100]
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@DefaultValue("0") @QueryParam("start") String start, @DefaultValue("10") @QueryParam("limit") String limit) {
        String validResult = MyLib.isValidPaginationInput(start, limit);
        if (!"OK".equals(validResult))
            return Response.status(Response.Status.BAD_REQUEST).entity(echoErrorMessage(validResult)).build();

        Books allBook = BookDao.getAll(start, limit);
        return (allBook == null) ? echoErrorMessage(EMPTY_DATA) : echoSuccessMessage(allBook.toJSON());
    }

    @GET
    @Path("{book_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("book_id") String bookId) {
        Book book = BookDao.get(bookId);
        return (book == null) ? echoErrorMessage(NOT_EXIST) : echoSuccessMessage(book.toJSON());
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insert(Book book) {
        if (book.getTitle() == null || book.getTitle().length() == 0 || book.getContent() == null || book.getContent().length() == 0)
            return echoErrorMessage("Invalid input data");
        Book dupBook = BookDao.get(book.getTitle(), book.getContent());
        if (dupBook != null) {
            return echoErrorMessage(DUPPLICATE_CONTENT + dupBook.getId());
        } else {
            Book insertBook = BookDao.insert(book);
            return (insertBook == null) ? echoErrorMessage(CANNOT_INSERT) : echoSuccessMessage(insertBook.toJSON());
        }
    }


    @DELETE
    @Path("/{book_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("book_id") String bookId) {
        Book book = BookDao.get(bookId);
        if (book == null) {
            return echoErrorMessage(NOT_EXIST);
        } else {
            if (BookDao.delete(book)) return echoSuccessMessage(DELETE_SUCCESSFUL);
            else return echoErrorMessage(CANNOT_DELETE);
        }
    }

    @PUT
    @Path("/{book_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("book_id") String bookId, Book book) {
        if (book.getTitle() == null || book.getTitle().length() == 0 || book.getContent() == null || book.getContent().length() == 0)
            return echoErrorMessage("Invalid input data");

        if (BookDao.get(bookId) == null) {
            return echoErrorMessage(NOT_EXIST);
        } else {
            Book dupBook = BookDao.getNotId(bookId, book.getTitle(), book.getContent());
            if (dupBook != null) {
                return echoErrorMessage(DUPPLICATE_CONTENT + dupBook.getId());
            } else {
                book.setId(bookId);
                Book bookUpdated = BookDao.update(book);
                if (bookUpdated != null) return echoSuccessMessage(bookUpdated.toJSON());
                else return echoErrorMessage(CANNOT_UPDATE);
            }
        }
    }

    @GET
    @Path("/{book_id}/category")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("book_id") String bookId, @DefaultValue("0") @QueryParam("start") String start, @DefaultValue("10") @QueryParam("limit") String limit) {
        String validResult = MyLib.isValidPaginationInput(start, limit);
        if (!"OK".equals(validResult))
            return Response.status(Response.Status.BAD_REQUEST).entity(echoErrorMessage(validResult)).build();

        if (BookDao.get(bookId) == null) {
            return echoErrorMessage(NOT_EXIST);
        } else {
            Categories allCategories = CategoryDao.getAllhasBook(bookId, start, limit);
            return (allCategories == null) ? echoErrorMessage(EMPTY_DATA) : echoSuccessMessage(allCategories.toJSON());
        }
    }

    @POST
    @Path("/{book_id}/category")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCategory(@PathParam("book_id") String bookId, String inputData) {
        Book book = BookDao.get(bookId);
        if (book == null) {
            return echoErrorMessage(NOT_EXIST);
        } else {
            Category category = getCategoryfromInput(inputData);
            if (category == null) return echoErrorMessage(INVALID_INPUT_DATA);
            else if (CategoryDao.get(category.getId()) == null) return echoErrorMessage(CategoryService.NOT_EXIST);
            else if (BookDao.ishasCategory(book, category.getId())) return echoErrorMessage(ALREADY_HAS_CATEGORY);
            else {
                BookCategory bookCategory = BookDao.insertCategory(book.getId(), category.getId());
                return bookCategory == null ? echoErrorMessage(CANNOT_INSERT_CATEGORY) : echoSuccessMessage(INSERT_CATEGORY_SUCCESSFUL);
            }
        }
    }

    @DELETE
    @Path("/{book_id}/category/{category_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCategory(@PathParam("book_id") String bookId, @PathParam("category_id") String categoryId) {
        Book book = BookDao.get(bookId);
        if (book == null) {
            return echoErrorMessage(NOT_EXIST);
        } else {
            Category category = CategoryDao.get(categoryId);
            if (category == null) return echoErrorMessage(CategoryService.NOT_EXIST);
            else {
                if (!BookDao.ishasCategory(book, category.getId())) return echoErrorMessage(NOT_HAS_CATEGORY);
                else {
                    return BookDao.deleteCategory(book.getId(), category.getId()) ? echoSuccessMessage(DELETE_CATEGORY_SUCCESSFUL) : echoErrorMessage(CANNOT_DELETE_CATEGORY);
                }
            }
        }
    }

    @DELETE
    @Path("/{book_id}/category")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAllCategory(@PathParam("book_id") String bookId) {
        Book book = BookDao.get(bookId);
        if (book == null) {
            return echoErrorMessage(NOT_EXIST);
        } else {
            if (BookCategoryDao.getFromBook(book.getId()) == null) return echoErrorMessage(HAS_NO_CATEGORY);
            else if (BookDao.deleteAllCategory(book)) return echoSuccessMessage(DELETE_ALL_CATEGORY_SUCCESSFUL);
            else return echoErrorMessage(CANNOT_DELETE_ALL_CATEGORY);
        }
    }


    private static Category getCategoryfromInput(String inputData) {
        try {
            JSONObject jsonObject = (JSONObject) (new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE)).parse(inputData);
            String categoryId = jsonObject.getAsString("category_id");
            if (StringUtils.isStrictlyNumeric(categoryId) && Integer.valueOf(categoryId) > 0) {
                return new Category(categoryId);
            } else return null;
        } catch (Exception ex) {
            return null;
        }
    }

}
