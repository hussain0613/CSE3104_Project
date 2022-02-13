import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

import models.*;

public class Setup {
    public static void create_tables() throws SQLException, IOException {
        User.create_table();
        Content.create_table();
        Shelf.create_table();
        Tag.create_table();
        ContentTag.create_table();
        ShelfTag.create_table();

        System.out.println("[*] Tables created");
    }

    public static void drop_tables() throws SQLException, IOException{
        ShelfTag.drop_table();
        ContentTag.drop_table();
        Tag.drop_table();
        Shelf.drop_table();
        Content.drop_table();
        User.drop_table();
        
        System.out.println("[*] Tables dropped");
    }
    public static void main(String[] args) throws IOException, SQLException {
        // take input
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter 1 to create tables, 2 to drop tables, 3 to exit");
        int choice = sc.nextInt();
        sc.close();

        switch(choice){
            case 1:
                create_tables();
                break;
            case 2:
                drop_tables();
                break;
            case 3:
                System.exit(0);
                break;
            default:
                System.out.println("[!] Invalid input");
                break;
        }
    }
}
