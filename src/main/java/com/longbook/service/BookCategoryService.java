package com.longbook.service;

import com.longbook.dao.BookCategoryDao;
import com.longbook.model.BookCategories;
import com.longbook.model.BookCategory;
import com.mysql.cj.util.StringUtils;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static lib.MyLib.*;

/**
 * Created by Long
 * Date: 11/15/2018
 * Time: 9:56 PM
 */

@Path("/bookcategory")
public class BookCategoryService {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@DefaultValue("0") @QueryParam("start") String start, @DefaultValue("10") @QueryParam("limit") String limit) {
        String validResult = isValidPaginationInput(start, limit);
        if (!"OK".equals(validResult)) return echoErrorMessage(validResult);

        BookCategories allBookCategory = BookCategoryDao.getAll(start, limit);
        return (allBookCategory == null) ? echoErrorMessage("Empty data") : echoSuccessMessage(allBookCategory.toJSON());
    }

    @GET
    @Path("{bookcategory_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("bookcategory_id") String bookCategoryId) {
        BookCategory bookCategory = BookCategoryDao.get(bookCategoryId);
        return (bookCategory == null) ? echoErrorMessage("This book category doesn't exist") : echoSuccessMessage(bookCategory.toJSON());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insert(String inputStringData) {
        BookCategory bookCategory = getBookCategoryfromInput(inputStringData);
        if (bookCategory == null) return echoErrorMessage("Invalid data");
        BookCategory dupBookCategory = BookCategoryDao.get(bookCategory.getBookId(), bookCategory.getCategoryId());
        if (dupBookCategory != null) {
            return echoErrorMessage("This book already has category " + dupBookCategory.getCategoryId());
        } else {
            BookCategory insertBookCategory = BookCategoryDao.insert(bookCategory);
            return (insertBookCategory == null) ? echoErrorMessage("Can't insert this book category") : echoSuccessMessage(insertBookCategory.toJSON());
        }
    }

    @DELETE
    @Path("/{bookcategory_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("bookcategory_id") String bookCategoryId) {
        BookCategory bookCategory = BookCategoryDao.get(bookCategoryId);
        if (bookCategory != null) {
            if (BookCategoryDao.delete(bookCategory)) return echoSuccessMessage("Delete this book category successful");
            else return echoErrorMessage("Can't delete this book category");
        } else return echoErrorMessage("This book category doesn't exist");
    }



    @PUT
    @Path("/{bookcategory_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("bookcategory_id") String bookCategoryId, String inputStringData) {
        BookCategory bookCategory = getBookCategoryfromInput(inputStringData);
        if (bookCategory == null) return echoErrorMessage("Invalid data");
        if (BookCategoryDao.get(bookCategoryId) != null) {
            BookCategory dupBookCategory = BookCategoryDao.getNotId(bookCategoryId, bookCategory.getBookId(), bookCategory.getCategoryId());
            if (dupBookCategory != null) {
                return echoErrorMessage("Duplicate content with book category " + dupBookCategory.getId());
            } else {
                bookCategory.setId(bookCategoryId);
                BookCategory bookCategoryUpdated = BookCategoryDao.update(bookCategory);
                if (bookCategoryUpdated != null) return echoSuccessMessage(bookCategoryUpdated.toJSON());
                else return echoErrorMessage("Can't update this book category");
            }
        } else return echoErrorMessage("This book category doesn't exist");
    }

    private static BookCategory getBookCategoryfromInput(String inputData) {
        try {
            JSONObject jsonObject = (JSONObject) (new net.minidev.json.parser.JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE)).parse(inputData);
            String bookId = jsonObject.getAsString("book_id");
            String categoryId = jsonObject.getAsString("category_id");
            if (StringUtils.isStrictlyNumeric(bookId) && Integer.valueOf(bookId) > 0
                    && StringUtils.isStrictlyNumeric(categoryId) && Integer.valueOf(categoryId) > 0) {
                return new BookCategory(bookId, categoryId);
            } else return null;
        } catch (Exception ex) {
            return null;
        }
    }
}
