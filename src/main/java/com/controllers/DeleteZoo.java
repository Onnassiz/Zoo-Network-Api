package com.controllers;

import com.google.gson.Gson;
import com.models.ZooServices;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/deleteZoo/{token}/{zoo_name}")
public class DeleteZoo {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteZoo(@PathParam("token") String token, @PathParam("zoo_name") String zoo_name){
        Gson gson = new Gson();
        JSONObject errors = new JSONObject();

        //Verify JSON web token
        if(!JWTServices.getInstance().verifyJsonWT(token)){
            errors.put("invalidToken", "Access Denied. UnAuthorized User");
            return Response.serverError().entity(gson.toJson(errors)).build();
        }

        //delete Zoo
        ZooServices.getInstance().deleteZoo(zoo_name);
        return Response.ok().build();
    }
}
