package jerco.network.generators;

import java.util.List;

import jerco.network.Layer;
import jerco.network.TestBase;
import static jerco.TestUtils.*;
import org.junit.Test;

public class TestKagomeGenerator extends TestBase {

    @Test
    public void test1x1() {
        KagomeGenerator generator = new KagomeGenerator();
        List<Layer> layers = generator.generate(1, 1);
        checkNetStructure(layers, 1, 1);
    }

    @Test
    public void test3x5() {
        KagomeGenerator generator = new KagomeGenerator();
        List<Layer> layers = generator.generate(3, 5);

        checkNetStructure(layers, 5, 11);
        Layer layer = layers.get(0);
        checkNode(layer.getNode(0), 0, new Integer[] { 1, 3 });
        checkNode(layer.getNode(1), 1, new Integer[] { 0, 3, 2 });
        checkNode(layer.getNode(2), 2, new Integer[] { 1 });
        checkBounds(NetGenerator.TOP_BOUNDS, layer.toArray());
        
        layer = layers.get(1);
        checkNode(layer.getNode(0), 3, new Integer[] { 0, 1, 4, 5 });

        layer = layers.get(2);
        checkNode(layer.getNode(0), 4, new Integer[] { 3, 5 });
        checkNode(layer.getNode(1), 5, new Integer[] { 3, 4, 6, 7 });
        checkNode(layer.getNode(2), 6, new Integer[] { 5, 7 });

        layer = layers.get(3);
        checkNode(layer.getNode(0), 7, new Integer[] { 5, 6, 9, 10 });

        layer = layers.get(4);
        checkNode(layer.getNode(0), 8, new Integer[] { 9 });
        checkNode(layer.getNode(1), 9, new Integer[] { 8, 7, 10 });
        checkNode(layer.getNode(2), 10, new Integer[] { 7, 9 });
        checkBounds(NetGenerator.BOTTOM_BOUNDS, layer.toArray());
    }

    @Test
    public void test5x4() {
        KagomeGenerator generator = new KagomeGenerator();
        List<Layer> layers = generator.generate(5, 5);

        checkNetStructure(layers, 5, 19);

        Layer layer = layers.get(0);
        checkNode(layer.getNode(0), 0, new Integer[] { 1, 5 });
        checkNode(layer.getNode(1), 1, new Integer[] { 0, 5, 2 });
        checkNode(layer.getNode(2), 2, new Integer[] { 1, 6, 3 });
        checkNode(layer.getNode(3), 3, new Integer[] { 2, 6, 4 });
        checkNode(layer.getNode(4), 4, new Integer[] { 3 });

        layer = layers.get(1);
        checkNode(layer.getNode(0), 5, new Integer[] { 0, 1, 7, 8 });
        checkNode(layer.getNode(1), 6, new Integer[] { 2, 3, 9, 10 });

        layer = layers.get(2);
        checkNode(layer.getNode(0), 7, new Integer[] { 5, 8 });
        checkNode(layer.getNode(1), 8, new Integer[] { 7, 5, 9, 12 });
        checkNode(layer.getNode(2), 9, new Integer[] { 8, 6, 10, 12 });
        checkNode(layer.getNode(3), 10, new Integer[] { 9, 6, 11, 13 });
        checkNode(layer.getNode(4), 11, new Integer[] { 10, 13 });

        layer = layers.get(3);
        checkNode(layer.getNode(0), 12, new Integer[] { 8, 9, 15, 16 });
        checkNode(layer.getNode(1), 13, new Integer[] { 10, 11, 17, 18 });

        layer = layers.get(4);
        checkNode(layer.getNode(0), 14, new Integer[] { 15 });
        checkNode(layer.getNode(1), 15, new Integer[] { 14, 12, 16 });
        checkNode(layer.getNode(2), 16, new Integer[] { 15, 12, 17 });
        checkNode(layer.getNode(3), 17, new Integer[] { 16, 13, 18 });
        checkNode(layer.getNode(4), 18, new Integer[] { 17, 13 });
    }

}
