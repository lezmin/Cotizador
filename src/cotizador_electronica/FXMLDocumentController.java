/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cotizador_electronica;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author leslijazmin
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Label label;
    @FXML 
    private TextField txtBucador;
    @FXML 
    private Button btnBuscar;
    @FXML
    private TableView<Productos> tbProductos;
    @FXML TableColumn colCodProduct,colDescripcion ,colPrecio,colCantidad,colTotal;
    ObservableList<Productos>productos;
     
     private ResultSet result;
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
     @FXML
    private void loadDataFromDatabase(ActionEvent event) throws ClassNotFoundException {
        try {
           conector cd = new conector("root","","mvelectronica");
            productos = FXCollections.observableArrayList();
           String codigo = (txtBucador.getText());
            System.out.println(codigo);
            // Execute query and store result in a resultset
            ResultSet rs = cd.search("SELECT * FROM productos WHERE Codigo='"+ codigo + " '; ");
           // while (rs.next()) {
                //get string from db,whichever way 
               // productos.add(new Productos( rs.getString(1),rs.getString(2)));
                String codProducto= rs.getString(1);
                String descripcion = rs.getString(2);
               float precio =Float.parseFloat(rs.getString(3));
                productos.add(new Productos( codProducto,descripcion,precio));
                
           // }

        } catch (SQLException ex) {
            System.err.println("Error"+ex);
       }
        
       
        
        //Set cell value factory to tableview.
        //NB.PropertyValue Factory must be the same with the one set in model class.
        colCodProduct.setCellValueFactory(new PropertyValueFactory<>("codProducto"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
  
        
        tbProductos.setItems(null);
        tbProductos.setItems(productos);

    }
}
