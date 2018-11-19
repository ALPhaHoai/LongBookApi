package com.longbook.service;

import com.longbook.dao.AdminDao;
import org.glassfish.jersey.internal.util.Base64;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.StringTokenizer;

/**
 * Created by Long
 * Date: 11/16/2018
 * Time: 3:49 PM
 */

@Provider
public class AuthFilter implements ContainerRequestFilter {

    //Valid Auth Header: Authorization: Basic Base64Encoed(username:password)
    private static final String AUTH_HEADER = "Authorization";
    private static final String AUTH_HEADER_PREFIX = "Basic ";

    @Override
    public void filter(ContainerRequestContext requestContext) {
        //Ignore auth if request is get method
        if ("GET".equalsIgnoreCase(requestContext.getMethod()) || "OPTIONS".equalsIgnoreCase(requestContext.getMethod()) ) return;
        try {
            String authToken = requestContext.getHeaders().get(AUTH_HEADER).get(0);
            if (authToken.startsWith(AUTH_HEADER_PREFIX)) {
                authToken = authToken.replaceFirst(AUTH_HEADER_PREFIX, "");
                authToken = Base64.decodeAsString(authToken);
                StringTokenizer tokenizer = new StringTokenizer(authToken, ":");//token like username:password
                if (AdminDao.isValid(tokenizer.nextToken(), tokenizer.nextToken())) {
                    return;
                }
            }
        } catch (Exception ex) {
            System.out.println("Authorization failed");
        }
        requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
    }

}
