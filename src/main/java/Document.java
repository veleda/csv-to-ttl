import org.openrdf.model.IRI;
import org.openrdf.model.Literal;
import org.openrdf.model.Model;
import org.openrdf.model.impl.LinkedHashModel;
import org.openrdf.model.impl.SimpleValueFactory;
import java.util.UUID;

/**
 * Created by noverhei on 19.02.2016.
 *
 */
class Document {

    private SimpleValueFactory factory = SimpleValueFactory.getInstance();
    private String namespace = "http://example.org/";

    private IRI documentId = factory.createIRI(namespace + UUID.randomUUID());

    private Model model = new LinkedHashModel();


    Document(String[] predicates, String[] data) {

        for (int i = 0; i < predicates.length; i++) {
            model.add(documentId, factory.createIRI(
                namespace + predicates[i].substring(0, 1).toLowerCase() + predicates[i].substring(1)),
                factory.createLiteral(data[i])
            );
        }

    }

    Model getModel() {
        return model;
    }

    private void handleAStatement(String predicate, Literal object) {

        DocUtils.getWriter().handleStatement(factory.createStatement(
                documentId,
                factory.createIRI(this.namespace + predicate),
                object
        ));
    }
}
