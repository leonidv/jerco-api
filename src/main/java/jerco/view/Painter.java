package jerco.view;

import java.awt.Dimension;
import java.awt.Graphics2D;

import jerco.network.RegularLattice;
import jerco.view.colorer.Colorer;


/**
 * Описывает стратегию отображения элементов сети.
 * 
 * @author leonidv
 * 
 */
public interface Painter {

    /**
     * Возвращает размерность картинки, которая получится в результате рисования
     * сети.
     * @param net  сеть, которая будет отображена.
     * 
     * @return
     */
    Dimension getDimension(RegularLattice net);

    void prepare(Graphics2D g, RegularLattice net);

    /**
     * Вызывает для отображения узла сети
     * 
     * @param node
     *          информация об отображемом узле.
     * @param g
     *          канва, на которой отображается узел
     */
    void paintNode(Node2D node, Graphics2D g);

    /**
     * Вызывается для отображения грани между двумя узлами
     * 
     * @param edge
     *          информация об отображемой грани
     * @param g
     *          канва, на которую выводится изображение
     */
    void paintEdge(Edge2D edge, Graphics2D g);
        
    /**
     * Устанавливает раскрашивателя узла 
     * @param colorer
     */
    public void setNodeColorer(Colorer colorer);
    
    /**
     * Возвращает текущего раскрашивателя узла
     * @param colorer
     * @return
     */
    public Colorer getNodeColorer();
}
