/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package thesis;

import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import static thesis.Test.test1;
import static thesis.Test.test2;

/**
 *
 * @author test
 */
public class Main {
    
    
    static Connection conn = null;
   static ResultSet rs  = null;
   static PreparedStatement pst = null;
   static String sql = "";
    
    
 
    
    
  public static void main (String args[]){
      connect();
      
       
      
      
     
 }
  
  public static void connect(){
      
      String user = "root";
      String pass = "";
      String host = "127.0.0.1";
      String db = "ontology_system";
      
      try {
          conn = DriverManager.getConnection(
                  "jdbc:mysql://" + host + "/" + db + "",
                  "" + user + "",
                  "" + pass + "");
      } catch (SQLException sQLException) {
          JOptionPane.showMessageDialog(null, sQLException);
      }
      
   
  
      Icon icon = null;
        try{
            sql = "select * from `table 1`";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            
            if(rs.next()){
                String s = rs.getString("ad_name");
                JOptionPane.showMessageDialog(null,s);
            }
            }
            catch(HeadlessException | SQLException err){
            JOptionPane.showMessageDialog(null,err);
        }
  }
  
 
}
