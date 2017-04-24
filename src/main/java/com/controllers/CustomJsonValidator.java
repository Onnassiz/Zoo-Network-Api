package com.controllers;


import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.fge.jsonschema.main.JsonValidator;
import com.google.gson.Gson;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.Iterator;

public class CustomJsonValidator {

    private Boolean hasNullFields(String json, String finalJson) throws org.json.simple.parser.ParseException{
        JSONParser parser = new JSONParser();
        JSONObject original = (JSONObject) parser.parse(json);
        JSONObject modifiedJson = (JSONObject) parser.parse(finalJson);

        return (original.size() != modifiedJson.size()) ? true : false;
    }

    private String deleteNullFields(String json) throws org.json.simple.parser.ParseException{
        JSONParser parser = new JSONParser();
        JSONObject validationJson = (JSONObject) parser.parse(json);
        JSONObject finalJson = new JSONObject();

        for(Iterator iterator = validationJson.keySet().iterator(); iterator.hasNext();) {
            String key = (String) iterator.next();
            if(!validationJson.get(key).equals("")){
                finalJson.put(key, validationJson.get(key));
            }
        }
        Gson gson = new Gson();
        return gson.toJson(finalJson);
    }

    private ProcessingReport getResult(String json, String jsonSchema) throws
            java.io.IOException,
            com.github.fge.jsonschema.core.exceptions.ProcessingException,
            org.json.simple.parser.ParseException{

        json = deleteNullFields(json);

        final JsonNode data = JsonLoader.fromString(json);
        final JsonNode dataSchema = JsonLoader.fromString(jsonSchema);

        final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
        JsonValidator validator = factory.getValidator();

        ProcessingReport report = validator.validate(dataSchema, data);

        return report;
    }

    public boolean isValid(String json, String jsonSchema) throws
            java.io.IOException,
            com.github.fge.jsonschema.core.exceptions.ProcessingException,
            org.json.simple.parser.ParseException{


        ProcessingReport report = this.getResult(json, jsonSchema);
        return (report.isSuccess()) ? true : false;
    }

    public JSONObject getErrors(String json, String jsonSchema) throws
            java.io.IOException,
            com.github.fge.jsonschema.core.exceptions.ProcessingException,
            org.json.simple.parser.ParseException{
        ProcessingReport report = this.getResult(json, jsonSchema);
        JSONObject errors = new JSONObject();
        Iterator iterator = report.iterator();
        while (iterator.hasNext()){
            ProcessingMessage message = (ProcessingMessage) iterator.next();

            if(hasNullFields(json, deleteNullFields(json))){
                Gson gson = new Gson();
                String missing = gson.toJson(message.asJson().get("missing"));
                JSONParser parser = new JSONParser();
                JSONObject missingFields = (JSONObject) parser.parse(missing);
                JSONArray fields = (JSONArray) missingFields.get("_children");
                for (int i = 0; i < fields.size(); i++){
                    JSONObject item = (JSONObject) fields.get(i);
                    errors.put(item.get("_value"), "This field is required");
                }
            }else {
                String index = message.asJson().get("schema").get("pointer").asText();
                String i = index.replace("/properties/", "");
                errors.put(i, message.getMessage());
            }
        }
        return errors;
    }
}
