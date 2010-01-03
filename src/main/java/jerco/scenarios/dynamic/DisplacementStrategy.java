package jerco.scenarios.dynamic;

import java.util.Collection;

import jerco.network.RegularLattice;
import jerco.network.Node;


/**
 * Стратегия определения, в какие узлы перетечет вещество из текущего узла
 * <p>
 * О различных видах стратегий см.
 * http://www.assembla.com/wiki/show/netpercolation/РеализацияВытеснения
 * 
 * @author leonidv
 * 
 */
public interface DisplacementStrategy {

    /**
     * Определяет, какие узлы будут замещены в результате заражения
     * 
     * @param sourceNode
     * @return
     */
    public NextNodesInfo findDestinationNodes(Node sourceNode);
}
