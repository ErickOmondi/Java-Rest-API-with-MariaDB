package com.example.Dao.Dbqueries;

import com.example.Dao.CustomerDao;
import com.example.customerDB.Database;
import com.example.models.Customer;
import com.example.models.StatusMessage;

import javax.inject.Inject;
import javax.sql.DataSource;
import javax.ws.rs.core.Response;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class CustomerDAOImpl implements CustomerDao {

    @Inject
    Logger logger = Logger.getLogger(String.valueOf(CustomerDAOImpl.class));
    public static Properties getConnectionData() {

        Properties props = new Properties();

        String fileName = "D:\\Docs and Files\\RestApiProject\\src\\main\\java\\restDB.properties";



        try (FileInputStream in = new FileInputStream(fileName)) {
            props.load(in);
        } catch (IOException ex) {
            Logger lgr = Logger.getLogger(Database.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }

        return props;
    }
    Properties props = getConnectionData();
    private String url = props.getProperty("db.url");
    private String user = props.getProperty("db.user");
    private String passwd = props.getProperty("db.passwd");
    private  Connection conn;


    @Override
    public Response getCustomer(int id) {

        PreparedStatement ps = null;
        ResultSet rs = null;

        Customer customer = null;
        String sql = "SELECT customer_id, first_name, last_name, address, "
                    + "city, state, zip_code, is_active FROM Customers "
                + "WHERE customer_id = ?";




        try {

            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                customer = new Customer();
                customer.setCustomerId(rs.getInt("customer_id"));
                customer.setFirstName(rs.getString("first_name"));
                customer.setLastName(rs.getString("last_name"));
                customer.setAddress(rs.getString("address"));
                customer.setCity(rs.getString("city"));
                customer.setState(rs.getString("state"));
                customer.setZipCode(rs.getInt("zip_code"));
                customer.setIsActive(rs.getString("is_active"));
                System.out.println(customer);
            } else {
                logger.info(
                        String.format("Customer with ID of %d is not found.", id));
                StatusMessage statusMessage = new StatusMessage();
                statusMessage.setStatus(Response.Status.NOT_FOUND.getStatusCode());
                statusMessage.setMessage(
                        String.format("Customer with ID of %d is not found.", id));
                return Response.status(404).entity(statusMessage).build();
            }
        } catch (SQLException e) {
            logger.info("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.info("Error closing resultset: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    logger.info("Error closing PreparedStatement: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.info("Error closing connection: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return Response.status(200).entity(customer).build();
    }

    @Override
    public Response createCustomer(Customer customer) {
        Connection conn = null;
        PreparedStatement ps = null;
        Statement stmt = null;
        ResultSet rs = null;
        StatusMessage statusMessage = null;
        int autoID = -1;

        String sql = "INSERT INTO customers (first_name, last_name, "
                + "address, city, state, zip_code, is_active) "
                + "VALUES (?,?,?,?,?,?,?)";

        try {
            conn = DriverManager.getConnection(url, user, passwd);
            ps = conn.prepareStatement(sql);
            ps.setString(1, customer.getFirstName());
            ps.setString(2, customer.getLastName());
            ps.setString(3, customer.getAddress());
            ps.setString(4, customer.getCity());
            ps.setString(5, customer.getState());
            ps.setInt(6, customer.getZipCode());
            ps.setString(7, customer.getIsActive());

            int rows = ps.executeUpdate();

            if (rows == 0) {
                logger.info("Unable to create customer...");
                statusMessage = new StatusMessage();
                statusMessage.setStatus(Response.Status.NOT_FOUND.getStatusCode());
                statusMessage.setMessage("Unable to create customer...");
                return Response.status(404).entity(statusMessage).build();
            }

            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");

            if (rs.next()) {
                autoID = rs.getInt(1);
                customer.setCustomerId(autoID);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.info("Error closing resultset: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    logger.info("Error closing PreparedStatement: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.info("Error closing connection: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return Response.status(200).entity(customer).build();
    }

    @Override
    public Response updateCustomer(Customer customer) {
        Connection conn = null;
        PreparedStatement ps = null;

        String sql = "UPDATE customers SET first_name=?, last_name=?, "
                + "address=?, city=?, state=?, zip_code=?, is_active=? "
                + "WHERE customer_id = ?";

        try {
            conn = DriverManager.getConnection(url, user, passwd);
            ps = conn.prepareStatement(sql);

            ps.setString(1, customer.getFirstName());
            ps.setString(2, customer.getLastName());
            ps.setString(3, customer.getAddress());
            ps.setString(4, customer.getCity());
            ps.setString(5, customer.getState());
            ps.setInt(6, customer.getZipCode());
            ps.setString(7, customer.getIsActive());
            ps.setInt(8, customer.getCustomerId());

            int rows = ps.executeUpdate();

            if (rows == 0) {
                logger.info("Unable to update customer...");
                StatusMessage statusMessage = new StatusMessage();
                statusMessage.setStatus(Response.Status.NOT_FOUND.getStatusCode());
                statusMessage.setMessage("Unable to update customer...");
                return Response.status(404).entity(statusMessage).build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    logger.info("Error closing PreparedStatement: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.info("Error closing connection: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return Response.status(200).entity(customer).build();
    }

    @Override
    public Response deleteCustomer(int id) {
        Connection conn = null;
        PreparedStatement ps = null;
        StatusMessage statusMessage = null;

        String sql = "DELETE FROM customers WHERE customer_id = ?";

        try {
            conn = DriverManager.getConnection(url, user, passwd);
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            int rows = ps.executeUpdate();

            if (rows == 0) {
                logger.info(
                        String.format("Unable to DELETE customer with ID of %d...", id));
                statusMessage = new StatusMessage();
                statusMessage.setStatus(Response.Status.NOT_FOUND.getStatusCode());
                statusMessage.setMessage(
                        String.format("Unable to DELETE customer with ID of %d...", id));
                return Response.status(404).entity(statusMessage).build();
            }
        } catch (SQLException e) {
            logger.info("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    logger.info(
                            "Error closing PreparedStatement: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.info("Error closing connection: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        statusMessage = new StatusMessage();
        statusMessage.setStatus(Response.Status.OK.getStatusCode());
        statusMessage.setMessage(
                String.format("Successfully deleted customer with ID of %d...", id));
        return Response.status(200).entity(statusMessage).build();
    }



    @Override
    public Response getAllCustomers() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<Customer> allCustomers = new ArrayList<>();
        String sql = "select customer_id, first_name, last_name, address, "
                + "city, state, zip_code, is_active from Customers";

        try {
            conn = DriverManager.getConnection(url, user, passwd);
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Customer cust = new Customer();
                cust.setCustomerId(rs.getInt("customer_id"));
                cust.setFirstName(rs.getString("first_name"));
                cust.setLastName(rs.getString("last_name"));
                cust.setAddress(rs.getString("address"));
                cust.setCity(rs.getString("city"));
                cust.setState(rs.getString("state"));
                cust.setZipCode(rs.getInt("zip_code"));
                cust.setIsActive(rs.getString("is_active"));
                allCustomers.add(cust);
                System.out.println(cust.getFirstName());
            }

            if (allCustomers.isEmpty()) {
                logger.info("No Customers Exists...");
                StatusMessage statusMessage = new StatusMessage();
                statusMessage.setStatus(Response.Status.NOT_FOUND.getStatusCode());
                statusMessage.setMessage("No Customers Exists...");
                return Response.status(404).entity(statusMessage).build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.info("Error closing resultset: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    logger.info(
                            "Error closing PreparedStatement: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.info("Error closing connection: " + e.getMessage());
                    e.printStackTrace();
                }

            }
        }

        return Response.status(200).entity(allCustomers).build();
    }


    public static void main(String[] args) {

        CustomerDAOImpl daoimpl = new CustomerDAOImpl();
        Response resp = daoimpl.getAllCustomers();

        System.out.println(resp);
//        if (resp != null){
//
//            System.out.println(resp + "runned succesful");
//
//        } else {System.out.println("Something Went wrong");
//
//        }

    }
}
