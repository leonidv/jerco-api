package jerco.network;

import java.util.Iterator;
import java.util.List;

import jerco.network.RegularLattice.NodeIterator;

/**
 * Описывает базовое понятие решетки и доступные с ней действия.
 * @author Leonid Vygovskiy
 *
 */
public interface Net extends Iterable<Node> {

    /**
     * Возвращает итератор перебора всех узлов в сети.
     * 
     * @return
     */
    public Iterator<Node> iterator();

    /**
     * Осуществляет заражение сети по методу <font color="red">Бернулли</font>
     * <p/>
     * При этом методе перколяция осуществляется следующим способом. Каждый узел
     * с заданной вероятностью подвергается возможности заражения.
     * 
     * @param p
     *            вероятнось заражения узла.
     */
    public void infect(double p);

    /**
     * Осуществляет поиск кластеров. В конце поиска кластеры ранжируются по
     * возрастанию мощности (размера) кластера.
     * 
     * @return количество кластеров в сети
     */
    public int findClusters();

    /**
     * Возвращает количество узлов в решетке.
     * 
     * @return
     */
    public int size();

    /**
     * Возвращает вероятность заражения узла сети, которая была установлена при
     * последнем заражении.
     * 
     * @return вероятность, с который были заражены узлы в последний раз.
     *         Double.NaN, в случае, если операция заражения не осуществлялась.
     */
    public double getInfectProbability();

    /**
     * Возвращает неизменяемое множество кластеров сети
     * 
     * @return - неизменяемое множество кластеров
     * @uml.property name="clusters"
     */
    public List<Cluster> getClusters();

    /**
     * Возвращает истину, если имеется хотя бы один перколяционный кластер.
     * 
     * @return
     */
    public boolean hasPercolationCluster();

    /**
     * Осуществляет полный сброс информации о сети.
     */
    public void reset();

}