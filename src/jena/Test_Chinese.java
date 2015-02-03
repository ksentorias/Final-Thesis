package jena;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.reasoner.rulesys.GenericRuleReasoner;
import com.hp.hpl.jena.reasoner.rulesys.Rule;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.ReasonerVocabulary;
import com.hp.hpl.jena.vocabulary.XSD;
import java.io.InputStream;
import java.util.List;
import javax.swing.JOptionPane;

public class Test_Chinese {

	public void test1() {
		String ns = "http://www.founder.com/student.owl#";
		String queryStr = "PREFIX Student:<" + ns +"> " + "SELECT ?student ?schoolmate " + "WHERE {?student Student:isSchoolMateWith ?schoolmate} ";
		String ruleFile = "C:\\Users\\test\\Documents\\NetBeansProjects\\Ontology_Thesis\\src\\jena\\expert\\student.rules";
		
		List<Rule> rules = Rule.rulesFromURL(ruleFile);
		GenericRuleReasoner reasoner = new GenericRuleReasoner(rules);
		reasoner.setOWLTranslation(true);
		reasoner.setDerivationLogging(true);
		reasoner.setTransitiveClosureCaching(true);
		
		OntModel ontModel = ModelFactory.createOntologyModel();
		
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
		zhangSan.setPropertyValue(name, ontModel.createLiteral("张三"));
		Individual liSi = ontModel.createIndividual(ns + "liSi", person);
		liSi.setOntClass(person);
		liSi.setRDFType(person);
		liSi.setPropertyValue(name, ontModel.createLiteral("李四"));
		Individual wangEr = ontModel.createIndividual(ns + "wangEr", person);
		wangEr.setOntClass(person);
		wangEr.setRDFType(person);
		wangEr.setPropertyValue(name, ontModel.createLiteral("王二"));
		Individual maWu = ontModel.createIndividual(ns + "maWu",person);
		maWu.setOntClass(person);
		maWu.setRDFType(person);
		maWu.setPropertyValue(name, ontModel.createLiteral("麻五"));
		wangEr.setPropertyValue(isSchoolMateWith, zhangSan);
		zhangSan.setPropertyValue(isSchoolMateWith, liSi);
		liSi.setPropertyValue(isSchoolMateWith, maWu);

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
	
	public void test2() {
		String ruleFile = "C:\\Users\\test\\Documents\\NetBeansProjects\\Ontology_Thesis\\src\\jena\\expert\\student.rules";
		String ontologyFile = "C:\\Users\\test\\Documents\\NetBeansProjects\\Ontology_Thesis\\src\\jena\\expert\\student.owl";
		InputStream in = FileManager.get().open( ontologyFile );
                String ns = "http://www.founder.com/student.owl#";
		String queryStr = "PREFIX Student:<" + ns +"> " + "SELECT ?student ?schoolmate " + "WHERE {?student Student:isSchoolMateWith ?schoolmate} ";

		List<Rule> rules = Rule.rulesFromURL(ruleFile);
		GenericRuleReasoner reasoner = new GenericRuleReasoner(rules);
		reasoner.setOWLTranslation(true);
		reasoner.setDerivationLogging(true);
		reasoner.setTransitiveClosureCaching(true);

		OntModel ontModel = ModelFactory.createOntologyModel();
		ontModel.read(in,null);
		
		OntClass person = ontModel.getOntClass(ns + "person");
		OntProperty isSchoolMateWith = ontModel.getObjectProperty(ns + "isSchoolMateWith");
		Individual liSi = ontModel.getIndividual(ns + "liSi");
		Individual maWu = ontModel.getIndividual(ns + "maWu");
		maWu.setRDFType(person);
		maWu.setOntClass(person);
		liSi.setPropertyValue(isSchoolMateWith, maWu);
		
		Resource configuration = ontModel.createResource();
		configuration.addProperty(ReasonerVocabulary.PROPruleMode, "forward");

		InfModel infModel = ModelFactory.createInfModel(reasoner, ontModel);

		Query query = QueryFactory.create(queryStr);
		QueryExecution qe = QueryExecutionFactory.create(query, infModel);
		ResultSet rs = qe.execSelect();
		ResultSetFormatter.out(System.out, rs, query);
		if (null != qe) {
			qe.close();
		}

		Resource resource1 = infModel.getResource(ns + "maWu");
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

	public static void main(String[] args) {
		Test_Chinese test = new Test_Chinese();
		          try {
                test.test1();
                JOptionPane.showMessageDialog(null, "success in 1");
            } catch (Exception e) {
                 JOptionPane.showMessageDialog(null, "in 1: " + e);
            } 
	        
                test.test2();

             
	}

}
