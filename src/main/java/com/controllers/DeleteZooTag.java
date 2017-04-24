package com.controllers;

import com.google.gson.Gson;
import com.models.AnimalServices;
import org.json.simple.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/deleteTag/{token}/{common_name}/{zoo_name}")
public class DeleteZooTag {
    @GET
    public Response deleteTag(@PathParam("token") String token, @PathParam("common_name") String common_name, @PathParam("zoo_name") String zoo_name){
        Gson gson = new Gson();
        JSONObject errors = new JSONObject();

        //Verify JSON web token
        if(!JWTServices.getInstance().verifyJsonWT(token)){
            errors.put("invalidToken", "Access Denied. UnAuthorized User");
            return Response.serverError().entity(gson.toJson(errors)).build();
        }
        //delete Tag
        AnimalServices.getInstance().deleteZooTag(common_name, zoo_name);
        return Response.ok().build();
    }
}
