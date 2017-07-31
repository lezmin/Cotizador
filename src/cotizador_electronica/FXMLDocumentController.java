/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cotizador_electronica;




import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.apache.poi.ss.formula.FormulaParseException;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.dialog.Dialogs;




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
    private TextField txtBuscador ,txtEdCantidad,txtCotizador,txtNomCliente,txtRFC,txtCP,txtCalle,txtColonia,txtCiudad,txtEstado,txtTelefono,txtEmail;
    @FXML 
    private Button btnBuscar,btnCopiar,btnExportarresre;
    @FXML
    private TableView<Productos> tbProductos;
    @FXML TableColumn colCodigo,colDescripcion ,colPrecio,colCantidad,colTotal;
    ObservableList<Productos>productos;
    ObservableList<guardarProductos>guardaPro=FXCollections.observableArrayList();
    ObservableList<guardarProductos>guardaCod=FXCollections.observableArrayList();
    ObservableList<guardarProductos>unificacion = FXCollections.observableArrayList();
     private ResultSet result;
    Stage document;
    String descripcionNP= "";
    
    public void setStage(Stage stage){
        this.document = stage;
    }
    
    public void setDescripcion(String descripcion){
        this.descripcionNP = descripcion;
        
    }
    public void copiarProductos(ActionEvent event){
           txtAreaListar.setText(productos.toString());
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
            String descrip =  rs.getString(2);
            float precio =Float.parseFloat(rs.getString(4));
            Integer cantidad= Integer.parseInt(txtEdCantidad.getText());
            float total = cantidad*precio;
           // agregar la busqueda en un objeto para manejarlo en el observableList
            productos.add(new Productos(descrip,precio,cantidad,total));
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
    private void validarText(KeyEvent k){
       txtNomCliente.setOnKeyTyped((KeyEvent event1) -> {
           char c = event1.getCharacter().charAt(0);
           
           System.out.println(c);
           
           if(!Character.isLetter(c)){
               event1.consume();
           }
        });
           
    }
    
   
    
   @FXML
   public void pruebaImg() throws FileNotFoundException, IOException{
   
   try {
    
   String coti = txtCotizador.getText();
   String nomCli= txtNomCliente.getText();
   String rfc= txtRFC.getText();
   String cp = txtCP.getText();
   String calle= txtCalle.getText();
   String colonia = txtColonia.getText();
   String ciudad = txtCiudad.getText();
   String estado = txtEstado.getText();
   String telefono = txtTelefono.getText();
   String email = txtEmail.getText();
   
   if(nomCli.isEmpty()|| cp.isEmpty() || telefono.isEmpty() || coti.isEmpty()){
        Alert alert = new Alert(Alert.AlertType.ERROR);
           alert.setTitle("Error");
           alert.setHeaderText("Campos vacios");
           alert.setContentText("Rellene los campos en color rojo");
           alert.show();
   
   
   }else{
   
   
   Workbook wb = new XSSFWorkbook();
   Sheet sheet = wb.createSheet("MV ELECTRONICA");
   sheet.setColumnWidth(0, conversion(3));
   sheet.setColumnWidth(1, conversion(8));
   sheet.setColumnWidth(2, conversion(24));
   sheet.setColumnWidth(3, conversion(8));
   sheet.setColumnWidth(4, conversion(8));
   sheet.addMergedRegion(new CellRangeAddress(1, 1, 3, 4));
   int pictureIdx;
       //Get the contents of an InputStream as a byte[].
       try ( //FileInputStream obtains input bytes from the image file
               InputStream inputStream = new FileInputStream("logo.png")) {
           //Get the contents of an InputStream as a byte[].
           byte[] bytes = IOUtils.toByteArray(inputStream);
           //Adds a picture to the workbook
           pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
           //close the input stream
       }

   //Returns an object that handles instantiating concrete classes
   CreationHelper helper = wb.getCreationHelper();

   //Creates the top-level drawing patriarch.
   Drawing drawing = sheet.createDrawingPatriarch();

   //Create an anchor that is attached to the worksheet
   ClientAnchor anchor = helper.createClientAnchor();
   //set top-left corner for the image
   anchor.setCol1(0);
   anchor.setRow1(0);
   
   //Crear imagen
   Picture pict = drawing.createPicture(anchor, pictureIdx);
   //Reset the image to the original sizearkia
   pict.resize();
   
   
    
    XSSFRow header = (XSSFRow) sheet.createRow(0); 
    Cell mv=header.createCell(2);
    mv.setCellValue("MV ELECTRONICA");
    CellStyle centro =wb.createCellStyle();
    centro.setAlignment(HorizontalAlignment.CENTER);
    mv.setCellStyle(centro);
    
    XSSFRow header1 = (XSSFRow) sheet.createRow(1);
    Cell nombre=header1.createCell(2);
    nombre.setCellValue("Cesar Arsenio Morales Gonzalez");
    nombre.setCellStyle(centro);
    Cell cotizacion=header1.createCell(3);
    Cell cotizacion2=header1.createCell(4);
    cotizacion.setCellValue("COTIZACIÓN");
    CellStyle style1 = wb.createCellStyle();
    
    
    style1.setBorderTop(BorderStyle.THICK);
    style1.setBorderLeft(BorderStyle.THICK);
    style1.setBorderRight(BorderStyle.THICK);
    style1.setBorderBottom(BorderStyle.THICK);
    style1.setTopBorderColor(IndexedColors.BLUE.getIndex());
    style1.setLeftBorderColor(IndexedColors.BLUE.getIndex());
    style1.setRightBorderColor(IndexedColors.BLUE.getIndex());
    style1.setBottomBorderColor(IndexedColors.BLUE.getIndex());
    style1.setAlignment(HorizontalAlignment.CENTER);
    cotizacion.setCellStyle(style1);
    cotizacion2.setCellStyle(style1);
    
    XSSFRow header2 = (XSSFRow) sheet.createRow(2);
    Cell dir=header2.createCell(2);
    Cell fecha=header2.createCell(3);
    
    Calendar fech=new GregorianCalendar();
             
     
    dir.setCellValue("Ave. 1° sur poniente #462, Col. Centro\n");
    dir.setCellStyle(centro);
    
    
    CellStyle bizq=wb.createCellStyle();
    bizq.setBorderLeft(BorderStyle.THICK);
    bizq.setLeftBorderColor(IndexedColors.BLUE.getIndex());
    fecha.setCellStyle(bizq);
    
    CellStyle bder=wb.createCellStyle();
    bder.setBorderRight(BorderStyle.THICK);
    bder.setRightBorderColor(IndexedColors.BLUE.getIndex());
    XSSFDataFormat fec=(XSSFDataFormat) wb.createDataFormat();
    bder.setDataFormat(fec.getFormat( "dd/mm/yyyy"));
    
    Cell fecact=header2.createCell(4);
    
    fecha.setCellValue("FECHA:");
    
    
    fecact.setCellValue(fech);
    fecact.setCellStyle(bder);
       
       
    
    XSSFRow header3 = (XSSFRow) sheet.createRow(3);
    Cell ciu=header3.createCell(2);
    ciu.setCellValue("TUXTLA GUTIERREZ, CHIAPAS");
    ciu.setCellStyle(centro);
    Cell foli=header3.createCell(3);
    foli.setCellValue("FOLIO:");
    foli.setCellStyle(bizq);
    Cell folinum=header3.createCell(4);
    
    CellStyle folionum=wb.createCellStyle();
    folionum.setBorderRight(BorderStyle.THICK);
    folionum.setRightBorderColor(IndexedColors.BLUE.getIndex());
    folinum.setCellType(CellType.NUMERIC);
    folinum.setCellStyle(folionum);
         
    
    XSSFRow header4 = (XSSFRow) sheet.createRow(4);
    Cell rfce=header4.createCell(2);
    rfce.setCellValue("CP.29000 RFC:MOGC920119EBA");
    rfce.setCellStyle(centro);
    Cell vig=header4.createCell(3);
    vig.setCellValue("VIGENCIA:");
    CellStyle vige=wb.createCellStyle();
    vige.setBorderLeft(BorderStyle.THICK);
    vige.setLeftBorderColor(IndexedColors.BLUE.getIndex());
    vige.setBorderBottom(BorderStyle.THICK);
    vige.setBottomBorderColor(IndexedColors.BLUE.getIndex());
    vig.setCellStyle(vige);
    
    Cell vigm=header4.createCell(4);
    fech.set(fech.get(Calendar.YEAR), fech.get(Calendar.MONTH)+1, fech.get(Calendar.DAY_OF_MONTH));
    vigm.setCellValue(fech);
    CellStyle vigme=wb.createCellStyle();
    vigme.setDataFormat(fec.getFormat( "dd/mm/yyyy"));
    vigme.setBorderRight(BorderStyle.THICK);
    vigme.setRightBorderColor(IndexedColors.BLUE.getIndex());
    vigme.setBorderBottom(BorderStyle.THICK);
    vigme.setBottomBorderColor(IndexedColors.BLUE.getIndex());
    vigm.setCellStyle(vigme);
//FIN CABECERA 
    
    //FORMULARIO
    XSSFRow formulario = (XSSFRow) sheet.createRow(6);
    CellStyle eizq=wb.createCellStyle();
    CellStyle farr=wb.createCellStyle();
    eizq.setBorderTop(BorderStyle.MEDIUM);
    eizq.setBorderLeft(BorderStyle.MEDIUM);
    farr.setBorderTop(BorderStyle.MEDIUM);
    formulario.createCell(0).setCellStyle(eizq);
    Cell ncli=formulario.createCell(1);
    ncli.setCellValue("No Cliente:");
    ncli.setCellStyle(farr);
    Cell rncli=formulario.createCell(2);
    rncli.setCellStyle(farr);
    
    Cell rfcn=formulario.createCell(3);
    rfcn.setCellValue("RFC:");
    rfcn.setCellStyle(farr);
    Cell resrfc=formulario.createCell(4);
    CellStyle eder=wb.createCellStyle();
    eder.setBorderTop(BorderStyle.MEDIUM);
    eder.setBorderRight(BorderStyle.MEDIUM);
    resrfc.setCellType(CellType.STRING);
    resrfc.setCellValue(rfc);
    resrfc.setCellStyle(eder);
    XSSFRow formulario2 = (XSSFRow) sheet.createRow(7);
    CellStyle ladizq=wb.createCellStyle();
    ladizq.setBorderLeft(BorderStyle.MEDIUM);
    formulario2.createCell(0).setCellStyle(ladizq);
    
    formulario2.createCell(1).setCellValue("Cliente:");
    Cell nomcliente=formulario2.createCell(2);
    nomcliente.setCellType(CellType.STRING);
    nomcliente.setCellValue(nomCli);
    formulario2.createCell(3).setCellValue("COLONIA:");
    Cell col= formulario2.createCell(4);
    CellStyle ladder=wb.createCellStyle();
    ladder.setBorderRight(BorderStyle.MEDIUM);
    col.setCellType(CellType.STRING);
    col.setCellValue(colonia);
    col.setCellStyle(ladder);
    
    XSSFRow formulario3 = (XSSFRow) sheet.createRow(8);
    formulario3.createCell(0).setCellStyle(ladizq);
    formulario3.createCell(1).setCellValue("CALLE:");
    Cell call= formulario3.createCell(2);
    call.setCellType(CellType.STRING);
    call.setCellValue(calle);
    formulario3.createCell(3).setCellValue("ESTADO:");
    Cell est=formulario3.createCell(4);
    est.setCellType(CellType.STRING);
    est.setCellValue(estado);
    est.setCellStyle(ladder);
    
    XSSFRow formulario4 = (XSSFRow) sheet.createRow(9);
    formulario4.createCell(0).setCellStyle(ladizq);
    formulario4.createCell(1).setCellValue("CIUDAD:");
    Cell ciud=formulario4.createCell(2);
    ciud.setCellType(CellType.STRING);
    ciud.setCellValue(ciudad);
    formulario4.createCell(3).setCellValue("CP:");
    Cell codP=formulario4.createCell(4);
    codP.setCellValue(Integer.parseInt(cp));
    codP.setCellType(CellType.NUMERIC);
    codP.setCellStyle(ladder);
    
    XSSFRow formulario5 = (XSSFRow) sheet.createRow(10);
    CellStyle ladizqa=wb.createCellStyle();
    ladizqa.setBorderLeft(BorderStyle.MEDIUM);
    ladizqa.setBorderBottom(BorderStyle.MEDIUM);
    formulario5.createCell(0).setCellStyle(ladizqa);
    
    Cell ema=formulario5.createCell(1);
    ema.setCellValue("EMAIL:");
    CellStyle emai=wb.createCellStyle();
    emai.setBorderBottom(BorderStyle.MEDIUM);
    ema.setCellStyle(emai);
    Cell emaill=formulario5.createCell(2);
    emaill.setCellType(CellType.STRING);
    emaill.setCellValue(email);
    emaill.setCellStyle(emai);
    Cell tel=formulario5.createCell(3);
    tel.setCellValue("TELEFONO:");
    CellStyle faba=wb.createCellStyle();
    faba.setBorderBottom(BorderStyle.MEDIUM);
    tel.setCellStyle(faba);
    
    XSSFDataFormat telef=(XSSFDataFormat) wb.createDataFormat();
    Cell rescel=formulario5.createCell(4);
    rescel.setCellValue(Long.parseLong(telefono));
    rescel.setCellType(CellType.NUMERIC);
    CellStyle rcel=wb.createCellStyle();
    rcel.setBorderBottom(BorderStyle.MEDIUM);
    rcel.setBorderRight(BorderStyle.MEDIUM);
    rcel.setAlignment(HorizontalAlignment.CENTER);
    rcel.setDataFormat(telef.getFormat("(##0)-###0-##0"));
    rescel.setCellStyle(rcel);
    
    
    
    //FIN FORMULARIO
    
    //TITULO
    XSSFRow titulos = (XSSFRow) sheet.createRow(12);
    CellStyle rodear=wb.createCellStyle();
    rodear.setBorderTop(BorderStyle.MEDIUM);
    rodear.setBorderBottom(BorderStyle.MEDIUM);
    rodear.setBorderLeft(BorderStyle.MEDIUM);
    rodear.setBorderRight(BorderStyle.MEDIUM);
    rodear.setAlignment(HorizontalAlignment.CENTER);
    
    Cell no=titulos.createCell(0);
    no.setCellValue("No");
    no.setCellStyle(rodear);
    
    Cell canti=titulos.createCell(1);
    canti.setCellValue("CANTIDAD");
    canti.setCellStyle(rodear);
    
    Cell descrip=titulos.createCell(2);
    descrip.setCellValue("DESCRIPCION");
    descrip.setCellStyle(rodear);
    
    Cell preci=titulos.createCell(3);
    preci.setCellValue("PRECIO");
    preci.setCellStyle(rodear);
    
    Cell totall=titulos.createCell(4);
    totall.setCellValue("IMPORTE");
    totall.setCellStyle(rodear);
    //FIN TITULOS
    
    
    //Llenar excel
    double tot = 0.0;
        int index = 13;
        Cell celda1;
        Cell celda2;
        Cell celda3;
        Cell celda4;
        Cell celda5;
        XSSFDataFormat precios=(XSSFDataFormat) wb.createDataFormat();
        CellStyle rodear2=wb.createCellStyle();
        rodear2.setBorderTop(BorderStyle.MEDIUM);
        rodear2.setBorderBottom(BorderStyle.MEDIUM);
        rodear2.setBorderLeft(BorderStyle.MEDIUM);
        rodear2.setBorderRight(BorderStyle.MEDIUM);
        rodear2.setAlignment(HorizontalAlignment.CENTER);
        rodear2.setDataFormat(precios.getFormat("$#,##0.00;$#,##0.00"));
    
        for (Productos producto : productos) {
            XSSFRow rowPro = (XSSFRow) sheet.createRow(index);
            Integer c = producto.cantidad.getValue();
            String d = producto.descripcion.getValue();
            Float p = producto.precio.getValue();
            Float t = producto.total.getValue();
            tot = tot +t;
            String tota= ("B"+ (index+1)+"*"+"D"+(index+1));
            System.out.println(c);
             System.out.println(d);
             int nu = index-12;
            celda1=rowPro.createCell(0);
            celda1.setCellValue(nu);
            celda1.setCellStyle(rodear);
            
            celda2=rowPro.createCell(1);
            celda2.setCellValue( c);
            celda2.setCellStyle(rodear);
            
            celda3=rowPro.createCell(2);
            celda3.setCellValue( d);
            celda3.setCellStyle(rodear);
            
            celda4=rowPro.createCell(3);
            celda4.setCellValue( p);
            celda4.setCellStyle(rodear2);
            
            celda5=rowPro.createCell(4);
            celda5.setCellFormula(tota);
            celda5.setCellStyle(rodear2);
            index++;
        }
        
        String subExcel = ("SUM(E14:E"+index + ")");
         XSSFRow total = (XSSFRow) sheet.createRow(index);
        Cell tt=total.createCell(3);
        tt.setCellValue("SubTotal:");
        tt.setCellStyle(rodear);
        Cell st=total.createCell(4);
        st.setCellFormula(subExcel);
        st.setCellStyle(rodear2);
      
        XSSFRow cotizador = (XSSFRow) sheet.createRow(index+2);
        Cell co = cotizador.createCell(2);
        co.setCellValue("Cotizacion realizada por:"+coti);
       
    
    
    //Write the Excel file
   String path = System.getProperty("user.home");
   FileOutputStream fileOut = null;
   fileOut = new FileOutputStream(path+"//Desktop//cotizaciones//"+nomCli+".xlsx");
   wb.write(fileOut);
   fileOut.close();
   
   Alert alert = new Alert(Alert.AlertType.INFORMATION);
   alert.setTitle("Information Dialog");
   alert.setHeaderText(null);
   alert.setContentText("Excel exportado");
   alert.showAndWait();
   }
  }
  catch (IOException | NumberFormatException | FormulaParseException e) {
   System.out.println(e);
  }
 }
   
   public int conversion(int medida){
       return 450 * medida;
   
   
   }
   
   
   @FXML 
   public void borrar(){
       int selectedIndex = tbProductos.getSelectionModel().getSelectedIndex();
    if (selectedIndex >= 0) {
       tbProductos.getItems().remove(selectedIndex);
       
    } else {
        // Nothing selected.
        Dialogs.create()
            .title("NO HAY ELEMENTO SELECCIONADO")
            .masthead("No ha seleccionado un elemento de la tabla")
            .message("Por favor selecciona uno.")
            .showWarning();
    }
   }
    @FXML 
    private void nuevaVentada(ActionEvent e) throws IOException{
        Stage stage = new Stage();
        
        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
        Parent root = (Parent) myLoader.load();
        Scene scene = new Scene(root);        
        stage.setScene(scene);
        stage.setTitle("COTIZADOR MV ELECTRONICA");
        
        FXMLDocumentController nueva = myLoader.getController();
        nueva.setStage(stage);       
        
        stage.show();    
        
        

    }
     
    @FXML
    private void agregarProducto(ActionEvent event) throws IOException {
     
        Stage stage = new Stage();        
        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("ConfirmBox.fxml"));
        Parent root = (Parent) myLoader.load();
        Scene scene = new Scene(root);        
        stage.setScene(scene);
        stage.setTitle("Hola");
        stage.setResizable(false);
        
        ConfirmBoxController data = myLoader.getController();     
        data.setStage(stage);
        
        stage.showAndWait();
       
        String d = data.des.getText();
        int c = Integer.parseInt(data.can.getText());
        float p = Float.parseFloat(data.prec.getText());
        Float t = c*p;
        System.out.println("Descripcion:"+d +"  Cantidad: "+c+"Precio:"+p+"Total:"+t);
        if(d==null){
            System.out.println("vacio");
        
        }else{
        productos.add(new Productos(d,p,c,t));
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
