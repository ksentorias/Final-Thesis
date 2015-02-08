/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package thesis;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.DatasetAccessor;
import com.hp.hpl.jena.query.DatasetAccessorFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateProcessor;
import com.hp.hpl.jena.update.UpdateRequest;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import java.awt.HeadlessException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import javax.swing.JOptionPane;
import static thesis.Test.conn;

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
      //ads();
      getBrands();
 }
  
  public static void ads(){
      
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
//  
  public static void specs(){
  
       
     
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
            sql = "select * from `table 2`";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            
            while(rs.next()){
                
                String brand = rs.getString("brand");
                String model = rs.getString("model");
                String network_technology = rs.getString("network_technology");
                String body_dimensions = rs.getString("body_dimensions");
                String body_weight = rs.getString("body_weight");
                String sim = rs.getString("sim");
                String display_type  = rs.getString("display_type");
                String display_size = rs.getString("display_size");
                String display_resolution = rs.getString("display_resolution");
                String multitouch = rs.getString("multitouch");
                String os = rs.getString("os");
                String chipset = rs.getString("chipset");
                String cpu = rs.getString("cpu");
                String gpu = rs.getString("gpu");
                String memory_card_slot  = rs.getString("memory_card_slot");
                String memory_internal = rs.getString("memory_internal");
                String camera_primary = rs.getString("camera_primary");
                String camera_features = rs.getString("camera_features");
                String camera_video = rs.getString("camera_video");
                String camera_secondary = rs.getString("camera_secondary");
                String alert_types = rs.getString("alert_types");
                String loudspeaker = rs.getString("loudspeaker");
                String three_point_five_mm_jack  = rs.getString("three_dot_five_mm_jack");
                String wlan = rs.getString("wlan");
                String bluetooth = rs.getString("bluetooth");
                String gps = rs.getString("gps");
                String radio = rs.getString("radio");
                String usb = rs.getString("usb");
                String sensors = rs.getString("sensors");
                String messaging = rs.getString("messaging");
                String battery = rs.getString("battery");
                String color = rs.getString("color");
                
              //  JOptionPane.showMessageDialog(null,brand+"\n"+model+"\n"+technology+"\n"+body_dimensions+"\n"+body_weight+"\n"+sim+"\n"+display_type+"\n"+display_size);
                
                
                String query = "PREFIX :<http://xu.edu.ph/ecommerce#> " +
                                "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                                "PREFIX owl:<http://www.w3.org/2002/07/owl#> " +
                                "PREFIX xsd:<http://www.w3.org/2001/XMLSchema#> " +
                                "  " +
                                "INSERT DATA  " +
                                "{ " +
                                ":model_"+i+" rdf:type owl:NamedIndividual; " +
                                "rdf:type :Model; " +
                                ":brand \""+brand+"\"; " +
                                ":model \""+model+"\"; " +
                                ":network_technology \""+network_technology+"\"; " +
                                ":dimensions \""+body_dimensions+"\"; " +
                                ":weight \""+body_weight+"\"; " +
                                ":sim \""+sim+"\"; " +
                                ":display_type  \""+display_type+"\"; " +
                                ":display_size \""+display_size+"\"; " +
                                ":display_resolution \""+display_resolution+"\"; " +
                                ":multi_touch \""+multitouch+"\"; " +
                                ":os \""+os+"\"; " +
                                ":chipset \""+chipset+"\"; " +
                                ":cpu \""+cpu+"\"; " +
                                ":gpu \""+gpu+"\"; " +
                                ":memory_card_slot  \""+memory_card_slot+"\"; " +
                                ":memory_internal \""+memory_internal+"\"; " +
                                ":camera_primary \""+camera_primary+"\"; " +
                                ":camera_features  \""+camera_features+"\"; " +
                                ":camera_video \""+camera_video+"\"; " +
                                ":camera_secondary \""+camera_secondary+"\"; " +
                                ":alert_types \""+alert_types+"\"; " +
                                ":loud_speaker \""+loudspeaker+"\"; " +
                                ":three_point_five_mm_jack \""+three_point_five_mm_jack+"\"; " +
                                ":wlan \""+wlan+"\"; " +
                                ":bluetooth \""+bluetooth+"\"; " +
                                ":gps  \""+gps+"\"; " +
                                ":radio \""+radio+"\"; " +
                                ":usb  \""+usb+"\"; " +
                                ":sensors \""+sensors+"\"; " +
                                ":messaging \""+messaging+"\"; " +
                                ":battery \""+battery+"\"; " +
                                ":colors \""+color+"\"; " +
                                "} 		";
                 
                 
              
                 

                UpdateRequest update  = UpdateFactory.create(query);
                UpdateProcessor qexec = UpdateExecutionFactory.createRemote(update, "http://localhost:3030/ds/update");
                qexec.execute();
                i++;
                                

            }
      }
       catch(SQLException | HeadlessException err){
          JOptionPane.showMessageDialog(null,err);
       
       }
       DatasetAccessor accessor = DatasetAccessorFactory.createHTTP("http://localhost:3030/ds/data");

       // Download the updated model
       Model updated = accessor.getModel();

       // Save the updated model over the original file
       try {
                updated.write(new FileOutputStream("C:\\Users\\test\\Documents\\test owl\\output.owl"), "RDF/XML");
                 System.out.print("success!");
            } catch (FileNotFoundException fileNotFoundException) {
                System.out.print(fileNotFoundException);
            }
  }
  
  public static String[] getBrands(){
      String[] brands = new String[50];
      int i = 0;
      
      OntModel model = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM_MICRO_RULE_INF);
      String inputFileName="C:\\Users\\test\\Documents\\test owl\\ecommerce.owl";
      String ns = "http://xu.edu.ph/ecommerce#";
      InputStream in = FileManager.get().open( inputFileName );
      if (in == null) {
      throw new IllegalArgumentException(
      "File: " + inputFileName + " not found");
      }
      model.read(in, null);
   
          OntClass brand = model.getOntClass(ns+"Brand");
   
          ExtendedIterator instances = brand.listInstances();

          while (instances.hasNext())
          {
            Individual thisInstance = (Individual) instances.next();
            
            if("Brand".equals(thisInstance.getOntClass().getLocalName())){
            //JOptionPane.showMessageDialog(null, thisInstance.getLocalName()+ "\n" +thisInstance.getOntClass().toString());
            brands[i] = thisInstance.getLocalName();
            }
            
            i++;
          }
          i = 0;
         
          
          System.out.print(Arrays.toString(brands));


    return brands; 
  }
  
}
