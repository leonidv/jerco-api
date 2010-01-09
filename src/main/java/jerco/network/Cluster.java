package jerco.network;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Класс одного кластера сети. Кластер представляет собой совокупность связанных
 * между собой зараженных узлов сети. В один кластер сети входят все связанные
 * между собой узлы сети. Таким образом, каждый узел сети строго принадлежит
 * одному кластеру.
 * <p>
 * Минимальный размер кластера составляет 1 узел, максимальный размер кластера
 * ограничен количеством узлов в сети.
 * </p>
 * <p>
 * Узлы в кластере хранятся в порядке их добавления в кластер.
 * </p>
 * <p>
 * После создания конструктора необходимо вызвать метод {@link #build()},
 * который отвечает за поиск узлов, входящих в кластер.
 * <p>
 * Класс реализует интерфейс {@link Comparable} . Сравнение осуществляется на
 * основе мощности кластера (количества узлов).
 * </p>
 * 
 * @author leonidv
 */
public class Cluster implements Iterable<Node>, Comparable<Cluster> {

    /**
     * Множество узлов в кластере.
     */
    private List<Node> nodes = new ArrayList<Node>();

    /**
     * Множестов границ, узлов из которых имеет кластер.
     */
    private Set<Integer> bounds = new HashSet<Integer>();

    /**
     * Создает кластер. Кластер всегда состоит из одного узла.
     * 
     * @param node
     *            - первый узел кластера. Если узел уже находится в каком-либо
     *            кластере, кидается исключение {@link IllegalArgumentException}
     * @throws IllegalArgumentException
     *             - в случае, если узел уже находится в каком-либо кластере
     */
    Cluster(Node node) {
        addToCluster(node);
    }

    /**
     * Добавляет в кластер новый узел
     * 
     * @param node
     *            - новый узел кластера. Если узел уже находится в каком-либо
     *            кластере, кидается исключение {@link IllegalArgumentException}
     * @throws IllegalArgumentException
     *             - в случае, если узел уже находится в каком-либо кластере
     *             либо не заражен.
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
        
        if (node.isInBound()) {
            bounds.add(node.getBound());
        }
    }

    /**
     * Реализует функцию построения кластера. Возвращает количество узлов в
     * новом кластере.
     * 
     * @return возвращает количество узлов в новом кластере
     * 
     */
    public int build() {
        for (int i = 0; i < nodes.size(); i++) {
            Node clusterNode = nodes.get(i);

            for (Node linkedNode : clusterNode) {
                // Проверка на включение в кластер нужна, т.к. один узел
                // может быть связан с несколькими узлами в одном кластере
                if (linkedNode.isInfected() && !linkedNode.isInCluster()) {
                    addToCluster(linkedNode);
                }
            }
        }
        return nodes.size();
    }

    /**
     * Возвращает количество узлов в кластере
     * 
     * @return
     */
    public int size() {
        return nodes.size();
    }

    /**
     * Возвращает неизменяемый список границ.
     * 
     * @return the bounds
     */
    public Set<Integer> getBounds() {
        return Collections.unmodifiableSet(bounds);
    }

    @Override
    public Iterator<Node> iterator() {
        return nodes.iterator();
    }

    /**
     * Возвращает неизменяемый список узлов в кластере
     * 
     * @return
     * @uml.property name="nodes"
     */
    public List<Node> getNodes() {
        return Collections.unmodifiableList(nodes);
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
