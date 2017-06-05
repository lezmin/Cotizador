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
    public SimpleStringProperty codProducto = new SimpleStringProperty();
    public SimpleStringProperty descripcion = new SimpleStringProperty();
    public SimpleFloatProperty precio = new SimpleFloatProperty();
    public SimpleIntegerProperty cantidad = new SimpleIntegerProperty();
    public SimpleFloatProperty total = new SimpleFloatProperty();
    
    public Productos(String codProducto,String descripcion,Float precio){
        this.codProducto = new SimpleStringProperty(codProducto);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.precio = new SimpleFloatProperty(precio);
    
    }
    
    public String getcodProducto(){
        return codProducto.get();
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
        return precio.get();
    }
    
    @Override
    public String toString(){
    
        return this.getcodProducto() + this.getDescripcion();
    }
}
