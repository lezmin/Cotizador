/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cotizador_electronica;

/**
 *
 * @author leslijazmin
 */
public class guardarProductos {

   
    String entrada;
    
    public guardarProductos(String entrada) {
        
        this.entrada= entrada;
      
    }
    
    public String getEntrada(){
       return entrada;
    }
    
    
  
     @Override
    public String toString(){
        return  entrada+ "\n";
    }
    
}
