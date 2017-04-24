package com.controllers;

import com.google.gson.Gson;
import com.models.AnimalServices;
import com.models.AnimalZoo;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@Path("/fetchAnimalZoos/{common_name}")
public class FetchAnimalZoos {
    @GET
    public Response fetchAnimalZoos(@PathParam("common_name") String common_name){
        Gson gson = new Gson();
        ArrayList<AnimalZoo> fetchAnimalZoos = AnimalServices.getInstance().fetchAnimalZoos(common_name);
        return Response.ok(gson.toJson(fetchAnimalZoos), MediaType.APPLICATION_JSON).build();
    }
}
