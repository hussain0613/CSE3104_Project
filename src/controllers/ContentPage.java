package controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import models.Content;
import models.ContentTag;
import models.Tag;
import models.User;

public class ContentPage {
    // TODO: title stays dashboard, need to fix this

    public TextField title_field, url_field, alternative_url_field, tag_field, username_field;
    public ChoiceBox<String> type_choice_box, privacy_choice_box, permission_choice_box;
    public TextArea details_area;
    public Label msg_label, tags_label;
    public Button edit_btn, save_btn, add_tag_btn;
    public VBox user_list_vbox;
    public Pane add_tag_pane, user_permission_pane, add_permission_pane;

    User current_user;
    Content current_content;
    ArrayList <Tag> tags = new ArrayList<Tag>();

    public void setData(User current_user, Content current_content){
        this.current_user = current_user;
        this.current_content = current_content;

        try {
            tags = Tag.get_by_content_id(current_content.get_id());
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void start(Pane contentAreaPane) throws IOException {
        FXMLLoader fl = new FXMLLoader();

        Parent root = fl.load(getClass().getResource("/views/content_page.fxml").openStream());

        ContentPage controller = fl.getController();
        
        controller.setData(current_user, current_content);
        controller.from_content_obj_to_view();
        
        controller.type_choice_box.getItems().addAll("Website", "Text", "E-Book", "Image", "Audio", "Video", "Other");
        controller.type_choice_box.getSelectionModel().select(0);
        
        controller.privacy_choice_box.getItems().addAll("Private", "Custom", "Public");
        controller.privacy_choice_box.getSelectionModel().select(0);
        
        controller.permission_choice_box.getItems().addAll("View", "Edit");
        controller.permission_choice_box.getSelectionModel().select(0);

        controller.privacy_choice_box.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            if(newValue.equals("Custom")){
                controller.user_permission_pane.setVisible(true);
                controller.add_permission_pane.setVisible(true);
            }
            else{
                controller.user_permission_pane.setVisible(false);
                controller.add_permission_pane.setVisible(false);
            }
        });
        
        contentAreaPane.getChildren().removeAll();
        contentAreaPane.getChildren().setAll(root);
    }

    private void from_content_obj_to_view(){
        title_field.setText(current_content.title);
        url_field.setText(current_content.url);
        alternative_url_field.setText(current_content.alternative_url);
        details_area.setText(current_content.details);

        type_choice_box.getSelectionModel().select(current_content.type);
        privacy_choice_box.getSelectionModel().select(current_content.privacy);

        tags_label.setText("");
        for(int i = 0; i < tags.size(); i++){
            tags_label.setText(tags_label.getText() + " #" + tags.get(i).tag);
        }

        if(current_content.privacy.equals("Custom")){
            int arbritrary_user_count = 10;
            for(int i = 0; i < arbritrary_user_count; i++){
                HBox row = new HBox();
                row.setSpacing(10);
                row.getChildren().add(new Label("User" + i));
                row.getChildren().add(new Label("-"));
                row.getChildren().add(new Label("Permission" + i));
                row.getChildren().add(new Label("-"));
                Button remove_btn = new Button("Remove");
                remove_btn.setOnAction(e -> {
                    user_list_vbox.getChildren().remove(row);
                });
                row.getChildren().add(remove_btn);
                user_list_vbox.getChildren().add(row);
            }
        }
    }

    private void from_view_to_content_obj(){
        current_content.title = title_field.getText();
        current_content.url = url_field.getText();
        current_content.alternative_url = alternative_url_field.getText();
        current_content.details = details_area.getText();
        current_content.type = type_choice_box.getValue();
        current_content.privacy = privacy_choice_box.getValue();

        // tags are added with it's own button event
        // users ar also added with it's own button event
    }

    private void set_editability(boolean editable){
        title_field.setEditable(editable);
        url_field.setEditable(editable);
        alternative_url_field.setEditable(editable);
        details_area.setEditable(editable);
        tag_field.setEditable(editable);
        
        type_choice_box.setDisable(!editable);
        privacy_choice_box.setDisable(!editable);
        add_tag_btn.setDisable(!editable);
        user_list_vbox.setDisable(!editable);
        permission_choice_box.setDisable(!editable);

        add_tag_pane.setVisible(editable);
        if(user_permission_pane.isVisible()){
            add_permission_pane.setVisible(editable);
        }
        
        save_btn.setDisable(!editable);
        save_btn.setVisible(editable);
    }
    
    
    public void addTag(Event event){
        // TODO
        // it would be better to use a list view instead of a text field
        // may be something like nested hbox,
        // and needs a rmeove option

        if(tag_field.getText().isEmpty()){
            msg_label.setText("Tag field is empty");
            return;
        }
        tags_label.setText(tags_label.getText() + " #" + tag_field.getText());
        Tag tag = new Tag();
        tag.tag = tag_field.getText();
        tags.add(tag);
        tag_field.clear();
    }

    
    public void addUser(Event event) throws IOException{
        // TODO:
        // not complete, it is just a demo
        // does not actually do anything with the database
        // need to complete this

        HBox row = new HBox();
        row.setSpacing(10);
        row.getChildren().add(new Label(username_field.getText()));
        row.getChildren().add(new Label("-"));
        row.getChildren().add(new Label(permission_choice_box.getValue()));
        row.getChildren().add(new Label("-"));
        Button remove_btn = new Button("Remove");
        remove_btn.setOnAction(e -> {
            user_list_vbox.getChildren().remove(row);
        });
        row.getChildren().add(remove_btn);
        user_list_vbox.getChildren().add(row);
    }

    
    public void saveContent(Event event) throws IOException, SQLException{
        from_view_to_content_obj();

        if(title_field.getText().isEmpty() || url_field.getText().isEmpty()){
            msg_label.setText("Please fill all the required fields");
            return;
        }

        
        try {
            current_content.modifier_id = current_user.get_id();
            current_content.update();
            msg_label.setText("Content updated successfully");
        } catch (SQLException e) {
            current_content.sync(true);
            if(e.getMessage().contains("Violation of UNIQUE KEY constraint")){
                msg_label.setText("You have already added a content with this Title/URL!");
                editButtonOnclick(new ActionEvent());
                return;
            }
            else{
                editButtonOnclick(new ActionEvent());
                throw e;
            }
        }

        for(int i = 0; i < tags.size(); i++){
            Tag tag = tags.get(i);

            if(tag.get_id() == -1){
                try{
                    tag.creator_id = current_user.get_id();
                    tag.insert();
                } catch (SQLException e) {
                    if(e.getMessage().contains("Violation of UNIQUE KEY constraint")){
                        tag = Tag.get_by_tag(tag.tag);
                    }else{
                        editButtonOnclick(new ActionEvent());
                        throw e;
                    }
                }
            }else{
                // if tag.id is not -1, then this means it has been loaded as part of the content
                // so no need to add it again to the database or to the content
                continue;
            }
            
            ContentTag contentTag = null;

            contentTag = new ContentTag(current_content.get_id(), tag.get_id());
            
            try{
                contentTag.insert();
            } catch (SQLException e) {
                if(e.getMessage().contains("Violation of PRIMARY KEY constraint")){
                    msg_label.setText("You have already added tag '" + tag.tag + "' to this content!");
                }else{
                    editButtonOnclick(new ActionEvent());
                    throw e;
                }
            }
        }
        editButtonOnclick(new ActionEvent());
    }
    
    
    public void editButtonOnclick(Event event) throws SQLException, IOException{
        
        if(edit_btn.getText().equals("Edit")){
            set_editability(true);
            edit_btn.setText("Cancel");
        }
        else{
            set_editability(false);
            edit_btn.setText("Edit");
            current_content.sync(true);
            tags = Tag.get_by_content_id(current_content.get_id());
            from_content_obj_to_view();
        }
    }
}
