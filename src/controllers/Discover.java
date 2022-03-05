package controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import models.User;
import models.Content;
import models.Shelf;

public class Discover {
    
    User current_user;
    String received_tag;

    public TableView<Row> table;
    public TableColumn<Row, String> title_col, mod_dt_col, url_col, type_col;
    public TextField search_value_field;
    public ChoiceBox<String> search_by_choice_box, filter_by_choice_box;

    Pane contentArea;
    
    public void setData(User current_user, String received_tag){
        this.current_user = current_user;
        this.received_tag = received_tag;
    }

    public void start(Pane contentAreaPane) throws IOException, SQLException {
        FXMLLoader fl = new FXMLLoader();

        Pane root = fl.load(getClass().getResource("/views/discover.fxml").openStream());

        Discover controller = fl.getController();
        controller.contentArea = contentAreaPane;
        controller.setData(current_user, received_tag);
        controller.populate_table(true);

        controller.search_by_choice_box.getItems().addAll("Title", "Tag");
        controller.search_by_choice_box.getSelectionModel().select(0);
        controller.filter_by_choice_box.getItems().addAll("All", "Website", "Text", "E-Book", "Image", "Audio", "Video", "Other");
        controller.filter_by_choice_box.getSelectionModel().select(0);

        contentAreaPane.getChildren().removeAll();
        contentAreaPane.getChildren().setAll(root);
    }

    public void populate_table(boolean allContents) throws SQLException, IOException{
        ArrayList<Content> contents = Content.get_by_creator_id(current_user.get_id());;
        ArrayList<Shelf> shelves = Shelf.get_by_creator_id(current_user.get_id());;

        if(allContents){
            if(received_tag == null){

            }
            else{
                contents = Content.discover_by_tag(received_tag);
                shelves = Shelf.discover_by_tag(received_tag);
            }
        }
        else{

        }
        
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

    public void search_btn_click(Event event) throws IOException, SQLException {
        populate_table(false);
    }

    public void mouse_click_event_handler(MouseEvent event) throws IOException, SQLException{
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

