package com.controllers;

import com.google.gson.Gson;
import com.models.Zoo;
import com.models.ZooServices;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/addZoo")
public class AddZoo {
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addZoo(String formData)throws java.io.IOException,
            com.github.fge.jsonschema.core.exceptions.ProcessingException,
            java.security.NoSuchAlgorithmException,
            org.json.simple.parser.ParseException{

        Gson gson = new Gson();
        JSONObject errors = new JSONObject();

        //Get Request Json Web Token
        JSONParser parser = new JSONParser();
        JSONObject formJson = (JSONObject) parser.parse(formData);
        String token = formJson.containsKey("token") ? formJson.get("token").toString() : "wrong";

        //Verify JSON web token
        if(!JWTServices.getInstance().verifyJsonWT(token)){
            errors.put("invalidToken", "Access Denied. UnAuthorized User");
            return Response.serverError().entity(gson.toJson(errors)).build();
        }

        //Validate JSON input
        String validationSchema = "{\"$schema\":\"http://json-schema.org/draft-04/schema#\",\"additionalProperties\":false,\"definitions\":{},\"id\":\"http://example.com/example.json\",\"properties\":{\"county\":{\"id\":\"/properties/county\",\"type\":\"string\"},\"house_number\":{\"id\":\"/properties/house_number\",\"type\":\"string\"},\"postcode\":{\"id\":\"/properties/postcode\",\"type\":\"string\"},\"street\":{\"id\":\"/properties/street\",\"type\":\"string\"},\"token\":{\"id\":\"/properties/token\",\"type\":\"string\"},\"website\":{\"id\":\"/properties/website\",\"type\":\"string\"},\"zoo_name\":{\"id\":\"/properties/zoo_name\",\"type\":\"string\"}},\"required\":[\"website\",\"zoo_name\",\"county\",\"token\",\"street\",\"postcode\"],\"type\":\"object\"}";
        CustomJsonValidator customJsonValidator = new CustomJsonValidator();
        if(!customJsonValidator.isValid(formData, validationSchema)){
            errors = customJsonValidator.getErrors(formData,validationSchema);
            return Response.serverError().entity(gson.toJson(errors)).build();
        }

        //Process Add New Zoo
        Zoo zoo = gson.fromJson(formData, Zoo.class);
        if(!ZooServices.getInstance().ZooExists(zoo.getZoo_name())){
            ZooServices.getInstance().addZoo(zoo);
            return Response.ok().build();
        }
        errors.put("zoo_name", "Zoo name already exists");
        return Response.serverError().entity(gson.toJson(errors)).build();
    }
}
