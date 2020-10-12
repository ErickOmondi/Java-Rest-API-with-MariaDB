package com.example.restExceptionHandler;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ApplicationExceptionHandler implements ExceptionMapper<RestApplicationException>
{

    @Override
    public Response toResponse(RestApplicationException exception) {
        return Response.status(Status.BAD_REQUEST).entity(exception.getMessage()).build();
    }


}
