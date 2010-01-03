package jerco.scenarios;

import static jerco.Constants.DOUBLE_PRECISION;

import java.util.ArrayList;
import java.util.List;

import jerco.network.Cluster;
import jerco.network.RegularLattice;
import jerco.utils.Bag;


/**
 * Реализует класс сбора статистики в эксперименте. Накакапливает информацию о
 * среднем числе кластеров по размеру, на основании которого высчитываются
 * характеристики эксперимента.
 * 
 * @author leonidv
 * 
 */
public class ExperimentsStatistics {
    // Ширина сети, над которой проводится эксперимент
    private int width;

    // Высота сети, над которой проводится эксперимент
    private int height;

    // Вероятность заражения узла в эксперименте
    private double probability;

    // Количество экспериментов в сценарии
    private int count = 0;

    // Список зараженных кластеров в результате эксперимента
    private List<Bag<Integer>> experimentsClustersSizes = new ArrayList<Bag<Integer>>();

    // Хранит средний максимальный размер кластера
    private double meanMaxSize = Double.NaN;

    /**
     * Создает объект сбора статистики
     * 
     * @param width
     * @param height
     * @param d
     */
    ExperimentsStatistics(int width, int height, double d) {
        super();
        this.width = width;
        this.height = height;
        this.probability = d;
        reset();
    }

    /**
     * Осуществляет сброс всех закэшированных значений
     */
    private void reset() {
        meanMaxSize = Double.NaN;
    }

    /**
     * Возвращает ширину сети, для которой осуществляется сбор статистики
     * 
     * @return
     */
    public int getWidth() {
        return width;
    }

    /**
     * Возвращает установленную высоту сети, для которой осуществляется сбор
     * статистики
     * 
     * @return
     */
    public int getHeight() {
        return height;
    }

    /**
     * Возвращает установленную вероятность зараженния узла, при которой
     * осуществляются эксперимент
     * 
     * @return
     */
    public double getProbability() {
        return probability;
    }

    /**
     * Возвращает количество проведенных экспериментов, для которых собраны
     * данные.
     * 
     * @return
     */
    public int getExperimentsCount() {
        return count;
    }

    /**
     * Добавляет данные эксперимента
     * 
     * @param net
     *          зараженная сеть
     * @throws IllegalArgumentException
     *           в случае, если один из параметров сети не соотвествует условиям
     *           эксперимента (см. {@link #getWidth()}, {@link #getHeight()},
     *           {@link #getProbability()})
     */
    public void addData(RegularLattice net) {
        if ((net.getWidth() != width) || (net.getHeight() != height)) {
            throw new IllegalArgumentException(
                    "Размер сети не соотвествует установленному "
                            + "размеру в эксперименте");
        }

        if (Math.abs(net.getInfectProbability() - probability) > DOUBLE_PRECISION) {
            throw new IllegalArgumentException(
                    "Вероятность заражения не соответствует "
                            + "установленной в эксперименте");
        }

        reset();
        // Добавляем данные о результатх
        experimentsClustersSizes.add(getClustersSizes(net));

        count++;
    }

    /**
     * Возвращает данные о количестве кластеров различных размеров.
     * 
     * @param net
     * @return
     */
    private Bag<Integer> getClustersSizes(RegularLattice net) {
        Bag<Integer> result = new Bag<Integer>();
        for (Cluster cluster : net.getClusters()) {
            result.add(cluster.size());
        }
        
        if (result.size() == 0) {
            result.add(0);
        }
        return result;
    }

    /**
     * Возвращает количество ячеек в сети
     * 
     * @return
     */
    public int getNetSize() {
        return width * height;
    }

    /**
     * Расчитывается средний максимальный размер у всех кластеров (M(L)).
     * <p>
     * Т.е. в каждом эксперименте находится максимальный кластер, который
     * добавляется в массив максимальных кластеров. Далее находится среднее
     * арифметическое данного массива.
     * 
     * @return
     */
    public double getMeanMaximumClusterSize() {
        // Небольшое кеширование
        if (!Double.isNaN(meanMaxSize)) {
            return meanMaxSize;
        }

        List<Integer> maximums = new ArrayList<Integer>();

        // Составляем массив из размеров максимального кластера в различных
        // экспериментах
        for (Bag<Integer> experimentData : experimentsClustersSizes) {
            maximums.add(experimentData.lastKey());
        }

        // Подсчиываем среднее арифметическое полученного массива и возвращаем
        // его как результат
        double temp = 0;
        for (int maxSize : maximums) {
            temp += maxSize;
        }
        meanMaxSize = temp / getExperimentsCount();
        return meanMaxSize;
    }

    /**
     * Возвращает вероятность принадлежности случайного выбранного узла
     * перколяционному кластеру (Pn(p), [Федер, 112])
     * 
     * @return
     */
    public double getProbabilityClusterInPercolation() {
        return getMeanMaximumClusterSize() / getNetSize();
    }

    public void clear() {
        count = 0;
        experimentsClustersSizes = new ArrayList<Bag<Integer>>();
        reset();
    }
}
