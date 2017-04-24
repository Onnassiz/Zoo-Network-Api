package com.controllers;

import com.google.gson.Gson;
import com.models.ZooServices;
import org.json.simple.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/deleteZooImages/{zoo_name}/{token}")
public class DeleteAllImagesInZoo {
    @GET
    public Response deleteImagesInZoo(@PathParam("zoo_name") String zoo_name, @PathParam("token") String token){
        Gson gson = new Gson();
        JSONObject errors = new JSONObject();

        //Verify JSON web token
        if(!JWTServices.getInstance().verifyJsonWT(token)){
            errors.put("invalidToken", "Access Denied. UnAuthorized User");
            return Response.serverError().entity(gson.toJson(errors)).build();
        }

        //delete Images
        ZooServices.getInstance().deleteZooImages(zoo_name);
        return Response.ok().build();
    }
}
