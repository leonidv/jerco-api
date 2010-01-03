package jerco.network;

import java.util.Iterator;

import jerco.network.RegularLattice.NodeIterator;


/**
 * Фильтр перебора только зараженных узлов.
 * 
 */
public class InfectedNodeIterator implements Iterator<Node>, Iterable<Node> {
    // Итератор перебора всех узлов в сети
    final private Iterator<Node> nodeIterator;
    
    // Следующей элемент перебора
    private Node nextNode;
    
    /**
     * Создает объект фильтра. На вход принимает
     * @param nodeIterator
     */
    public InfectedNodeIterator(Iterator<Node> nodeIterator) {
        this.nodeIterator = nodeIterator;
    }
    
    @Override
    public boolean hasNext() {
        boolean finded = false;
        
        while(nodeIterator.hasNext() && !finded) {
            Node node = nodeIterator.next();
            if (node.isInfected()) {
                finded = true;
                nextNode = node;
            }
        }
        
        return finded;
    }

    @Override
    public Node next() {
        return nextNode;
    }

    @Override
    public void remove() {
        nodeIterator.remove();            
    }

    @Override
    public Iterator<Node> iterator() {
        return this;
    }
    
}