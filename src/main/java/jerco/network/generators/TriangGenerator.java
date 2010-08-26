package jerco.network.generators;

import java.util.List;

import jerco.network.Layer;
import jerco.network.Node;

class TriangGenerator implements NetGenerator {
    final public static TriangGenerator INSTANCE = new TriangGenerator();

    public TriangGenerator() {

    }

    void addlinkLayers(Layer a, Layer b) {
        assert (a.size() == b.size()) : "Количество узлов в слоях не равно";
        int size = a.size();
        for (int i = 0; i < size - 1; i++) {
            Node.linkNodes(a.getNode(i), b.getNode(i + 1));// соединяем по
                                                           // диагонали
        }

    }

    public List<Layer> generate(int width, int height) {
        List<Layer> layers = RectGenerator.INSTANCE.generate(width, height);

        for (int i = 0; i < height - 1; i++) {
            addlinkLayers(layers.get(i), layers.get(i + 1));
        }

        return layers;
    }

    /**
     * Сравнение генераторов осуществляется на основе их класса
     */
    @Override
    public boolean equals(Object obj) {
        return obj.getClass() == this.getClass();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "Треугольная";
    }

}
