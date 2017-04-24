package com.controllers;

import com.google.gson.Gson;
import com.models.Admin;
import com.models.LoginServices;
import org.json.simple.JSONObject;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/login")
public class Login {
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(String formData)throws java.io.IOException,
            com.github.fge.jsonschema.core.exceptions.ProcessingException,
            java.security.NoSuchAlgorithmException,
            org.json.simple.parser.ParseException{

        Gson gson = new Gson();
        String validationSchema = "{\"$schema\":\"http://json-schema.org/draft-04/schema#\",\"definitions\":{},\"id\":\"http://example.com/example.json\",\"properties\":{\"email\":{\"id\":\"/properties/email\",\"type\":\"string\"},\"password\":{\"id\":\"/properties/password\",\"type\":\"string\"}},\"required\":[\"password\",\"email\"],\"type\":\"object\"}";

        JSONObject errors = new JSONObject();
        CustomJsonValidator customJsonValidator = new CustomJsonValidator();
        if(!customJsonValidator.isValid(formData, validationSchema)){
            errors = customJsonValidator.getErrors(formData,validationSchema);
            return Response.serverError().entity(gson.toJson(errors)).build();
        }

        Admin admin = gson.fromJson(formData, Admin.class);

        if(LoginServices.getInstance().isValidCredential(admin)){
            String token = LoginServices.getInstance().getToken(admin.getEmail());
            JSONObject responseData = new JSONObject();
            responseData.put("token", token);
            return Response.ok(gson.toJson(responseData), MediaType.APPLICATION_JSON).build();
        }
        errors.put("email", "Invalid Credentials");
        return Response.serverError().entity(gson.toJson(errors)).build();
    }
}
