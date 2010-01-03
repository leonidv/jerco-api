package jerco.network;

import static org.junit.Assert.*;

import java.net.NetworkInterface;
import java.util.Iterator;

import jerco.network.InfectedNodeIterator;
import jerco.network.RegularLattice;
import jerco.network.Node;
import jerco.network.generators.RectGenerator;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class TestInfectedNodeIterator extends TestBase {
    static private RegularLattice net5x5;
    static private RegularLattice netEmpty;
    private InfectedNodeIterator iterator5x5;

    @BeforeClass
    static public void createNet() throws Exception {
        Node.resetIdCounter();
        net5x5 = new RegularLattice();
        net5x5.load(makeTestFile(TestBase.FILE_CLUSTER_5X5));

        Node.resetIdCounter();
        netEmpty = new RegularLattice();
    }

    @Before
    public void createIterators() {
        iterator5x5 = new InfectedNodeIterator(net5x5.iterator());
    }

    private void checkNodesCounts(Iterator<Node> iterator, int count) {
        int counter = 0;
        while (iterator.hasNext()) {
            iterator.next();
            counter++;
        }
        assertEquals(count, counter);
    }

    /**
     * Проверяем, что возвращает определенное количество узлов. В решетке 5x5
     * хранится 14 зараженных узлов.
     */
    @Test
    public void testHasNext() {
        checkNodesCounts(iterator5x5, 14);
        checkNodesCounts(new InfectedNodeIterator(netEmpty.iterator()), 0);
    }

    /**
     * Решетка 5x5 хранит определенные узлы. Проверяем, что все эти узлы будут
     * возвращены итератором.
     */
    @Test
    public void testNext() {
        checkNodes((Iterable<Node>) iterator5x5, 0, 2, 4, 7, 9, 10, 11, 12, 14,
                19, 20, 21, 22, 24);
    }

}
