package com.models;


import org.json.simple.JSONArray;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

public class AdminServices {
    public static AdminServices adminServices = null;
    private static String dburl = "jdbc:mysql://kunet.kingston.ac.uk/db_k1559378";
    private static String user = "k1559378";
    private static String password = "Pass1234Ben";

    private AdminServices(){

    }

    public static AdminServices getInstance(){
        if(adminServices == null){
            adminServices = new AdminServices();
        }
        return adminServices;
    }

    //Generate hash password salt
    private String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

    //Generate hash password with password and salt
    public String getPasswordHash(String password, String salt) throws java.security.NoSuchAlgorithmException{
        String plaintext = password + salt;
        MessageDigest m = MessageDigest.getInstance("MD5");
        m.reset();
        m.update(plaintext.getBytes());
        byte[] digest = m.digest();
        BigInteger bigInt = new BigInteger(1,digest);
        String hashtext = bigInt.toString(16);

        // Now we need to zero pad it if you actually want the full 32 chars.
        while(hashtext.length() < 32 ){
            hashtext = "0"+hashtext;
        }

        return hashtext;
    }

    //Create admin
    public void createAdmin(Admin admin) throws java.security.NoSuchAlgorithmException{
        String sql = "INSERT INTO admin VALUE (?, ?, ?, ?, ?, ?)";

        String salt = getSaltString();
        String password_hash = getPasswordHash(admin.getPassword(), salt);

        JSONArray params = new JSONArray();
        params.add(0, null);
        params.add(1, admin.getEmail());
        params.add(2, admin.getFirst_name());
        params.add(3, admin.getLast_name());
        params.add(4, password_hash);
        params.add(5, salt);
        Util.getInstance().createRecordSQL(sql, params);
    }

    //Check if email is taken
    public Boolean emailExists(String email){
        String sql = "select * from admin where email = ?";
        return Util.getInstance().fieldExistsSQL(sql, email);
    }
}
