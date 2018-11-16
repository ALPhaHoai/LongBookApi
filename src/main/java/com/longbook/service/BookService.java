package com.longbook.service;

import com.longbook.dao.BookDao;
import com.longbook.dao.CategoryDao;
import com.longbook.model.Book;
import com.longbook.model.Books;
import com.longbook.model.Categories;
import lib.MyLib;

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
        return (allBook == null) ? echoErrorMessage("Empty data") : echoSuccessMessage(allBook.toJSON());
    }

    @GET
    @Path("{book_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("book_id") String bookId) {
        Book book = BookDao.get(bookId);
        return (book == null) ? echoErrorMessage("This book doesn't exist") : echoSuccessMessage(book.toJSON());
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insert(Book book) {
        Book dupBook = BookDao.get(book.getTitle(), book.getContent());
        if (dupBook != null) {
            return echoErrorMessage("Duplicate content with book " + dupBook.getId());
        } else {
            Book insertBook = BookDao.insert(book);
            return (insertBook == null) ? echoErrorMessage("Can't insert this book") : echoSuccessMessage(insertBook.toJSON());
        }
    }



    @DELETE
    @Path("/{book_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("book_id") String bookId) {
        Book book = BookDao.get(bookId);
        if (book != null) {
            if (BookDao.delete(book)) return echoSuccessMessage("Delete this book successful");
            else return echoErrorMessage("Can't delete this book");
        } else return echoErrorMessage("This book doesn't exist");
    }

    @PUT
    @Path("/{book_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("book_id") String bookId, Book book) {
        if (BookDao.get(bookId) != null) {
            Book dupBook = BookDao.getNotId(bookId, book.getTitle(), book.getContent());
            if (dupBook != null) {
                return echoErrorMessage("Duplicate content with book " + dupBook.getId());
            } else {
                book.setId(bookId);
                Book bookUpdated = BookDao.update(book);
                if (bookUpdated != null) return echoSuccessMessage(bookUpdated.toJSON());
                else return echoErrorMessage("Can't update this book");
            }
        } else return echoErrorMessage("This book doesn't exist");
    }

    @GET
    @Path("/{book_id}/category")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("book_id") String bookId, @DefaultValue("0") @QueryParam("start") String start, @DefaultValue("10") @QueryParam("limit") String limit) {
        String validResult = MyLib.isValidPaginationInput(start, limit);
        if (!"OK".equals(validResult))
            return Response.status(Response.Status.BAD_REQUEST).entity(echoErrorMessage(validResult)).build();

        Categories allCategories = CategoryDao.getAllhasBook(bookId, start, limit);
        return (allCategories == null) ? echoErrorMessage("Empty data") : echoSuccessMessage(allCategories.toJSON());
    }


}
