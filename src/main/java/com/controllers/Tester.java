package com.controllers;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/tester")
public class Tester {
    @GET()
    public Response getSearchKeys(){
        return Response.ok("I am an example").build();
    }
}