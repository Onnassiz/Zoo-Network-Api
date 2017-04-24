package com.controllers;

import com.google.gson.Gson;
import com.models.Animal;
import com.models.AnimalServices;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@Path("/searchAnimalByKey/{key}")
public class SearchAnimalByKey {
    @GET
    public Response searchAnimalByKey(@PathParam("key") String key){
        Gson gson = new Gson();
        ArrayList<Animal> animals = AnimalServices.getInstance().searchAnimalByKey(key);
        return Response.ok(gson.toJson(animals)).build();
    }
}
