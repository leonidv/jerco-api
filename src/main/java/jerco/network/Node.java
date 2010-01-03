package jerco.network;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author leonidv
 */
public class Node implements Iterable<Node> {
    // Узел не содержит вещества
    final public static int NONE = 0;

    // Узел содержит воздух
    final public static int OXYGEN = 1;

    // Узел содержит воду
    final public static int WATER = 2;

    /**
     * @uml.property name="idCounter"
     */
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
     * Метод сбрасывает счетчик узлов. Используется при модульном тестировании.
     */
    static void resetIdCounter() {
        idCounter = 0;
    }

    /**
     * Метод возвращает текущее значение счетчика узлов. Применяется при
     * модульном тестировании
     * 
     * @return - количество созданных узлов
     * @uml.property name="idCounter"
     */
    static int getIdCounter() {
        return idCounter;
    }

    // Уникальный идентификатор узла. В первую очередь нужен для операций
    // сравнения и подсчета хэша
    /**
     * @uml.property name="id"
     */
    final private int id = nextId();

    // Связанные с этим узлом узлы
    private List<Node> linkedNodes = new ArrayList<Node>();

    // Флажок посещения узла
    /**
     * @uml.property name="visited"
     */
    private boolean visited;

    // Флажок зараженности узла
    /**
     * @uml.property name="infected"
     */
    private boolean infected;

    // Содержит флажок, принадлежит узел какому-нибудь кластеру или нет
    /**
     * @uml.property name="inCluster"
     */
    private boolean inCluster;

    // Содержит флажок признака, принадлежит ли узел верхней границе сети
    /**
     * @uml.property name="inTopBound"
     */
    private boolean inTopBound;

    // Содержит флажок, принадлежит ли узел нижней границе сети
    /**
     * @uml.property name="inBottomBound"
     */
    private boolean inBottomBound;

    // Содержит флажок, принадлежит ли узел перколяционному кластеру
    /**
     * @uml.property name="inPercolationCluster"
     */
    private boolean inPercolationCluster;

    // Содержит вероятность замещения одного вещества другим
    private double displaceProbability;

    // Идентификатор вещества, которое содержит узел
    private int substance = NONE;

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
    public void addLinkedNode(Node node) {
        if (linkedNodes.contains(node)) {
            return;
        }

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
     * Возвращает идентификатор узла
     * 
     * @return
     * @uml.property name="id"
     */
    public int getId() {
        return id;
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
     * Возвращает истину, если узел принадлежит верхней границе сети
     * 
     * @return
     * @uml.property name="inTopBound"
     */
    public boolean isInTopBound() {
        return inTopBound;
    }

    /**
     * Устанавливает, принадлежит ли узел нижней границе сети или нет
     * 
     * @param boundary
     * @uml.property name="inTopBound"
     */
    public void setInTopBound(boolean boundary) {
        this.inTopBound = boundary;
    }

    /**
     * Возвращает истину, если узел принадлежит нижней границе сети
     * 
     * @return
     * @uml.property name="inBottomBound"
     */
    public boolean isInBottomBound() {
        return inBottomBound;
    }

    /**
     * Устанавливает, принадлежит ли узел нижней границе сети
     * 
     * @param inBottomBound
     * @uml.property name="inBottomBound"
     */
    public void setInBottomBound(boolean inDownBound) {
        this.inBottomBound = inDownBound;
    }

    /**
     * Возвращает истину, если узел находится в перколяционном кластере
     * 
     * @return истина, если узел принадлежит перколяционному кластеру
     * @uml.property name="inPercolationCluster"
     */
    public boolean isInPercolationCluster() {
        return inPercolationCluster;
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
    public double getDisplaceProbability() {
        return displaceProbability;
    }

    /**
     * Устанавливае вероятность замещения одного вещества другим
     * 
     * @param displaceProbability
     */
    public void setDisplaceProbability(double displaceProbability) {
        this.displaceProbability = displaceProbability;
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
        return String.format(
                "Node id = %d, infected = %b, visited = %b, substance = %d",
                id, infected, visited, substance);
    }
}
