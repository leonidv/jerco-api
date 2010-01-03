package jerco.network;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Layer is basic part of structured net. For example, it's may be interpeted as
 * row in table.
 * 
 * @author Leonid Vygovskiy
 * 
 */
public class Layer implements Iterable<Node> {
    private final static Logger LOG = LoggerFactory.getLogger(Layer.class);

    /**
     * Link nodes each other (a linked to b, b linked to a). If a == b don't do
     * anything.
     * 
     * @param a
     * @param b
     */
    static public void linkNodes(Node a, Node b) {
        LOG.debug(String.format("%d ←→ %d", a.getId(), b.getId()));
        
        if (a == b) {
            return;
        }
        
        a.addLinkedNode(b);
        b.addLinkedNode(a);
    }

    // Список узлов слоя
    private List<Node> nodes;

    /**
     * Создает слой узлов.
     * 
     * @param size
     *            - количество узлов в данном слое
     */
    public Layer(int size) {
        nodes = new ArrayList<Node>(size);
        for (int i = 0; i < size; i++) {
            nodes.add(new Node());
        }
    }

    /**
     * Соединяет соседние узлы слоя между собой. {@code a-b-c} will do link
     * (a<->b) and (b<->c).
     */
    public void linkNeighbors() {
        for (int i = 0; i < nodes.size() - 1; i++) {
            linkNodes(nodes.get(i), nodes.get(i + 1));
        }
    }

    /**
     * Convert layer to array
     * 
     * @return
     */
    public Node[] toArray() {
        return nodes.toArray(new Node[nodes.size()]);
    }

    /**
     * Count nodes in layer.
     * 
     * @return
     */
    public int size() {
        return nodes.size();
    }

    /**
     * Nodes layer iterator
     * 
     * @return
     */
    @Override
    public Iterator<Node> iterator() {
        return nodes.iterator();
    }

    /**
     * A leftmost node is node with lowest index in layer. In table's row is
     * realy leftmost.
     * 
     * @return
     */
    public Node getLeftmost() {
        return nodes.get(0);
    }

    /**
     * A rightmost node is node with higest index in layer. In table's row is
     * realy rightmost.
     * 
     * @return
     */
    public Node getRightmost() {
        return nodes.get(nodes.size() - 1);
    }

    /**
     * Return node by index.
     * 
     * @param index
     * @throws IllegalArgumentException
     *             - передан не допустимый индекс
     * @return
     */
    public Node getNode(int index) throws IllegalArgumentException {
        if ((index < 0) || (index > nodes.size() - 1)) {
            throw new IllegalArgumentException(String.format(
                    "index = %d выходит за допустимый диапазон", index));
        }

        return nodes.get(index);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String separator = "";

        for (Node node : this) {
            result.append(separator);
            result.append(node.getId());
            separator = ", ";
        }

        return result.toString();
    }

}
