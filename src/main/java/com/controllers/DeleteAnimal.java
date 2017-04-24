package com.controllers;

import com.google.gson.Gson;
import com.models.AnimalServices;
import com.models.ZooServices;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/deleteAnimal/{token}/{common_name}")
public class DeleteAnimal {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteAnimal(@PathParam("token") String token, @PathParam("common_name") String common_name){
        Gson gson = new Gson();
        JSONObject errors = new JSONObject();

        //Verify JSON web token
        if(!JWTServices.getInstance().verifyJsonWT(token)){
            errors.put("invalidToken", "Access Denied. UnAuthorized User");
            return Response.serverError().entity(gson.toJson(errors)).build();
        }

        //delete Animal
        AnimalServices.getInstance().deleteAnimal(common_name);
        return Response.ok().build();
    }
}
