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
     * @param width
     *            - ширина решетки
     * @param height
     *            - высота решетки
     * @return - first row of the generated net
     */
    public List<Layer> generate(int width, int height);
    
    
    /**
     * Возвращает имя генератора, выводимое пользователю. 
     * @return
     */
    public String getName();
    
    
}
