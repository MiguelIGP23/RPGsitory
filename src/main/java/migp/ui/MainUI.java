package migp.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainUI extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        //Indica ruta del FXML principal (ventana principal)
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/migp/ui/ventanaCombate.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Pantalla Prinncipal");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
