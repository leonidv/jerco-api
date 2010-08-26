package jerco.network.io;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jerco.network.Node;

/**
 * Очень построй построитель сетей. Сеть определяется во время программирования.
 * <p>
 * В первую очередь предназначен для модульного тестирования.
 * 
 * @author Leonid Vygovskiy
 * 
 */
public class BasicNetGenerator implements NetReader {

    private final Set<Node> nodes = new HashSet<Node>();

    private final Map<Integer, Node> nodesId = new HashMap<Integer, Node>();

    @Override
    public Set<Node> read() throws JercoReaderException {
        return nodes;
    }

    public void add(Node... nodes) {
        for (Node node : nodes) {
            this.nodes.add(node);
            nodesId.put(node.getId(), node);
        }
    }

    public Node node(int id) {
        return nodesId.get(id);
    }
}
