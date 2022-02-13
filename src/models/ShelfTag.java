package models;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import utils.DBConnector;

public class ShelfTag {
    DBConnector connector;

    private int id;
    public int creator_id, modifier_id;
    private String creation_datetime, modification_datetime;

    public int shelf_id, tag_id;


    public ShelfTag(){
        id = -1;
        creator_id = -1;
        modifier_id = -1;
        
        shelf_id = -1;
        tag_id = -1;
        
        creation_datetime = null;
        modification_datetime = null;
    }

    public ShelfTag(int id) throws SQLException, IOException{
        this.id = id;

        sync(true);
    }

    public ShelfTag(int creator_id, int modifier_id, int shelf_id, int tag_id){
        this.creator_id = creator_id;
        this.modifier_id = modifier_id;
        this.shelf_id = shelf_id;
        this.tag_id = tag_id;
    }

    public int get_id(){
        return this.id;
    }

    public String get_creation_datetime(){
        return this.creation_datetime;
    }

    public String get_modification_datetime(){
        return this.modification_datetime;
    }

    public void insert() throws SQLException, IOException{
        connector = new DBConnector();
        connector.createStatement().executeUpdate("INSERT INTO \"shelf-tag\" (creator_id, shelf_id, tag_id,) VALUES (" + creator_id + ", " + ", " + shelf_id + ", " + tag_id + ");");
        connector.close();
    }

    public void update() throws SQLException, IOException{
        sync(false);
    }

    public void sync(boolean update_object) throws SQLException, IOException{
        if(update_object){
            String sql = "select * from \"shelf-tag\" where id=" + id;
            DBConnector connector = new DBConnector();
            
            ResultSet resultSet = connector.createStatement().executeQuery(sql);
            System.out.println(resultSet);
            resultSet.next();
            from_resultSet_To_ShelfTag(resultSet);
            
            resultSet.close();
            connector.close();
        }else{
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            modification_datetime = dateFormat.format(date);
            String sql = "update \"shelf-tag\" set creator_id=" + creator_id + ", modifier_id=" + modifier_id + ", shelf_id=" + shelf_id + ", tag_id=" + tag_id + ", modification_datetime='"+ modification_datetime + "' where id=" + id;
            DBConnector connector = new DBConnector();
            connector.createStatement().executeUpdate(sql);

            connector.close();
        }
    }

    public void delete() throws SQLException, IOException{
        String sql = "delete from \"shelf-tag\" where id=" + id;
        DBConnector connector = new DBConnector();
        connector.createStatement().execute(sql);
        
        connector.close();
    }

    public void from_resultSet_To_ShelfTag(ResultSet resultSet) throws SQLException{
        id = resultSet.getInt("id");
        creator_id = resultSet.getInt("creator_id");
        modifier_id = resultSet.getInt("modifier_id");
        shelf_id = resultSet.getInt("shelf_id");
        tag_id = resultSet.getInt("tag_id");
        creation_datetime = resultSet.getString("creation_datetime");
        modification_datetime = resultSet.getString("modification_datetime");
    }

    public static ShelfTag get_by_id(int id) throws SQLException, IOException{
        return new ShelfTag(id);
    }


    public static void create_table() throws SQLException, IOException{
        String sql = "create table \"shelf-tag\"("
            + "shelf_id int,"
            + "tag_id int,"
            
            + "constraint \"pk_shelf-tag_shelf_id_tag_id\" primary key(shelf_id, tag_id),"
            + "constraint \"fk_shelf-tag_shelf_id\" foreign key(shelf_id) references shelf(id) on delete cascade,"
            + "constraint \"fk_shelf-tag_tag_id\" foreign key(tag_id) references tag(id) on delete cascade"
        + ");";
        
        
        
        DBConnector connector = new DBConnector();
        connector.createStatement().execute(sql);

        connector.close();
    }

    public static void drop_table() throws SQLException, IOException{
        String sql = "drop table \"shelf-tag\"";
        DBConnector connector = new DBConnector();
        connector.createStatement().execute(sql);

        connector.close();
    }
}
