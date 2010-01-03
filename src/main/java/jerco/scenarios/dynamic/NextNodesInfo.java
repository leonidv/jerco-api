package jerco.scenarios.dynamic;

import java.util.Collection;
import java.util.HashSet;

import jerco.network.Node;


/**
 * Класс для передачи результата из процедуры поиска следующего узла.
 * {@link DisplacementScenario#findDestinationNode(Node) 
 * 
 */
public final class NextNodesInfo {
    // Узел, в которой перетечет вещество
    private Collection<Node> nextNodes = new HashSet<Node>();
    // Общее количество подходящих узлов
    private int findedCount;
    
    private boolean currentToNext;
    
    public Collection<Node> getNextNodes() {
        return nextNodes;
    }
    
    public void addNextNode(Node node) {
        nextNodes.add(node);
    }
    
    public int getFindedCount() {
        return findedCount;
    }
    
    public void setFindedCount(int findedCount) {
        this.findedCount = findedCount;
    }
    
    public void incFindedCount() {
        findedCount++;
    }

    public boolean isCurrentToNext() {
        return currentToNext;
    }

    public void setCurrentToNext(boolean currentToNext) {
        this.currentToNext = currentToNext;
    }
    
    
}