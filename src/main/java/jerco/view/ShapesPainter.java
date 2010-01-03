package jerco.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;

import jerco.network.RegularLattice;
import jerco.network.Node;
import jerco.view.colorer.Colorer;
import jerco.view.colorer.DefaultNodeColorer;


/**
 * 
 * @author leonidv
 *
 */
abstract class ShapesPainter implements Painter {
    // Радиус кружка отображения узла
    protected double shapeSize = 3;

    // Длина ребра соеднения узлов
    protected double edgeLength = 20;

    // Цвет фона
    private Color backgroundColor = Color.WHITE;

    // Цвет ребра
    private Color edgeColor = Color.LIGHT_GRAY;
    
    // Раскрасщик узлов
    protected Colorer nodeColorer = new DefaultNodeColorer();

    /**
     * Возвращает место положения кружка структурированной решетки
     * 
     * @param layerIndex
     * @param nodeIndex
     * @return
     */
    protected abstract Point2D.Double getShapeCenter(int layerIndex, int nodeIndex);

    /**
     * Строит кружок обозначения узла сети
     * 
     * @param center -
     *          координаты центра кружкаcircles
     * @return - кружок обозначения узла сети
     */
    protected abstract Shape makeShape(Node2D node);

    /**
     * Рассчитывает одну сторону размера канвы
     * 
     * @param nodeCount -
     *          количество узлов в грани
     * @return
     */
    protected abstract double calculateDimension(int nodeCount);

    /**
     * Осуществляет рисование ребра между двумя переданными узлами.
     * 
     * @param nodeA -
     *          один из связываемых узлов
     * @param nodeB -
     *          второй из связываемых узлов
     * @param shapesTable -
     *          таблица фигур, которые отображают узлы
     * @return TODO
     */
    private Shape createEdgeShape(Edge2D edge) {
        Node2D nodeA = edge.getSource();
        Node2D nodeB = edge.getDestination();

        assert (nodeA != null);
        assert (nodeB != null);

        Point2D centerA = getShapeCenter(nodeA.getLayerIndex(), nodeA
                .getNodeIndex());
        Point2D centerB = getShapeCenter(nodeB.getLayerIndex(), nodeB
                .getNodeIndex());

        return new Line2D.Double(centerA, centerB);
    }

    /**
     * Рисует кластер в виде кружочка. В зависимости от признаков узла
     * устанавливается различный цвет заливки кружочков.
     */
    @Override
    public void paintNode(Node2D node2D, Graphics2D g) {
        Shape shape = makeShape(node2D);
        Node node = node2D.getNode();       
        g.setPaint(nodeColorer.getColor(node));
        g.draw(shape);
        g.fill(shape);

    }

    /**
     * Рисует ребро между двумя узлами
     */
    @Override
    public void paintEdge(Edge2D edge, Graphics2D g) {
        Shape line = createEdgeShape(edge);
        g.setPaint(edgeColor);
        g.draw(line);
    }

    /**
     * Подготавливает канву для отображения сети
     */
    @Override
    public void prepare(Graphics2D g, RegularLattice net) {
        g.setBackground(backgroundColor);
        Dimension dim = getDimension(net); 
        g.clearRect(0, 0, dim.width, dim.height);
    }

    /**
     * Возвращает размер картинки, который необходим для правильного изображения
     * сети.
     * <p>
     * Размер картинки расчитывается как суммарная длина ребер, плюс отводится
     * место для одно узла. Этот узел есть половина одного крайнего узла плюс
     * половина другого крайнего узла.
     */
    @Override
    public Dimension getDimension(RegularLattice net) {
        Dimension result = new Dimension();
        double width = calculateDimension(net.getWidth());
        double height = calculateDimension(net.getHeight());
        result.setSize(width, height);
        return result;
    }

    /**
     * Возвращает объект-стратегию выбора цвета для узла.
     * @return
     */
    @Override
    public Colorer getNodeColorer() {
        return nodeColorer;
    }
    
    @Override
    public void setNodeColorer(Colorer nodeColorer) {
        this.nodeColorer = nodeColorer;
    }
    
    
}
