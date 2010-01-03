package jerco.view.colorer;

import java.awt.Color;

import jerco.network.Node;


/**
 * Описывает метод-стратегию получения цвета для узла
 */
public interface Colorer {
    
    /**
     * Возвращает отображения узла. Цвет может менятся из-за различных характеристик
     * узла. Таких, как зараженность, наличие перколяционного кластера и другие.
     * @param node - узел, для которого требуется определить цвет
     * @return цвет его отображения
     */
    Color getColor(Node node);
    
}
