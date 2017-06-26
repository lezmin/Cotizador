/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cotizador_electronica;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.css.SimpleStyleableFloatProperty;

/**
 *
 * @author leslijazmin
 */
public class Productos {
    public SimpleStringProperty codigo = new SimpleStringProperty();
    public SimpleStringProperty descripcion = new SimpleStringProperty();
    public SimpleFloatProperty precio = new SimpleFloatProperty();
    public SimpleIntegerProperty cantidad = new SimpleIntegerProperty();
    public SimpleFloatProperty total = new SimpleFloatProperty();
    public SimpleFloatProperty tot = new SimpleFloatProperty();
    
    public Productos(String codigo,String descripcion,Float precio,Integer cantidad,Float total){
        this.codigo = new SimpleStringProperty(codigo);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.precio = new SimpleFloatProperty(precio);
        this.cantidad = new SimpleIntegerProperty(cantidad);
        this.total = new SimpleFloatProperty(total);
    
    }

    public Productos(String codigo,String descripcion) {
        this.codigo = new SimpleStringProperty(codigo);
        this.descripcion = new SimpleStringProperty(descripcion);
    }
    
    
    
    public String getcodProducto(){
        return codigo.get();
    }
    
    public String getDescripcion(){
        return descripcion.get();
    }
    
    public Float getPrecio(){
        return precio.get();
    }
    
    public Integer getCantidad(){
        return cantidad.get();
    }
    
    public Float getTotal(){
        return total.get();
    }
    
   
    
    @Override
    public String toString(){
        return   this.getCantidad()+"  "+ this.getDescripcion() + " "+ this.getTotal()+ " \n";
    }
    
    
      
    
}


