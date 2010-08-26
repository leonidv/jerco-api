package jerco.network.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import jerco.network.Node;

public class TestLink {
    
    @Test
    public void testEqualsLinks() {
        Node a = new Node(1);
        Node b = new Node(2);
        
        Link ab = new Link(a,b);
        Link ba = new Link(b,a);
        
        assertEquals(ab, ba);
        assertEquals(ba, ab);
        
        assertEquals(ab.hashCode(),ba.hashCode());
    }
    
    @Test
    public void testNotEquals() {
        Node a = new Node(1);
        Node b = new Node(2);
        Node c = new Node(3);
        
        Link ab = new Link(a,b);
        Link ac = new Link(a,c);
        
        assertFalse(ab.equals(ac));
        assertFalse(ac.equals(ab));
        assertFalse(ac.hashCode() == ab.hashCode());
    }
}
