package jerco.network.generators;

import java.util.List;

import jerco.network.Layer;


/**
 * Create tor network. Tor is net with connected leftmost and rightmost nodes.
 * You can give another {@link NetGenerator}. Default base generator is
 * {@link RectGenerator}
 * 
 * @author leonidv
 * 
 */
public class CylinderGenerator implements NetGenerator {
    /**
     * Base generator.
     */
    private NetGenerator baseGenerator = RectGenerator.INSTANCE;

    /**
     * Create object with {@link RectGenerator} as base generator
     */
    public CylinderGenerator() {

    };

    /**
     * Create object with given base generator. This generator uses for creating
     * base net, that row's sides will be connected node-per-node.
     * 
     * @param baseGenerator
     */
    public CylinderGenerator(NetGenerator baseGenerator) {
        this.baseGenerator = baseGenerator;
    }

    @Override
    public List<Layer> generate(int width, int height) {
        List<Layer> torus = RectGenerator.INSTANCE.generate(width, height);

        for (Layer layer : torus) {
            Layer.linkNodes(layer.getLeftmost(), layer.getRightmost());
        }
        return torus;
    }

    public NetGenerator getBaseGenerator() {
        return baseGenerator;
    }

    public void setBaseGenerator(NetGenerator baseGenerator) {
        this.baseGenerator = baseGenerator;
    }

    @Override
    public String getName() {
        return "Цилиндр";
    }
}
