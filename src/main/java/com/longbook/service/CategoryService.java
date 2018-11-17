package com.longbook.service;

import com.longbook.dao.BookDao;
import com.longbook.dao.CategoryDao;
import com.longbook.model.Books;
import com.longbook.model.Categories;
import com.longbook.model.Category;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static lib.MyLib.*;

/**
 * Created by Long
 * Date: 11/15/2018
 * Time: 7:33 PM
 */
@Path("/category")
public class CategoryService {
    /**
     * start: [0 - ~]
     * limit: [1 - 100]
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@DefaultValue("0") @QueryParam("start") String start, @DefaultValue("10") @QueryParam("limit") String limit) {
        String validResult = isValidPaginationInput(start, limit);
        if (!"OK".equals(validResult)) return echoErrorMessage(validResult);

        Categories allCategory = CategoryDao.getAll(start, limit);
        return (allCategory == null) ? echoErrorMessage("Empty data") : echoSuccessMessage(allCategory.toJSON());
    }

    @GET
    @Path("{category_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("category_id") String categoryId) {
        Category category = CategoryDao.get(categoryId);
        return (category == null) ? echoErrorMessage("This category doesn't exist") : echoSuccessMessage(category.toJSON());
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insert(String categoryData) {
        String name = getCategoryNamefromInput(categoryData);
        if (name == null) return echoErrorMessage("Invalid input data");
        Category category = new Category();
        category.setName(name);
        Category dupCategory = CategoryDao.gethasName(category.getName());
        if (dupCategory != null) {
            return echoErrorMessage("Duplicate content with category " + dupCategory.getId());
        } else {
            Category insertCategory = CategoryDao.insert(category);
            return (insertCategory == null) ? echoErrorMessage("Can't insert this category") : echoSuccessMessage(insertCategory.toJSON());
        }

    }

    @DELETE
    @Path("/{category_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("category_id") String categoryId) {
        Category category = CategoryDao.get(categoryId);
        if (category != null) {
            if (CategoryDao.delete(category)) return echoSuccessMessage("Delete this category successful");
            else return echoErrorMessage("Can't delete this category");
        } else return echoErrorMessage("This category doesn't exist");
    }

    @PUT
    @Path("/{category_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("category_id") String categoryId, String categoryData) {
        String name = getCategoryNamefromInput(categoryData);
        if (name == null) return echoErrorMessage("Invalid input data");
        Category category = new Category();
        category.setName(name);
        Category dupCategory = CategoryDao.gethasNamenotId(categoryId, category.getName());

        if (CategoryDao.get(categoryId) != null) {
            if (dupCategory != null) return echoErrorMessage("Duplicate category: " + dupCategory.getId());
            else {
                category.setId(categoryId);
                Category categoryUpdated = CategoryDao.update(category);
                if (categoryUpdated != null) return echoSuccessMessage(categoryUpdated.toJSON());
                else return echoErrorMessage("Can't update this category");
            }
        } else return echoErrorMessage("This category doesn't exist");
    }


    /**
     * start: [0 - ~]
     * limit: [1 - 100]
     */
    @GET
    @Path("/{category_id}/book")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBook(@PathParam("category_id") String categoryId, @DefaultValue("0") @QueryParam("start") String start, @DefaultValue("10") @QueryParam("limit") String limit) {
        String validResult = isValidPaginationInput(start, limit);
        if (!"OK".equals(validResult)) return echoErrorMessage(validResult);

        if (CategoryDao.get(categoryId) != null) {
            Books allBook = BookDao.getAllhasCategory(categoryId, start, limit);
            return (allBook == null) ? echoErrorMessage("Empty data") : echoSuccessMessage(allBook.toJSON());
        } else return echoErrorMessage("This category doesn't exist");
    }


    private static String getCategoryNamefromInput(String categoryData) {
        try {
            JSONObject jsonObject = (JSONObject) (new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE)).parse(categoryData);
            String name = jsonObject.getAsString("name");
            return (name == null || name.length() == 0) ? null : name;
        } catch (Exception ex) {
            return null;
        }

    }
}
