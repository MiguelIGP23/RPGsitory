package migp.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class VentanaCombateController {

    //Instancia componentes
    @FXML
    private Label lblMensaje;

    //Inicializa componentes instanciados
    @FXML
    private void initialize(){
        lblMensaje.setText("Prueba mensaje Java FX + Scene Build");
    }
}
