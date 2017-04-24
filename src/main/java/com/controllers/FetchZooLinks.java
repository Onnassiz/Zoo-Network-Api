package com.controllers;

import com.google.gson.Gson;
import com.models.ZooLink;
import com.models.ZooServices;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@Path("/fetchZooLinks")
public class FetchZooLinks {
    @GET
    public Response fetchZooLinks(){
        Gson gson = new Gson();
        ArrayList<ZooLink> zooLinks = ZooServices.getInstance().fetchZooLinks();
        return Response.ok(gson.toJson(zooLinks)).build();
    }
}
