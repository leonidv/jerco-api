package jerco.scenarios;

import java.util.TreeMap;

import jerco.Constants;


/**
 * Сценарий расчета Pn(P). Pn(P) характерезует вероятность попадания случайно
 * взятого узла в перколяционный кластер. Pn(P) = M(L)/N, где N есть размер
 * решетки. <br/> В эксперименте задается фиксированный размер решетки.
 * Вероятность решетки изменяется в заданных пределах с заданным шагом.
 * 
 * @author leonidv
 * 
 */
public class NodeInPercolationProbabilityScenario extends CalculateTableScenario{
    private static final long serialVersionUID = 2947709664445498656L;

    // Ширина решетки
    private int width = 100;

    // Высота решетки
    private int height = 100;

    // Начальное значение вероятности заражения
    private double startProbability = 0;

    // Конечное значение вероятности заражения
    private double endProbability = 1;

    // Шаг изменения вероятности заражения
    private double step = 0.1;

    /**
     * Создает сценарий со значениями по умолчанию:
     * <ul>
     * <li>start = 0;</li>
     * <li>end = 1; </li>
     * <li>step = 0.1</li>
     * </ul>
     */
    public NodeInPercolationProbabilityScenario() {

    }

    /**
     * Создает сценарий, позволяя задать характеристики изменения вероятности
     * 
     * @param startProbability
     *          начальное значение
     * @param endProbability
     *          конечное значение
     * @param step
     *          шаг изменения
     */
    public NodeInPercolationProbabilityScenario(double startProbability,
            double endProbabitlty, double step) {
        this();
        this.startProbability = startProbability;
        this.endProbability = endProbabitlty;
        this.step = step;
    }

    /**
     * Осуществляет выполнение сценария. На выход возвращает таблицу результата,
     * где каждой вероятности заражения сопоставлена вероятность попадания узла
     * в перколяционный кластера
     */
    public void doScenario() {
        result = new TreeMap<Double, Double>();

        getIndicator().init(
                (int) ((endProbability - startProbability) / step) + 1);

        int counter = 1;
        for (double p = startProbability; p <= endProbability
                + Constants.DOUBLE_PRECISION; p += step) {
            if (stop) {
                stop = false;
                break;
            }

            final ExperimentsStatistics statistics = makeExperiments(width,
                    height, p);
            result.put(p, statistics.getProbabilityClusterInPercolation());
            getIndicator().progress(counter);
            counter++;
        }
        getIndicator().done();
    }

    /**
     * Возвращает ширину сети в эксперименте
     * 
     * @return
     */
    public int getWidth() {
        return width;
    }

    /**
     * Устанавливает ширину сети в эксперименте
     * 
     * @param width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Возвращает высоту сети в эксперименте
     * 
     * @return
     */
    public int getHeight() {
        return height;
    }

    /**
     * Устанавливает высоту сети в эксперименте
     * 
     * @param height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Возвращает начальное значение вероятности заражения узла сети в
     * эксперименте
     * 
     * @return
     */
    public double getStartProbability() {
        return startProbability;
    }

    /**
     * Устанавливает начальное значение вероятности заражения узла сети в
     * эксперименте
     * 
     * @param startProbability
     */
    public void setStartProbability(double startProbability) {
        this.startProbability = startProbability;
    }

    /**
     * Возвращает конечное значение вероятности заражение сети узла
     * 
     * @return
     */
    public double getEndProbability() {
        return endProbability;
    }

    /**
     * Устанавливает конечное значение вероятности заражения сети узла
     * 
     * @param endProbability
     */
    public void setEndProbability(double endProbabitlty) {
        this.endProbability = endProbabitlty;
    }

    /**
     * Возвращает шаг изменения вероятности заражение узла
     * 
     * @return
     */
    public double getStep() {
        return step;
    }

    /**
     * Устанавливает шаг изменения вероятности заражения узла
     * 
     * @param step
     */
    public void setStep(double step) {
        this.step = step;
    }

    @Override
    public String getKeyDescription() {
        // TODO Auto-generated method stub
        return "p";
    }

    @Override
    public String getValueDescription() {
        // TODO Auto-generated method stub
        return "Pn";
    }

    @Override
    public String getTitle() {
        return "Вероятность нахождения узла в перколяционном кластере";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Constants.getDoubleHashCode(endProbability);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + height;
        temp = Constants.getDoubleHashCode(startProbability);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Constants.getDoubleHashCode(step);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + width;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        System.out.println("check equlas");
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final NodeInPercolationProbabilityScenario other = (NodeInPercolationProbabilityScenario) obj;
        if (Constants.isEqualsDoubles(endProbability, other.endProbability))
            return false;
        if (height != other.height)
            return false;
        if (Constants.isEqualsDoubles(startProbability, other.startProbability))
            return false;
        if (Constants.isEqualsDoubles(step,other.step))
            return false;
        if (width != other.width)
            return false;
        
        System.out.println(true);
        return true;
    }

}
