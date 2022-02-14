package controllers;

import java.io.IOException;
import java.sql.SQLException;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import models.Shelf;
import models.ShelfTag;
import models.Tag;
import models.User;

public class CreateShelf {
    public TextField title_field, tag_field;
    public ChoiceBox<String> privacy_choice_box;
    public TextArea details_area;

    public Button cancel_btn, reset_btn, create_btn;

    User current_user;

    public void setData(User current_user){
        this.current_user = current_user;
    }

    public void start(Pane contentAreaPane) throws IOException {
        FXMLLoader fl = new FXMLLoader();

        Pane root = fl.load(getClass().getResource("/views/create_shelf.fxml").openStream());

        CreateShelf controller = fl.getController();
        controller.setData(current_user);
        controller.privacy_choice_box.getItems().addAll("Private", "Custom", "Public");
        
        contentAreaPane.getChildren().removeAll();
        contentAreaPane.getChildren().setAll(root);
    }
    
    
    public void createShelf(Event event) throws IOException, SQLException{
        Shelf shelf = new Shelf(current_user.get_id(), title_field.getText(), details_area.getText(), privacy_choice_box.getValue(), true);
        
        try {
            shelf.insert();
        } catch (SQLException e) {
            if(e.getMessage().contains("Violation of UNIQUE KEY constraint")){
                System.out.println("[!] Shelf already exists"); // TODO: show a proper message in the gui
                return;
            }
            else{
                throw e;
            }
        }
        
        Tag tag = null;
        tag = Tag.get_by_tag(tag_field.getText());
        ShelfTag shelfTag = null;
        if(tag != null){
            shelfTag = new ShelfTag(shelf.get_id(), tag.get_id());
        }else{
            tag = new Tag(current_user.get_id(), tag_field.getText());
            tag.insert();
            shelfTag = new ShelfTag(shelf.get_id(), tag.get_id());
        }
        shelfTag.insert();
    }
}
