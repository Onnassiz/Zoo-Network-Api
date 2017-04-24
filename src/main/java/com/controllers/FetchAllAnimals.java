package com.controllers;

import com.google.gson.Gson;
import com.models.Animal;
import com.models.AnimalServices;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@Path("/fetchAllAnimals")
public class FetchAllAnimals {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response fetchAllAnimals() throws org.json.simple.parser.ParseException{
        Gson gson = new Gson();
        ArrayList<Animal> animals = AnimalServices.getInstance().fetchAllAnimals();
        return Response.ok(gson.toJson(animals), MediaType.APPLICATION_JSON).build();
    }
}
