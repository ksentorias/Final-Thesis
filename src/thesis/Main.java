/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package thesis;

import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateProcessor;
import com.hp.hpl.jena.update.UpdateRequest;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

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
      int i = 4;
      
      try {
          conn = DriverManager.getConnection(
                  "jdbc:mysql://" + host + "/" + db + "",
                  "" + user + "",
                  "" + pass + "");
      } catch (SQLException sQLException) {
          JOptionPane.showMessageDialog(null, sQLException);
      }
      
      try{
            sql = "select * from `table 1`";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            
            while(rs.next()){
                
                String ad_name = rs.getString("ad_name");
                String site = rs.getString("site");
                String price = rs.getString("price");
                String date_posted = rs.getString("date_posted");
                String condition = rs.getString("2nd_or_brandnew");
                String location = rs.getString("location");
                String posted_by = rs.getString("posted_by");
                String description = rs.getString("description");
                
                
                
                 String qry = "PREFIX :<http://xu.edu.ph/ecommerce#> " +
                            "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                            "PREFIX owl:<http://www.w3.org/2002/07/owl#> " +
                            "PREFIX xsd:<http://www.w3.org/2001/XMLSchema#>"
                     + "INSERT DATA " +
                            "{" +
                            "  :ad_"+i+" rdf:type owl:NamedIndividual;" +
                            "        rdf:type :Phones;" +
                            "        :ad_name \""+ad_name+"\";      "
                         +  "        :site \""+site+"\";}"
                         +  "        :price \""+price+"\";}"
                         +  "        :date_posted \""+date_posted+"\";}"
                         +  "        :2nd_or_brandnew \""+condition+"\";}"
                         +  "        :location \""+location+"\";}"
                         +  "        :posted_by \""+posted_by+"\";}"
                         +  "        :description \""+description+"\";}";
                 
                 String query = "PREFIX :<http://xu.edu.ph/ecommerce#> " +
                                "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                                "PREFIX owl:<http://www.w3.org/2002/07/owl#> " +
                                "PREFIX xsd:<http://www.w3.org/2001/XMLSchema#> " +
                                "  " +
                                "INSERT DATA  " +
                                "{ " +
                                ":ad_"+i+" rdf:type owl:NamedIndividual; " +
                                "rdf:type :Phones; " +
                                ":ad_name \""+ad_name+"\"; " +
                                ":site \""+site+"\"; " +
                                ":price \""+price+"\"; " +
                                ":date_posted \""+date_posted+"\"; " +
                                ":2nd_or_brandnew \""+condition+"\"; " +
                                ":location \""+location+"\"; " +
                                ":posted_by  \""+posted_by+"\"; " +
                                ":description \""+description+"\" " +
                                "} 		";
              
                 

                UpdateRequest update  = UpdateFactory.create(query);
                UpdateProcessor qexec = UpdateExecutionFactory.createRemote(update, "http://localhost:3030/ds/update");
                qexec.execute();
                
                JOptionPane.showMessageDialog(null,ad_name+"\n"+site+"\n"+price+"\n"+date_posted+"\n"+condition+"\n"+location+"\n"+posted_by+"\n"+description);
                i++;
                
            }
            }
            catch(HeadlessException | SQLException err){
            JOptionPane.showMessageDialog(null,err);
        }
  }
  
 
}
