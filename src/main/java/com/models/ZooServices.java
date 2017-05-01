package com.models;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ZooServices {
    public static ZooServices zooServices = null;
    private static String dburl = "jdbc:mysql://kunet.kingston.ac.uk/db_k1559378";
    private static String user = "k1559378";
    private static String password = "Pass1234Ben";

    private ZooServices(){

    }

    public static ZooServices getInstance(){
        if(zooServices == null){
            zooServices = new ZooServices();
        }
        return zooServices;
    }

    //Checks of zoo already exists
    public Boolean ZooExists(String zoo_name){
        String sql = "select * from zoos where zoo_name = ?";
        return Util.getInstance().fieldExistsSQL(sql, zoo_name);
    }


    //Create New Zoo
    public void addZoo(Zoo zoo){
        String sql = "INSERT INTO zoos VALUE (?, ?, ?, ?, ?, ?, ?)";
        JSONArray params = new JSONArray();

        params.add(0, null);
        params.add(1, zoo.getZoo_name());
        params.add(2, zoo.getWebsite());
        params.add(3, zoo.getHouse_number());
        params.add(4, zoo.getStreet());
        params.add(5, zoo.getCounty());
        params.add(6, zoo.getPostcode());
        Util.getInstance().createRecordSQL(sql, params);
    }

    //Map result set to Zoo class
    private Zoo mapRSToZoo(ResultSet rs)
    {
        try
        {
            Zoo z = new Zoo();
            z.setZoo_name(rs.getString("zoo_name"));
            z.setWebsite(rs.getString("website"));
            z.setHouse_number(rs.getString("house_number"));
            z.setCounty(rs.getString("county"));
            z.setStreet(rs.getString("street"));
            z.setPostcode(rs.getString("postcode"));
            return z;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    //Execute Fetch All Zoo SQL
    private ArrayList<Zoo> fetchZoosSQL(String sql){
        ArrayList<Zoo> zoos = new ArrayList();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(dburl, user, password);
            PreparedStatement st = con.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                zoos.add(mapRSToZoo(rs));
            }
        }catch (Exception ex){
        }
        return zoos;
    }

    //Fetch All Zoos
    public ArrayList<Zoo> fetchZoos(){
        String sql = "select * from zoos";
        return fetchZoosSQL(sql);
    }

    //Delete zoo instance
    public void deleteZoo(String zoo_name){
        String sql = "delete from zoos where zoo_name = ?";
        Util.getInstance().deleteRecordSQL(sql, zoo_name);
    }

    //Retrieve zoo instance id
    public int getZooId(String zoo_name){
        String sql = "select * from zoos where zoo_name = ?";
        return Util.getInstance().getIdSQL(sql, zoo_name);
    }

    //Save Zoo image link
    public void saveZooImageLink(JSONObject jsonParams){
        String sql = "insert into zoo_images value (?, ?, ?)";
        JSONArray params = new JSONArray();
        params.add(0, null);
        params.add(1, getZooId(jsonParams.get("zoo_name").toString()));
        params.add(2, jsonParams.get("image_url").toString());

        Util.getInstance().createRecordSQL(sql, params);
    }

    //Map result set to zoo link
    private ZooImageLink mapRsToZooLink(ResultSet rs){
        try
        {
            ZooImageLink zl = new ZooImageLink();
            zl.setZoo_name(rs.getString("zoo_name"));
            zl.setLink(rs.getString("link"));
            return zl;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    //Execute fetch zoo image link
    private ArrayList<ZooImageLink> fetchZooLinksSQL(String sql){
        ArrayList<ZooImageLink> zooImageLinks = new ArrayList();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(dburl, user, password);
            PreparedStatement st = con.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                zooImageLinks.add(mapRsToZooLink(rs));
            }
        }catch (Exception ex){
        }
        return zooImageLinks;
    }

    //Fetch Zoo Image link
    public ArrayList<ZooImageLink> fetchZooImageLinks(){
        String sql = "select zoo_images.link as 'link', zoos.zoo_name as 'zoo_name' from zoos inner join zoo_images on zoo_images.zoo_id = zoos.id";
        return fetchZooLinksSQL(sql);
    }

    //Execute delete zoo images
    public void deleteZooImagesSQL(String sql, int id){
        try {
            int field = id;
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(dburl, user, password);
            PreparedStatement st = con.prepareStatement(sql);
            st.setObject(1, field);
            st.executeUpdate();
        }catch (Exception ex){
        }
    }

    //Delete zoo instance images
    public void deleteZooImages(String zoo_name){
        int id = getZooId(zoo_name);
        String sql = "delete from zoo_images where zoo_id = ?";
        deleteZooImagesSQL(sql, id);
    }

    //Map result set to animal Zoos
    private AnimalZoo mapRsToAnimalZoo(ResultSet rs){
        try
        {
            AnimalZoo az = new AnimalZoo();
            az.setZoo_name(rs.getString("zoo_name"));
            az.setCommon_name(rs.getString("common_name"));
            az.setAnimal_count(rs.getInt("animal_count"));
            return az;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    private ArrayList<AnimalZoo> fetchZooAnimalsSQL(String sql, String zoo_name){
        ArrayList<AnimalZoo> animalZoos = new ArrayList();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(dburl, user, password);
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, zoo_name);
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                animalZoos.add(mapRsToAnimalZoo(rs));
            }
        }catch (Exception ex){
        }
        return animalZoos;
    }

    //Fetch all animal in a particular zoo
    public ArrayList<AnimalZoo> fetchZooAnimals(String zoo_name){
        String sql = "SELECT animals.common_name AS 'common_name', zoos.zoo_name AS 'zoo_name', animals_in_zoos.animal_count as 'animal_count' FROM animals INNER JOIN animals_in_zoos ON animals.id = animals_in_zoos.animal_id INNER JOIN zoos ON animals_in_zoos.zoo_id = zoos.id WHERE zoos.zoo_name = ?";
        return fetchZooAnimalsSQL(sql, zoo_name);
    }
}
