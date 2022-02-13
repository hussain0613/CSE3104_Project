package models;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import utils.DBConnector;

public class ContentTag {
    DBConnector connector;

    private int id;
    public int creator_id, modifier_id;
    private String creation_datetime, modification_datetime;

    public int content_id, tag_id;


    public ContentTag(){
        id = -1;
        creator_id = -1;
        modifier_id = -1;
        
        content_id = -1;
        tag_id = -1;
        
        creation_datetime = null;
        modification_datetime = null;
    }

    public ContentTag(int id) throws SQLException, IOException{
        this.id = id;

        sync(true);
    }

    public ContentTag(int creator_id, int modifier_id, int content_id, int tag_id){
        this.creator_id = creator_id;
        this.modifier_id = modifier_id;
        this.content_id = content_id;
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
        connector.createStatement().executeUpdate("INSERT INTO \"content-tag\" (creator_id, content_id, tag_id,) VALUES (" + creator_id + ", " + ", " + content_id + ", " + tag_id + ");");
        connector.close();
    }

    public void update() throws SQLException, IOException{
        sync(false);
    }

    public void sync(boolean update_object) throws SQLException, IOException{
        if(update_object){
            String sql = "select * from \"content-tag\" where id=" + id;
            DBConnector connector = new DBConnector();
            
            ResultSet resultSet = connector.createStatement().executeQuery(sql);
            System.out.println(resultSet);
            resultSet.next();
            from_resultSet_To_ContentTag(resultSet);
            
            resultSet.close();
            connector.close();
        }else{
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            modification_datetime = dateFormat.format(date);
            String sql = "update \"content-tag\" set creator_id=" + creator_id + ", modifier_id=" + modifier_id + ", content_id=" + content_id + ", tag_id=" + tag_id + ", modification_datetime='"+ modification_datetime + "' where id=" + id;
            DBConnector connector = new DBConnector();
            connector.createStatement().executeUpdate(sql);

            connector.close();
        }
    }

    public void delete() throws SQLException, IOException{
        String sql = "delete from \"content-tag\" where id=" + id;
        DBConnector connector = new DBConnector();
        connector.createStatement().execute(sql);
        
        connector.close();
    }

    public void from_resultSet_To_ContentTag(ResultSet resultSet) throws SQLException{
        id = resultSet.getInt("id");
        creator_id = resultSet.getInt("creator_id");
        modifier_id = resultSet.getInt("modifier_id");
        content_id = resultSet.getInt("content_id");
        tag_id = resultSet.getInt("tag_id");
        creation_datetime = resultSet.getString("creation_datetime");
        modification_datetime = resultSet.getString("modification_datetime");
    }

    public static ContentTag get_by_id(int id) throws SQLException, IOException{
        return new ContentTag(id);
    }


    public static void create_table() throws SQLException, IOException{
        String sql = "create table \"content-tag\"("
            + "content_id int,"
            + "tag_id int,"
            
            + "constraint \"pk_content-tag_content_id_tag_id\" primary key(content_id, tag_id),"
            + "constraint \"fk_content-tag_content_id\" foreign key(content_id) references content(id) on delete cascade,"
            + "constraint \"fk_content-tag_tag_id\" foreign key(tag_id) references tag(id) on delete cascade"
        + ");";
        
        System.out.println(sql);
        
        
        DBConnector connector = new DBConnector();
        connector.createStatement().execute(sql);

        connector.close();
    }

    public static void drop_table() throws SQLException, IOException{
        String sql = "drop table \"content-tag\"";
        DBConnector connector = new DBConnector();
        connector.createStatement().execute(sql);

        connector.close();
    }
}
