package jerco.network.generators;

import java.util.List;

import jerco.network.Layer;

/**
 * Реализация шаблона Стратегия для генераторов сети.
 * 
 * @author Leonid Vygovskiy
 * 
 */
public interface NetGenerator {
    /**
     * Константа для обозначения верхней границы.
     */
    public static final int TOP_BOUNDS = 0;

    /**
     * Константа для обозначения нижней границы.
     */
    public static final int BOTTOM_BOUNDS = 1;
    
    /**
     * Константа для обозначения левой границы.
     */
    public static final int LEFT_BOUNDS = 2;
    
    /**
     * Константа для обозначения правой границы.
     */
    public static final int RIGHT_BOUNDS = 3;
    
    /**
     * @param width
     *            - ширина решетки
     * @param height
     *            - высота решетки
     * @return - first row of the generated net
     */
    public List<Layer> generate(int width, int height);

    /**
     * Возвращает имя генератора, выводимое пользователю.
     * 
     * @return
     */
    public String getName();

}
