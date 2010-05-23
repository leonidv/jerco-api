package jerco.network.generators;

import java.util.ArrayList;
import java.util.List;

import jerco.network.Layer;
import jerco.network.Node;



class TriangGenerator extends BaseGenerator implements NetGenerator {
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
        List<Layer> Rlayers ;
        List<Layer> Tlayers=new ArrayList<Layer>() ;

        RectGenerator rect = new RectGenerator();
        Rlayers = rect.generate(width, height);
        // теперь добавить дополнительную связь
        
        
       Layer currentLayer=Rlayers.get(0);
      
        for (int i = 1; i < height - 1; i++) {
            Layer prevousLayer = currentLayer;
            currentLayer = Rlayers.get(i);
            addlinkLayers(prevousLayer, currentLayer);
            Tlayers.add(currentLayer);
        }

        // Устанавливаем у узлов признак нахождения в верхнем слое
        for (Node node : Tlayers.get(0)) {
            node.setBound(NetGenerator.TOP_BOUNDS);
        }

        // Устанавливаем у узлов признак нахожднеия в нижнем слое
        for (Node node : Tlayers.get(Tlayers.size() - 1)) {
            node.setBound(NetGenerator.BOTTOM_BOUNDS);
        }

        return Tlayers;
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

