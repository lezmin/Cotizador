/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cotizador_electronica;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.controlsfx.control.textfield.TextFields;



/**
 *
 * @author leslijazmin
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Label label;
    
    @FXML 
    private TextArea txtAreaListar;
    @FXML 
    private TextField txtBuscador ,txtEdCantidad,txtCalle,txtNumero,txtColonia,txtCiudad,txtEstado,txtCP,txtTelefono,txtEmail;
    @FXML 
    private Button btnBuscar,btnMostrarLista;
    @FXML
    private TableView<Productos> tbProductos;
    @FXML TableColumn colCodigo,colDescripcion ,colPrecio,colCantidad,colTotal;
    ObservableList<Productos>productos;
    ObservableList<guardarProductos>guardaPro=FXCollections.observableArrayList();
    ObservableList<guardarProductos>guardaCod=FXCollections.observableArrayList();
    ObservableList<guardarProductos>unificacion = FXCollections.observableArrayList();
     private ResultSet result;
   
    public void listarProductos(ActionEvent event ){
           
      
        txtAreaListar.setText(productos.toString());
           
    
    }
    
    public void copiarProductos(ActionEvent event){
           txtAreaListar.selectAll();
           txtAreaListar.copy();
           
    }
    

  public void cargarDatosProducto() throws ClassNotFoundException, SQLException {
        try {
           
            conector cd = new conector("root","","mvelectronica");
             ResultSet rs = cd.search("SELECT * FROM `productos` ");
            while(rs.next()){
                 String des = rs.getString(2);
                 String cod = rs.getString(1);
                 guardaPro.add(new guardarProductos(des));
            }
            //System.out.println(guardaPro.toString());
             /*for (guardarProductos producto : guardaPro) {
                  String d = producto.entrada;
                  String c = producto.codigo;
                  System.out.println(c +"\n"+ d);
                        
             }*/
             
        } catch (ClassNotFoundException | SQLException e) {
        }
         
    }
  
  public void cargarDatosCodigo(){
      try {
           
            conector cd = new conector("root","","mvelectronica");
             ResultSet rs = cd.search("SELECT * FROM `productos` ");
            while(rs.next()){
                 String codPro= rs.getString(1);
                 guardaCod.add(new guardarProductos(codPro));
            }
            //System.out.println(guardaCod.toString());
             
          
        } catch (ClassNotFoundException | SQLException e) {
        }
  
  }
    
  public void unirListas(){
  
      
      unificacion.addAll(guardaCod);
      unificacion.addAll(guardaPro);
      System.out.println(unificacion);
  }
    
   
    public void buscarEnBD(ActionEvent event) throws ClassNotFoundException{
      try {
        
           
           conector cd = new conector("root","","mvelectronica");
           String descripcion = (txtBuscador.getText());
           
           //ResultSet rs = cd.search("SELECT * FROM productos WHERE Codigo='"+ descripcion + " '; ");
           // Hacer consulta en la base de datos 
           ResultSet rs = cd.search("SELECT * FROM productos WHERE Descripcion='"+ descripcion +"'or Codigo='"+descripcion+ " '; ");
            String codProducto= rs.getString(1);
            String descrip =  rs.getString(2);
            float precio =Float.parseFloat(rs.getString(4));
            Integer cantidad= Integer.parseInt(txtEdCantidad.getText());
            float total = cantidad*precio;
           // agregar la busqueda en un objeto para manejarlo en el observableList
            productos.add(new Productos(codProducto,descrip,precio,cantidad,total));
            txtBuscador.clear();
        } catch (SQLException ex) {
            System.err.println("Error"+ex);
       }
    
    }
    
     public ObservableList<Productos> inicializarTablaProducto(){
         
         //Establecemos los valores del objeto Productos en la columna correspondiente
         
          colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
          colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
         colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
         colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        
        
         //Se recorre el array del observableList y se agrega a la tabla 
          tbProductos.setEditable(true);
          productos = FXCollections.observableArrayList();
          tbProductos.setItems(productos);
          
          return productos;
     }
      
    @FXML
    private void tabEnter(KeyEvent k){
        if(txtBuscador.getText().length() >= 4){
             TextFields.bindAutoCompletion(txtBuscador, unificacion);
              txtBuscador.setOnKeyPressed((KeyEvent event1) -> {
            if (event1.getCode().equals(KeyCode.ENTER)) {
                txtEdCantidad.requestFocus();
                
                System.out.println("cambia a edcantidad");
            }
        });
        }
    }
    
     @FXML
    private void tabBtn(KeyEvent k){
       txtEdCantidad.setOnKeyPressed((KeyEvent event1) -> {
            if (event1.getCode().equals(KeyCode.ENTER)) {
                
                try {
                    buscarEnBD(null);
                    System.out.println("ENTRO");
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("cambia a boton ");
                txtBuscador.requestFocus();
                txtEdCantidad.setText("1");
            }
        });
           
    }
   
    
    @FXML
    public void exportarExcel() throws FileNotFoundException, IOException{
        
        double tot = 0.0;
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("detalles ");
        XSSFRow header = sheet.createRow(0);
        header.createCell(0).setCellValue("No Cliente:");
        header.createCell(3).setCellValue("Nombre:");
         XSSFRow titulos = sheet.createRow(1);
         titulos.createCell(0).setCellValue("No");
         titulos.createCell(1).setCellValue("CANTIDAD");
         titulos.createCell(2).setCellValue("DESCRIPCION");
         titulos.createCell(3).setCellValue("PRECIO");
         titulos.createCell(4).setCellValue("TOTAL");
        int index = 2;
        for (Productos producto : productos) {
            XSSFRow rowPro = sheet.createRow(index);
            Integer c = producto.cantidad.getValue();
            String d = producto.descripcion.getValue();
            Float p = producto.precio.getValue();
            Float t = producto.total.getValue();
            tot = tot +t;
            System.out.println(c);
             System.out.println(d);
             int nu = index-1;
             rowPro.createCell(0).setCellValue(nu);
            rowPro.createCell(1).setCellValue( c);
            rowPro.createCell(2).setCellValue( d);
            rowPro.createCell(3).setCellValue( p);
            rowPro.createCell(4).setCellValue( t);
            index++;
        }
        
        System.out.println(index );
         System.out.println(tot );
        try (FileOutputStream fileOut = new FileOutputStream("Cotizador.xlsx")) {
        wb.write(fileOut);
        }
    
    
    }
        
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
           
            cargarDatosProducto();
            cargarDatosCodigo();
            unirListas();
            this.inicializarTablaProducto();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }    
}
