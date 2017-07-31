/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cotizador_electronica;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;


/**
 *
 * @author leslijazmin
 */
public class Validaciones {

    /**
     *
     * @param txtCotizador
     * @param txtNomCliente
     * @param txtRFC
     * @param txtCP
     * @param txtCalle
     * @param txtColonia
     * @param txtCiudad
     * @param txtEstado
     * @param txtTelefono
     * @param txtEmail
     */
    
     @FXML 
    private final String txtCalle,txtRFC,txtColonia,txtCiudad,txtEstado,txtCP,txtTelefono,txtEmail,txtNomCliente,txtCotizador;
    
    public Validaciones(String txtCotizador,String txtNomCliente,String txtRFC,String txtCP,String txtCalle,String txtColonia,String txtCiudad,String txtEstado,String txtTelefono,String txtEmail) {
        this.txtCotizador = txtCotizador;
        this.txtNomCliente = txtNomCliente;
        this.txtRFC = txtRFC;
        this.txtCP=txtCP;
        this.txtCalle = txtCalle;
        this.txtColonia = txtColonia;
        this.txtCiudad = txtCiudad;
        this.txtEstado = txtEstado;
        this.txtTelefono= txtTelefono;
        this.txtEmail = txtEmail; 
        
    }
    
   public boolean validarVacio(){
       
      //  System.out.println("dia"+ dia);
      
       if(txtNomCliente.compareTo("")==0 && txtCotizador.compareTo("")==0 && txtRFC.compareTo("")==0 && 
          txtCP.compareTo("")==0 &&txtCalle.compareTo("")==0 &&txtColonia.compareTo("")==0
          &&txtCiudad.compareTo("")==0 &&txtEstado.compareTo("")==0 &&txtTelefono.compareTo("")==0
            &&txtEmail.compareTo("")==0   ){
            Alert alert = new Alert(Alert.AlertType.ERROR);
           alert.setTitle("Campos vacios");
           alert.setHeaderText("Rellene todos los campos");
           alert.setContentText("uno o mas campos estan vacios");
           alert.show();
           
                 
       }else{
          
           
       }
              
       return true;
    }
    

}
