/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cotizador_electronica;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author leslijazmin
 */
public class ConfirmBoxController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML TextField des,can,prec;
     Stage confirm;
     static String descripcion="";
     static int cantidad=0;
     static float precio=0;
    public void setStage(Stage stage){
        this.confirm = stage;
    }
    
    @FXML
    public void agregar(ActionEvent event){
        descripcion = des.getText();
        cantidad = Integer.parseInt(can.getText());
        precio = Float.parseFloat(prec.getText());
        this.confirm.close();
    }
    
    @FXML
    public void cancelar(ActionEvent event){
       
    this.confirm.close();
    
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
