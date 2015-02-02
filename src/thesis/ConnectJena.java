/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package thesis;

import com.hp.hpl.jena.query.DatasetAccessor;
import com.hp.hpl.jena.query.DatasetAccessorFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 *
 * @author Ken
 */
public class ConnectJena {
    
     public static void main (String args[]) 
 {

 }
     public void connect(){
          InputStream in = null;
         
         String serviceURI = "http://localhost:3030/gg/data";
            DatasetAccessorFactory factory = null;
            DatasetAccessor accessor;
            accessor = factory.createHTTP(serviceURI);
            
            try{
            in = new FileInputStream("C:\\Users\\Merz\\Documents\\k3n\\thesis\\csstnns\\university.owl");}
            catch(FileNotFoundException err){}
            
            
            Model m = ModelFactory.createDefaultModel();
            String base = "http://people.brunel.ac.uk/~csstnns/university.owl";
            m.read(in, base, "RDF/XML");
            
            accessor.putModel(m);
     
         
     }
    
}
