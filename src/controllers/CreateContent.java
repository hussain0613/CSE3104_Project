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
import models.Content;
import models.ContentTag;
import models.Tag;
import models.User;

public class CreateContent {
    public TextField title_field, url_field, alternative_url_field, tag_field;
    public ChoiceBox<String> type_choice_box, privacy_choice_box;
    public TextArea details_area;

    public Button cancel_btn, reset_btn, create_btn;

    User current_user;

    public void setData(User current_user){
        this.current_user = current_user;
    }

    public void start(Pane contentAreaPane) throws IOException {
        FXMLLoader fl = new FXMLLoader();

        Pane root = fl.load(getClass().getResource("/views/create_content.fxml").openStream());

        CreateContent controller = fl.getController();
        controller.setData(current_user);
        controller.type_choice_box.getItems().addAll("Website", "Text", "E-Book", "Image", "Audio", "Video", "Other");
        controller.privacy_choice_box.getItems().addAll("Private", "Custom", "Public");
        
        contentAreaPane.getChildren().removeAll();
        contentAreaPane.getChildren().setAll(root);
    }
    
    
    public void createContent(Event event) throws IOException, SQLException{
        Content content = new Content(current_user.get_id(), title_field.getText(), url_field.getText(), alternative_url_field.getText(), type_choice_box.getValue(), privacy_choice_box.getValue(), details_area.getText(), true);
        
        try {
            content.insert();
        } catch (SQLException e) {
            if(e.getMessage().contains("Violation of UNIQUE KEY constraint")){
                System.out.println("[!] Content already exists"); // TODO: show a proper message in the gui
                return;
            }
            else{
                throw e;
            }
        }
        
        Tag tag = null;
        tag = Tag.get_by_tag(tag_field.getText());
        ContentTag contentTag = null;
        if(tag != null){
            contentTag = new ContentTag(content.get_id(), tag.get_id());
        }else{
            tag = new Tag(current_user.get_id(), tag_field.getText());
            tag.insert();
            contentTag = new ContentTag(content.get_id(), tag.get_id());
        }
        contentTag.insert();
    }
}
