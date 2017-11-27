package com.models;

import org.json.simple.JSONArray;

import java.sql.*;

public class Util {
    public static Util util = null;

    private static String dburl = "jdbc:postgresql://localhost:5432/waid";
    private static String user = "postgres";
    private static String password = "postgres";

    private Util(){

    }

    public static Util getInstance(){
        if(util == null){
            util = new Util();
        }
        return util;
    }

    //Create Record
    public void createRecordSQL(String sql, JSONArray params){
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection(dburl, user, password);
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
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection(dburl, user, password);
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
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection(dburl, user, password);
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
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection(dburl, user, password);
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
