package jerco.network;

import java.util.Collections;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author leonidv
 */
public class Node implements Iterable<Node>, Comparable<Node> {
    private static final Logger LOG = LoggerFactory.getLogger(Node.class);

    // Узел не содержит вещества
    final public static int NONE = 0;

    // Узел содержит воздух
    final public static int OXYGEN = 1;

    // Узел содержит воду
    final public static int WATER = 2;

    /**
     * Default label object
     */
    final public static String NO_LABEL = "";

    private static int idCounter = 0;

    /**
     * Метод генерирует уникальный идентификатор узла
     * 
     * @return - идентификатор узла
     */
    private static int nextId() {
        return idCounter++;
    }

    /**
     * Метод сбрасывает счетчик узлов.
     */
    static void resetIdCounter() {
        idCounter = 0;
    }

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

        a.linkTo(b);
        b.linkTo(a);
    }

    /**
     * Метод возвращает текущее значение счетчика узлов. Применяется при
     * модульном тестировании
     * 
     * @return - количество созданных узлов
     * @uml.property name="idCounter"
     */
    static public int getIdCounter() {
        return idCounter;
    }

    // Уникальный идентификатор узла. В первую очередь нужен для операций
    // сравнения и подсчета хэша
    final private int id;

    private String label = NO_LABEL;

    // Связанные с этим узлом узлы
    private NavigableSet<Node> linkedNodes = new TreeSet<Node>();

    // Флажок посещения узла
    private boolean visited;

    // Флажок зараженности узла
    private boolean infected;

    // Содержит флажок, принадлежит узел какому-нибудь кластеру или нет
    private boolean inCluster;

    // Содержит флажок, принадлежит ли узел перколяционному кластеру
    private boolean inPercolationCluster;

    /**
     * Флажок, принадлежит ли узел границе или нет.
     */
    private boolean inBound;

    /**
     * Номер границы, если узел ей принадлежит.
     */
    private int bound;

    /**
     * Сопоставленная узлу вероятность.
     */
    private double probability;

    // Идентификатор вещества, которое содержит узел
    private int substance = NONE;

    /**
     * Create node with deafault unique id.
     */
    public Node() {
        this(nextId());
    }

    /**
     * Create node with given id. It's highly recommend use this method only in
     * testing purporse.
     * 
     * @param id
     */
    public Node(int id) {
        this.id = id;
    }

    /**
     * Create node with given label and default (next) id. * @param label
     */
    public Node(String label) {
        this();
        this.label = label;
    }

    /**
     * Возвращает, заражен узел или нет
     * 
     * @return - истина, если узел заражен
     * @uml.property name="infected"
     */
    public boolean isInfected() {
        return infected;
    }

    /**
     * Устанавливает флажок зараженности узла
     * 
     * @param infected
     * @uml.property name="infected"
     */
    public void setInfected(boolean infected) {
        this.infected = infected;
        if (!this.infected) {
            setInCluster(false);
            setSubstance(Node.NONE);
        }
    }

    /**
     * Возвращает, был узел уже посещен или нет
     * 
     * @return
     * @uml.property name="visited"
     */
    public boolean isVisited() {
        return visited;
    }

    /**
     * Устанавливает, был узел уже посещен или нет
     * 
     * @param visited
     * @uml.property name="visited"
     */
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    /**
     * Связывает текущий узел с переданным
     * 
     * @param node
     */
    public void linkTo(Node node) {
        linkedNodes.add(node);
    }

    /**
     * Возвращает итератор по связанным с этим узлом узлам.
     * 
     * @return
     */
    public Iterator<Node> iterator() {
        return linkedNodes.iterator();
    }

    /**
     * Возвращает неизменяемое множество всех связанных узлов.
     * 
     * @return the linkedNodes
     */
    public Set<Node> getLinkedNodes() {
        return Collections.unmodifiableSet(linkedNodes);
    }

    /**
     * @param linkedNodes
     *            the linkedNodes to set
     */
    public void setLinkedNodes(NavigableSet<Node> linkedNodes) {
        this.linkedNodes = linkedNodes;
    }

    /**
     * Возвращает идентификатор узла
     * 
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Return node label, that used only for user-friendly display.
     * 
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Set node lable, that used only for user-friendly display.
     * 
     * @param label
     *            the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Возвращает истину, если узел принадлежит какой-либо границе.
     * <p>
     * Значение устанавливается при вызове функции {@link #setBound(int)}.
     * 
     * @return the inBound
     */
    public boolean isInBound() {
        return inBound;
    }

    /**
     * Возвращает номер границы, к которой принадлежит узел.
     * 
     * @return номер границы, к которой принадлежит узел.
     * @throws IllegalStateException
     *             если inBound == false.
     */
    public int getBound() {
        if (!inBound) {
            throw new IllegalStateException("Узел не принадлежит границе");
        }
        return bound;
    }

    /**
     * @param bound
     *            the bound to set
     */
    public void setBound(int bound) {
        this.bound = bound;
        this.inBound = true;
    }

    /**
     * Удаляет узел из границы.
     */
    public void removeFromBound() {
        this.inBound = false;
    }

    /**
     * @return the inPercolationCluster
     */
    public boolean isInPercolationCluster() {
        return inPercolationCluster;
    }

    /**
     * Возвращает истину, если узел уже принадлежит какому-либо кластеру.
     * 
     * @return
     * @uml.property name="inCluster"
     */
    boolean isInCluster() {
        return inCluster;
    }

    /**
     * Устанавливает принадлежность какому-либо кластеру.
     * 
     * @param inCluster
     * @uml.property name="inCluster"
     */
    public void setInCluster(boolean inCluster) {
        if (!isInfected() && inCluster) {
            throw new IllegalStateException("Node is not infected");
        }
        this.inCluster = inCluster;
        if (!this.inCluster) {
            setInPercolationCluster(false);
        }
    }

    /**
     * Устанавливает флажок, принадлежит ли узел перколяционному кластеру/
     * 
     * @param inPercolationCluster
     * @uml.property name="inPercolationCluster"
     */
    void setInPercolationCluster(boolean inPercolationCluster) {
        this.inPercolationCluster = inPercolationCluster;
    }

    /**
     * Возвращает вероятность замещения одного вещества другим
     * 
     * @return
     */
    public double getProbability() {
        return probability;
    }

    /**
     * Устанавливае вероятность замещения одного вещества другим
     * 
     * @param probability
     */
    public void setProbability(double displaceProbability) {
        this.probability = displaceProbability;
    }

    /**
     * Возвращает идентификатор содержащегося вещества
     * 
     * @return
     */
    public int getSubstance() {
        return substance;
    }

    /**
     * Устанавливает новое содержащееся вещество
     * 
     * @param substance
     *            - идентификатор вещества
     */
    public void setSubstance(int substance) {
        this.substance = substance;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Node other = (Node) obj;
        if (id != other.id)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Node [id=" + id + ", infected=" + infected + ", probability="
                + probability + ", visited=" + visited + ", inBound=" + inBound
                + ", bound=" + bound + ", inCluster=" + inCluster
                + ", inPercolationCluster=" + inPercolationCluster
                + ", linkedNodes.size=" + linkedNodes.size() + ", substance="
                + substance + "]";
    }

    @Override
    public int compareTo(Node o) {
        return id - o.getId();
    }
}
