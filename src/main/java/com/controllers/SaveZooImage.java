package com.controllers;

import com.google.gson.Gson;
import com.models.ZooServices;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/saveZooImage")
public class SaveZooImage {
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveZooImages(String params) throws org.json.simple.parser.ParseException{
        Gson gson = new Gson();

        JSONObject errors = new JSONObject();

        //Get Request Json Web Token
        JSONParser parser = new JSONParser();
        JSONObject jsonParams = (JSONObject) parser.parse(params);
        String token = jsonParams.containsKey("token") ? jsonParams.get("token").toString() : "wrong";

        //Verify JSON web token
        if(!JWTServices.getInstance().verifyJsonWT(token)){
            errors.put("invalidToken", "Access Denied. UnAuthorized User");
            return Response.serverError().entity(gson.toJson(errors)).build();
        }

        //Save Image Link
        ZooServices.getInstance().saveZooImageLink(jsonParams);
        return Response.ok().build();
    }
}
