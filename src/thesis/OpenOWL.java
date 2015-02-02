package thesis;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 *
 * @author Saidi Ali-(+216 22 790 538) - (+216 50 590 538) - fb.com/idiasila - Tunisia (2012-2013)
 *
 *
 *
 */

class OpenOWL {

     static  String  s;

      // Open a connection to MonFichierOwl.OWL

     static  OntModel OpenConnectOWL(){
        
    OntModel mode = null;
    mode = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM_RULE_INF );
    java.io.InputStream in = FileManager.get().open( "C:/MonFichierOWL.owl" );  //MyFile

    //test
    if (in == null) {
        throw new IllegalArgumentException("Pas de base de connaissance");  // there i no file to connect
    }
        return  (OntModel) mode.read(in, "");
    }


    // jena.query.ResultSet  return

     static  com.hp.hpl.jena.query.ResultSet ExecSparQl(String Query){
         
          com.hp.hpl.jena.query.Query query = QueryFactory.create(Query);

                QueryExecution qe = QueryExecutionFactory.create(query, OpenConnectOWL());
                com.hp.hpl.jena.query.ResultSet results = qe.execSelect();

                return results;  // Retrun jena.query.ResultSet 
         
     }

     // String return (convert jena.query.ResultSet  to String)

      static  String ExecSparQlString(String Query){
        try {
            com.hp.hpl.jena.query.Query query = QueryFactory.create(Query);

                  QueryExecution qe = QueryExecutionFactory.create(query, OpenConnectOWL());

                  com.hp.hpl.jena.query.ResultSet results = qe.execSelect();

                  // Test
                  if(results.hasNext()){

                  	// if iS good 

                     ByteArrayOutputStream go = new ByteArrayOutputStream ();
                    ResultSetFormatter.out((OutputStream)go ,results, query);
                  //  String s = go.toString();
                       s = new String(go.toByteArray(), "UTF-8");
                   }
                   // not okay
                  else{

                      s = "rien";
                  }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(OpenOWL.class.getName()).log(Level.SEVERE, null, ex);
        }
         return s;   // return  jena.query.ResultSet  as string 
      }
      
    /*  
// example using  jena.query.ResultSet  
public void BoxActivite(){  // method
     
     String rol =  role.getText();
     String p =  pr.getSelectedItem().toString();
      try {
            // OntModel model = OpenOWL.OpenConnectOWL();
    
     System.out.println("Avoir les activités");  // get the activity from my File OWL 

                    String queryString;
                    queryString = "PREFIX saidi:<http://www.owl-ontologies.com/Ontology1364995044.owl#> "
                                      + "SELECT  (str(?x) as ?xs) "
                                      + "where { ?y saidi:hasnameactivite ?x."
                                      + " ?y saidi:avoirrole ?ro. "
                                      + " ?y saidi:Activitepour ?p. "
                                      + "?ro saidi:hasnamerole ?nr."
                                      + " FILTER (?p ='"+p+"') "
                                      + "FILTER (?nr ='"+rol+"') }";

                  // call method ExecSparQl from class OpenOWL
                 // ExecSparQl return a Resultset 
                com.hp.hpl.jena.query.ResultSet results = OpenOWL.ExecSparQl(queryString);

                     while (results.hasNext()) {

                           QuerySolution soln = results.nextSolution();
                           String nomactiviter = soln.getLiteral("xs").getString();
                            //test --
                            System.out.println("nomactiviter  " + nomactiviter.toString());
                            
                              // public ArrayList<String> ListActivite = new ArrayList<String>();

                            ListActivite.add(nomactiviter.toString());
                     }
                       //Jcombobox   (ac = new javax.swing.JComboBox();)

                       ac.removeAllItems();
                      for (int i = 0; i < ListActivite.size(); i++) { 

                        // add activity to my Jcombobox ac
                         ac.addItem(ListActivite.get(i));
                     }
                         } catch (Exception ex) {
            Logger.getLogger(UserFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
 }  // end first method

                // Example using String return 
                // use the code where u want :p
                    String queryString;
                    queryString = "PREFIX saidi:<http://www.owl-ontologies.com/Ontology1364995044.owl#> "
                                       + "SELECT (str(?nomprojet  ) as ?NomProjet) (str(?nomproduit) as ?NomProduit)  (str(?country) as ?Pays) (str(?cout) as ?Coût) (str(?date) as ?Date) (str(?besoin) as ?RepondreauxBesoins)"
                                       + "WHERE {?projet saidi:hasnameProject  ?nomprojet. ?projet saidi:hasdate ?date."
                                       + " ?projet saidi:hascountry ?country. "
                                       + "?projet  saidi:realise ?prod.  " 
                                       + "?prod saidi:hasnameproduct ?nomproduit. ?prod saidi:hasnamecategory ?category. "
                                       + "?projet saidi:cost ?cout."
                                       + "OPTIONAL { ?prod saidi:repondreA ?bes. ?bes saidi:hasnamebesoin ?besoin.  }"
                                       + " FILTER (?nomprojet =\""+ this.pr.getSelectedItem().toString() +"\") "
                                       + "FILTER (?country =\""+ this.Pays.getSelectedItem().toString() +"\")"
                                        + "FILTER (?cout >='"+ this.Cmin.getSelectedItem().toString()+"')"
                                      + "FILTER (?cout <='"+ this.Cmax.getSelectedItem().toString()+"')"
                                       + "FILTER (?date >='"+ this.datemin.getSelectedItem().toString() +"')"
                                        + "FILTER (?date <='"+ this.datemax.getSelectedItem().toString() +"')}";
           

                  // call method ExecSparQlString from class  OpenOWL    
                  // ExecSparQlString return a String  
                 String s = OpenOWL.ExecSparQlString(queryString);
                     
                      //test 
                      System.out.println(s); 

                        if (s=="rien"){

                            System.out.println("rien");

                          }else{

                          // diSplay reSult aS String in an appendVersPane in blue color 

                          appendVersPane(TxtPane, "'"+s+"'\n", Color.BLUE);

                          }


                          // thats All
                          */
 
      
}
