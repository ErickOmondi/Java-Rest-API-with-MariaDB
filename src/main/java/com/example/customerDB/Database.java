package com.example.customerDB;

import org.mariadb.jdbc.MySQLDataSource;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {


    private static Connection conn = null;
    private static Statement stmt = null;
    private static ResultSet rs = null;


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


    public static void driverManagerFinalCon(String dbQuery){

        //        --------DATA DriverManager CONNECTION---------

        Properties props = getConnectionData();

        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String passwd = props.getProperty("db.passwd");


        try (Connection conn = DriverManager.getConnection(url, user, passwd);
             PreparedStatement pst = conn.prepareStatement(dbQuery);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()){

                System.out.println("ID: " + rs.getInt(1));
                System.out.println("First Name: " + rs.getString(2));
                System.out.println("Last Name: " + rs.getString(3));
                System.out.println("Address: " + rs.getString(4));
                System.out.println("City: " + rs.getString(5));
                System.out.println("State: " + rs.getString(6));
                System.out.println("Zip Code: " + rs.getInt(7));
                System.out.println("Active Status: " + rs.getString(8));


            }


        } catch (SQLException throwables) {
            Logger lgr = Logger.getLogger(Database.class.getName());
            lgr.log(Level.SEVERE, throwables.getMessage(), throwables);
            throwables.printStackTrace();
        }
    }


    //        --------DATA Datasource CONNECTION---------

    public static DataSource getMysqlDataSource() throws SQLException {
        Properties props = getConnectionData();
        MySQLDataSource dataSource = new MySQLDataSource();

        // Set dataSource Properties
        dataSource.setServerName(props.getProperty("db.url2"));
        dataSource.setPortNumber(Integer.parseInt(props.getProperty("db.port")));
        dataSource.setDatabaseName(props.getProperty("db.name"));
        dataSource.setUser(props.getProperty("db.user"));
        dataSource.setPassword(props.getProperty("db.passwd"));
        return dataSource;
    }


    public static void dataSourceFinalCon(String dbQuery){
        try {

            // Get connection from DataSource
            conn = getMysqlDataSource().getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(dbQuery);

            if (rs.next()) {
                System.out.println("ID: " + rs.getInt(1));
                System.out.println("First Name: " + rs.getString(2));
                System.out.println("Last Name: " + rs.getString(3));
                System.out.println("Address: " + rs.getString(4));
                System.out.println("City: " + rs.getString(5));
                System.out.println("State: " + rs.getString(6));
                System.out.println("Zip Code: " + rs.getInt(7));
                System.out.println("Active Status: " + rs.getString(8));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {

//        String query = "SELECT * FROM customers";
        String query = "SELECT customer_id, first_name, last_name, address, "
                + "city, state, zip_code, is_active FROM Customers "
                + "WHERE customer_id = 1";

        driverManagerFinalCon(query);
//        dataSourceFinalCon(query);


    }
}


