package jerco.network;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;

import jerco.network.Cluster;
import jerco.network.RegularLattice;
import jerco.network.Node;
import jerco.network.RegularLattice.BadNetFileFormatException;

import org.junit.Before;
import org.junit.Test;


/**
 * Тестирование алгоритма построения одного кластера. Тестирование
 * осуществляется на прямоугольной решетке следующего вида:
 * 
 * <pre>
 *  1 0 2 0 3
 *  0 0 2 0 3
 *  2 2 2 0 3 
 *  0 0 0 0 3 
 *  4 4 4 0 3
 * </pre>
 * 
 * Цифрами помечен номер теста, который проверяет кластер. Например,
 * testBuild10.
 * 
 * @author leonidv
 * 
 */
public class TestCluster extends TestBase {
    private RegularLattice net;

    @Before
    public void reloadNet() throws Exception {
        net = new RegularLattice();
        net.load(makeTestFile(FILE_CLUSTER_5X5));
        net.resetClusters();

    }

    /**
     * Осуществляет проверку поиска первого кластера. Кластер находится в
     * верхнем левом углу решетки и состоит из одного зараженного узла.
     */
    @Test
    public void testBuild01() {
        final Node node = getNodeByIndex(net, 0, 0);
        checkCluster(node, 1, false, new Integer[] { 0 });
    }

    /**
     * Осуществляет проверку поиска кластера. Кластер начинается в третьем узле
     * первого ряда и состоит из 5 узлов.
     * 
     */
    @Test
    public void testBuild02_top() {
        final Node node = getNodeByIndex(net, 0, 2);
        checkCluster(node, 5, false, new Integer[] { 2, 7, 10, 11, 12 });
    }

    @Test
    public void testBuild02_center() {
        final Node node = getNodeByIndex(net, 2, 2);
        checkCluster(node, 5, false, new Integer[] { 2, 7, 10, 11, 12 });
    }

    /**
     * Осуществляет проверку поиска кластера. Кластер начинается в 5 узле
     * первого ряда и состоит из 5 узлов. Кластер является перколяционным.
     */
    @Test
    public void testBuild03() {
        final Node node = getNodeByIndex(net, 0, 4);
        checkCluster(node, 5, true, new Integer[] { 4, 9, 14, 19, 24 });
    }

    /**
     * Проверяет кластер, который состоит из первых 3 узлов нижнего ряда.
     */
    @Test
    public void testBuild04() {
        final Node node = getNodeByIndex(net, 4, 0);
        checkCluster(node, 3, false, new Integer[] { 20, 21, 22 });
    }

    /**
     * Осуществляет проверку созданного кластера для переданного узла.
     * 
     * @param node -
     *          узел, для которого будет построен кластер
     * @param size -
     *          ожидаемый размер кластера
     * @param percolation -
     *          является ли кластер перколяционным
     * @param nodeIDs -
     *          идентификаторы узлов в кластере
     */
    private void checkCluster(Node node, int size, boolean percolation,
            Integer... nodeIDs) {
        Cluster cluster = new Cluster(node);
        cluster.build();

        assertEquals(String.format(
                "Размер кластера (%d) не равен ожидаемому (%d)", size, cluster
                        .size()), size, cluster.size());

        assertEquals("Не совпало свойство перколяционности кластера",
                percolation, cluster.isPercolation());

        checkNodes(cluster, nodeIDs);
    }

    /**
     * Возвращает узел сети по его индексами
     * 
     * @param net
     * @param layerIndex
     * @param nodeIndex
     * @return
     */
    private Node getNodeByIndex(RegularLattice net, int layerIndex, int nodeIndex) {
        return net.getLayers().get(layerIndex).getNode(nodeIndex);
    }

    @Test
    public void testCompare() {
        // Мелкий хак. Сохраняем указатель на загруженную сеть, а потом
        // поле net начинает указывает на другой объект загруженное сети.
        RegularLattice otherNet = net;

        Iterator<Cluster> iterator = net.getClusters().iterator();
        Iterator<Cluster> otherIterator = otherNet.getClusters().iterator();

        while (iterator.hasNext()) {
            assertTrue(otherIterator.hasNext());
            assertSame(0, iterator.next().compareTo(otherIterator.next()));
        }
    }

}
