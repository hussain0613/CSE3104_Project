package controllers;

import java.io.IOException;
import java.sql.SQLException;

import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.User;

public class Profile{
    public User current_user;

    public TextField name_field, email_field, username_field, role_field, status_field;
    public Label msg_label, creation_datetime_label, modification_datetime_label, password_label, confirm_password_label; 
    public PasswordField password_field, confirm_password_field;

    public Button btn;

    public void setData(User current_user){
        this.current_user = current_user;
        from_user_obj_to_view();
    }

    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fl = new FXMLLoader();

        Pane root = fl.load(getClass().getResource("/views/profile.fxml").openStream());
        ((Profile)fl.getController()).setData(current_user);
        
        primaryStage.setTitle("Profile");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    private void from_user_obj_to_view(){
        name_field.setText(current_user.name);
        email_field.setText(current_user.email);
        username_field.setText(current_user.username);
        role_field.setText(current_user.role);
        status_field.setText(current_user.status ? "Active" : "Inactive");
        creation_datetime_label.setText(current_user.get_creation_datetime());
        modification_datetime_label.setText(current_user.get_modification_datetime());

        password_field.setText(current_user.get_password());
        confirm_password_field.setText(current_user.get_password());
    }

    private void from_view_to_user_obj(){
        current_user.name = name_field.getText();
        current_user.email = email_field.getText();
        current_user.username = username_field.getText();
        current_user.set_password(password_field.getText());
    }

    private void set_editability(boolean editable){
        name_field.setEditable(editable);
        email_field.setEditable(editable);
        username_field.setEditable(editable);
        
        if(current_user.role.equals("admin")){
            role_field.setEditable(editable);
            status_field.setEditable(editable);
        }
        
        password_label.setVisible(editable);
        password_field.setEditable(editable);
        password_field.setVisible(editable);
        
        confirm_password_label.setVisible(editable);
        confirm_password_field.setEditable(editable);
        confirm_password_field.setVisible(editable);
    }
    
    public void buttonOnclick(Event event) throws SQLException, IOException{
        if(btn.getText().equals("Edit")){
            set_editability(true);
            btn.setText("Save");
        }
        else{
            set_editability(false);
            btn.setText("Edit");
            if(password_field.getText().equals(confirm_password_field.getText())){
                try {
                    from_view_to_user_obj();
                    current_user.update();
                    modification_datetime_label.setText(current_user.get_modification_datetime());
                    msg_label.setText("User updated successfully");
                } catch (SQLException e) {
                    current_user.sync(true);
                    from_user_obj_to_view();
                    msg_label.setText(e.getMessage());
                }
            }
            else{
                current_user.sync(true);
                from_user_obj_to_view();
                msg_label.setText("Passwords do not match");
            }
        }

    }

    public void logout(Event event) throws IOException{
        Signup signup = new Signup();
        signup.start((Stage) ((Button) event.getSource()).getScene().getWindow());
    }
}
