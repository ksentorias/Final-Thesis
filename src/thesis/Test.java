package thesis;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
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
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDFS;
import java.io.InputStream;
import javax.swing.JOptionPane;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class Test 
{
 public static void main (String args[]) 
 {
     //sparqltest();
     //rdftest();
    test1();
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
 static void test1(){
 
     
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
         
         System.out.println("Title: " + soln.getLiteral("s").getString());
         
         ResultSetFormatter.out(System.out, results, query);
     }
 }
 static void test2(){
 
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
 static void test3(){
 
  ModelD2RQ m=new ModelD2RQ("file:doc/example/mapping-iswc.ttl");
  FileManager fManager = FileManager.get();
  //Model m = fManager.loadModel("C:\\Users\\Ken\\Documents\\NetBeansProjects\\thesis\\src\\thesis\\mapping-iswc.ttl");

  String sparql="PREFIX dc: <http://purl.org/dc/elements/1.1/>" + 
          "PREFIX foaf: <http://xmlns.com/foaf/0.1/>" + 
          "SELECT ?paperTitle ?authorName WHERE {"+ 
          "    ?paper dc:title ?paperTitle . "+ 
          "    ?paper dc:creator ?author ."+ 
          "    ?author foaf:name ?authorName ."+ "}";
  
  
  Query q=QueryFactory.create(sparql);
  ResultSet rs=QueryExecutionFactory.create(q,m).execSelect();
  while (rs.hasNext()) {
    QuerySolution row=rs.nextSolution();
    
    
    //ResultSetFormatter.out(System.out, rs, q);
    System.out.println("Title: " + row.getLiteral("paperTitle").getString());
    System.out.println("Author: " + row.getLiteral("authorName").getString());
  }
  m.close();
 }

}

