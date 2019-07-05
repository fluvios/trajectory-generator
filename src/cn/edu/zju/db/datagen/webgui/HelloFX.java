package cn.edu.zju.db.datagen.webgui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloFX extends Application {

    @Override
    public void start(Stage stage) throws Exception {                   
        Parent root = FXMLLoader.load(getClass().getResource("home.fxml"));
        
        Scene scene = new Scene(root, 1280, 690);
    
        stage.setTitle("Indoor Trajectory Generator");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}