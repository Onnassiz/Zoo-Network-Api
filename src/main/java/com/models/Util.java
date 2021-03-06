package com.models;

import org.json.simple.JSONArray;

import java.sql.*;

public class Util {
    public static Util util = null;

    private static String Driver = "org.postgresql.Driver";
    private static String dburl = "jdbc:postgresql://localhost:5432/waid";
    private static String user = "postgres";
    private static String password = "postgres";

    private static String staging_dburl = "jdbc:postgresql://ec2-54-243-185-123.compute-1.amazonaws.com:5432/d32mr83d7okce3?sslmode=require";
    private static String staging_user = "poedbnjtgrsvip";
    private static String staging_password = "2ea846cbc7e275f64f91e6f5c737bd71a75993a0f1975f1f811620720d417e52";

    private Util(){

    }

    public static Util getInstance(){
        if(util == null){
            util = new Util();
        }
        return util;
    }

    public Connection getConnection() throws SQLException {
        try {
            Class.forName(Driver);
        } catch (ClassNotFoundException ex) {
            System.out.println("Where is your PostgreSQL JDBC Driver? "
                    + "Include in your library path!");
            return null;
        }
        Connection conn = DriverManager.getConnection(staging_dburl, staging_user, staging_password);
        return conn;
    }


    //Create Record
    public void createRecordSQL(String sql, JSONArray params){
        try {
            Connection con = getConnection();
            PreparedStatement st = con.prepareStatement(sql);

            for (int i = 0; i < params.size(); i++){
                st.setObject(i+1, params.get(i));
            }

            st.executeUpdate();
            st.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return;
    }

    //Delete Record
    public void deleteRecordSQL(String sql, String whereValue){
        try {
            Connection con = getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setObject(1, whereValue);
            st.executeUpdate();
            st.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    //Get Id
    public int getIdSQL(String sql, String whereValue){
        int id = 0;
        try {
            Connection con = getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setObject(1, whereValue);
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                id = rs.getInt("id");
            }
            st.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return id;
    }


    //Check if value already exists in field
    public Boolean fieldExistsSQL(String sql, String field){
        Boolean isUnique = true;
        try {
            Connection con = getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, field);
            ResultSet rs = st.executeQuery();
            isUnique = (rs.next()) ? true : false;
            st.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return isUnique;
    }
}
