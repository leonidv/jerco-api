package jerco.network.io;

import static jerco.TestUtils.loadTestFile;
import jerco.network.Node;

import org.junit.Test;

public class TestGraphMLReader extends TestReaders{

    @Test
    public void test2nodes() throws Exception {
        new Node(); // This set up node counter to 1, as is excepted to tests
        GraphMLReader reader = new GraphMLReader(loadTestFile("2nodes.graphml"));
        test2Nodes(reader);
    }
    
    @Test
    public void testStar() throws Exception {
        new Node();
        GraphMLReader reader = new GraphMLReader(loadTestFile("star.graphml"));
        testStar(reader);
    }
}
