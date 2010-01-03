package jerco.view;

/**
 * Класс представляет описание грани между двумя узлами. Грань имеет направление.
 * @author   leonidv
 */
public class Edge2D {
    // Ссылка на узел источник (от кого исходит ребро)
    final private Node2D source;

    // Ссылка на узел направление (к кому направлено ребро)
    final private Node2D destination;

    /**
     * Создает описание ребра между двумя узлами
     * 
     * @param source - узел источник (от кого)
     * @param destination  - узел цели (к кому)
     */
    public Edge2D(Node2D source, Node2D destination) {
        this.source = source;
        this.destination = destination;
    }

    /**
     * @return
     * @uml.property  name="source"
     */
    public Node2D getSource() {
        return source;
    }

    /**
     * @return
     * @uml.property  name="destination"
     */
    public Node2D getDestination() {
        return destination;
    }
    
}
