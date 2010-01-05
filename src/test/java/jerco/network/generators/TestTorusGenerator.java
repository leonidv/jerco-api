package jerco.network.generators;

import static jerco.TestUtils.checkNetStructure;
import static jerco.TestUtils.checkNode;

import java.util.List;

import jerco.network.Layer;
import jerco.network.Node;
import jerco.network.TestBase;

import org.junit.Before;
import org.junit.Test;

public class TestTorusGenerator extends TestBase {

    private CylinderGenerator generator;

    @Before
    public void setUp() {
        generator = new CylinderGenerator();
    }

    @Test
    public void generate_1x1() {
        List<Layer> net = generator.generate(1, 1);

        checkNetStructure(net, 1, 1);

        final Node node = net.get(0).getNode(0);
        System.out.println(node);
        checkNode(node, 0);
    }

    /**
     * <pre>
     * 00 01 02 03 04 
     * 05 06 07 08 09
     * 10 11 12 13 14
     * </pre>
     * 
     * Check connection between:
     * 
     * <pre>
     * 0 - {1, 4, 5}, 4 - {3, 9, 0)
     * </pre>
     * 
     * and so on
     */
    @Test
    public void generate_5x3() {
        List<Layer> net = generator.generate(5, 3);

        checkNetStructure(net, 3, 15);

        Node leftmost = net.get(0).getLeftmost();
        Node rightmost = net.get(0).getRightmost();

        checkNode(leftmost, 0, 1, 5, 4);
        checkNode(rightmost, 4, 3, 9, 0);

        leftmost = net.get(1).getLeftmost();
        rightmost = net.get(1).getRightmost();

        checkNode(leftmost, 5, 0, 6, 10, 9);
        checkNode(rightmost, 9, 4, 8, 14, 5);

        leftmost = net.get(2).getLeftmost();
        rightmost = net.get(2).getRightmost();

        checkNode(leftmost, 10, 5, 11, 14);
        checkNode(rightmost, 14, 9, 13, 10);
    }
}
