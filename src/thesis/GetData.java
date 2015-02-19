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
import java.awt.HeadlessException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import static thesis.Ontology_System.conn;
import static thesis.Ontology_System.sql;

/**
 *
 * @author test
 */
public class GetData extends javax.swing.JDialog {
    
   static Connection conn = null;
   static ResultSet rs  = null;
   static PreparedStatement pst = null;

    Ontology_System home;
    private static  List<String[]> data;
    
    
    
    public GetData(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(parent);
        jLabel3.setVisible(false);
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
        
               get_data_from_web();
        
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
    
    public List getBrands(){
          Logger rootLogger = Logger.getRootLogger();
          rootLogger.setLevel(Level.INFO);
          rootLogger.addAppender(new ConsoleAppender(new PatternLayout("%-6r [%p] %c - %m%n")));
          
        System.out.println("getting brands list...");
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
                    
               //     System.out.println(a);
      
      //  brands = new String[] {"samsung","xolo","xiaomi","lg","zte","yezz","blu","toshiba","htc","niu","micromax","pantech","blackberry","motorola","vodafone","celkon","lava","lenovo","maxwest","gigabyte","vivo","verykool","pretigio","acer","nokia","microsoft","spice","plum","sony","gionee","apple","huawei","alcatel","asus","parla"};
       
    System.out.println("brand list submitted...");
    return brands;
    
       
  }
    
    public List getModels(String brand){
        
         Logger rootLogger = Logger.getRootLogger();
          rootLogger.setLevel(Level.INFO);
          rootLogger.addAppender(new ConsoleAppender(new PatternLayout("%-6r [%p] %c - %m%n")));
         
      List <String> models = new ArrayList();
      int i = 0;
        String a = "";
        
        System.out.println("fetching models from the selected brand \""+ brand.toUpperCase()+"\"...");
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
        //            System.out.println("list of models:\n"+a);
                    //<editor-fold defaultstate="collapsed" desc="check model values">
                    /* int j = 0;
                    while(models[j]!=null){
                    
                    System.out.println(models[j]);
                    
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
        System.out.println("inserting gathered data to database...");
        
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
        catch(SQLException err){
                    JOptionPane.showMessageDialog(null,"set_new_to_db: "+ err);
                }
        
        
        
        
    }
    
    public void get_data_from_web(){
        Connection conn = getConnectiontoDB();
        
        String sqlb = "";
        
        String limit = "limit";
        if (jCheckBox1.isSelected()) {
            sqlb = "TRUNCATE TABLE olx;\n" +
                    "TRUNCATE TABLE lazada;\n" +
                    "TRUNCATE TABLE ebay;\n" +
                    "TRUNCATE TABLE ontology_system_final_table;\n" +
                    "TRUNCATE TABLE ads;\n\n";
        }
        if (jCheckBox3.isSelected()) {
            limit = limit + " " + jTextField2.getText();
            
        }
        else limit = "";
    
        try{
            sqlb = sqlb + "LOAD DATA LOCAL INFILE 'C:/Users/test/Documents/final ad crawler csv/ebay crawl.csv' INTO TABLE ebay FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\\n' IGNORE 1 LINES;# 54 rows affected.\n" +
                "\n" +
                "LOAD DATA LOCAL INFILE 'C:/Users/test/Documents/final ad crawler csv/olx crawl.csv' INTO TABLE olx FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\\n' IGNORE 1 LINES;# 581 rows affected.\n" +
                "\n" +
                "LOAD DATA LOCAL INFILE 'C:/Users/test/Documents/final ad crawler csv/lazada crawl.csv' INTO TABLE lazada FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\\n' IGNORE 1 LINES;# 32 rows affected.\n" +
                "\n" +
                "\n" +
                "\n" +
                "INSERT INTO ads (ad_name, site, price, location, posted_by, description, image, link)\n" +
                "(SELECT link, g, h, cdf, i, asd, image, e FROM ebay "+limit+")\n" +
                "union\n" +
                "(SELECT g, ad_name, h, dsf, cdf, zxcz, i, link FROM olx "+limit+")\n" +
                "union\n" +
                "(SELECT g, ad_name, cxzc, cdf, zxcz, dsf, h, link FROM lazada "+limit+")# 15 rows affected.";
            
             JOptionPane.showMessageDialog(null,"get data from web: \n"+ sqlb);

           PreparedStatement pstb = conn.prepareStatement(sqlb);
           pstb.execute(sql);
            
            
            
            }
        catch(Exception err){
                    JOptionPane.showMessageDialog(null,"get data from web: "+ err);
                }
    }
    
    public List<String[]> ads(){
         //<editor-fold defaultstate="collapsed" desc="rj logger">
        Logger rootLogger = Logger.getRootLogger();
        rootLogger.setLevel(Level.INFO);
        rootLogger.addAppender(new ConsoleAppender(new PatternLayout("%-6r [%p] %c - %m%n")));
        
//</editor-fold>
        System.out.println("getting ads list...");
        
        String limit = "";
     
        if (jCheckBox4.isSelected()) {
            limit = "limit " + jTextField3.getText();
        }

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
            sql = "select * from `ads` "+limit+" ";
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
                                ":ad_name \""+ad_name.replaceAll("\\s+","_").replaceAll("[^a-zA-Z0-9]+","_")+"\"; " +
                                ":price \""+price.replaceAll("\\s+","_").replaceAll("[^a-zA-Z0-9]+","_")+"\"; " +
                                ":location \""+location.replaceAll("\\s+","_").replaceAll("[^a-zA-Z0-9]+","_")+"\"; " +
                                ":posted_by \""+posted_by.replaceAll("\\s+","_").replaceAll("[^a-zA-Z0-9]+","_")+"\"; " +
                                ":image \""+image.replaceAll("\\s+","_").replaceAll("[^a-zA-Z0-9]+","_")+"\"; " +
                                ":link \""+link.replaceAll("\\s+","_").replaceAll("[^a-zA-Z0-9]+","_")+"\"; " +
                                ":description  \""+description.replaceAll("\\s+","_").replaceAll("[^a-zA-Z0-9]+","_")+"\"; " +
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
                    System.err.println(fileNotFoundException);
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
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jButton1 = new javax.swing.JButton();
        jCheckBox3 = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jCheckBox4 = new javax.swing.JCheckBox();
        jLabel4 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Fetch Data");
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jCheckBox1.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox1.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jCheckBox1.setText("Truncate DB");

        jButton1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton1.setText("GO");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jCheckBox3.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox3.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jCheckBox3.setText("Limit fetch");
        jCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox3ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jLabel2.setText("Value:");
        jLabel2.setEnabled(false);

        jTextField2.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jTextField2.setEnabled(false);
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 102, 102));
        jLabel3.setText("Please wait while data is being processed");

        jCheckBox4.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox4.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jCheckBox4.setText("Limit data");
        jCheckBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox4ActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jLabel4.setText("Value:");
        jLabel4.setEnabled(false);

        jTextField3.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jTextField3.setEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jCheckBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel2)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jCheckBox1))
                .addGap(33, 33, 33)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(45, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(103, 103, 103))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jCheckBox1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox3))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jCheckBox4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jCheckBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox4ActionPerformed
        if (jCheckBox4.isSelected()) {
            jLabel4.setEnabled(true);
            jTextField3.setEnabled(true);
        }
        else{
            jLabel4.setEnabled(false);
            jTextField3.setEnabled(false);

        }

    }//GEN-LAST:event_jCheckBox4ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox3ActionPerformed
        if (jCheckBox3.isSelected()) {
            jTextField2.setEnabled(true);
            jLabel2.setEnabled(true);
        }
        else{
            jTextField2.setEnabled(false);
            jLabel2.setEnabled(false);
        }

    }//GEN-LAST:event_jCheckBox3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        jLabel3.setVisible(true);
        index();
        jLabel3.setText("Success! Click close to view data");
        jButton1.setText("close");
        jLabel3.setForeground(new java.awt.Color(0, 153, 51));
    }//GEN-LAST:event_jButton1ActionPerformed

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
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GetData.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GetData.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GetData.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GetData.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                GetData dialog = new GetData(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
}
