package com.controllers;

import com.google.gson.Gson;
import com.models.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@Path("/fetchAnimalLinks")
public class FetchAnimalLinks {
    @GET
    public Response fetchAnimalLinks(){
        Gson gson = new Gson();
        ArrayList<AnimalLink> animalLinks = AnimalServices.getInstance().fetchAnimalLinks();
        return Response.ok(gson.toJson(animalLinks)).build();
    }
}
