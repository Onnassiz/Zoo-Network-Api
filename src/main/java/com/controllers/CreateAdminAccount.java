package com.controllers;

import com.google.gson.Gson;
import com.models.Admin;
import com.models.AdminServices;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/createAdminAccount")
public class CreateAdminAccount {
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)

    public Response createAdminAccount(String formJsonData)
            throws java.io.IOException,
            com.github.fge.jsonschema.core.exceptions.ProcessingException,
            java.security.NoSuchAlgorithmException,
            org.json.simple.parser.ParseException{

        Gson gson = new Gson();

        String validationJsonSchema = "{\"$schema\":\"http://json-schema.org/draft-04/schema#\",\"additionalProperties\":false,\"definitions\":{},\"id\":\"http://example.com/example.json\",\"properties\":{\"email\":{\"id\":\"/properties/email\",\"type\":\"string\"},\"first_name\":{\"id\":\"/properties/first_name\",\"type\":\"string\"},\"last_name\":{\"id\":\"/properties/last_name\",\"type\":\"string\"},\"password\":{\"id\":\"/properties/password\",\"type\":\"string\"},\"password_confirmation\":{\"id\":\"/properties/password_confirmation\",\"type\":\"string\"}},\"required\":[\"password\",\"first_name\",\"last_name\",\"password_confirmation\",\"email\"],\"type\":\"object\"}";

        JSONObject errors = new JSONObject();
        CustomJsonValidator customJsonValidator = new CustomJsonValidator();
        if(!customJsonValidator.isValid(formJsonData, validationJsonSchema)){
            errors = customJsonValidator.getErrors(formJsonData,validationJsonSchema);
            return Response.serverError().entity(gson.toJson(errors)).build();
        }

        Admin admin = gson.fromJson(formJsonData, Admin.class);

        if(!AdminServices.getInstance().emailExists(admin.getEmail())){
            AdminServices.getInstance().createAdmin(admin);
            return Response.ok().build();
        }

        errors.put("email", "This email is already taken");
        return Response.serverError().entity(gson.toJson(errors)).build();
    }
}
