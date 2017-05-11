package com.blogspot.sontx.bottle.fs.rest;

import com.blogspot.sontx.bottle.fs.bean.UploadResult;
import com.blogspot.sontx.bottle.fs.security.Role;
import com.blogspot.sontx.bottle.fs.security.Secured;
import com.blogspot.sontx.bottle.fs.service.StorageService;
import com.blogspot.sontx.bottle.fs.service.StorageServiceImpl;
import lombok.extern.log4j.Log4j;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.InputStream;

@Path("storage")
@Log4j
public class StorageRestController {

    private StorageService storageService = new StorageServiceImpl();

    @POST
    @Secured(Role.ROLE_USER)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response upload(@FormDataParam("file") InputStream in, @Context SecurityContext securityContext) {
        log.info("uploading...");
        UploadResult uploadResult = storageService.upload(in);
        if (uploadResult != null) {
            log.info("uploaded: " + uploadResult);
            return Response.ok(uploadResult).build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DELETE
    @Secured(Role.ROLE_ADMIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{fileName}")
    public Response delete(@PathParam("fileName") String fileName) {
        log.info("deleting " + fileName);
        boolean ok = storageService.delete(fileName);
        return ok ? Response.ok().build() : Response.status(400).build();
    }
//
//    @GET
//    @Path("{fileName}")
//    public Response download(@PathParam("fileName") String fileName, @QueryParam("dl") int downloadFlag) {
//        if (downloadFlag == 0) {
//            return Response.ok(storageService.getStreamingOutput(fileName)).build();
//        } else {
//            File file = storageService.getFile(fileName);
//            if (file.exists())
//                return Response.ok(file).build();
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
//        }
//    }
}
