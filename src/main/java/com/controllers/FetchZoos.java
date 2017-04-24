package com.controllers;

import com.google.gson.Gson;
import com.models.Zoo;
import com.models.ZooServices;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@Path("/fetchZoos")
public class FetchZoos {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response fetchZoos() throws org.json.simple.parser.ParseException{
        Gson gson = new Gson();
        ArrayList<Zoo> zoos = ZooServices.getInstance().fetchZoos();
        return Response.ok(gson.toJson(zoos), MediaType.APPLICATION_JSON).build();
    }
}
