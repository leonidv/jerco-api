package jerco.scenarios.dynamic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.List;

import jerco.network.Node;
import jerco.network.RegularLattice;
import jerco.network.TestBase;

import org.junit.Before;
import org.junit.Test;
import static jerco.TestUtils.*;

/**
 * Тестирования алгоритмов сети осуществляется на стратегии абсолютного
 * замещения.
 * 
 * @author leonidv
 * 
 */
public class TestDisplacementScenario extends TestBase {
    private RegularLattice net;
    private DisplacementScenario scenario;

    @Before
    public void setUp() throws Exception {
        net = new RegularLattice();
        net.load(loadTestFile("cluster 5x5 test06.txt"));
        scenario = new DisplacementScenario();
        scenario.setNet(net);
        scenario.setStrategy(new AbsoluteDisplacementStrategy());
        scenario.setDisplaceMean(0.5);
        scenario.setDisplaceDeveration(0.1);

    }

    @Test
    public void testGenerateDisplaceProbability() {

        double lastValue = 0;
        for (int i = 0; i < 100; i++) {
            double value = scenario.generateDisplaceProbability();
            assertTrue(String.valueOf(0.39999),
                    Double.compare(0.399999, value) < 0);
            assertTrue(String.valueOf(0.60001),
                    Double.compare(0.60001, value) > 0);
            assertFalse("Compare with previous value", Double.compare(value,
                    lastValue) == 0);
            lastValue = value;
        }
    }

    @Test
    public void testPrepareNet() {
        scenario.prepareNet(net);
        for (Node node : net) {
            if (node.isInPercolationCluster()) {
                assertEquals(Node.OXYGEN, node.getSubstance());
            } else {
                assertEquals(Node.NONE, node.getSubstance());
            }
        }
    }

    /**
     * Карта вероятности заражения (--- помечены элементы, не входящие в
     * перколяционный кластер, + - точка заражения):
     * 
     * <pre>
     *     -0- -1- -2- -3- -4-
     * -0-  +  0.1 0.2 --- 0.1  
     * -1- 0.2 0.3 0.4 --- 0.1 
     * -2- 0.5 0.6 0.7 0.6 0.4 
     * -3- --- --- --- --- 0.2 
     * -4- 0.7 0.6 0.5 0.4 0.3
     * </pre>
     * 
     */
    private void prepare() {
        setDiplaceProbability(0, 1, 0.1);
        setDiplaceProbability(0, 2, 0.2);
        setDiplaceProbability(0, 4, 0.1);

        setDiplaceProbability(1, 0, 0.2);
        setDiplaceProbability(1, 1, 0.3);
        setDiplaceProbability(1, 2, 0.4);
        setDiplaceProbability(1, 4, 0.1);

        setDiplaceProbability(2, 0, 0.5);
        setDiplaceProbability(2, 1, 0.6);
        setDiplaceProbability(2, 2, 0.7);
        setDiplaceProbability(2, 3, 0.6);
        setDiplaceProbability(2, 4, 0.4);

        setDiplaceProbability(3, 4, 0.2);

        setDiplaceProbability(4, 0, 0.7);
        setDiplaceProbability(4, 1, 0.6);
        setDiplaceProbability(4, 2, 0.5);
        setDiplaceProbability(4, 3, 0.4);
        setDiplaceProbability(4, 4, 0.3);

        final Node startNode = net.getLayers().get(0).getNode(0);
        scenario.addInitialNode(startNode);
    }

    @Test
    public void testMakeStep() {
        prepare();

        Collection<Node> front = checkStep(scenario.getInitialFront(), 0, 1);
        front = checkStep(front, 1, 2, 5);
        front = checkStep(front, 6, 7, 10);
        front = checkStep(front, 11, 12);
        front = checkStep(front, 13);
        front = checkStep(front, 14);
        front = checkStep(front, 9, 14);
        front = checkStep(front, 4, 19);
        front = checkStep(front, 24);
        front = checkStep(front, 23);
        front = checkStep(front, 22);
        front = checkStep(front, 21);
        front = checkStep(front, 20);
        front = checkStep(front);
        assertEquals(0, front.size());

    }

    @Test
    public void testResults() {
        prepare();

        scenario.setSaveImages(true);
        scenario.setFileNameTemplate("target/test-temp-results/06 %05d.png");
        scenario.doScenario();

        assertEquals("Percolation's step is not good", 8,
                scenario.getPercolationStep());
        
        List<StepInfo> result = scenario.getResult();
        for (StepInfo stepInfo : result) {
            System.out.println(stepInfo);
        }
        
        checkStepInfo(result.get(0), 1, 1);
        checkStepInfo(result.get(1), 2, 2);
        checkStepInfo(result.get(2), 3, 4);
        //checkStepInfo(result.get(3), 3, 7);
        checkStepInfo(result.get(4), 2, 9);
        checkStepInfo(result.get(5), 1, 10);
        // checkStep(front,9,14)
        checkStepInfo(result.get(6), 1, 11);
        checkStepInfo(result.get(7), 2, 12);
        checkStepInfo(result.get(8), 2, 14);
        checkStepInfo(result.get(9), 2, 16);
        checkStepInfo(result.get(10), 1, 17);
        // checkStep(front,21,20)
        checkStepInfo(result.get(11), 1, 18);
        checkStepInfo(result.get(12), 1, 19);
        checkStepInfo(result.get(13), 0, 19);
    }

    private void checkStepInfo(StepInfo stepInfo, int front, int total) {
        assertEquals("Количество узлов во фронте не совпало", front, stepInfo
                .getFrontSize());
        assertEquals("Количество замещенных узлов не совпало", total, stepInfo
                .getTotalDisplacement());
    }

    private Collection<Node> checkStep(Collection<Node> front, Integer... nodes) {
        front = scenario.makeStep(net, front);
        checkNodes(front, nodes);
        for (Node node : front) {
            assertEquals("Вещество не совпадает с ожидаемым", Node.WATER, node
                    .getSubstance());
        }
        return front;
    }

    private void setDiplaceProbability(int layer, int column, double p) {
        final Node node = net.getLayers().get(layer).getNode(column);
        node.setProbability(p);
        node.setInfected(true);
        node.setSubstance(Node.OXYGEN);
    }
}
