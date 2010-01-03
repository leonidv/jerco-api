package jerco.view.colorer;

import java.awt.Color;

import jerco.network.Node;


/**
 * Определяет раскраску узлов при генерации картинок по умолчанию.
 * 
 * @author leonidv
 * 
 */
public class DefaultNodeColorer implements Colorer {
    // Цвет не зараженного узла
    private Color defaultColor = Color.BLACK;

    // Цвет зараженного узла
    private Color infectedColor = Color.RED;

    // Цвет узла в перколяцинном кластере
    private Color percolationClusterColor = Color.GREEN;

    @Override
    public Color getColor(Node node) {
        if (node.isInPercolationCluster()) {
            return percolationClusterColor;
        } else if (node.isInfected()) {
            return infectedColor;
        } else {
            return defaultColor;
        }

    }

    /**
     * Возвращает цвет не зараженных узлов
     * 
     * @return
     */
    public Color getDefaultColor() {
        return defaultColor;
    }

    /**
     * Устанавливает цвет для незараженных узлов
     * 
     * @param defaultColor
     */
    public void setDefaultColor(Color defaultColor) {
        this.defaultColor = defaultColor;
    }

    /**
     * Возвращает цвет зараженных узлов
     * 
     * @return
     */
    public Color getInfectedColor() {
        return infectedColor;
    }

    /**
     * Возвращает цвет зараженных узлов
     * 
     * @param infectedColor
     */
    public void setInfectedColor(Color infectedColor) {
        this.infectedColor = infectedColor;
    }

    /**
     * Возвращает цвет узлов, принадлежащих перколяционному кластеру
     * 
     * @return
     */
    public Color getPercolationClusterColor() {
        return percolationClusterColor;
    }

    /**
     * Устанавливает цвет отображения узлов, принадлежащих перколяционному
     * кластеру
     * 
     * @param percolationClusterColor
     */
    public void setPercolationClusterColor(Color percolationClusterColor) {
        this.percolationClusterColor = percolationClusterColor;
    }
}
