package jerco.view.colorer;

import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jerco.network.Node;
import jerco.scenarios.dynamic.DisplacementScenario;
import jerco.scenarios.dynamic.StepInfo;


/**
 * <p>
 * Стратегия определения цвета узла для отображения процесса перколяции
 * замещения.
 * </p>
 * <p>
 * На вход принимается информация о протекании перколяции, представленная в виде
 * списка {@link StepInfo}. Раскрасщик выбирает цвет узлов исходя из
 * </p>
 * <p>
 * Применяется следующая индексация узлов:
 * <ul>
 * <li>Узел не заражен - цвет узла, установленный по умолчанию;</li>
 * <li>Узел заражен, но вещество не установлено. Цвет узла по умолчанию</li>
 * <li>Для веществ применяются цвета, заданные методом
 * {@link #setSubstanceColor(int, Color)}
 * <li>Для узлов, которые находятся во фронте текущего шага применяется цвет
 * {@link #setFrontColor(Color)}
 * </ul>
 * </p>
 * 
 * @author leonidv
 * 
 */
public class DisplacementColorer extends DefaultNodeColorer {
    
    
    public final static Color OXYGEN_COLOR = new Color(153, 204, 255);
    public final static Color WATER_COLOR = new Color(101, 135, 168);

    // Таблица сопоставления веществам цветом их отображения
    private Map<Integer, Color> colors = new HashMap<Integer, Color>(3);

    // Цвет отображения узла который находится во фронте текущего шага
    private Color frontColor = Color.RED;

    // Номер текущего шага, для которого необходимо раскрасить узлы
    private int step;

    private Collection<Node> front;

    /**
     * Создает объект раскрасщика. На вход принимает информацию о шагах процесса
     * протекания
     * 
     * @param stepsInfo
     */
    public DisplacementColorer() {
        colors.put(Node.OXYGEN, OXYGEN_COLOR);
        colors.put(Node.WATER, WATER_COLOR);
    }

    /**
     * Возвращает цвет узла
     */
    @Override
    public Color getColor(Node node) {
        if (!node.isInfected() || (node.getSubstance() == Node.NONE)) {
            return getDefaultColor();

        } else if (front.contains(node)) {
            return getFrontColor();

        } else {
            return getSubstanceColor(node.getSubstance());
        }
    }

    public Color getSubstanceColor(int substance) {
        Color result = colors.get(substance);
        if (result == null) {
            result = Color.RED;
        }

        return result;
    }

    /**
     * Возвращает текущий шаги, для которых осущесвляется расскраска
     * 
     * @return
     */
    public Collection<Node> getFront() {
        return front;
    }

    /**
     * Устанавливает набор узлов, которые будут отображаться в виде фронта
     * 
     * @param front
     */
    public void setFront(Collection<Node> front) {
        this.front = front;
    }

    /**
     * Устанавливает для вещества цвет отображения узла
     * 
     * @param substance -
     *          вещество
     * @param color -
     *          цвет отображения
     */
    public void setSubstanceColor(int substance, Color color) {
        colors.put(substance, color);
    }

    /**
     * Возвращает цвет отображения узлов во фронте шага. По умолчанию - желтый.
     * 
     * @return
     */
    public Color getFrontColor() {
        return frontColor;
    }

    /**
     * Устанавливает цвет отображения узлов во фронте шага
     * 
     * @param frontColor
     */
    public void setFrontColor(Color frontColor) {
        this.frontColor = frontColor;
    }

    /**
     * Возвращает номер шага, для которого осуществляется отображения фронта
     * 
     * @return
     */
    public int getStep() {
        return step;
    }
}
