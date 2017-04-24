package com.controllers;

import com.google.gson.Gson;
import com.models.AnimalServices;
import org.json.simple.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/deleteAnimalImages/{common_name}/{token}")
public class DeleteAllImagesForAnimal {
    @GET
    public Response deleteAllImagesForAnimal(@PathParam("common_name") String common_name, @PathParam("token") String token){
        Gson gson = new Gson();
        JSONObject errors = new JSONObject();

        //Verify JSON web token
        if(!JWTServices.getInstance().verifyJsonWT(token)){
            errors.put("invalidToken", "Access Denied. UnAuthorized User");
            return Response.serverError().entity(gson.toJson(errors)).build();
        }

        //delete Images
        AnimalServices.getInstance().deleteAnimalImages(common_name);
        return Response.ok().build();
    }
}
