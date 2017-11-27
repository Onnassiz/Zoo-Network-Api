package com.models;

import com.controllers.JWTServices;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginServices {
    public static LoginServices loginServices = null;
    private static String dburl = "jdbc:postgresql://localhost:5432/waid";
    private static String user = "postgres";
    private static String password = "postgres";

    private LoginServices(){

    }

    public static LoginServices getInstance(){
        if(loginServices == null){
            loginServices = new LoginServices();
        }
        return loginServices;
    }

    private String getEmailSaltSQL(String sql, String email){
        String hashCode = "";
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection(dburl, user, password);
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, email);
            ResultSet rs = st.executeQuery();
            con.close();
            while (rs.next()){
                hashCode = rs.getString("salt");
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return hashCode;
    }


    private String getEmailSalt(String email){
        String sql = "select * from admin where email = ?";
        return getEmailSaltSQL(sql, email);
    }

    private Boolean findUserSql(String sql, String email, String passwordHash){
        Boolean found = false;
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection(dburl, user, password);
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, email);
            st.setString(2, passwordHash);
            ResultSet rs = st.executeQuery();
            found = (rs.next()) ? true : false;
            con.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return found;
    }

    private Boolean findUser(String email, String password){
        String sql = "select * from admin where email = ? and password = ?";
        return findUserSql(sql, email, password);
    }


    public Boolean isValidCredential(Admin admin) throws java.security.NoSuchAlgorithmException{
        if(!AdminServices.getInstance().emailExists(admin.getEmail())){
            return false;
        }
        String passwordHash = AdminServices.getInstance().getPasswordHash(admin.getPassword(), getEmailSalt(admin.getEmail()));
        return findUser(admin.getEmail(), passwordHash);
    }

    private Admin getUserDetailsSQL(String sql, String email){
        Admin admin = new Admin();
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection(dburl, user, password);
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, email);
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                admin.setFirst_name(rs.getString("first_name"));
                admin.setLast_name(rs.getString("last_name"));
                admin.setEmail(rs.getString("email"));
            }
            con.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return admin;
    }

    private Admin getUserDetails(String email){
        String sql = "select * from admin where email = ?";
        return getUserDetailsSQL(sql, email);
    }

    public String getToken(String email){
        Admin admin = getUserDetails(email);
        String token = JWTServices.getInstance().generateJsonWT(admin.getEmail(), admin.getFirst_name(), admin.getLast_name());
        return token;
    }
}
