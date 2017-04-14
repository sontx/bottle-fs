package com.blogspot.sontx.bottle.fs.rest;

import com.blogspot.sontx.bottle.fs.bean.UploadFile;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/home")
public class HomeRestController {
    @GET
    public Response home() {
        return Response.ok("Welcome to bottle-fs").build();
    }

    @POST
    public Response post(UploadFile uploadFileStorage) {

        return Response.ok().build();
    }
}
