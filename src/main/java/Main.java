import org.openrdf.model.Model;
import org.openrdf.model.impl.LinkedHashModel;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFWriter;
import org.openrdf.rio.Rio;

import java.io.*;

/**
 * Created by noverhei on 19.02.2016.
 *
 * This application converts a csv file to rdf using Sesame/RDF4J.
 * Created for making a RDF dataset with somewhat realistic data to query on Stardog.
 *
 * Execution time:
 * 63 triples --> 116 milis
 * 68256 triples --> 1762 milis
 * 2438982 triples --> 22171 milis
 *
 */
public class Main {

    private static Model model = new LinkedHashModel();

    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();

        BufferedOutputStream out = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream("out.ttl"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        createDocuments();
        System.out.println(model.size());
        RDFWriter writer = Rio.createWriter(RDFFormat.TURTLE, out);
        try {
            writer.startRDF();
            writer.handleNamespace("document", "http://example.org/");
            model.forEach(writer::handleStatement);
            writer.endRDF();
        }
        catch (RDFHandlerException e) {
            // oh no, do something!
        }

        long endTime = System.currentTimeMillis();
        long time = endTime - startTime;

        System.out.println("Execution time: " + time);
    }
    private static void createDocuments() {

        String line;
        String delimiter = ";";
        int numberOfDocuments = 0;
        int numberOfInvalidDocuments = 0;

        try {

            String filename = "/home/veronika/Projects/csv-to-ttl/src/main/resources/SacramentocrimeJanuary2006.csv";
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));

            // The first line is always the column names/predicates/attributes.
            line = bufferedReader.readLine();
            String[] attributes = line.split(delimiter);

            while ((line = bufferedReader.readLine()) != null) {
                String[] csvinput = line.split(delimiter);

                // Skipping resources that is uncomplete.
                if (csvinput.length < attributes.length) {
                    numberOfInvalidDocuments++;
                }

                // Only handling resources who are complete.
                else if (csvinput.length == attributes.length) {

                    Document document = new Document(attributes, csvinput);
                    numberOfDocuments++;
                    model.addAll(document.getModel());
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            System.out.println("Number of documents created: " + numberOfDocuments);
            System.out.println("Number of documents skipped: " + numberOfInvalidDocuments);
        }
    }
}
