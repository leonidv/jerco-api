package jerco.network;

import org.junit.Before;
import org.junit.Ignore;

@Ignore
public class TestBase {
    protected static final String FILE_CLUSTER_5X5 = "cluster 5x5 test01.txt";

    @Before
    public void initNodeCounter() {
        Node.resetIdCounter();
    }

}