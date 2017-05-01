package com.models;

import org.json.simple.JSONArray;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Util {
    public static Util util = null;
    private static String dburl = "jdbc:mysql://kunet.kingston.ac.uk/db_k1559378";
    private static String user = "k1559378";
    private static String password = "Pass1234Ben";
    private static String exceptionMessage = null;

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
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(dburl, user, password);
            PreparedStatement st = con.prepareStatement(sql);

            for (int i = 0; i < params.size(); i++){
                st.setObject(i+1, params.get(i));
            }

            st.executeUpdate();
        }catch (Exception ex){
        }
        return;
    }

    //Delete Record
    public void deleteRecordSQL(String sql, String whereValue){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(dburl, user, password);
            PreparedStatement st = con.prepareStatement(sql);
            st.setObject(1, whereValue);
            st.executeUpdate();
        }catch (Exception ex){
            exceptionMessage = ex.getMessage();
        }
    }

    //Get Id
    public int getIdSQL(String sql, String whereValue){
        int id = 0;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(dburl, user, password);
            PreparedStatement st = con.prepareStatement(sql);
            st.setObject(1, whereValue);
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                id = rs.getInt("id");
            }
        }catch (Exception ex){
            exceptionMessage = ex.getMessage();
        }
        return id;
    }


    //Check if value already exists in field
    public Boolean fieldExistsSQL(String sql, String field){
        Boolean isUnique = true;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(dburl, user, password);
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, field);
            ResultSet rs = st.executeQuery();
            isUnique = (rs.next()) ? true : false;
        }catch (Exception ex){
        }
        return isUnique;
    }
}
