package com.controllers;

import com.google.gson.Gson;
import com.models.AnimalServices;
import org.json.simple.JSONArray;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/getSearchKeys")
public class GetSearchKeys {
    @GET()
    public Response getSearchKeys(){
        Gson gson = new Gson();
        JSONArray jsonArray = AnimalServices.getInstance().getSearchKeys();
        return Response.ok(gson.toJson(jsonArray)).build();
    }
}
