package com.controllers;

import com.google.gson.Gson;
import com.models.AnimalZoo;
import com.models.ZooServices;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@Path("/fetchZooAnimals/{zoo_name}")
public class FetchZooAnimals {
    @GET
    public Response fetchZooAnimals(@PathParam("zoo_name") String zoo_name){
        Gson gson = new Gson();
        ArrayList<AnimalZoo> animalZoos = ZooServices.getInstance().fetchZooAnimals(zoo_name);
        return Response.ok(gson.toJson(animalZoos)).build();
    }
}
