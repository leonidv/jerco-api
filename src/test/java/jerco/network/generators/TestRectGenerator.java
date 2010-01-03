package jerco.network.generators;

import static org.junit.Assert.assertTrue;

import java.util.List;

import jerco.network.Layer;
import jerco.network.RegularLattice;
import jerco.network.NetStructureInfo;
import jerco.network.Node;
import jerco.network.TestBase;
import jerco.network.generators.RectGenerator;

import org.junit.Before;
import org.junit.Test;


public class TestRectGenerator extends TestBase {
    private NetStructureInfo structureInfo;
    
    @Before
    public void setUp() {
        structureInfo = new NetStructureInfo();
        structureInfo.setGenerator(RectGenerator.INSTANCE);
    }
    
    /**
     * Проверяет связанность двух слоев:
     * 
     * <pre>
     * 0 1 2
     * | | |
     * 3 4 5
     * </pre>
     */
    @Test
    public void linkLayers() {
        Layer layerA = new Layer(3);
        Layer layerB = new Layer(3);

        BaseGenerator generator = RectGenerator.INSTANCE;
        generator.linkLayers(layerA, layerB);

        checkNode(layerA.getNode(0), 0, new Integer[] { 3 });
        checkNode(layerA.getNode(1), 1, new Integer[] { 4 });
        checkNode(layerA.getNode(2), 2, new Integer[] { 5 });

        checkNode(layerB.getNode(0), 3, new Integer[] { 0 });
        checkNode(layerB.getNode(1), 4, new Integer[] { 1 });
        checkNode(layerB.getNode(2), 5, new Integer[] { 2 });
    }

    /**
     * Проверяет крайний случай решетки, состоящей из одного элемента.
     */
    @Test
    public void testGenerate1x1() {
        structureInfo.setWidth(1);        
        structureInfo.setHeight(1);
        
        RegularLattice net = new RegularLattice(structureInfo);

        List<Layer> layers = net.getLayers();
        checkNetStructure(layers, 1, 1);
        checkLayerLength(layers.get(0).toArray(), 1);
        
    }

    /**
     * Проверяет структуру решетки 2x2:
     * 
     * <pre>
     * 0-1
     * | |
     * 2-3
     * </pre>
     */
    @Test
    public void testGenerate2x2() {
        structureInfo.setWidth(2);
        structureInfo.setHeight(2);
        
        RegularLattice net = new RegularLattice(structureInfo);

        List<Layer> layers = net.getLayers();
        checkNetStructure(layers, 2, 4);

        Node[] nodes = checkLayerLength(layers.get(0).toArray(), 2);
        checkNode(nodes[0], 0, new Integer[] { 1, 2 });
        checkNode(nodes[1], 1, new Integer[] { 0, 3 });

        nodes = checkLayerLength(layers.get(1).toArray(), 2);
        checkNode(nodes[0], 2, new Integer[] { 0, 3 });
        checkNode(nodes[1], 3, new Integer[] { 2, 1 });
    }

    /**
     * Проверяет структуру решетки 3x3:
     * 
     * <pre>
     * 0-1-2
     * | | |
     * 3-4-5
     * | | |
     * 6-7-8
     * </pre>
     */
    @Test
    public void testGenerate3x3() {
        structureInfo.setWidth(3);
        structureInfo.setHeight(3);
        RegularLattice net = new RegularLattice(structureInfo);

        List<Layer> layers = net.getLayers();
        checkNetStructure(layers, 3, 9);

        Node[] nodes = checkLayerLength(layers.get(0).toArray(), 3);
        checkNode(nodes[0], 0, new Integer[] { 1, 3 });
        checkNode(nodes[1], 1, new Integer[] { 0, 2, 4 });

        checkNode(nodes[2], 2, new Integer[] { 1, 5 });

        nodes = checkLayerLength(layers.get(1).toArray(), 3);
        checkNode(nodes[0], 3, new Integer[] { 0, 4, 6 });
        checkNode(nodes[1], 4, new Integer[] { 1, 3, 5, 7 });
        checkNode(nodes[2], 5, new Integer[] { 2, 4, 8 });

        nodes = checkLayerLength(layers.get(2).toArray(), 3);
        checkNode(nodes[0], 6, new Integer[] { 3, 7 });
        checkNode(nodes[1], 7, new Integer[] { 6, 4, 8 });
        checkNode(nodes[2], 8, new Integer[] { 7, 5 });
    }
    
    @Test
    public void testBoundaryNodes() {
        structureInfo.setWidth(3);
        structureInfo.setHeight(3);        
        RegularLattice net = new RegularLattice(structureInfo); 
        for (Node node : net.getLayers().get(0)) {
            assertTrue(node.isInTopBound());
        }
        
        for (Node node : net.getLayers().get(2)) {
            assertTrue(node.isInBottomBound());
        }
        
        
    }
}