package com.controllers;

import com.google.gson.Gson;
import com.models.Animal;
import com.models.AnimalServices;
import com.models.ZooServices;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/addAnimal")
public class AddAnimal {
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addAnimal(String formData) throws java.io.IOException,
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

        //Validate Json Input
        String validationSchema = "{\"$schema\":\"http://json-schema.org/draft-04/schema#\",\"additionalProperties\":false,\"definitions\":{},\"id\":\"http://example.com/example.json\",\"properties\":{\"animal_class\":{\"id\":\"/properties/animal_class\",\"type\":\"string\"},\"common_name\":{\"id\":\"/properties/common_name\",\"type\":\"string\"},\"description\":{\"id\":\"/properties/description\",\"type\":\"string\"},\"family\":{\"id\":\"/properties/family\",\"type\":\"string\"},\"genus\":{\"id\":\"/properties/genus\",\"type\":\"string\"},\"order\":{\"id\":\"/properties/order\",\"type\":\"string\"},\"species\":{\"id\":\"/properties/species\",\"type\":\"string\"},\"token\":{\"id\":\"/properties/token\",\"type\":\"string\"}},\"required\":[\"animal_class\",\"description\",\"family\",\"species\",\"token\",\"common_name\",\"genus\",\"order\"],\"type\":\"object\"}";
        CustomJsonValidator customJsonValidator = new CustomJsonValidator();
        if(!customJsonValidator.isValid(formData, validationSchema)){
            errors = customJsonValidator.getErrors(formData,validationSchema);
            return Response.serverError().entity(gson.toJson(errors)).build();
        }

        //Process Add New Animal
        Animal animal = gson.fromJson(formData, Animal.class);

        if(!AnimalServices.getInstance().AnimalExists(animal.getCommon_name())){
            AnimalServices.getInstance().addAnimal(animal);
            return Response.ok().build();
        }
        errors.put("common_name", "Animal name already exists");
        return Response.serverError().entity(gson.toJson(errors)).build();
    }
}
