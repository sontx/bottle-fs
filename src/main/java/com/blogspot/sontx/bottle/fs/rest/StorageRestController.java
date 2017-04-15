package com.blogspot.sontx.bottle.fs.rest;

import com.blogspot.sontx.bottle.fs.bean.UploadResult;
import com.blogspot.sontx.bottle.fs.security.Secured;
import com.blogspot.sontx.bottle.fs.service.StorageService;
import com.blogspot.sontx.bottle.fs.service.StorageServiceImpl;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.InputStream;

@Path("storage")
public class StorageRestController {

    private StorageService storageService = new StorageServiceImpl();

    @POST
    @Secured
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response upload(@FormDataParam("file") InputStream in, @Context SecurityContext securityContext) {
        UploadResult uploadResult = storageService.upload(in);
        if (uploadResult != null)
            return Response.ok(uploadResult).build();
        else
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @GET
    @Path("{fileName}")
    public StreamingOutput downloadAsStream(@PathParam("fileName") String fileName) {
        return storageService.getStreamingOutput(fileName);
    }
}
