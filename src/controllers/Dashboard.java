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
import models.Content;
import models.Shelf;

public class Dashboard {
    
    User current_user;

    public TableView<Row> table;
    public TableColumn<Row, String> title_col, mod_dt_col, url_col, type_col;

    Pane contentArea;
    
    public void setData(User current_user){
        this.current_user = current_user;
    }

    public void start(Pane contentAreaPane) throws IOException, SQLException {
        FXMLLoader fl = new FXMLLoader();

        Pane root = fl.load(getClass().getResource("/views/dashboard.fxml").openStream());

        Dashboard controller = fl.getController();
        controller.contentArea = contentAreaPane;
        controller.setData(current_user);
        controller.populate_table();
        
        contentAreaPane.getChildren().removeAll();
        contentAreaPane.getChildren().setAll(root);
    }

    public void populate_table() throws SQLException, IOException{
        ArrayList<Content> contents = Content.get_by_creator_id(current_user.get_id());
        ArrayList<Shelf> shelves = Shelf.get_by_creator_id(current_user.get_id());
        
        table.getItems().clear();
        
        ObservableList<Row> content_rows = Row.from_contents(contents);
        table.setItems(content_rows);

        ObservableList<Row> shelf_rows = Row.from_shelves(shelves);
        table.getItems().addAll(shelf_rows);

        title_col.setCellValueFactory(new PropertyValueFactory<Row, String>("title"));
        mod_dt_col.setCellValueFactory(new PropertyValueFactory<Row, String>("modification_datetime"));
        url_col.setCellValueFactory(new PropertyValueFactory<Row, String>("url"));
        type_col.setCellValueFactory(new PropertyValueFactory<Row, String>("type"));
    }

    public void mouse_click_event_handler(MouseEvent event) throws IOException{
        if(event.getClickCount() == 2){
            Row row = table.getSelectionModel().getSelectedItem();
            if(row != null){
                if(row.content != null){
                    ContentPage content_page = new ContentPage();
                    content_page.setData(current_user, row.content);
                    content_page.start(contentArea);
                }
            }
        }
    }
}

