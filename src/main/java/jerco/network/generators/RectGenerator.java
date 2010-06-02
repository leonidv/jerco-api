package jerco.network.generators;

import java.util.ArrayList;
import java.util.List;

import jerco.network.Layer;
import jerco.network.Node;
import jerco.network.generators.RegularWrapper;

/**
 * Генератор создания прямоугольной реетки. Первый созданный слой является
 * верхней границей, последний созданный слой является нижней границей.
 *
 * @author Leonid Vygovskiy
 * 
 */
public final class RectGenerator extends BaseGenerator implements NetGenerator {
    final public static RectGenerator INSTANCE = new RectGenerator();

    public RectGenerator() {

    }

    public List<Layer> generate(int width, int height) {
        List<Layer> layers = new ArrayList<Layer>(height);

        /*
         * Create first table layer.
         */
        Layer currentLayer = new Layer(width);
        currentLayer.linkNeighbors();
        layers.add(currentLayer);

        /*
         * Обрабатываем все последующие слои. Алгоритм: 1. Создать новый слой и
         * сделать его текущим. 2. Попарно соединить точки нового слоя с
         * предыдущим. 3. Соединить соседние точки в текущем слое
         */
        for (int i = 0; i < height - 1; i++) {
            Layer prevousLayer = currentLayer;
            currentLayer = new Layer(width);
            linkLayers(prevousLayer, currentLayer);
            currentLayer.linkNeighbors();
            layers.add(currentLayer);
        }

        // Устанавливаем у узлов признак нахождения в верхнем слое
        for (Node node : layers.get(0)) {
            node.setBound(NetGenerator.TOP_BOUNDS);
        }

        // Устанавливаем у узлов признак нахожднеия в нижнем слое
        for (Node node : layers.get(layers.size() - 1)) {
            node.setBound(NetGenerator.BOTTOM_BOUNDS);
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
        return "Прямоугольная";
    }
}
