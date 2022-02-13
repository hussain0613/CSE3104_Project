package controllers;

import java.io.IOException;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import models.User;

public class Menu {
    public User current_user;

    public Pane contentArea;
    public Label username_label;
    public Button userlist_btn;
    
    public void setData(User current_user){
        this.current_user = current_user;
    }

    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fl = new FXMLLoader();

        Parent root = fl.load(getClass().getResource("/views/menu.fxml").openStream());
        Menu controller = fl.getController();
        controller.setData(current_user);
        controller.from_user_obj_to_view();
        
        primaryStage.setTitle("Digital Content Organizer");
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/style/theme.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void from_user_obj_to_view(){
        if(current_user.role.equals("admin")){
            username_label.setText("username: " + current_user.username + " (admin)");
        }
        else{
            username_label.setText("username: " + current_user.username + " (" + current_user.role + ")");
            userlist_btn.setVisible(false);
        }
    }

    public void gotoProfile(Event event) throws IOException{

        Profile profile = new Profile();
        profile.setData(current_user);
        profile.start(contentArea);
    }

    public void logout(Event event) throws IOException{
        Signup signup = new Signup();
        signup.start((Stage) ((Button) event.getSource()).getScene().getWindow());
    }
}
