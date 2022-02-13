package controllers;

import java.io.IOException;
import java.sql.SQLException;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.User;

public class Signup {
    public TextField name_field, email_field, username_field, pass_field, confpass_field, login_username_field, login_pass_field;
    public Label msg_label; 

    public void start(Stage primaryStage) throws IOException {
        Pane root = FXMLLoader.load(getClass().getResource("/views/signup.fxml"));
        primaryStage.setTitle("Welcome");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public void signup(Event event){

        String name = name_field.getText();
        String email = email_field.getText();
        String username = username_field.getText();
        String pass = pass_field.getText();
        String confpass = confpass_field.getText();

        if(pass.equals(confpass)){
            User user = new User(name, email, username, pass, "guest", true);
            try {
                user.insert();

                msg_label.setText("Successfully signed up!");

            } catch (SQLException e) {
                // TODO Auto-generated catch block
                msg_label.setText(e.getMessage());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                msg_label.setText(e.getMessage());
            }
        }
        else{
            msg_label.setText("Passwords do not match");
        }

    }

    public void login(Event event) throws IOException{
        String username = login_username_field.getText();
        String pass = login_pass_field.getText();

        try {
            User user = User.get_by_username(username);
            if(user.get_password().equals(pass)){
                // msg_label.setText("Successfully logged in!");
                Menu menu = new Menu();
                menu.current_user = user;
                menu.start((Stage) ((Button) event.getSource()).getScene().getWindow());
            }
            else{
                msg_label.setText("Wrong username or password");
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            msg_label.setText(e.getMessage());
        }
    }
        
}