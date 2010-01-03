package jerco.view;

import jerco.network.Node;

/**
 * Реализует вспомогательный класс-хранилище узла сети. В качестве координат принимает номер слоя сети (nodeIndex) и номер узла в ней (layerIndex).
 * @author   leonidv
 */
final class Node2D {
    // Отображаемый узел
    final private Node node;

    // Координата центра отображения узла
    /**
     * @uml.property  name="nodeIndex"
     */
    final private int nodeIndex;

    // Координата центра отображения узла
    /**
     * @uml.property  name="layerIndex"
     */
    final private int layerIndex;    
    
    /**
     * Создает объект-хранилище. На вход принимает все необходимые данные
     * 
     * @param node
     *          отображаемый узел
     * @param layerIndex
     *          координата по оси ординат
     * @param nodeIndex
     *          координата по оси абцисс
     */
    public Node2D(Node node, int layerIndex, int nodeIndex) {
        super();
        this.node = node;
        this.nodeIndex = nodeIndex;
        this.layerIndex = layerIndex;
    }

    /**
     * @return
     * @uml.property  name="node"
     */
    public Node getNode() {
        return node;
    }

    /**
     * @return
     * @uml.property  name="nodeIndex"
     */
    public int getNodeIndex() {
        return nodeIndex;
    }

    /**
     * @return
     * @uml.property  name="layerIndex"
     */
    public int getLayerIndex() {
        return layerIndex;
    }
    
    
}
