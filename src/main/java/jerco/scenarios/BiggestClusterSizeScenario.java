package jerco.scenarios;

import java.util.Map;
import java.util.TreeMap;

import jerco.Constants;
import jerco.network.generators.NetGenerators;


public class BiggestClusterSizeScenario extends CalculateTableScenario {
    public static void main(String[] args) {
        BiggestClusterSizeScenario scenario = new BiggestClusterSizeScenario();

        scenario.setGenerator(NetGenerators.Rectangle.getGenerator());
        scenario.setProbabality(0.6);

        scenario.setStartSize(1000);
        scenario.setEndSize(3000);
        scenario.setStep(500);

        scenario.setExperimentsCount(1);

        scenario.setIndicator(ScenarioProgressIndicator.SYSTEM_OUT_INDICATOR);
        scenario.doScenario();
        Map<Double, Double> result = scenario.getResult(); 
        for (Map.Entry<Double, Double> entry : result.entrySet()) {
            System.out.printf("{%d,%.5f},\n", entry.getKey(), entry.getValue());
        }

    }

    // Начальная ширина решетки
    private int startSize = 100;

    // Конечная ширина решетки
    private int endSize = 500;

    // Шаг изменения ширины решетки
    private int step = 50;

    // Вероятнось заражения, используемая в эксперименте
    private double probabality = 0.593;
    
    /**
     * Конструктор по умолчанию
     */
    public BiggestClusterSizeScenario(){
        
    }
    
    /**
     * Создает новый объект сценария. 
     * @param startSize начальный размер решетки
     * @param endSize конечный размер решетки
     * @param step шаг изменения размера решетки
     */
    public BiggestClusterSizeScenario(int startSize, int endSize, int step) {
        super();
        this.startSize = startSize;
        this.endSize = endSize;
        this.step = step;
    }

    /**
     * Осуществляет выполнение эксперимента.
     */
    @Override
    public void doScenario() {
        result = new TreeMap<Double, Double>();

        /*
         * Инициализируем ширину и высоту решетки в эксперименте в начальные
         * значения
         */
        int size = startSize;

        int count = (endSize - startSize) / step;
        getIndicator().init(count+1);

        int counter = 1;
        while (size <= endSize) {
            if (stop) {
                stop = false;
                break;
            }
            
            ExperimentsStatistics experiment = makeExperiments(size,size,probabality);
            result.put(new Double(size), experiment.getMeanMaximumClusterSize());

            size += step;

            getIndicator().progress(counter);
            counter++;
        }
        getIndicator().done();
    }



    /**
     * Возвращает начальную ширину решетки в сценарии
     * 
     * @return
     */
    public int getStartSize() {
        return startSize;
    }

    /**
     * Устанавливает начальную ширину решетки в сценарии
     * 
     * @param startSize
     */
    public void setStartSize(int startWidth) {
        this.startSize = startWidth;
    }

    /**
     * Возвращает конечную ширину решетки в сценарии
     * 
     * @return
     */
    public int getEndSize() {
        return endSize;
    }

    /**
     * Устанавливает конечную ширину решетки в сценарии
     * 
     * @param endSize
     */
    public void setEndSize(int endWidth) {
        this.endSize = endWidth;
    }

    /**
     * Возвращает шаг изменения ширины решетки в сценарии
     * 
     * @return
     */
    public int getStep() {
        return step;
    }

    /**
     * Устанавливает шаг изменения ширины решетки в сценарии
     * 
     * @param step
     */
    public void setStep(int stepWidth) {
        this.step = stepWidth;
    }

    /**
     * Возвращает вероятность заражения узла в сценарии
     * 
     * @return
     */
    public double getProbabality() {
        return probabality;
    }

    /**
     * Устанавливает вероятность заражения узла в сценарии
     * 
     * @param probabality
     */
    public void setProbabality(double probabality) {
        this.probabality = probabality;
    }

    
    /**
     * Возвращает описание эксперимента в виде строки: <code> 
     * width(start,end,step),
     * height(start,end,step), p = probabality, expCount = experimentsCount
     * </code>
     * 
     */
    public String toString() {
        return String.format(
                "size(%d,%d,%d) p = %sf, expCount = %d",
                getStartSize(), getEndSize(), getStep(),
                getProbabality(), getExperimentsCount());
    }

    @Override
    public String getKeyDescription() {
        return "Lg(L)";
    }

    @Override
    public String getValueDescription() {
        return "Lg(M)";
    }

    @Override
    public String getTitle() {
        return "Размер максимального кластера от размера решетки";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + endSize;
        long temp;
        temp = Double.doubleToLongBits(probabality);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + startSize;
        result = prime * result + step;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final BiggestClusterSizeScenario other = (BiggestClusterSizeScenario) obj;
        if (endSize != other.endSize)
            return false;
        if (Constants.isEqualsDoubles(probabality, other.probabality))
            return false;
        if (startSize != other.startSize)
            return false;
        if (step != other.step)
            return false;
        return true;
    }
    
    
}
