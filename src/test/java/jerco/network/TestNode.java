package jerco.network;

import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;


public class TestNode {
    private Node node = new Node();
    
    @Before
    public void setUp() {
        node.setInfected(true);
        node.setInCluster(true);
        node.setInPercolationCluster(true);
    }
    
    
    @Test
    public void testSetInfected() {
        node.setInfected(false);
        assertFalse(node.isInCluster());
        assertFalse(node.isInPercolationCluster());
    }
    
    @Test
    public void testSetInCluster() {
        node.setInCluster(false);
        assertFalse(node.isInPercolationCluster());
    }

}
