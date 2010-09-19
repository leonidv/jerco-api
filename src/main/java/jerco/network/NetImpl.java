package jerco.network;

import static jerco.Constants.DOUBLE_PRECISION;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import jerco.network.io.JercoReaderException;
import jerco.network.io.NetReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Описывает сеть состояющую из множества узлов. Реализует операции заражения и
 * поиска кластеров.
 * 
 * Способ заполнения узлов не определен и оставляется на усмотрение
 * класса-потомка.
 * 
 * @author Leonid Vygovskiy
 * 
 */
public class NetImpl implements Net {
    private static final Logger LOG = LoggerFactory.getLogger(NetImpl.class);

    /**
     * Множество узлов, которые представляют собой сеть.
     */
    protected Set<Node> nodes = new HashSet<Node>();

    /**
     * Вероятность заражения сети
     */
    protected double infectProbability = Double.NaN;

    /**
     * Кластеры в сети
     */
    protected List<Cluster> clusters = new ArrayList<Cluster>();

    /**
     * Найденные перколяционные кластеры.
     */
    private List<Cluster> percolationClusters;

    /**
     * Границы в сети.
     */
    private Set<Integer> bounds = new HashSet<Integer>();

    /**
     * Метод нужен чтобы сильно не ломать RegularLattice
     */
    @Deprecated
    public NetImpl() {

    }

    public NetImpl(NetReader reader) throws JercoReaderException {
        nodes = reader.read();
        findBounds();
    }

    /**
     * Осуществляет поиск границ в сети.
     * <p>
     * private-метод для вызова в конструкторе.
     */
    final protected void findBounds() {
        for (Node node : this) {
            if (node.isInBound()) {
                bounds.add(node.getBound());
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public Iterator<Node> iterator() {
        return nodes.iterator();
    }

    /**
     * Реализует простейший способ перколяции, основываясь только на переданной
     * вероятности и случайных числах.
     * <p>
     * После заражения ищет кластеры.
     */
    public void infect(double p) {
        infectProbability = p;
        for (Node node : this) {
            boolean infected = (infectProbability - Math.random()) > DOUBLE_PRECISION;
            node.setInfected(infected);
        }
        findClusters();
    }

    /**
     * {@inheritDoc}
     */
    public int findClusters() {
        resetClusters();
        for (Node node : this) {
            if (!node.isInCluster() && node.isInfected()) {
                Cluster cluster = new Cluster(node);
                cluster.build();
                clusters.add(cluster);
            }
        }
        Collections.sort(clusters);
        return clusters.size();
    }

    /**
     * Осуществляет сброс кластеров сети. При этом создается новый список
     * кластеров, а все узлы помечаются как не находящиеся в каком-либо кластере
     */
    public void resetClusters() {
        LOG.debug("reset clusters");
        clusters = new ArrayList<Cluster>();
        percolationClusters = null;
        for (Node node : this) {
            node.setInCluster(false);
        }
    }

    /**
     * {@inheritDoc}
     */
    public double getInfectProbability() {
        return infectProbability;
    }

    /**
     * Осуществляет сбор флага "посещено" у всех узлов сети.
     */
    public void resetVisited() {
        LOG.debug("reset visited");
        for (Node node : this) {
            node.setVisited(false);
        }
    }

    /**
     * Осуществляет сбор флага "заражен" у всех узлов сети. Эта операция также
     * приводит к сбросу списка кластеров в сети
     */
    public void resetInfected() {
        LOG.debug("reset infected");
        for (Node node : this) {
            node.setInfected(false);
        }
        clusters.clear();
    }

    /**
     * {@inheritDoc}
     */
    public List<Cluster> getClusters() {
        return Collections.<Cluster> unmodifiableList(clusters);
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IllegalStateException
     *             в случае, если границы не были заданы.
     */
    public boolean hasPercolationCluster() {
        if (bounds.isEmpty()) {
            final String msg = "Your try to check percolatin cluster, "
                    + "but net doesn't have bounds";
            LOG.warn(msg);
            throw new IllegalStateException(msg);
        }

        percolationClusters = new ArrayList<Cluster>(1);
        for (Cluster cluster : clusters) {
            if (cluster.getBounds().equals(bounds)) {
                percolationClusters.add(cluster);
            }
        }

        return !percolationClusters.isEmpty();
    }

    @Override
    public List<Cluster> getPercolationClusters() throws IllegalStateException {
        if (percolationClusters == null) {
            throw new IllegalStateException(
                    "You must to call hasPercolationCluster() before "
                            + "call this method");
        }

        return percolationClusters;
    }

    /**
     * {@inheritDoc}
     */
    public void reset() {
        resetClusters();
        resetVisited();
        resetInfected();
    }

    /**
     * {@inheritDoc}
     * 
     * @return
     * @see java.util.List#size()
     */
    public int size() {
        return nodes.size();
    }

    @Override
    public int boundsCounts() {       
        return bounds.size();
    }

}
