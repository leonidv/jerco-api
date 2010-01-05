package jerco.network.generators;

import jerco.network.Layer;
import jerco.network.Node;

public class BaseGenerator {

    public BaseGenerator() {
        super();
    }

    /**
     * Осуществляет связывание узлов двух соседних слоев. У прямоугольной
     * решетки алгоритм очень простой и заключается в попарном связывание узлов.
     * 
     * @param a
     * @param b
     */
    protected void linkLayers(Layer a, Layer b) {
        assert (a.size() == b.size()) : "Количество узлов в слоях не равно";
        int size = a.size();
        for (int i = 0; i < size; i++) {
            Node.linkNodes(a.getNode(i), b.getNode(i));
        }
    }

}