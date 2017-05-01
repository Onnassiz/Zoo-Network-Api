package com.controllers;

import com.google.gson.Gson;
import com.models.ZooImageLink;
import com.models.ZooServices;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@Path("/fetchZooImageLinks")
public class FetchZooLinks {
    @GET
    public Response fetchZooLinks(){
        Gson gson = new Gson();
        ArrayList<ZooImageLink> zooImageLinks = ZooServices.getInstance().fetchZooImageLinks();
        return Response.ok(gson.toJson(zooImageLinks)).build();
    }
}
