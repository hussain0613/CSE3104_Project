package controllers;

import java.io.IOException;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Signup {
    public void start(Stage primaryStage) throws IOException {
        Pane root = FXMLLoader.load(getClass().getResource("/views/signup.fxml"));
        primaryStage.setTitle("Sign Up");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public void signup(Event event){
        System.out.println("Clicked Signup!");
    }
        
}
