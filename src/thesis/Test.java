package thesis;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.query.DatasetAccessor;
import com.hp.hpl.jena.query.DatasetAccessorFactory;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.reasoner.rulesys.GenericRuleReasoner;
import com.hp.hpl.jena.reasoner.rulesys.Rule;
import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateProcessor;
import com.hp.hpl.jena.update.UpdateRequest;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.OWL;
import static com.hp.hpl.jena.vocabulary.OWL.Ontology;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.ReasonerVocabulary;
import com.hp.hpl.jena.vocabulary.XSD;
import java.awt.HeadlessException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.JOptionPane;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import static thesis.Main.conn;

public class Test 
{
    static Connection conn = null;
    static java.sql.ResultSet rs  = null;
    static PreparedStatement pst = null;
    static String sql = "";
    static Scanner s = new Scanner (System.in);

 public static void main (String args[]) 
 {
     
     
     
     System.out.print("choose: ");
     int yeah = s.nextInt();
     
     switch(yeah){
         
         case 1:  test1(); break;
         case 2:  test2();break;
         case 3: test3();  break;
         case 4: test4(); break;
         case 5: test5(); break;
         case 6: test6(); break;
         case 7: test7(); break;
         case 8: test8(); break;
         case 9: test9(); break;
         case 10: test10(); break;
         case 11: test11(); break;
         case 12: test12(); break;
         case 13: test13(); break;
         case 14: test14(); break;
         case 15: test15(); break;
  
                 
         
     
         
     }
     //sparqltest();
     //rdftest();
   // test1();
     //test2();
    // test3();
       
 }
 static void sparqltest(){
    Logger.getRootLogger().setLevel(Level.OFF);
 
     FileManager.get().addLocatorClassLoader(Test.class.getClassLoader());
    Model model  = FileManager.get().loadModel("C:\\Users\\Ken\\Documents\\NetBeansProjects\\thesis\\src\\thesis\\university.owl");
    String queryString =  "SELECT ?x ?s ?o {?x ?s ?o}";
   
  

    Query query = QueryFactory.create(queryString);
    try(QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
    ResultSet results = qexec.execSelect();
    while ( results.hasNext()) {
            QuerySolution soln = results.nextSolution();
            Literal name = soln.getLiteral("x");
            System.out.println(name);
    }
    }
 }
 /*static void rdftest(){
 
     FileManager fManager = FileManager.get();
Model model = fManager.loadModel("C:\\Users\\Ken\\Documents\\NetBeansProjects\\thesis\\src\\thesis\\university.owl");

Property someRelevantProperty = model. createProperty("http://your.data.org/ontology/",
                          "someRelevantProperty");

SimpleSelector selector = new SimpleSelector(null, null, (RDFNode)null) {
    public boolean selects(Statement s)
        { return s.getPredicate().equals(someRelevantProperty);}
};

StmtIterator iter = model.listStatements(selector);
while(iter.hasNext()) {
   Statement stmt = iter.nextStatement();
   System.out.print(stmt.getSubject().toString());
   System.out.print(stmt.getPredicate().toString());
   System.out.println(stmt.getObject().toString());
}
 }
 
 */
 static void test1(){ //first successful ouput of sparql query
 
     
 OntModel model = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM_MICRO_RULE_INF);
String inputFileName="C:\\Users\\test\\Documents\\test owl\\ecommerce.owl";
String ns = "http://xu.edu.ph/ecommerce#";
InputStream in = FileManager.get().open( inputFileName );
if (in == null) {
    throw new IllegalArgumentException(
         "File: " + inputFileName + " not found");
}
model.read(in, null);


DatatypeProperty hasAge = model.createDatatypeProperty(ns + "hasAge");


String que = "prefix rdfs: <" + RDFS.getURI() + ">"
             + " select ?s {?y rdfs:subClassOf ?s}";

String queryString =
        "prefix pizza: <www.co-ode.org/ontologies/pizza/pizza.owl#Pizza> "+        
        "prefix rdfs: <" + RDFS.getURI() + "> "           +
        "prefix owl: <" + OWL.getURI() + "> "             +
        "select ?pizza where {?pizza a owl:Class ; "      +
        "rdfs:subClassOf ?restriction. "                  +
        "?restriction owl:onProperty pizza:hasTopping ;"  +
        "owl:someValuesFrom pizza:PeperoniSausageTopping" +
        "}";
        
Query query = QueryFactory.create(que);
     try (QueryExecution qe = QueryExecutionFactory.create(query, model)) {
         com.hp.hpl.jena.query.ResultSet results =  qe.execSelect();
         
         QuerySolution soln = results.nextSolution() ;
         
       
         
         //RDFNode x = soln.get("varName") ;
         //JOptionPane.showMessageDialog(null, results);
         
         //System.out.println("Title: " + soln.getLiteral("s").getString());
         
         ResultSetFormatter.out(System.out, results, query);
     }
 }
 
 static void test2(){ //attempt to get data in namespaces
 
       Model model = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM_MICRO_RULE_INF);
       
       String inputFileName="C:\\Users\\Ken\\Documents\\NetBeansProjects\\thesis\\src\\thesis\\university.owl";
InputStream in = FileManager.get().open( inputFileName );
if (in == null) {
    throw new IllegalArgumentException(
         "File: " + inputFileName + " not found");
}
model.read(in, null);
  String queryString = "prefix rdfs: <" + RDFS.getURI() + ">"
             + " select * {?y rdfs:subClassOf ?s}";
  Query query = QueryFactory.create(queryString) ;
  try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
    ResultSet results = qexec.execSelect() ;
    for ( ; results.hasNext() ; )
    {
      QuerySolution soln = results.nextSolution() ;
      RDFNode x = soln.get("varName") ;       // Get a result variable by name.
      Resource r = soln.getResource("VarR") ; // Get a result variable - must be a resource
      Literal l = soln.getLiteral("VarL") ;   // Get a result variable - must be a literal
      
      System.out.print(r);
    }
  }
 }
 
 static void test3(){ // attempt to get data in namespaces
 
  //ModelD2RQ m=new ModelD2RQ("file:doc/example/mapping-iswc.ttl");
  FileManager fManager = FileManager.get();
  Model m = fManager.loadModel("C:\\Users\\Ken\\Documents\\NetBeansProjects\\thesis\\src\\thesis\\mapping-iswc.ttl");

  String sparql="PREFIX dc: <http://purl.org/dc/elements/1.1/>" + 
          "PREFIX foaf: <http://xmlns.com/foaf/0.1/>" + 
          "SELECT ?paperTitle ?authorName WHERE {"+ 
          "    ?paper dc:title ?paperTitle . "+ 
          "    ?paper dc:creator ?author ."+ 
          "    ?author foaf:name ?authorName ."+ "}";
  
  String query = "select ?d {?d ?s ?f}";
  
  
  Query q =QueryFactory.create(sparql);
  Query eq =QueryFactory.create(query);
  
  ResultSet rs=QueryExecutionFactory.create(q,m).execSelect();
  ResultSet rse=QueryExecutionFactory.create(eq,m).execSelect();
  
  //ResultSetFormatter.out(System.out, rse, eq);
  
  
  while (rse.hasNext()) {
    QuerySolution row=rse.nextSolution();
    
    
    ResultSetFormatter.out(System.out, rse, eq);
    //System.out.println("Title: " + row.getLiteral("paperTitle").getString());
    
    System.out.println("Title: " + row.getLiteral("?d").getString());
    System.out.println("Author: " + row.getLiteral("authorName").getString());
  }
  m.close();
 }
 
 static void test4(){ //extract data from the web
     
         String service = "http://dbpedia.org/sparql";
         String service_2 = "http://people.brunel.ac.uk/~csstnns/university.owl";
    String query = "SELECT ?abstract"+
            "WHERE {"+
            "{"+ 
            "<http://dbpedia.org/resource/Akbar> <http://dbpedia.org/ontology/abstract> ?abstract."+
            "FILTER langMatches( lang(?abstract), 'en')"+
            "}"+
            "}";
    
    String q  =  "select * {?s ?p ?o}";
    QueryExecution qe = QueryExecutionFactory.sparqlService(service_2, q);
    try {
        ResultSet results = qe.execSelect();

        for (; results.hasNext();) {

            QuerySolution sol = (QuerySolution) results.next();

            System.out.println(sol.get("?abstract"));

        }

    }catch(Exception e){

        e.printStackTrace();
    }
 

       qe.close();
    
 
 }
 
 static void test5(){// extract data from the web
 
     
     try {
         String url = "http://people.brunel.ac.uk/~csstnns/university.owl";
         Model model = ModelFactory.createDefaultModel();
         model.read(url, null);
         model.write(System.out);
     } catch (Exception e) {
         System.out.print(e);
     }
 }
 
 static void test6(){// insert triple in owl file
    try{
    ArrayList<String> subject = new ArrayList<String>();
    ArrayList<String> predicate = new ArrayList<String>();
    ArrayList<String> object = new ArrayList<String>();

    subject.add("<http://people.brunel.ac.uk/~csstnns/university.owl#>");
    predicate.add("<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>");
    object.add("<http://people.brunel.ac.uk/~csstnns/university.owl#Lecturer>");
    
    //testing
   String sub = "<http://people.brunel.ac.uk/~csstnns/university.owl#>";
   String pre = "<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>";
   String obj = "<http://people.brunel.ac.uk/~csstnns/university.owl#Lecturer>";


    for(int i = 0; i < subject.size(); i++){
        String qry = "INSERT DATA"+
                "{"+
               sub+"\n"+
               pre+"\n"+
               obj+"\n"+
                "}";

        UpdateRequest update  = UpdateFactory.create(qry);
        UpdateProcessor qexec = UpdateExecutionFactory.createRemote(update, "http://localhost:3030/ds/update");
        qexec.execute();
    }
    }catch(Exception e){
       
    }
    
 }
 
 static void test7(){// run server through these codes

           String[] command = { "C:\\Users\\Merz\\Documents\\NetBeansProjects\\Ontology_Thesis\\src\\jena-fuseki-1.1.1\\fuseki-server.bat", "--update", "--mem", "/ds" };
           File directory = new File("C:\\Users\\Merz\\Documents\\NetBeansProjects\\Ontology_Thesis\\src\\jena-fuseki-1.1.1\\");
           ProcessBuilder pb = new ProcessBuilder(command);
           pb.directory(directory);

           try {
               pb.start();
               System.out.print("success");
           } catch (IOException e) {
               System.out.print(e);
           }
        }

 static void test8(){// output data from jena server

       DatasetAccessor accessor = DatasetAccessorFactory.createHTTP("http://localhost:3030/ds/data");

       // Download the updated model
       Model updated = accessor.getModel();

       // Save the updated model over the original file
       try {
                updated.write(new FileOutputStream("C:\\Users\\test\\Documents\\output.owl"), "RDF/XML");
                 System.out.print("success!");
            } catch (FileNotFoundException fileNotFoundException) {
                System.out.print(fileNotFoundException);
            }
        }
 
 static void test9(){
 
     String ns = "http://www.founder.com/student.owl#";
		String queryStr = "PREFIX Student:<" + ns +"> " + "SELECT ?student ?schoolmate " + "WHERE {?student Student:isSchoolMateWith ?schoolmate} ";
		String ruleFile = "C:\\Users\\test\\Documents\\NetBeansProjects\\Ontology_Thesis\\src\\jena\\expert\\student.rules";
		
		List<Rule> rules = Rule.rulesFromURL(ruleFile);
		GenericRuleReasoner reasoner = new GenericRuleReasoner(rules);
		reasoner.setOWLTranslation(true);
		reasoner.setDerivationLogging(true);
		reasoner.setTransitiveClosureCaching(true);
                
                OntModel ontModel = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM_MICRO_RULE_INF);
                String inputFileName="C:\\Users\\Ken\\Documents\\NetBeansProjects\\thesis\\src\\thesis\\university.owl";
                InputStream in = FileManager.get().open( inputFileName );
                if (in == null) {
                    throw new IllegalArgumentException(
                         "File: " + inputFileName + " not found");
                }
                ontModel.read(in, null);

		
		//OntModel ontModel = ModelFactory.createOntologyModel();
		
                OntClass person = ontModel.createClass(ns + "person");
                OntProperty name = ontModel.createDatatypeProperty(ns + "name");
                name.setDomain(person);
                name.setRange(XSD.xstring);
                OntProperty isSchoolMateWith = ontModel.createObjectProperty(ns + "isSchoolMateWith");
                isSchoolMateWith.setDomain(person);
                isSchoolMateWith.setRange(person);
		
		Individual zhangSan = ontModel.createIndividual(ns + "zhangSan", ontModel.getResource(ns + "person"));
		zhangSan.setOntClass(person);
		zhangSan.setRDFType(person);
                
                
                
                // output here
                
                Resource configuration = ontModel.createResource();
		configuration.addProperty(ReasonerVocabulary.PROPruleMode, "hybrid");

		InfModel infModel = ModelFactory.createInfModel(reasoner, ontModel);
		
		Query query = QueryFactory.create(queryStr);
		QueryExecution qe = QueryExecutionFactory.create(query, infModel);
		ResultSet rs = qe.execSelect();
		ResultSetFormatter.out(System.out, rs, query);
		if(null != qe) {
			qe.close();
		}
		
		Resource resource1 = infModel.getResource(ns + "liSi");
		Resource resource2 = infModel.getResource(ns + "wangEr");
		StmtIterator stmtIterator = infModel.listStatements(resource1, null, resource2);
		if (!stmtIterator.hasNext()) {
			System.out.println("there is no relation between " + resource1.getLocalName() + " and " + resource2.getLocalName());
		}
		while (stmtIterator.hasNext()) {
			Statement stmt = stmtIterator.next();
			System.out.println("Relation between " + resource1.getLocalName() + " and " + resource2.getLocalName() + " is : " + resource1.getLocalName() + " " + stmt.getPredicate().getLocalName() + " " + resource2.getLocalName());
		}
 }
 
 static void test10(){//get data from jena fuseki
 
     DatasetAccessor accessor = DatasetAccessorFactory.createHTTP("http://localhost:3030/ds/data");
     
     Model updated = accessor.getModel();
     

    String que = "prefix rdfs: <" + RDFS.getURI() + ">"
                 + " select ?s {?y rdfs:subClassOf ?s}";

    Query query = QueryFactory.create(que);
         try (QueryExecution qe = QueryExecutionFactory.create(query, updated)) {
             com.hp.hpl.jena.query.ResultSet results =  qe.execSelect();


             String qry = "PREFIX :<http://xu.edu.ph/ecommerce#>\n" +
                            "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                            "PREFIX owl:<http://www.w3.org/2002/07/owl#>\n" +
                            "PREFIX xsd:<http://www.w3.org/2001/XMLSchema#>"
                     + "INSERT DATA\n" +
                            "{\n" +
                            "  :ad_4 rdf:type owl:NamedIndividual;" +
                            "        rdf:type :Phones;" +
                            "        :ad_name \"SALE!! wohohoho\";      }";

                UpdateRequest update  = UpdateFactory.create(qry);
                UpdateProcessor qexec = UpdateExecutionFactory.createRemote(update, "http://localhost:3030/ds/update");
                qexec.execute();

             ResultSetFormatter.out(System.out, results, query);
     }
     
 }
 
 static void test11(){

    // Create an empty ontology model
	OntModel ontModel = ModelFactory.createOntologyModel();
	String ns = new String("http://www.example.com/onto1#");
	String baseURI = new String("http://www.example.com/onto1");
        
        
	Ontology onto = ontModel.createOntology(baseURI);

	// Create â€˜Personâ€™, â€˜MalePersonâ€™ and â€˜FemalePersonâ€™ classes
	OntClass person = ontModel.createClass(ns + "Person");
	OntClass malePerson = ontModel.createClass(ns + "MalePerson");
	OntClass femalePerson = ontModel.createClass(ns + "FemalePerson");

	// FemalePerson and MalePerson are subclasses of Person
	person.addSubClass(malePerson);
	person.addSubClass(femalePerson);

	// FemalePerson and MalePerson are disjoint
	malePerson.addDisjointWith(femalePerson);
	femalePerson.addDisjointWith(malePerson);

	// Create datatype property 'hasAge'
	DatatypeProperty hasAge = ontModel.createDatatypeProperty(ns + "hasAge");
	// 'hasAge' takes integer values, so its range is 'integer'
	// Basic datatypes are defined in the â€˜vocabularyâ€™ package
	hasAge.setDomain(person);
	hasAge.setRange(XSD.integer); // com.hp.hpl.jena.vocabulary.XSD

	// Create individuals
	Individual john = malePerson.createIndividual(ns + "John");
	Individual jane = femalePerson.createIndividual(ns + "Jane");
	Individual bob = malePerson.createIndividual(ns + "Bob");

	// Create statement 'John hasAge 20'
	Literal age20 = ontModel.createTypedLiteral("20", XSDDatatype.XSDint);
	Statement johnIs20 = ontModel.createStatement(john, hasAge, age20);
	ontModel.add(johnIs20);
        
        try {
                ontModel.write(new FileOutputStream("C:\\Users\\test\\Documents\\test owl\\person.owl"), "RDF/XML");
                 System.out.print("success!");
            } catch (FileNotFoundException fileNotFoundException) {
                System.out.print(fileNotFoundException);
            }
        }

 static void test12(){
     
     
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
 
 static void test13(){ //try and iterrations
 
     OntModel model = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM_MICRO_RULE_INF);
      String inputFileName="C:\\Users\\test\\Documents\\test owl\\output.owl";
      String ns = "http://xu.edu.ph/ecommerce#";
      String[] a = {""};
   DatasetAccessor accessor = DatasetAccessorFactory.createHTTP("http://localhost:3030/ds/data");

       // Download the updated model
    // Model EcommerceModel =  accessor.getModel();
      
      InputStream in = FileManager.get().open( inputFileName );
      if (in == null) {
      throw new IllegalArgumentException(
      "File: " + inputFileName + " not found");
      }
      model.read(in, null);

        ExtendedIterator classes = model.listClasses();
        
        System.out.print("choose: ");
        int chosen = s.nextInt();
        
        switch(chosen){
        
            case 1:{
            //<editor-fold defaultstate="collapsed" desc="get brands">
        while (classes.hasNext())
        {
            OntClass thisClass = (OntClass) classes.next();
            
            OntClass brand = model.getOntClass(ns+"Brand");
            
            
            //System.out.println(thisClass.toString());
           // JOptionPane.showMessageDialog(null, brand);
            
            
            ExtendedIterator instances = brand.listInstances();
            
            while (instances.hasNext())
            {
                Individual thisInstance = (Individual) instances.next();
                
                if("Brand".equals(thisInstance.getOntClass().getLocalName()))
                    
                    ///System.out.println(thisInstance.toString());
                    JOptionPane.showMessageDialog(null, thisInstance.getLocalName()+ "\n" +thisInstance.getOntClass().toString());
                
            }
        }
//</editor-fold>
             break;
            }
            
            case 2: {
                //<editor-fold defaultstate="collapsed" desc="search models">
                
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
                    "   ?model :isaModelof :apple\n" +
                    "}";


                     QueryExecution exec = QueryExecutionFactory.create( query, model );
                     ResultSet rs = exec.execSelect();
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
                     ResultSet mrs = mexec.execSelect();
                    while ( mrs.hasNext() ) {
                     QuerySolution mqs = mrs.next();
                    JOptionPane.showMessageDialog(null, qs.get("model").toString().substring(qs.get("model").toString().indexOf("#")+1) + "\n" +mqs.get( "model_of_data_value" ));
                    }
               
               
               }
               
             
                    
                    ///System.out.println(thisInstance.toString());
                    
                
            
      
        
            
//</editor-fold>
            
                break;
            }
        }
        
        

        
        
        
        
        


 }
 
 static void test14(){
     
     
     OntModel model = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM_MICRO_RULE_INF);
      String inputFileName="C:\\Users\\test\\Documents\\test owl\\ecommerce.owl";
      String ns = "http://xu.edu.ph/ecommerce#";
      String[] a = {""};
   DatasetAccessor accessor = DatasetAccessorFactory.createHTTP("http://localhost:3030/ds/data");

       // Download the updated model
       Model updated = accessor.getModel();
       
      InputStream in = FileManager.get().open( inputFileName );
      if (in == null) {
      throw new IllegalArgumentException(
      "File: " + inputFileName + " not found");
      }
      model.read(in, null);

 
     
    //javax.swing.JDialog jDialog1 = new javax.swing.JDialog();       

        String uri="file:///D:/base_connaissance.owl#";
        /*owlModel = ProtegeOWL.createJenaOWLModelFromURI("file:///D:/base_connaissance.owl");//  crée  un modele owl  a partir du ficher owl charger
        model = owlModel.getOntModel();  */
        //JOptionPane.showMessageDialog(jDialog1,"chargement du fichier  effectuer avec succé","Information",JOptionPane.INFORMATION_MESSAGE);        

        ArrayList<Resource> results = new ArrayList<Resource>();            
        ExtendedIterator  individuals = model.listIndividuals();
        while (individuals.hasNext()) {
            Resource individual = (Resource) individuals.next();
            results.add(individual);
        }
        System.out.println("\n");

        for(int i = 0; i < results.size(); i++)
        {
            
        JOptionPane.showMessageDialog(null,results.get(i).toString());
        Individual ind = model.getIndividual(results.get(i).toString());
        System.out.println(""+ind);
        StmtIterator it = ind.listProperties();

        while ( it.hasNext()) {
               Statement s = (Statement) it.next();    

            if (s.getObject().isLiteral()) {


                //System.out.println(""+s.getLiteral()+" type = "+s.getPredicate().getLocalName());

                }


            else    System.out.println(""+s.getObject()+" type = "+s.getPredicate().getLocalName());


                 }
        System.out.println("\n");
            }



 }   
 
 static void test15(){
     
     String find  = "lumia 652";
     
     String raw = "ambot ani palit mog nokia nga asha 200";
     
     System.out.println(raw.contains(find));
 
     
 }
      }
 


 
 
 
 



