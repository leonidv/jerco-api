package jerco.network.io;

import static jerco.TestUtils.checkBounds;
import static jerco.TestUtils.checkNode;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jerco.network.Node;
import jerco.network.TestBase;
import junit.runner.BaseTestRunner;

public class TestReaders extends TestBase {

    /**
     * Тест просто считывает файл, в котором 2 связанных между собой узла.
     * 
     * <pre>
     * 1 ‒‒ 2
     * </pre>
     */
    protected void test2Nodes(NetReader reader) throws JercoReaderException {
        List<Node> nodes = new ArrayList<Node>(reader.read());
        Collections.sort(nodes);

        checkNode(nodes.get(0), 1, new Integer[] { 2 });
        checkNode(nodes.get(1), 2, new Integer[] { 1 });
        
        assertFalse(nodes.get(0).isInBound());
        assertFalse(nodes.get(1).isInBound());
    }
    /**
     * Тест проверяет связность, заданную на вкладке "структура". Также
     * используется не числовый символ для обозначения связи.
     * <p>
     * Заданная структура сети (* помечены ключевые узлы):
     * 
     * <pre>
     *     1*
     *     │
     * 5‒‒‒6‒‒‒2
     *    ╱ ╲
     *   4‒‒‒3*
     * </pre>
     */
    protected void testStar(NetReader reader) throws JercoReaderException {
        List<Node> nodes = new ArrayList<Node>(reader.read());
        Collections.sort(nodes);

        checkNode(nodes.get(0), 1, new Integer[] { 6 });
        checkNode(nodes.get(1), 2, new Integer[] { 6 });
        checkNode(nodes.get(2), 3, new Integer[] { 6, 4 });
        checkNode(nodes.get(3), 4, new Integer[] { 6, 3 });
        checkNode(nodes.get(4), 5, new Integer[] { 6 });

        checkNode(nodes.get(5), 6, new Integer[] { 1, 2, 3, 4, 5 });
        
        checkBounds(0, nodes.get(0));
        checkBounds(1, nodes.get(2));
    }
}
