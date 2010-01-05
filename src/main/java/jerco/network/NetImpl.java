package jerco.network;

import static jerco.Constants.DOUBLE_PRECISION;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
class NetImpl implements Net {

    /**
     * Множество узлов, которые представляют собой сеть.
     */
    protected Set<Node> nodes;

    /**
     * Вероятность заражения сети
     */
    protected double infectProbability = Double.NaN;

    /**
     * Кластеры в сети
     */
    protected List<Cluster> clusters;

    /**
     * Метод нужен чтобы сильно не ломать RegularLattice
     */
    @Deprecated
    public NetImpl() {
        
    }
    
    public NetImpl(NetReader reader) {
        nodes = reader.read();
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
        clusters = new ArrayList<Cluster>();
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
        for (Node node : this) {
            node.setVisited(false);
        }
    }

    /**
     * Осуществляет сбор флага "заражен" у всех узлов сети. Эта операция также
     * приводит к сбросу списка кластеров в сети
     */
    public void resetInfected() {
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
     */
    public boolean hasPercolationCluster() {
        for (Cluster cluster : clusters) {
            if (cluster.isPercolation()) {
                return true;
            }
        }
        return false;
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
        return clusters.size();
    }

}
