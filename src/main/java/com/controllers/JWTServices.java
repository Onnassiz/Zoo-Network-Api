package com.controllers;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;

public class JWTServices {
    private static JWTServices jwtServices = null;
    private static String token = null;
    private static Boolean isValid = false;
    private static String email_Cliam = null;

    private JWTServices(){

    }

    public static JWTServices getInstance(){
        if(jwtServices == null){
            jwtServices = new JWTServices();
        }
        return jwtServices;
    }

    public String generateJsonWT(String email, String first_name, String last_name){
        long ONE_MINUTE_IN_MILLIS=60000;//millisecs
        Calendar date = Calendar.getInstance();
        long t= date.getTimeInMillis();
        Date afterAddingOneDay = new Date(t + (1440 * ONE_MINUTE_IN_MILLIS));
        try {
            Algorithm algorithm = Algorithm.HMAC256("waid2");
            token = JWT.create()
                    .withIssuer("auth0")
                    .withClaim("email", email)
                    .withClaim("first_name", first_name)
                    .withClaim("last_name", last_name)
                    .withExpiresAt(afterAddingOneDay)
                    .sign(algorithm);
        } catch (UnsupportedEncodingException exception){
            token = exception.getMessage();
        } catch (JWTCreationException exception){
            token = exception.getMessage();
        }
        return token;
    }

    public Boolean verifyJsonWT(String thisToken){
        try {
            Algorithm algorithm = Algorithm.HMAC256("waid2");
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build();
            verifier.verify(thisToken);
            isValid = true;
        } catch (UnsupportedEncodingException exception){
            isValid = false;
        } catch (JWTVerificationException exception){
            isValid = false;
        }
        return isValid;
    }

    public String getEmailClaim(String thisToken){
        try {
            Algorithm algorithm = Algorithm.HMAC256("waid2");
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build();
            DecodedJWT jwt = verifier.verify(thisToken);
            email_Cliam = jwt.getClaim("email").asString();
        } catch (UnsupportedEncodingException exception){

        } catch (JWTVerificationException exception){

        }
        return email_Cliam;
    }
}
