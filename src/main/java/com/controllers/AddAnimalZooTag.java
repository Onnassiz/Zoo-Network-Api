package com.controllers;

import com.google.gson.Gson;
import com.models.AnimalServices;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/addAnimalZooTag")
public class AddAnimalZooTag {
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addAnimalZooTag(String formData) throws
            org.json.simple.parser.ParseException,
            java.io.IOException, com.github.fge.jsonschema.core.exceptions.ProcessingException{

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

        //Validate Json Input Token
        String validationSchema = "{\"$schema\":\"http://json-schema.org/draft-04/schema#\",\"additionalProperties\":false,\"definitions\":{},\"id\":\"http://example.com/example.json\",\"properties\":{\"animal_count\":{\"id\":\"/properties/animal_count\",\"type\":\"string\"},\"common_name\":{\"id\":\"/properties/common_name\",\"type\":\"string\"},\"token\":{\"id\":\"/properties/token\",\"type\":\"string\"},\"zoo_name\":{\"id\":\"/properties/zoo_name\",\"type\":\"string\"}},\"required\":[\"common_name\",\"animal_count\",\"token\",\"zoo_name\"],\"type\":\"object\"}";
        CustomJsonValidator customJsonValidator = new CustomJsonValidator();
        if(!customJsonValidator.isValid(formData, validationSchema)){
            errors = customJsonValidator.getErrors(formData,validationSchema);
            return Response.serverError().entity(gson.toJson(errors)).build();
        }

        //Add Animal Zoo Tag
        AnimalServices.getInstance().addAnimalZooTag(formJson);
        return Response.ok().build();
    }
}
