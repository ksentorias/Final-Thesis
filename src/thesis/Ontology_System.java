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
import javax.swing.JOptionPane;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import static thesis.Main.conn;

/**
 *
 * @author test
 */
public class Ontology_System extends javax.swing.JFrame {

   static Connection conn = null;
   static ResultSet rs  = null;
   static PreparedStatement pst = null;
   
   String ns = "http://xu.edu.ph/ecommerce#";
   
   
   static String sql = ""; 
   
    Ontology_System() {
        initComponents();
        this.setVisible(true);
      //  ads();
      // index();
      // setSpecsOntology();
        getModels("samsung");
     // getBrands();
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
    
    public Model loadModelfromServer(){
    
        DatasetAccessor accessor = DatasetAccessorFactory.createHTTP("http://localhost:3030/ds/data");
        
        Model loadedModel =  accessor.getModel();
        
        return loadedModel;
    }
   
    public void index(){
        
        Logger rootLogger = Logger.getRootLogger();
          rootLogger.setLevel(Level.INFO);
          rootLogger.addAppender(new ConsoleAppender(new PatternLayout("%-6r [%p] %c - %m%n")));
       
       int i = 0;
       int brandCounter = 0;
       int modelCounter;
       String brand = "";
       boolean haveBrand = false;
       boolean haveModel = false;
       
       
       
       
       String[][] newQuery = new String[20][100];
   
       String[][] products = ads();
       String[] BrandsfromOntology = getBrands();
       String[] modelsfromOntology;
       
        System.out.println("starting...");
        
       
        
        while(products[i][0]!=null){
            brandCounter=0;
            modelCounter= 0;
            
            //<editor-fold defaultstate="collapsed" desc="check brand">
   
                while(brandCounter<34){
                System.out.println("is "+products[i][0].toLowerCase()+" contains brand: " +BrandsfromOntology[brandCounter]);
                if(products[i][0].toLowerCase().contains(BrandsfromOntology[brandCounter])) {
                    System.out.println("yes");
                    
                    
                    //initialize with matched brand
                     JOptionPane.showMessageDialog(null,BrandsfromOntology[brandCounter]);
                    modelsfromOntology  = getModels(BrandsfromOntology[brandCounter]);
                    
                    
                    while(modelsfromOntology[modelCounter]!=null){
                        
                        System.out.println("is "+products[i][0].toLowerCase()+" contains model: " +modelsfromOntology[modelCounter]);
                        if(products[i][0].toLowerCase().contains(modelsfromOntology[modelCounter].toLowerCase())){
                            System.out.println("yes");
                            JOptionPane.showMessageDialog(null, "yes!\n"+products[i][0].toLowerCase()+"\n"+modelsfromOntology[modelCounter]);
                        }
                        
                        
                        
                        modelCounter++;
                    }
                }
                
                
                brandCounter++;
            }
            
           
            
//</editor-fold>
        i++;
        }
        
        
       
   }
    
    public String[][] ads(){
        
        System.out.println("getting ads list...");
        
        String [][] ads =  new String[100][8];
      
      String user = "root";
      String pass = "";
      String host = "127.0.0.1";
      String db = "ontology_system";
      int i = 0;
      
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
      
      try{
          System.out.println("query'ing ads...");
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
                 
             //   UpdateRequest update  = UpdateFactory.create(query);
             //   UpdateProcessor qexec = UpdateExecutionFactory.createRemote(update, "http://localhost:3030/ds/update");
            //    qexec.execute();
                
                ads[i][0] = ad_name;
                ads[i][1] = site;
                ads[i][2] = price;
                ads[i][3] = date_posted;
                ads[i][4] = condition;
                ads[i][5] = location;
                ads[i][6] = posted_by;
                ads[i][7] = description;
                
                
                
                
                
               // JOptionPane.showMessageDialog(null,ad_name+"\n"+site+"\n"+price+"\n"+date_posted+"\n"+condition+"\n"+location+"\n"+posted_by+"\n"+description);
                i++;
                
            }
            
            System.out.println("ads successfully initialized...");
            }
            catch(HeadlessException | SQLException err){
            JOptionPane.showMessageDialog(null,err);
        }
      /* i = 0;
      while (ads[i][0]!= null){System.out.printlnln(Arrays.toString(ads[i])); i++;}
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
            sql = "select * from `ken_2`";
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
                                //":colors \""+color+"\" " +
                                 "}";
                 
                 
              
                 

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
                 System.out.println("success!");
            } catch (FileNotFoundException fileNotFoundException) {
                System.out.println(fileNotFoundException);
            }
  }
  
    public String[] getBrands(){
          Logger rootLogger = Logger.getRootLogger();
          rootLogger.setLevel(Level.INFO);
          rootLogger.addAppender(new ConsoleAppender(new PatternLayout("%-6r [%p] %c - %m%n")));
          
        System.out.println("getting brands list...");
        String[] brands = new String[50];
        int i = 0;
        
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
                     brands[i] = qs.get("brands").toString().substring(qs.get("brands").toString().indexOf("#")+1);
                     i++;
                    }
                    
                    System.out.println(a);
      
      //  brands = new String[] {"samsung","xolo","xiaomi","lg","zte","yezz","blu","toshiba","htc","niu","micromax","pantech","blackberry","motorola","vodafone","celkon","lava","lenovo","maxwest","gigabyte","vivo","verykool","pretigio","acer","nokia","microsoft","spice","plum","sony","gionee","apple","huawei","alcatel","asus","parla"};
       
    System.out.println("brand list submitted...");
    return brands;
    
       
  }
    
    public String[] getModels(String brand){
        
         Logger rootLogger = Logger.getRootLogger();
          rootLogger.setLevel(Level.INFO);
          rootLogger.addAppender(new ConsoleAppender(new PatternLayout("%-6r [%p] %c - %m%n")));
         
      String[] models = new String[1000];
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
                    
                   // models[i] = mqs.get( "model_of_data_value" ).toString();
                    i++;
                    }
               
               
               }
                    System.out.println("list of models:\n"+a);
                    //<editor-fold defaultstate="collapsed" desc="check model values">
                    /* int j = 0;
                    while(models[j]!=null){
                    
                    System.out.println(models[j]);
                    
                    j++;
                    }*/
//</editor-fold>
                    
    
                    return models;
    }
    
    public String[] getSpecs(String modelfound){
        
         Logger rootLogger = Logger.getRootLogger();
          rootLogger.setLevel(Level.INFO);
          rootLogger.addAppender(new ConsoleAppender(new PatternLayout("%-6r [%p] %c - %m%n")));
         
    
        String[] models = new String[1000];
      int i = 0;
        
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
                    "   ?model :isaModelof :"+modelfound.toLowerCase()+"\n" +
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
                   // JOptionPane.showMessageDialog(null, qs.get("model").toString().substring(qs.get("model").toString().indexOf("#")+1) + "\n" +mqs.get( "model_of_data_value" ));
                    
                    models[i] = mqs.get( "model_of_data_value" ).toString();
                    i++;
                    }
               
               
               }
                    //<editor-fold defaultstate="collapsed" desc="check model values">
                    /* int j = 0;
                    while(models[j]!=null){
                    
                    System.out.println(models[j]);
                    
                    j++;
                    }*/
//</editor-fold>
                    
    
                    return models;
    }
    
   
    
    public boolean isAlpha(String name){
    
         char[] chars = name.toCharArray();

    for (char c : chars) {
        if(!Character.isLetter(c)) {
            return false;
        }
    }

    return true;
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
        jTable1 = new javax.swing.JTable();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Ad", "Site Location", "Brand", "Model", "Price", "Chipset", "CPU", "Camera", "Memory", "Date posted", "Condition", "Location"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jTextField1.setFont(new java.awt.Font("Calibri Light", 0, 18)); // NOI18N
        jTextField1.setText("Samsung");

        jLabel2.setFont(new java.awt.Font("Calibri Light", 1, 18)); // NOI18N
        jLabel2.setText("Search for a desired Product to begin");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1102, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
