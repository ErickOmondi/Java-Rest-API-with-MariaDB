package com.example.serviceResource;

import com.example.Dao.CustomerDao;
import com.example.Dao.Dbqueries.CustomerDAOImpl;
import com.example.models.Customer;
import com.example.restExceptionHandler.RestApplicationException;


import javax.inject.Inject;
import javax.json.stream.JsonGenerationException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Root resource (exposed at "myresource" path)
 */



@Path("myresource")
public class MyResource {


    private CustomerDao daoImpl = new CustomerDAOImpl();
    private Logger logger = Logger.getLogger(String.valueOf(MyResource.class));


    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getIt() {
        return "this is ok!";
    }

    @Path("status")
    @GET
    @Produces(value = {MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public String getStatus() {
        logger.info("Inside getStatus()...");
        System.out.println("runned succesfully");
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<note>\n" +
                "  <to>Erick</to>\n" +
                "  <from>Omondi</from>\n" +
                "  <heading>Reminder</heading>\n" +
                "  <body>JBOSS JNDIExample Status is OK...!</body>\n" +
                "</note>";

    }

    @GET
    @Path("getcustomer")
    @Produces(value = {MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public Response getCustomer(@DefaultValue("0") @QueryParam("id") int id) throws RestApplicationException {

        Response resp = daoImpl.getCustomer(id);
        logger.info("Inside getCustomer...");
        return resp;
    }

    @POST
    @Path("addcustomer")
    @Consumes(value = {MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @Produces(value = {MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public Response createCustomer(Customer customer) {


        logger.info("Inside createCustomer...");

        Response resp = daoImpl.createCustomer(customer);
        return resp;
    }

    @PUT
    @Path("updatecustomer")
    @Consumes(value = {MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @Produces(value = {MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public Response updateCustomer(Customer customer) {


        logger.info("Inside createCustomer...");

        Response resp = daoImpl.updateCustomer(customer);
        return resp;
    }

    @DELETE
    @Path("deletecustomer")
    @Consumes(value = {MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @Produces(value = {MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public Response deleteCustomer(
            @DefaultValue("0") @QueryParam("id") int id) {


        logger.info("Inside deleteCustomer...");

        Response resp = daoImpl.deleteCustomer(id);
        return resp;
    }

    @GET
    @Path("showallcustomers")
    @Produces(value = {MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public Response showAllCustomers() throws JsonGenerationException, IOException, RestApplicationException {

        logger.info("Inside showAllCustomers...");
        Response resp = daoImpl.getAllCustomers();

        if (resp == null){

            throw new RestApplicationException("id is not present in the request!!");
        }

        return resp;
    }
}
