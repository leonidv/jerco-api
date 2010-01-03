package jerco.network.generators;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jerco.network.Layer;
import jerco.network.Node;

/**
 * Генератор решетки Кагоме.
 * <p>
 * Для более подробной информации об алгоритме смотрите <a href=
 * "http://vygovskiy.com/xwiki/bin/view/Аспирантура/Генерация+регулярных+структур"
 * > Генерация регулярных структур</a>. Реализация алгоритма учитывает, что
 * разница между слоями 3 и 5, а также 2 и 4 заключается в одном слагаемом при
 * подсчете связей с предыдущим узлом.
 * 
 * @author Leonid Vygovskiy
 * 
 */
public class KagomeGenerator implements NetGenerator {
    @SuppressWarnings("unused")
    private final static Logger LOG = LoggerFactory
            .getLogger(KagomeGenerator.class);

    @Override
    public List<Layer> generate(int width, int height) {
        List<Layer> net = new ArrayList<Layer>(height);

        Layer layer = new Layer(width);
        layer.linkNeighbors();
        net.add(layer);
        for (int layerNumber = 1; layerNumber < height; layerNumber++) {
            Layer previousLayer = net.get(layerNumber - 1);
            switch (getBasePartLayerNumber(layerNumber)) {
            case 3:
            case 5:
                layer = createLinkedLayer(layerNumber, previousLayer, width);
                break;
            case 2:
            case 4:
                layer = createRareLevel(layerNumber, previousLayer, width);
                break;
            }

            net.add(layer);
        }
        return net;
    }

    /**
     * Возвращает номер слоя в базовой части. Нумерация начинается с 1.
     * 
     * @param layerNumber
     * @return
     */
    private int getBasePartLayerNumber(int layerNumber) {
        return (layerNumber % 5) + 1;
    }

    /**
     * Создает связанный слой.
     * 
     * @param layerNumber
     * @param previousLayer
     * @param width
     */
    private Layer createLinkedLayer(int layerNumber, Layer previousLayer,
            int width) {
        Layer layer = new Layer(width);
        layer.linkNeighbors();

        int substraction = 0;
        if (getBasePartLayerNumber(layerNumber) == 5) {
            substraction = 1;
        }

        for (int i = substraction; i < layer.size(); i++) {
            Node a = layer.getNode(i);
            int previousIndex = (i - substraction) / 2;
            if (previousIndex < previousLayer.size()) {
                Node b = previousLayer.getNode(previousIndex);
                Layer.linkNodes(a, b);
            }
        }

        return layer;
    }

    /**
     * Создает связанный слой.
     * 
     * @param layerNumber
     * @param previousLayer
     * @param width
     *            - ширина создаваемой сети
     * @return
     */
    private Layer createRareLevel(int layerNumber, Layer previousLayer,
            int width) {
        Layer layer = new Layer(width / 2);

        int addition;
        if (getBasePartLayerNumber(layerNumber) == 2) {
            addition = 0;
        } else {
            assert (getBasePartLayerNumber(layerNumber) == 4);
            addition = 1;
        }

        for (int i = 0; i < layer.size(); i++) {
            Node a = layer.getNode(i);

            Node b = previousLayer.getNode(i * 2 + addition);
            Layer.linkNodes(a, b);

            b = previousLayer.getNode(i * 2 + 1 + addition);
            Layer.linkNodes(a, b);
        }

        return layer;
    }

    @Override
    public String getName() {
        return "Решетка Кагоме";
    }

}