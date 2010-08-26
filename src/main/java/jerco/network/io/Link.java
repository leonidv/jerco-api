package jerco.network.io;

import jerco.network.Node;

/**
 * Linking between two nodes.
 * 
 * @author Leonid Vygovskiy
 * 
 */
public class Link {
    private final Node a;
    private final Node b;

    public Link(Node a, Node b) {
        super();
        this.a = a;
        this.b = b;
    }

    /**
     * @return the a
     */
    public Node getA() {
        return a;
    }

    /**
     * @return the b
     */
    public Node getB() {
        return b;
    }

   
    @Override
    public int hashCode() {
        return a.hashCode()*b.hashCode();
    }

    /**
     * Links are equals if their has same nodes. So, link(a,b) is equals to
     * link(b,a)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Link other = (Link) obj;

        if (a == null) {
            if (other.a != null)
                return false;
        } else if (!(a.equals(other.a) || a.equals(other.b)))
            return false;
        if (b == null) {
            if (other.b != null)
                return false;
        } else if (!(b.equals(other.b) || b.equals(other.a)))
            return false;
        return true;
    }

}
