package com.example.Dao;

import javax.ws.rs.core.Response;
import com.example.models.Customer;

public interface CustomerDao {

    public Response getCustomer(int id);
    public Response createCustomer(Customer customer);
    public Response updateCustomer(Customer customer);
    public Response deleteCustomer(int id);
    public Response getAllCustomers();
}
