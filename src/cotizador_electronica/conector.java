/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cotizador_electronica;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import org.jboss.logging.Logger;

/**
 *
 * @author leslijazmin
 */
public class conector {
       private java.sql.Connection myConnection;
    private Statement statement;
    
    public conector(String user, String password, String dataBase) throws ClassNotFoundException, SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            myConnection = DriverManager.getConnection("jdbc:mysql://localhost/" + dataBase, user, password);
            statement = myConnection.createStatement();
        } catch (ClassNotFoundException ex) {
            throw new ClassNotFoundException("Conector no encontrado. Mensaje de error: " + ex.getMessage());
        } catch (SQLException ex) {
            throw new SQLException("Error en SQL. Mensaje de error: " + ex.getMessage());
        }
    }
    
     public ResultSet search(String instruccion) {
        try{
            if (!myConnection.isClosed()){
                ResultSet resultSet = statement.executeQuery(instruccion);
                if(!resultSet.next()){
                    System.out.println("No hay resultados que coincidan con la búsqueda.");
                    return null;
                }

                return resultSet;
            }else{
                System.out.println("La conexión con la Base de Datos está cerrada.");
                return null;
            }
        }catch(SQLException ex){
            System.out.println("Error " + ex.getMessage());
            return null;
        }
    }
}
