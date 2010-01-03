package jerco.network;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Класс одного кластера сети. Кластер представляет собой совокупность связанных между собой зараженных узлов сети. В один кластер сети входят все связанные между собой узлы сети. Таким образом, каждый узел сети строго принадлежит одному кластеру. <p> Минимальный размер кластера составляет 1 узел, максимальный размер кластера ограничен количеством узлов в сети. </p> <p> Узлы в кластере хранятся в порядке их добавления в кластер. </p> <p> Класс реализует интерфейс  {@link Comparable} . Сравнение осуществляется на основе мощности кластера (количества в нем узлов). </p>
 * @author  leonidv
 */
public class Cluster implements Iterable<Node>, Comparable<Cluster> {
    // Множество узлов в кластере
    /**
     * @uml.property  name="nodes"
     */
    private List<Node> nodes = new ArrayList<Node>();

    // Флажок того, что кластер является перколяционным
    /**
     * @uml.property  name="percolation"
     */
    private boolean percolation;

    /**
     * Создает кластер. Кластер всегда состоит из одного узла.
     * 
     * @param node -
     *          первый узел кластера. Если узел уже находится в каком-либо
     *          кластере, кидается исключение {@link IllegalArgumentException}
     * @throws IllegalArgumentException -
     *           в случае, если узел уже находится в каком-либо кластере
     */
    Cluster(Node node) {
        addToCluster(node);
    }

    /**
     * Добавляет в кластер новый узел
     * 
     * @param node -
     *          новый узел кластера. Если узел уже находится в каком-либо
     *          кластере, кидается исключение {@link IllegalArgumentException}
     * @throws IllegalArgumentException -
     *           в случае, если узел уже находится в каком-либо кластере либо не
     *           заражен.
     */
    private void addToCluster(Node node) {
        if (node.isInCluster()) {
            throw new IllegalArgumentException(
                    "Узел уже находится в каком-то кластере");
        }

        if (!node.isInfected()) {
            throw new IllegalArgumentException(
                    "Узел не заражен. Кластеры создаются из зараженных узлов");
        }
        nodes.add(node);
        node.setInCluster(true);
    }

    /**
     * Реализует функцию построения кластера. Возвращает количество узлов в
     * новом кластере.
     * 
     * @return возвращает количество узлов в новом кластере
     * 
     */
    public void build() {
        Node firstNode = nodes.get(0);

        boolean hasNodeInTopBound = firstNode.isInTopBound();
        boolean hasNodeInBottomBound = firstNode.isInBottomBound();

        for (int i = 0; i < nodes.size(); i++) {
            Node clusterNode = nodes.get(i);

            // Перебираем все связанные с этим узлом узлом
            for (Node linkedNode : clusterNode) {
                // Проверка на включение в кластер нужна, т.к. на один узел
                // может быть связан с несколькими узлами в одном кластере
                if (linkedNode.isInfected() && !linkedNode.isInCluster()) {
                    addToCluster(linkedNode);

                    if (linkedNode.isInTopBound()) {
                        hasNodeInTopBound = true;
                    }
                    if (linkedNode.isInBottomBound()) {
                        hasNodeInBottomBound = true;
                    }
                }
            }
        }
        /*
         * Разбираемся с перколяционным кластером. Смотрим, является ли он
         * перколяционным. Если является, задаем каждому узлу кластера признак
         * принадлежности перколяционному кластеру.
         */
        percolation = hasNodeInTopBound && hasNodeInBottomBound;
        if (percolation) {
            for (Node node : this) {
                node.setInPercolationCluster(true);
            }
        }
    }

    /**
     * Возвращает количество узлов в кластере
     * 
     * @return
     */
    public int size() {
        return nodes.size();
    }

    @Override
    public Iterator<Node> iterator() {
        return nodes.iterator();
    }

    /**
     * Возвращает неизменяемый список узлов в кластере
     * @return
     * @uml.property  name="nodes"
     */
    public List<Node> getNodes() {
        return Collections.unmodifiableList(nodes);
    }

    /**
     * Возвращает, является кластер перколяционным или нет. <p> Кластер называется перколяционным, если он соединяет грани структуры. В случае структурируемой решетки границами структуры считается верхний и нижний слой сети. </p>
     * @return
     * @uml.property  name="percolation"
     */
    public boolean isPercolation() {
        return percolation;
    }

    /**
     * Кластеры считаются равными в том случае, если они содержать одинаковое
     * количество зараженных узлов.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Cluster)) {
            return false;
        }

        Cluster other = (Cluster) obj;
        return other.size() == size();
    }

    @Override
    public int hashCode() {
        return nodes.size();
    }

    @Override
    public int compareTo(Cluster o) {
        return (this.size() - o.size());
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("cluster size = "
                + nodes.size());

        for (Node node : this) {
            result.append(" [");
            result.append(node);
            result.append("]");
        }

        return result.toString();
    }

}
