/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package thesis;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.DatasetAccessor;
import com.hp.hpl.jena.query.DatasetAccessorFactory;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateProcessor;
import com.hp.hpl.jena.update.UpdateRequest;
import com.hp.hpl.jena.util.FileManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.HeadlessException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/**
 *
 * @author test
 */
public class Ontology_System extends javax.swing.JFrame {

    Connection conn = null;
    ResultSet rs  = null;
    PreparedStatement pst = null;
   public static int ID = 0;
   DefaultTableModel model;
   String ns = "http://xu.edu.ph/ecommerce#";
   static String sql = ""; 
   private View_full_details view;
   
   GetData getdata;
 
   
    Ontology_System() {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        
     // ads();
       // index();
     // setSpecsOntology();
      //getModels("samsung");
     // getBrands();
      //getSpecs("mi_4");
      //getModelfromAd("Solar Outdoor Rugged Powerbank & Solar Outdoor Gadgets lumia"); 
        
   //    populate_table();
       testbrands();
    }
    
     public void testbrands(){
          
          
          List b = getBrands();
          List m;
          
          for (Object object : b) {
              
              m = getModels(object.toString());
              
              System.out.println("************************\n"+object.toString());
              
              for (Object object1 : m) {
                  System.out.println(object1);
                  
              }
             
         }
    
       
  }
   
    public OntModel loadModel(){
    
      OntModel model = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM_MICRO_RULE_INF);
      String inputFileName="C:\\Users\\test\\Documents\\test owl\\output.owl";
      InputStream in = FileManager.get().open( inputFileName );
      if (in == null) {
      throw new IllegalArgumentException(
      "File: " + inputFileName + " not found");
      }
      model.read(in, null);
      
      return model;
    }
    
    public Connection getConnectiontoDB(){
    
         //<editor-fold defaultstate="collapsed" desc="variables">
        String user = "root";
        String pass = "";
        String host = "127.0.0.1";
        String db = "ontology_system";
//</editor-fold>
      
        //<editor-fold defaultstate="collapsed" desc="db connection">
        
        try {
            System.out.println("connecting to db...");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://" + host + "/" + db + "",
                    "" + user + "",
                    "" + pass + "");
            
            System.out.println("successfull connection...");

          
          
      } catch (SQLException sQLException) {
          JOptionPane.showMessageDialog(null, sQLException);
      }
//</editor-fold>
        
        return conn;
    }
    
    public Model loadModelfromServer(){
    
        DatasetAccessor accessor = DatasetAccessorFactory.createHTTP("http://localhost:3030/ds/data");
        
        Model loadedModel =  accessor.getModel();
        
        return loadedModel;
    }
   
    public void index(){
        
          //<editor-fold defaultstate="collapsed" desc="rj logger">
        Logger rootLogger = Logger.getRootLogger();
        rootLogger.setLevel(Level.INFO);
        rootLogger.addAppender(new ConsoleAppender(new PatternLayout("%-6r [%p] %c - %m%n")));
        
//</editor-fold>
        
          //<editor-fold defaultstate="collapsed" desc="variables">
          boolean haveBrand;
          boolean haveModel;
          boolean haveModelfromToken;
          
          List<String[]> newQuery = new ArrayList();
          List<String[]> products = ads();
          List BrandsfromOntology = getBrands();
          List modelsfromOntology;
          //</editor-fold>
           System.out.println("starting...");
           
          for (String[] product : products){
              int i = 0;
              haveBrand = false;
              haveModel = false;
              haveModelfromToken = false;
 
              String[] productWithfullAttribute = new String[45];
              
          //<editor-fold defaultstate="collapsed" desc="add ad details to new query">
              for (Object productSpecs : product) {
                  
                  productWithfullAttribute[i] = StringEscapeUtils.escapeSql( productSpecs.toString()).replaceAll("\"","&quote;");
                  
                  
                  if (i==2) { productWithfullAttribute[i] = "Php " + productSpecs.toString().replaceAll("\\s+","").replaceAll("\\?", "").replaceAll("[^\\d.]", "");
                  
                  }
                  
                  i++;
              }
//</editor-fold>
         
          //<editor-fold defaultstate="collapsed" desc="check brand and model then get specs">
                for(Object brand: BrandsfromOntology ){
                System.out.println("is \""+product[0].toLowerCase()+"\" contains brand: \""+brand.toString()+"\"");
                if(!product[0].toLowerCase().contains(brand.toString().toLowerCase())) {
                } else {
                    
                    //if have brand add it to table that he is +brand+
                    productWithfullAttribute[43] = brand.toString();
                    
                    System.out.println("yes");
                    haveBrand = true;
                    
                    //initialize with matched brand
                 //   JOptionPane.showMessageDialog(null,"Brand found: "+ brand +" from ad: "+product[0]);
                    
                    modelsfromOntology  = getModels(brand.toString());
                    for(Object model: modelsfromOntology){
                        
                        //<editor-fold defaultstate="collapsed" desc="check model and get specs">
                        if (!haveModel) {
                            
                             //if have already a model list all the models found and store it to model_others
                            productWithfullAttribute[44] = productWithfullAttribute[44] +"\n"+ model.toString();
                            
                            //logs
                            System.out.println("is ad \""+product[0].toLowerCase().replaceAll("[^a-zA-Z0-9]+"," ")+"\" contains model: \"" +model.toString().toLowerCase().replaceAll("[^a-zA-Z0-9]+"," ")+"\"");
                            
                            if(product[0].toLowerCase().replaceAll("[^a-zA-Z0-9]+"," ").contains(model.toString().toLowerCase().replaceAll("[^a-zA-Z0-9]+"," "))){
                                
                                System.out.println("yes");
                                haveModel = true;
                                
                                //<editor-fold defaultstate="collapsed" desc="if have already a found specific brand and model truncate the data in thes columns">
                                productWithfullAttribute[43] = "";
                                productWithfullAttribute[44] = "";
                                //</editor-fold>
                                
                                //add specifications to the list
                                for (String specifications : getSpecs(model.toString().replaceAll("\\s+","_").toLowerCase().replaceAll("[^a-zA-Z0-9]+","_"))) {
                                    productWithfullAttribute[i] = specifications;
                                    //  JOptionPane.showMessageDialog(null,"specifications "+i+" = " + specifications);
                                    i++;
                                }
                                
                        //        JOptionPane.showMessageDialog(null, "yes!\n"+product[0].toLowerCase()+"\n"+model);
                               
                                
                                /*for (Object object : productWithfullAttribute) {
                                
                                System.out.println("object = " + object);
                                
                                }*/
                                
                            }
                            
                        }
//</editor-fold>
                        
                    }
                }
            }
                System.out.println("end of brands");
//</editor-fold>
             
            //temp var for model iff more than 1 model
            String modelFound = "";
          //<editor-fold defaultstate="collapsed" desc="if brand is not found in ad String try in tokenizing and compare to list of models">
            if (!haveBrand) {
                for (Object modelFoundfromTokens : getModelfromAd(product[0])) {
                    modelFound  =  modelFound +"\n"+ modelFoundfromTokens.toString();
                    haveModelfromToken = true;
                }
            }
//</editor-fold>

            //if tokenizing is success store it in index 44 which is "model_found"
            if (haveModelfromToken) productWithfullAttribute[44] = modelFound;
            
            //counter for the next statement
            int j = 0;
            
            //<editor-fold defaultstate="collapsed" desc="final lap, if product have empty of null value store "record not found" ">
            
           
            
            for (String string : productWithfullAttribute) {
            if (string == null || "".equals(string)) productWithfullAttribute[j] = " ";
            j++;
            }
            
               
               
            
//</editor-fold>
            
            //in the end, store all products in a new query to be store in db
            newQuery.add(productWithfullAttribute);
            
            }
          
          System.out.println("end of products...");
          
          for (String[] object : newQuery) {
          
          insertFinaldataToDB(object);
          
          }
   }
    
    public List<String[]> ads(){
         //<editor-fold defaultstate="collapsed" desc="rj logger">
        Logger rootLogger = Logger.getRootLogger();
        rootLogger.setLevel(Level.INFO);
        rootLogger.addAppender(new ConsoleAppender(new PatternLayout("%-6r [%p] %c - %m%n")));
        
//</editor-fold>
        System.out.println("getting ads list...");

       // List<List<String>> ads = new ArrayList<>();
        List<String[]> ads = new ArrayList<>();
            
        //<editor-fold defaultstate="collapsed" desc="variables">
        String user = "root";
        String pass = "";
        String host = "127.0.0.1";
        String db = "ontology_system";
        int i = 0;
//</editor-fold>
      
        //<editor-fold defaultstate="collapsed" desc="db connection">
        
        try {
            System.out.println("connecting to db...");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://" + host + "/" + db + "",
                    "" + user + "",
                    "" + pass + "");
            
            System.out.println("successfull connection...");

          
          
      } catch (SQLException sQLException) {
          JOptionPane.showMessageDialog(null, sQLException);
      }
//</editor-fold>
      
      try{
          System.out.println("query'ing ads...");
            sql = "select * from `ads`";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            
            while(rs.next()){
                
                String ad_name = rs.getString("ad_name");
                String site = rs.getString("site");
                String price = rs.getString("price");
                String location = rs.getString("location");
                String posted_by = rs.getString("posted_by");
                String description = rs.getString("description");
                String image = rs.getString("image");
                String link = rs.getString("link");
                
                 String querya = "PREFIX :<http://xu.edu.ph/ecommerce#>\n" +
                                "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                                "PREFIX owl:<http://www.w3.org/2002/07/owl#>\n" +
                                "PREFIX xsd:<http://www.w3.org/2001/XMLSchema#>\n" +
                                "\n" +
                                "INSERT DATA\n" +
                                "{\n" +
                                ":ad_"+i+" rdf:type owl:NamedIndividual;\n" +
                                "rdf:type :Products;\n" +
                                ":offers :"+site.trim().toLowerCase()+";\n" +
                                ":ad_name \""+ad_name.replaceAll("[^a-zA-Z0-9]+","_")+"\"; " +
                                ":price \""+price.replaceAll("[^a-zA-Z0-9]+","_")+"\"; " +
                                ":location \""+location.replaceAll("[^a-zA-Z0-9]+","_")+"\"; " +
                                ":posted_by \""+posted_by.replaceAll("[^a-zA-Z0-9]+","_")+"\"; " +
                                ":image \""+image.replaceAll("[^a-zA-Z0-9]+","_")+"\"; " +
                                ":link \""+link.replaceAll("[^a-zA-Z0-9]+","_")+"\"; " +
                                ":description  \""+description.replaceAll("[^a-zA-Z0-9]+","_")+"\"; " +
                                "}";
                 
                try {
                    UpdateRequest update = UpdateFactory.create(querya);
                    UpdateProcessor qexec = UpdateExecutionFactory.createRemote(update, "http://localhost:3030/ds/update");
                    qexec.execute();
                } catch (Exception e) {
                    System.err.println("insert sparql ads = " + e);
                }
                
                //<editor-fold defaultstate="collapsed" desc="from --mem /ds to output.owl">
                DatasetAccessor accessor = DatasetAccessorFactory.createHTTP("http://localhost:3030/ds/data");
                
                // Download the updated model
                Model updated = accessor.getModel();
                
                // Save the updated model over the original file
                try {
                    updated.write(new FileOutputStream("C:\\Users\\test\\Documents\\test owl\\with ads.owl"), "RDF/XML");
                    System.out.println("success!");
                } catch (FileNotFoundException fileNotFoundException) {
                    System.out.println(fileNotFoundException);
                }
//</editor-fold>
                 
                //List ad_specs = new ArrayList();//inner List for specs
                
                String[] ad_specs = new String[10];
                
                ad_specs[0] = ad_name;
                ad_specs[1] = site;
                ad_specs[2] = price;
                ad_specs[3] = "";
                ad_specs[4] = "";
                ad_specs[5] = location;
                ad_specs[6] = posted_by;
                ad_specs[7] = description;
                ad_specs[8] = image;
                ad_specs[9] = link;
            
                
              
                ads.add(ad_specs); //add the inner list to ads List
               // ads.add(ad_specs); 
                i++;
                
            }
            
            System.out.println("ads successfully initialized...");
            }
            catch(HeadlessException | SQLException err){
            JOptionPane.showMessageDialog(null,err);
        }
      
      /* for (List<String> list : ads) {
      
      System.out.println(list);
      
      }
      */
      
      System.out.println("ads returned...");
      return ads;
      
      
      
  }
  
    public void setSpecsOntology(){
  
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
            sql = "select * from `specifications`";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            
            while(rs.next()){
                
                String brand = rs.getString("brand").replaceAll("\"","&quote;");
                String model = rs.getString("model").replaceAll("\"","&quote;");
                String network_technology = rs.getString("network_technology").replaceAll("\"","&quote;");
                String body_dimensions = rs.getString("body_dimensions").replaceAll("\"","&quote;");
                String body_weight = rs.getString("body_weight").replaceAll("\"","&quote;");
                String sim = rs.getString("sim").replaceAll("\"","&quote;");
                String display_type  = rs.getString("display_type").replaceAll("\"","&quote;");
                String display_size = rs.getString("display_size").replaceAll("\"","&quote;");
                String display_resolution = rs.getString("display_resolution").replaceAll("\"","&quote;");
                String multitouch = rs.getString("multitouch").replaceAll("\"","&quote;");
                String os = rs.getString("os").replaceAll("\"","&quote;");
                String chipset = rs.getString("chipset").replaceAll("\"","&quote;");
                String cpu = rs.getString("cpu").replaceAll("\"","&quote;");
                String gpu = rs.getString("gpu").replaceAll("\"","&quote;");
                String memory_card_slot  = rs.getString("memory_card_slot").replaceAll("\"","&quote;");
                String memory_internal = rs.getString("memory_internal").replaceAll("\"","&quote;");
                String camera_primary = rs.getString("camera_primary").replaceAll("\"","&quote;");
                String camera_features = rs.getString("camera_features").replaceAll("\"","&quote;");
                String camera_video = rs.getString("camera_video").replaceAll("\"","&quote;");
                String camera_secondary = rs.getString("camera_secondary").replaceAll("\"","&quote;");
                String alert_types = rs.getString("alert_types").replaceAll("\"","&quote;");
                String loudspeaker = rs.getString("loudspeaker").replaceAll("\"","&quote;");
                String three_point_five_mm_jack  = rs.getString("three_dot_five_mm_jack").replaceAll("\"","&quote;");
                String wlan = rs.getString("wlan").replaceAll("\"","&quote;");
                String bluetooth = rs.getString("bluetooth").replaceAll("\"","&quote;");
                String gps = rs.getString("gps").replaceAll("\"","&quote;");
                String radio = rs.getString("radio").replaceAll("\"","&quote;");
                String usb = rs.getString("usb").replaceAll("\"","&quote;");
                String sensors = rs.getString("sensors").replaceAll("\"","&quote;");
                String messaging = rs.getString("messaging").replaceAll("\"","&quote;");
                String battery = rs.getString("battery").replaceAll("\"","&quote;");
                String color = rs.getString("color").replaceAll("\"","&quote;");
                String link = rs.getString("_pageUrl").replaceAll("\"","&quote;");
                String image = rs.getString("image_specs").replaceAll("\"","&quote;");
                
              //  JOptionPane.showMessageDialog(null,brand+"\n"+model+"\n"+technology+"\n"+body_dimensions+"\n"+body_weight+"\n"+sim+"\n"+display_type+"\n"+display_size);
                System.out.println("*******"+model.replaceAll("\\s+","_").toLowerCase()+"********");
              //   JOptionPane.showMessageDialog(null,"\""+color+"\""+"\n"+model.replaceAll("\\s+","_").toLowerCase());
                 
                
                String query = "PREFIX :<http://xu.edu.ph/ecommerce#>\n" +
                                "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                                "PREFIX owl:<http://www.w3.org/2002/07/owl#>\n" +
                                "PREFIX xsd:<http://www.w3.org/2001/XMLSchema#>\n" +
                                "\n" +
                                "INSERT DATA\n" +
                                "{\n" +
                                ":"+model.replaceAll("\\s+","_").toLowerCase().replaceAll("[^a-zA-Z0-9]+","_")+" rdf:type owl:NamedIndividual;\n" +
                                "rdf:type :Model;\n" +
                                ":isaModelof :"+brand.trim().toLowerCase()+";\n" +
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
                                ":link \""+link+"\"; " +
                                ":image \""+image+"\"; " +
                               // ":battery \""+color+"\"; " +
                              //  ":colors \""+color.toLowerCase().replaceAll("[^a-zA-Z0-9]+","_")+"\" " +
                                 "}";
                 
                 
              
                 
                try {
                    
                    UpdateRequest update = UpdateFactory.create(query);
                    UpdateProcessor qexec = UpdateExecutionFactory.createRemote(update, "http://localhost:3030/ds/update");
                    qexec.execute();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,e + "\n" + ":os \""+os+"\"; ");
                }
                i++;
                                

            }
      }
       catch(SQLException | HeadlessException err){
          JOptionPane.showMessageDialog(null,err);
       
       }
      
      //<editor-fold defaultstate="collapsed" desc="from --mem to output.owl">
      DatasetAccessor accessor = DatasetAccessorFactory.createHTTP("http://localhost:3030/ds/data");
      
      // Download the updated model
      Model updated = accessor.getModel();
      
      // Save the updated model over the original file
      try {
          updated.write(new FileOutputStream("C:\\Users\\test\\Documents\\test owl\\output.owl"), "RDF/XML");
          System.out.println("success!");
      } catch (FileNotFoundException fileNotFoundException) {
          System.out.println(fileNotFoundException);
      }
//</editor-fold>
  }
  
    public List getBrands(){
          Logger rootLogger = Logger.getRootLogger();
          rootLogger.setLevel(Level.INFO);
          rootLogger.addAppender(new ConsoleAppender(new PatternLayout("%-6r [%p] %c - %m%n")));
          
        
        List <String> brands = new ArrayList();
     
        
        String a = "";
        
         Model model = loadModelfromServer();
      
      String query =
                    "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "prefix : <http://xu.edu.ph/ecommerce#>\n" +
                    "\n" +
                    "SELECT DISTINCT ?brands \n" +
                    "WHERE{\n" +
                    "?brands a :Brand\n" +
                    "}";
      
      QueryExecution exec = QueryExecutionFactory.create( query, model );
                     com.hp.hpl.jena.query.ResultSet rs = exec.execSelect();
                    while ( rs.hasNext() ) {
                     QuerySolution qs = rs.next();
                     
                     a = a + "\n" + "*******"+qs.get("brands")+"********";
                     brands.add(qs.get("brands").toString().substring(qs.get("brands").toString().indexOf("#")+1));
                 
                    }
                    
               //    
      
      //  brands = new String[] {"samsung","xolo","xiaomi","lg","zte","yezz","blu","toshiba","htc","niu","micromax","pantech","blackberry","motorola","vodafone","celkon","lava","lenovo","maxwest","gigabyte","vivo","verykool","pretigio","acer","nokia","microsoft","spice","plum","sony","gionee","apple","huawei","alcatel","asus","parla"};
       
   
    return brands;
    
       
  }
    
    public List getModels(String brand){
        
         Logger rootLogger = Logger.getRootLogger();
          rootLogger.setLevel(Level.INFO);
          rootLogger.addAppender(new ConsoleAppender(new PatternLayout("%-6r [%p] %c - %m%n")));
         
      List <String> models = new ArrayList();
      int i = 0;
        String a = "";
        
       
        Model model = loadModelfromServer();
                
                    String query =
                    "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                    "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    "prefix : <http://xu.edu.ph/ecommerce#>\n" +
                    "\n" +
                    "\n" +
                    "SELECT ?model\n" +
                    "WHERE {\n" +
                    "   ?model :isaModelof :"+brand.toLowerCase()+"\n" +
                    "}";


                     QueryExecution exec = QueryExecutionFactory.create( query, model );
                     com.hp.hpl.jena.query.ResultSet rs = exec.execSelect();
                    while ( rs.hasNext() ) {
                     QuerySolution qs = rs.next();
                     
                        a = a + "\n" + "*******"+qs.get("model")+"********";
                     
                     String mquery =
                    "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                    "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    "prefix : <http://xu.edu.ph/ecommerce#>\n" +
                    "\n" +
                    "\n" +
                    "SELECT ?model_of_data_value\n" +
                    "WHERE {\n" +
                    "   :"+ qs.get("model").toString().substring(qs.get("model").toString().indexOf("#")+1)+" :model ?model_of_data_value\n" +
                    "}";


                     QueryExecution mexec = QueryExecutionFactory.create( mquery, model );
                     com.hp.hpl.jena.query.ResultSet mrs = mexec.execSelect();
                    while ( mrs.hasNext() ) {
                     QuerySolution mqs = mrs.next();
                   // JOptionPane.showMessageDialog(null, qs.get("model").toString().substring(qs.get("model").toString().indexOf("#")+1) + "\n" +mqs.get( "model_of_data_value" ));
                    
                   models.add(mqs.get( "model_of_data_value" ).toString());
                    i++;
                    }
               
               
               }
        //           
                    //<editor-fold defaultstate="collapsed" desc="check model values">
                    /* int j = 0;
                    while(models[j]!=null){
                    
            
                    
                    j++;
                    }*/
//</editor-fold>
                    
    
                    return models;
    }
    
    public List getModelfromAd(String ad){
        //<editor-fold defaultstate="collapsed" desc="logger">
        Logger rootLogger = Logger.getRootLogger();
        rootLogger.setLevel(Level.INFO);
        rootLogger.addAppender(new ConsoleAppender(new PatternLayout("%-6r [%p] %c - %m%n")));
//</editor-fold>
        
        List <String> modelSelected = new ArrayList();
        boolean gotsomething = false;
        
        Model model = loadModelfromServer();
                
 
        String query =
                    "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                    "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    "prefix : <http://xu.edu.ph/ecommerce#>\n" +
                    "\n" +
                    "SELECT DISTINCT ?model\n" +
                    "WHERE{\n" +
                    "?model a :Model\n" +
                    "}";


                     QueryExecution exec = QueryExecutionFactory.create( query, model );
                     com.hp.hpl.jena.query.ResultSet rs = exec.execSelect();
                     while ( rs.hasNext() ) {
                     QuerySolution qs = rs.next();
                     
                     
                     
                     String mquery =
                    "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                    "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    "prefix : <http://xu.edu.ph/ecommerce#>\n" +
                    "\n" +
                    "\n" +
                    "SELECT ?model_of_data_value\n" +
                    "WHERE {\n" +
                    "   :"+ qs.get("model").toString().substring(qs.get("model").toString().indexOf("#")+1)+" :model ?model_of_data_value\n" +
                    "}";


                     QueryExecution mexec = QueryExecutionFactory.create( mquery, model );
                     com.hp.hpl.jena.query.ResultSet mrs = mexec.execSelect();
                    while ( mrs.hasNext() ) {
                     QuerySolution mqs = mrs.next();
                     
                     for (StringTokenizer stringTokenizer = new StringTokenizer(ad); stringTokenizer.hasMoreTokens();) {
                        String adTokenized = stringTokenizer.nextToken();
                        
                        //JOptionPane.showMessageDialog(null, ad +"\n"+adTokenized+"\n");
                        
                         for (StringTokenizer stringTokenizer1 = new StringTokenizer(mqs.get("model_of_data_value").toString()); stringTokenizer1.hasMoreTokens();) {
                             String modelTokenized = stringTokenizer1.nextToken();
                             
            //  JOptionPane.(null, ad +"\n"+adTokenized+"\n"+modelTokenized);
                             
                             if (modelTokenized.toLowerCase().equals(adTokenized.toLowerCase())) {
                                 
                                //JOptionPane.showMessageDialog(null, ad +"\n"+adTokenized+"\n"+modelTokenized+"\n"+mqs.get("model_of_data_value").toString());
                                modelSelected.add(mqs.get("model_of_data_value").toString());
                                
                                gotsomething = true;
                                break;
                        }
                             if (gotsomething)  break;
                         }
                         
                         if (gotsomething)  break; 
                    }
                      if (gotsomething)  break;
                   
                    }
                     }
                     
                     
                     /*JOptionPane.showMessageDialog(null,modelSelected);
                     
                     
                     
                     if (gotsomething) {
                     System.out.println("we got something!");
                     } else {
                     System.out.println("nothing");
                     }*/
        
        
        
        return modelSelected;
        
    }
    
    public String[] getSpecs(String modelfound){
        
         Logger rootLogger = Logger.getRootLogger();
          rootLogger.setLevel(Level.INFO);
          rootLogger.addAppender(new ConsoleAppender(new PatternLayout("%-6r [%p] %c - %m%n")));
          
          System.out.println("extracting specification data from the selected model \""+ modelfound.toUpperCase()+"\"...");
         
        String[] specs = new String[33];
        int i = 0;
        
       Model model = loadModelfromServer();
                
                     String mquery =
                             //<editor-fold defaultstate="collapsed" desc="sparql query to get data properties">
                             "prefix : <http://xu.edu.ph/ecommerce#>\n" +
                             "\n" +
                             "\n" +
                             "SELECT ?brand ?model ?alert_types ?battery ?bluetooth ?camera_features ?camera_primary ?camera_secondary ?camera_video ?chipset ?colors ?cpu ?dimensions \n" +
                             "?display_resolution ?display_size ?display_type ?gps ?gpu ?image ?loud_speaker ?memory_card_slot ?memory_internal ?messaging ?multi_touch ?network_technology \n" +
                             "?os ?radio ?sensors ?sim ?three_point_five_mm_jack ?usb ?weight ?wlan ?link ?image\n" +
                             "WHERE {\n" +
                             "    :"+modelfound+"  :brand ?brand ;\n" +
                             "         :model ?model ;\n" +
                             "         :alert_types ?alert_types ;\n" +
                             "         :battery ?battery ;\n" +
                             "         :bluetooth ?bluetooth  ;\n" +
                             "         :camera_features ?camera_features  ;\n" +
                             "         :camera_primary ?camera_primary  ;\n" +
                             "         :camera_secondary ?camera_secondary ;\n" +
                             "         :camera_video ?camera_video  ;\n" +
                             "         :chipset ?chipset  ;\n" +
                             "         :cpu ?cpu  ;\n" +
                             "         :dimensions ?dimensions  ;\n" +
                             "         :display_resolution ?display_resolution  ;\n" +
                             "         :display_size ?display_size  ;\n" +
                             "         :display_type ?display_type  ;\n" +
                             "         :gps ?gps ;\n" +
                             "         :gpu  ?gpu    ;      \n" +
                             "         :loud_speaker ?loud_speaker ;\n" +
                             "         :memory_card_slot ?memory_card_slot  ;\n" +
                             "         :memory_internal ?memory_internal  ;\n" +
                             "         :messaging ?messaging  ;\n" +
                             "         :multi_touch ?multi_touch  ;\n" +
                             "         :network_technology ?network_technology ;\n" +
                             "         :os ?os  ;\n" +
                             "         :radio ?radio   ;\n" +
                             "         :sensors ?sensors  ;\n" +
                             "         :sim ?sim   ;\n" +
                             "         :three_point_five_mm_jack ?three_point_five_mm_jack ;\n" +
                             "         :usb ?usb  ;\n" +
                             "         :weight ?weight  ;\n" +
                             "         :wlan ?wlan  ;\n" +
                             "         :link ?link  ;\n" +
                             "         :image ?image  ;\n" +
                             "}";
//</editor-fold>
  
                     QueryExecution mexec = QueryExecutionFactory.create( mquery, model );
                     com.hp.hpl.jena.query.ResultSet mrs = mexec.execSelect();
                     
                     if(mrs.hasNext()){
                            QuerySolution mqs = mrs.next();
                            //<editor-fold defaultstate="collapsed" desc="specs[] to specs value">
                            
                            specs[0] = mqs.get("brand").toString();
                            specs[1] = mqs.get("model").toString();
                            specs[2] = mqs.get("alert_types").toString();
                            specs[3] = mqs.get("battery").toString();
                            specs[4] = mqs.get("bluetooth").toString();
                            specs[5] = mqs.get("camera_features").toString();
                            specs[6] = mqs.get("camera_primary").toString();
                            specs[7] = mqs.get("camera_secondary").toString();
                            specs[8] = mqs.get("camera_video").toString();
                            specs[9] = mqs.get("chipset").toString();
                            specs[10] = mqs.get("cpu").toString();
                            specs[11] = mqs.get("dimensions").toString();
                            specs[12] = mqs.get("display_resolution").toString();
                            specs[13] = mqs.get("display_size").toString();
                            specs[14] = mqs.get("display_type").toString();
                            specs[15] = mqs.get("gps").toString();
                            specs[16] = mqs.get("gpu").toString();
                            specs[17] = mqs.get("loud_speaker").toString();
                            specs[18] = mqs.get("memory_card_slot").toString();
                            specs[19] = mqs.get("memory_internal").toString();
                            specs[20] = mqs.get("messaging").toString();
                            specs[21] = mqs.get("multi_touch").toString();
                            specs[22] = mqs.get("network_technology").toString();
                            specs[23] = mqs.get("os").toString();
                            specs[24] = mqs.get("radio").toString();
                            specs[25] = mqs.get("sensors").toString();
                            specs[26] = mqs.get("sim").toString();
                            specs[27] = mqs.get("three_point_five_mm_jack").toString();
                            specs[28] = mqs.get("usb").toString();
                            specs[29] = mqs.get("weight").toString();
                            specs[30] = mqs.get("wlan").toString();
                            specs[31] = mqs.get("link").toString();
                            specs[32] = mqs.get("image").toString();
                            
//</editor-fold>
                        } 
                     
                    
    
                    return specs;
    }
    
    public void insertFinaldataToDB(String [] finalQuery){
        Connection conn = getConnectiontoDB();
        String eachData = "";
        
        int i = 0;
            for (String string : finalQuery) {
                eachData = eachData + "\n"+ "\""+finalQuery[i]+"\",";
                i++;
            }
            
            //System.out.println("\n\n\n"+ eachData.substring(0, eachData.length()-1));

            try{
            sql = "INSERT INTO ontology_system_final_table ( `ad_name`, `site`, `price`, `date_posted`, `2nd_or_brandnew`, `location`, \n" +
"                 `posted_by`, `description`, `image`, `link`, `brand`, \n" +
"                 `model`, `alert_types`, `battery`, `bluetooth`, `camera_features`, \n" +
"                 `camera_primary`, `camera_secondary`, `camera_video`, `chipset`, `cpu`, `body_dimensions`, \n" +
"                 `display_resolution`, `display_size`, `display_type`, `gps`, `gpu`, `loudspeaker`, \n" +
"                 `memory_card_slot`, `memory_internal`, `messaging`, `multitouch`, `network_technology`, `os`, \n" +
"                `radio`, `sensors`, `sim`, `three_dot_five_mm_jack`, `usb`, `body_weight`, \n" +
"                 `wlan`, `specs_link`, `image_specs`, `brand_others`, `model_others` ) VALUES ("+eachData.substring(0, eachData.length()-1)+");";

           pst = conn.prepareStatement(sql);
            pst.executeUpdate(sql);
            
            
            
            }
        catch(Exception err){
                    JOptionPane.showMessageDialog(null,"set_new_to_db: "+ err);
                }
        
        
        
        
    }
    
    public void populate_table(){
        
        model  = (DefaultTableModel) ad_table.getModel();
       // table_overview.getColumnModel().getColumn(1).setCellRenderer(new TableCellLongTextRenderer ());
        ad_table.getTableHeader().setFont(new java.awt.Font("Segoe UI", Font.BOLD, 14));
        ad_table.getTableHeader().setBackground(Color.WHITE);
        ad_table.setShowGrid(true);
        
       try{ 
        sql = "select * from ontology_system_final_table";
        pst = getConnectiontoDB().prepareStatement(sql);
        rs = pst.executeQuery();
        
        while(rs.next()){
            
            model.addRow(new Object[]{rs.getString("ad_name"), rs.getString("site"), rs.getString("brand"), rs.getString("model"),rs.getString("price"), rs.getString("chipset"), rs.getString("cpu"), rs.getString("camera_primary"),rs.getString("memory_internal")});
        
        }     
       }
       catch(SQLException err){
           System.err.println(err);
       }
    }
    
    public void populate_search(){
        DefaultTableModel model  = (DefaultTableModel) ad_table.getModel();
       // table_overview.getColumnModel().getColumn(1).setCellRenderer(new TableCellLongTextRenderer ());
        ad_table.getTableHeader().setFont(new java.awt.Font("Segoe UI", Font.BOLD, 14));
        ad_table.getTableHeader().setBackground(Color.WHITE);
        ad_table.setShowGrid(true);
        
        String searched = jTextField1.getText();
        searched = "%"+ searched + "%";
        
        String category = (String) jComboBox1.getSelectedItem();
        
        switch(category){
            
            case "Ad/Product":{
                category = "ad_name";
            break;
            }
            case "Brand":{
                category = "brand";
            break;
            }
            case "Model":{
                category = "model";
            break;
            }
            case "Site":{
                category = "site";
            break;
                }
        }
        
        for(int row_index = ad_table.getRowCount();ad_table.getRowCount()!=0;row_index--){
            if(row_index==0){JOptionPane.showMessageDialog(null,"last");}
            model.removeRow(row_index-1);
        }
        
       try{ 
        sql = "SELECT * FROM `ontology_system_final_table` WHERE `"+category+"` LIKE '"+searched+"' ";
        pst = getConnectiontoDB().prepareStatement(sql);
        rs = pst.executeQuery();
        
        while(rs.next()){
            
            model.addRow(new Object[]{rs.getString("ad_name"), rs.getString("site"), rs.getString("brand"), rs.getString("model"),rs.getString("price"), rs.getString("chipset"), rs.getString("cpu"), rs.getString("camera_primary"),rs.getString("memory_internal")});
        
        }     
       }
       catch(SQLException err){
           System.err.println(err);
       }
    }
    
    public String commaMaker(String price){
    
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');
        String pattern = "#,##0.0#";
        DecimalFormat df = new DecimalFormat(pattern, symbols);
        df.setParseBigDecimal(true);     
        
        
        /*price = proposed.replaceAll(Pattern.quote(","),"");
        equi = equi.replaceAll(Pattern.quote(","),"");
        other = other.replaceAll(Pattern.quote(","),"");
        
        if("".equals(proposed)){proposed = "0";}
        if("".equals(equi)){equi = "0";}
        if("".equals(other)){other = "0";}*/
        
        BigDecimal price_i = new BigDecimal(price);
        
        /*      BigDecimal equi_i = new BigDecimal(equi);
        BigDecimal other_i = new BigDecimal(other);
        BigDecimal total = new BigDecimal("0");
        
        total = total.add(proposed_i);
        total = total.add(equi_i);
        total = total.add(other_i);
        */
        return df.format(price_i);
        
      
        
        
        
    }  
    
    public int get_id_selected_row(){
        int id_row = 0;
        try{
            
            
         sql= "select id from ontology_system_final_table where id = "+ ad_table.getSelectedRow();
         pst = conn.prepareStatement(sql);
         rs = pst.executeQuery();
         
         if(rs.next()){
             
             id_row = rs.getInt("id");
        }
        
        }
        catch(SQLException | HeadlessException err){
            JOptionPane.showMessageDialog(null, "get_id "+err);
        }
        ID = id_row+1;
        return id_row;
        
    }
    
    public static int get_ID(){
        return ID;
    }
        
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        ad_table = new javax.swing.JTable();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("E-COMMERCE ONTOLOGY SYSTEM");

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));

        ad_table.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        ad_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Ad", "Site Location", "Brand", "Model", "Price", "Chipset", "CPU", "Camera", "Memory"
            }
        ));
        jScrollPane1.setViewportView(ad_table);

        jTextField1.setFont(new java.awt.Font("Calibri Light", 0, 18)); // NOI18N
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jButton1.setText("view full details");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton2.setText("Search");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jComboBox1.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ad/Product", "Brand", "Model", "Site" }));

        jMenu1.setText("File");

        jMenuItem1.setText("Get Data");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 973, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
        get_id_selected_row();
        
        if (ad_table.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "Please select a ad before to view");
        } else {
            
                view = new View_full_details();
                view.setVisible(true);
            
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
      
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        populate_search();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        getdata = new GetData(this, true);
        getdata.setVisible(true);
        
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Ontology_System.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Ontology_System.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Ontology_System.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Ontology_System.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Ontology_System().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable ad_table;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
