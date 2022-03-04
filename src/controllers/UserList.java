package controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import models.User;


public class UserList {
    
    User current_user;

    public TableView<UserRow> table;
    public TableColumn<UserRow, String> username_col, name_col, email_col, role_col, status_col;

    Pane contentArea;
    
    public void setData(User current_user){
        this.current_user = current_user;
    }

    public void start(Pane contentAreaPane) throws IOException, SQLException {
        FXMLLoader fl = new FXMLLoader();

        Pane root = fl.load(getClass().getResource("/views/user_list.fxml").openStream());

        UserList controller = fl.getController();
        controller.contentArea = contentAreaPane;
        controller.setData(current_user);
        controller.populate_table();
        
        contentAreaPane.getChildren().removeAll();
        contentAreaPane.getChildren().setAll(root);
    }

    public void populate_table() throws SQLException, IOException{
        ArrayList<User> users = User.get_all();
        
        table.getItems().clear();
        
        ObservableList<UserRow> user_rows = UserRow.from_users(users);
        table.setItems(user_rows);

        username_col.setCellValueFactory(new PropertyValueFactory<UserRow, String>("username"));
        name_col.setCellValueFactory(new PropertyValueFactory<UserRow, String>("name"));
        email_col.setCellValueFactory(new PropertyValueFactory<UserRow, String>("email"));
        role_col.setCellValueFactory(new PropertyValueFactory<UserRow, String>("role"));
        status_col.setCellValueFactory(new PropertyValueFactory<UserRow, String>("status"));
    }

    public void mouse_click_event_handler(MouseEvent event) throws IOException{
        if(event.getClickCount() == 2){
            UserRow row = table.getSelectionModel().getSelectedItem();
            if(row != null){
                if(row.user != null){
                    UserPage user_page = new UserPage();
                    user_page.setData(current_user, row.user);
                    user_page.start(contentArea);
                }
            }
        }
    }
}

