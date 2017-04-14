package com.blogspot.sontx.bottle.fs.rest;

import com.blogspot.sontx.bottle.fs.bean.UploadResult;
import com.blogspot.sontx.bottle.fs.service.StorageService;
import com.blogspot.sontx.bottle.fs.service.StorageServiceImpl;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.InputStream;

@Path("storage")
public class StorageRestController {

    private StorageService storageService = new StorageServiceImpl();

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response upload(@FormDataParam("file") InputStream in, @Context SecurityContext securityContext) {
        UploadResult uploadResult = storageService.upload(in);
        if (uploadResult != null)
            return Response.ok(uploadResult).build();
        else
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
}
