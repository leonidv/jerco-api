package jerco.scenarios;

import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Описывает базовый класс сценария, который возвращает таблицы вещественных
 * чисел. Применяется для сценариев, которые предназначены для построения
 * графиков.
 * 
 * @author leonidv
 * 
 */
public abstract class CalculateTableScenario extends Scenario {

    protected SortedMap<Double, Double> result = new TreeMap<Double, Double>();

    /**
     * Возвращает результат выполнения сценария.
     * 
     * @return
     */
    public SortedMap<Double, Double> getResult() {
        return Collections.unmodifiableSortedMap(result);
    }

    /**
     * Возвращает описание ключа в таблице результатов. В первую очередь нужно
     * для построения графиков сценария (в общем случае, играет роль подписи
     * абциссы)
     * 
     * @return
     */
    public abstract String getKeyDescription();

    /**
     * Возвращает описание значения в таблице результатов. В первую очередь
     * нужно для построения графиков сценария (в общем случае, играет роль
     * подписи ординаты)
     * 
     * @return
     */
    public abstract String getValueDescription();

    /**
     * Возвращает название эксперимента. В первую очередь служит для описания
     * графика
     * 
     * @return
     */
    public abstract String getTitle();

}
