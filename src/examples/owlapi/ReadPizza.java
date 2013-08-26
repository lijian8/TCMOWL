package examples.owlapi;

import java.io.File;
import java.util.Set;

import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntaxOntologyFormat;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.expression.ParserException;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyFormat;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import uk.ac.manchester.cs.owl.owlapi.mansyntaxrenderer.ManchesterOWLSyntaxOWLObjectRendererImpl;

public class ReadPizza {

	public static void main(String[] args) throws ParserException,
			OWLOntologyCreationException, OWLOntologyStorageException {

		File file = new File("ontologies/pizza.man");

		OWLOntologyManager manager;
		OWLOntology localOntology = null;
		OWLDataFactory factory;
		// IRI iri = IRI.create("http://localhost/p3.man");

		IRI iri = IRI.create(file);
		// try {

		// loading the ontology
		manager = OWLManager.createOWLOntologyManager();

		try {
			localOntology = manager.loadOntologyFromOntologyDocument(iri);
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}

		factory = localOntology.getOWLOntologyManager().getOWLDataFactory();

		OWLOntologyFormat format = manager.getOntologyFormat(localOntology);
		System.out.println("    format: " + format);

		ManchesterOWLSyntaxOntologyFormat manSyntaxFormat = new ManchesterOWLSyntaxOntologyFormat();
		if (format.isPrefixOWLOntologyFormat()) {
			manSyntaxFormat
					.copyPrefixesFrom(format.asPrefixOWLOntologyFormat());
		}

		manager.setOntologyFormat(localOntology, manSyntaxFormat);
		// manager.saveOntology(localOntology, manSyntaxFormat);
		System.out.println("Manchester syntax: --- saved in Manchester.owl");

		ManchesterOWLSyntaxOWLObjectRendererImpl rendering = new ManchesterOWLSyntaxOWLObjectRendererImpl();

		/*
		OWLClass c1 = factory
				.getOWLClass(IRI
						.create("http://lod.cintcm.com/ontology/pizza/pizza.owl#Pizza"));*/
		OWLClass c1 = factory
				.getOWLClass(IRI
						.create("http://www.co-ode.org/ontologies/pizza/pizza.owl#Pizza"));
		OWLObjectProperty contains = factory.getOWLObjectProperty(IRI.create("http://www.co-ode.org/ontologies/pizza/pizza.owl#Pizza"));

		Set<OWLClassExpression> c1eqclasses = c1
				.getEquivalentClasses(localOntology);
		for (OWLClassExpression c1e : c1eqclasses)
			System.out.println("Equivalent: " + rendering.render(c1e));

		c1eqclasses = c1.getDisjointClasses(localOntology);
		for (OWLClassExpression c1e : c1eqclasses)
			System.out.println("Disjoint: " + rendering.render(c1e));

		c1eqclasses = c1.getSubClasses(localOntology);
		for (OWLClassExpression c1e : c1eqclasses)
			System.out.println("Subclass: " + rendering.render(c1e));

		c1eqclasses = c1.getSuperClasses(localOntology);
		for (OWLClassExpression c1e : c1eqclasses)
			System.out.println("Superclass: " + rendering.render(c1e));
		
		Set<OWLIndividual> individuals = c1.getIndividuals(localOntology);
		for (OWLIndividual ind : individuals){
			System.out.println("Individual: " + rendering.render(ind));
			ind.getObjectPropertyValues(contains, localOntology);
		}
		
		
		// } catch (OWLOntologyStorageException e) {
		// System.out.println("Could not save ontology: " + e.getMessage());
		// }

	}

}
