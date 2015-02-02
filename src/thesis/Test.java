package thesis;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.DatasetAccessor;
import com.hp.hpl.jena.query.DatasetAccessorFactory;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateProcessor;
import com.hp.hpl.jena.update.UpdateRequest;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDFS;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JOptionPane;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class Test 
{
 public static void main (String args[]) 
 {
     
     Scanner s = new Scanner (System.in);
     
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
String inputFileName="C:\\Users\\Ken\\Documents\\NetBeansProjects\\thesis\\src\\thesis\\university.owl";
InputStream in = FileManager.get().open( inputFileName );
if (in == null) {
    throw new IllegalArgumentException(
         "File: " + inputFileName + " not found");
}
model.read(in, null);

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
    finally {

       qe.close();
    }
 
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

        static void test8(){

       DatasetAccessor accessor = DatasetAccessorFactory.createHTTP("http://localhost:3030/ds/data");

       // Download the updated model
       Model updated = accessor.getModel();

       // Save the updated model over the original file
       try {
                updated.write(new FileOutputStream("example.owl"), "RDF/XML");
            } catch (FileNotFoundException fileNotFoundException) {
                System.out.print(fileNotFoundException);
            }
        }

}

