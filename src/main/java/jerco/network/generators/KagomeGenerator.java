package jerco.network.generators;

import java.util.ArrayList;
import java.util.List;

import jerco.network.Layer;
import jerco.network.Node;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Генератор решетки Кагоме. Первый слой является верхней границей, последний
 * слой ‒ нижней.
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
    private final static Logger LOG = LoggerFactory
            .getLogger(KagomeGenerator.class);

    @Override
    public List<Layer> generate(int width, int height) {
        List<Layer> layers = new ArrayList<Layer>(height);

        LOG.debug("Generate layer 1/0");
        Layer layer = new Layer(width);
        layer.linkNeighbors();
        layers.add(layer);
        LOG.debug("Generated layer {}",layer);

        for (int layerNumber = 1; layerNumber < height; layerNumber++) {
            Layer previousLayer = layers.get(layerNumber - 1);
            final int basePartLayerNumber = getBasePartLayerNumber(layerNumber);
            LOG.debug("Generate layer {}/{}", basePartLayerNumber, layerNumber);
            switch (basePartLayerNumber) {
            case 1:
            case 3:
            case 5:
                layer = createLinkedLayer(layerNumber, previousLayer, width);
                break;
            case 2:
            case 4:
                layer = createRareLevel(layerNumber, previousLayer, width);
                break;
            }

            layers.add(layer);
            LOG.debug("Generated layer {}",layer);
        }

        for (Node node : layers.get(0)) {
            node.setBound(NetGenerator.TOP_BOUNDS);
        }

        for (Node node : layers.get(layers.size() - 1)) {
            node.setBound(NetGenerator.BOTTOM_BOUNDS);
        }

        return layers;
    }

    /**
     * Возвращает номер слоя в базовой части. Нумерация начинается с 1.
     * 
     * @param layerNumber
     * @return
     */
    private int getBasePartLayerNumber(int layerNumber) {
        return (layerNumber % 4) + 1;
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
        LOG.debug("Generate linked layer #{}", layerNumber);

        Layer layer = new Layer(width);
        layer.linkNeighbors();

        int substraction = 0;
        if (getBasePartLayerNumber(layerNumber) == 1) {
            substraction = 1;
        }

        for (int i = substraction; i < layer.size(); i++) {
            Node a = layer.getNode(i);
            int previousIndex = (i - substraction) / 2;
            if (previousIndex < previousLayer.size()) {
                Node b = previousLayer.getNode(previousIndex);
                Node.linkNodes(a, b);
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

        LOG.debug("Generate rare layer #{}", layerNumber);
        Layer layer;
        int addition;

        final int baseNumber = getBasePartLayerNumber(layerNumber);
        if (baseNumber == 2 || baseNumber == 1) {
            addition = 0;
            layer = new Layer(width / 2);

        } else {
            assert (baseNumber == 4);
            addition = 1;
            if (width % 2 == 0) {
                layer = new Layer((width - 1) / 2);
            } else {
                layer = new Layer(width / 2);
            }
        }

        for (int i = 0; i < layer.size(); i++) {
            Node a = layer.getNode(i);

            Node b = previousLayer.getNode(i * 2 + addition);
            Node.linkNodes(a, b);

            b = previousLayer.getNode(i * 2 + 1 + addition);
            Node.linkNodes(a, b);
        }

        return layer;
    }

    @Override
    public String getName() {
        return "Кагоме";
    }

}
