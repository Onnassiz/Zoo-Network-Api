package com.models;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class AnimalServices {
    public static AnimalServices animalServices = null;
    private static String dburl = "jdbc:postgresql://localhost:5432/waid";
    private static String user = "postgres";
    private static String password = "postgres";

    private AnimalServices(){

    }

    //Return object of this class
    public static AnimalServices getInstance(){
        if(animalServices == null){
            animalServices = new AnimalServices();
        }
        return animalServices;
    }

    //Checks if AnimalExist
    public Boolean AnimalExists(String common_name){
        String sql = "select * from animals where common_name = ?";
        return Util.getInstance().fieldExistsSQL(sql, common_name);
    }

    //Create New Animal
    public void addAnimal(Animal animal){
        String sql = "INSERT INTO animals(common_name, animal_class, family, animal_order, species, genus, description) VALUES (?, ?, ?, ?, ?, ?, ?)";
        JSONArray params = new JSONArray();

        params.add(0, animal.getCommon_name());
        params.add(1, animal.getAnimal_class());
        params.add(2, animal.getFamily());
        params.add(3, animal.getOrder());
        params.add(4, animal.getSpecies());
        params.add(5, animal.getGenus());
        params.add(6, animal.getDescription());
        Util.getInstance().createRecordSQL(sql, params);
    }

    //Map ResultSet to Animal class
    private Animal mapRSToAnimals(ResultSet rs)
    {
        try
        {
            Animal animal = new Animal();
            animal.setCommon_name(rs.getString("common_name"));
            animal.setAnimal_class(rs.getString("animal_class"));
            animal.setDescription(rs.getString("description"));
            animal.setOrder(rs.getString("animal_order"));
            animal.setGenus(rs.getString("genus"));
            animal.setSpecies(rs.getString("species"));
            animal.setFamily(rs.getString("family"));
            return animal;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    //Execute Fetch All animal Sql
    private ArrayList<Animal> fetchAllAnimalsSQL(String sql){
        ArrayList<Animal> animals = new ArrayList();
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection(dburl, user, password);
            PreparedStatement st = con.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                animals.add(mapRSToAnimals(rs));
            }
            con.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return animals;
    }

    //Fetch All Animals
    public ArrayList<Animal> fetchAllAnimals(){
        String sql = "select * from animals";
        return fetchAllAnimalsSQL(sql);
    }

    //Delete an animal Record
    public void deleteAnimal(String common_name){
        String sql = "delete from animals where common_name = ?";
        Util.getInstance().deleteRecordSQL(sql, common_name);
    }

    //Get animal Id
    private int getAnimalId(String common_name){
        String sql = "select * from animals where common_name = ?";
        return Util.getInstance().getIdSQL(sql, common_name);
    }

    //Save animal Image link
    public void saveAnimalImageLink(JSONObject jsonParams){
        String sql = "insert into animal_images(animal_id, link) values (?, ?)";
        JSONArray params = new JSONArray();
        params.add(0, getAnimalId(jsonParams.get("common_name").toString()));
        params.add(1, jsonParams.get("image_url").toString());
        Util.getInstance().createRecordSQL(sql, params);
    }

    //Map result set to Animal Image Link Class
    private AnimalImageLink mapRsToAnimalLink(ResultSet rs){
        try
        {
            AnimalImageLink animalImageLink = new AnimalImageLink();
            animalImageLink.setCommon_name(rs.getString("common_name"));
            animalImageLink.setLink(rs.getString("link"));
            return animalImageLink;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    //Map result set to AnimalZoo class
    private AnimalZoo mapRsToAnimalZoos(ResultSet rs){
        try
        {
            AnimalZoo animalZoo = new AnimalZoo();
            animalZoo.setCommon_name(rs.getString("common_name"));
            animalZoo.setZoo_name(rs.getString("zoo_name"));
            animalZoo.setAnimal_count(rs.getInt("animal_count"));
            return animalZoo;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    //Execute Fetch Link SQL
    private ArrayList<AnimalImageLink> fetchAnimalImageLinksSQL(String sql){
        ArrayList<AnimalImageLink> animalImageLinks = new ArrayList();
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection(dburl, user, password);
            PreparedStatement st = con.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                animalImageLinks.add(mapRsToAnimalLink(rs));
            }
            con.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return animalImageLinks;
    }

    //Retrieve the image links of a particular animal
    public ArrayList<AnimalImageLink> fetchAnimalImageLinks(){
        String sql = "select animal_images.link, animals.common_name from animals inner join animal_images on animal_images.animal_id = animals.id";
        return fetchAnimalImageLinksSQL(sql);
    }

    private ArrayList<AnimalZoo> fetchAnimalZoosSQL(String sql, String common_name){
        ArrayList<AnimalZoo> animalZoos = new ArrayList();
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection(dburl, user, password);
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, common_name);
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                animalZoos.add(mapRsToAnimalZoos(rs));
            }
            con.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return animalZoos;
    }

    //Get the zoos of a particular animal
    public ArrayList<AnimalZoo> fetchAnimalZoos(String common_name){
        String sql = "SELECT animals.common_name, zoos.zoo_name, animals_in_zoos.animal_count FROM zoos INNER JOIN animals_in_zoos ON zoos.id = animals_in_zoos.zoo_id INNER JOIN animals ON animals_in_zoos.animal_id = animals.id WHERE animals.common_name = ?";
        return fetchAnimalZoosSQL(sql, common_name);
    }

    //Clear the Image link of a particular animal
    public void deleteAnimalImages(String common_name){
        int id = getAnimalId(common_name);
        String sql = "delete from animal_images where animal_id = ?";
        ZooServices.getInstance().deleteZooImagesSQL(sql, id);
    }

    //Execute checkTag SQL
    private Boolean checkTagExistsSQL(String sql, int animal_id, int zoo_id){
        Boolean exists = false;
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection(dburl, user, password);
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, animal_id);
            st.setInt(2, zoo_id);
            ResultSet rs = st.executeQuery();
            exists = (rs.next()) ? true : false;
            con.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return exists;
    }

    //Check if a selected animal has been tagged to a selected zoo
    private Boolean checkTagExists(int animal_id, int zoo_id){
        String sql = "select * from animals_in_zoos where animal_id = ? and zoo_id = ?";
        return checkTagExistsSQL(sql, animal_id, zoo_id);
    }

    //Tag an animal to a zoo
    public void addAnimalZooTag(JSONObject jsonParams){
        int zoo_id = ZooServices.getInstance().getZooId(jsonParams.get("zoo_name").toString());
        int animal_id = getAnimalId(jsonParams.get("common_name").toString());
        int animal_count = Integer.parseInt(jsonParams.get("animal_count").toString());

        String sql = "insert into animals_in_zoos (animal_id, zoo_id, animal_count) values (?, ?, ?)";
        String sql_update = "update animals_in_zoos set animal_count = ? where animal_id = ? and zoo_id = ?";
        JSONArray params = new JSONArray();
        JSONArray params_update = new JSONArray();
        params.add(0, animal_id);
        params.add(1, zoo_id);
        params.add(2, animal_count);

        params_update.add(0, animal_count);
        params_update.add(1, animal_id);
        params_update.add(2, zoo_id);

        if(!checkTagExists(animal_id, zoo_id)){
            Util.getInstance().createRecordSQL(sql, params);
        }else {
            Util.getInstance().createRecordSQL(sql_update, params_update);
        }
    }

    //Delete animal zoo Tag
    public void deleteZooTag(String common_name, String zoo_name){
        int animal_id = getAnimalId(common_name);
        int zoo_id = ZooServices.getInstance().getZooId(zoo_name);
        JSONArray params = new JSONArray();
        params.add(0, animal_id);
        params.add(1, zoo_id);

        String sql = "delete from animals_in_zoos where animal_id = ? and zoo_id = ?";
        Util.getInstance().createRecordSQL(sql, params);
    }

    //Prepare all search keys and add all keys to a jsonArray
    public JSONArray getSearchKeys(){
        ArrayList<Animal> animals = fetchAllAnimals();
        JSONArray keys = new JSONArray();
        for (Animal animal: animals){
            if(!keys.contains(animal.getCommon_name())){
                keys.add(animal.getCommon_name());
            }
            if(!keys.contains(animal.getAnimal_class())){
                keys.add(animal.getAnimal_class());
            }
            if(!keys.contains(animal.getFamily())){
                keys.add(animal.getFamily());
            }
            if(!keys.contains(animal.getGenus())){
                keys.add(animal.getGenus());
            }
            if(!keys.contains(animal.getOrder())){
                keys.add(animal.getOrder());
            }
            if(!keys.contains(animal.getSpecies())){
                keys.add(animal.getSpecies());
            }
        }
        return keys;
    }

    //Execute search Animal by key
    public ArrayList<Animal> searchAnimalByKeySQL(String sql, String key){
        ArrayList<Animal> animals = new ArrayList();
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection(dburl, user, password);
            PreparedStatement st = con.prepareStatement(sql);
            for (int i = 0; i < 6; i++){
                st.setString(i + 1, key);
            }
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                animals.add(mapRSToAnimals(rs));
            }
            con.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return animals;
    }

    //Search animal using a specified search key
    public ArrayList<Animal> searchAnimalByKey(String key){
        key = "%"+key.trim()+"%";
        String sql = "select * from animals where ( (common_name like ?) or (family like ?) or (animal_order like ?) or (animal_class like ?) or (species like ?) or (genus like ?) )";
        return searchAnimalByKeySQL(sql, key);
    }
}
