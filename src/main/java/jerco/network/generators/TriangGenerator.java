package jerco.network.generators;

import java.util.ArrayList;
import java.util.List;

import jerco.network.Layer;
import jerco.network.Node;



class TriangGenerator  implements NetGenerator {
	final public static TriangGenerator  INSTANCE = new TriangGenerator ();
     
    public TriangGenerator () {

    }
    protected void addlinkLayers(Layer a, Layer b) {
        assert (a.size() == b.size()) : "Количество узлов в слоях не равно";
        int size = a.size();
               for (int i = 0; i < size-1; i++) {
            Node.linkNodes(a.getNode(i), b.getNode(i+1));// соединяем по диагонали
        }
        
    }  
    public List<Layer> generate(int width, int height) {
    	List<Layer> Tring = RectGenerator.INSTANCE.generate(width, height);
        // теперь добавить дополнительную связь
        
        
       
        for (int i = 0; i < height; i++) {
            addlinkLayers(Tring.get(i), Tring.get(i+1));
        }

        // Устанавливаем у узлов признак нахождения в верхнем слое
        for (Node node : Tring.get(0)) {
            node.setBound(NetGenerator.TOP_BOUNDS);
        }

        // Устанавливаем у узлов признак нахожднеия в нижнем слое
        for (Node node : Tring.get(Tring.size() - 1)) {
            node.setBound(NetGenerator.BOTTOM_BOUNDS);
        }

        return Tring;
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

