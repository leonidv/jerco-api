package jerco.scenarios;

import java.io.Serializable;

import jerco.network.NetStructureInfo;
import jerco.network.RegularLattice;
import jerco.network.generators.NetGenerator;
import jerco.network.generators.RectGenerator;

/**
 * Класс описывает базовые возможности сценария. Каждый сценарий должен
 * реализовать метод doScenario(). При желании, можно воспользоваться
 * вспомогательным методом makeExperiments().
 * 
 * @author leonidv
 * 
 */
public abstract class Scenario implements Serializable {

    protected int experimentsCount = 30;
    private ScenarioProgressIndicator indicator = ScenarioProgressIndicator.NULL_INDICATOR;
    protected boolean stop;
    protected NetGenerator generator = RectGenerator.INSTANCE;
    private RegularLattice net = new RegularLattice();

    public Scenario() {
        super();
    }

    public abstract void doScenario();

    /**
     * Возвращает количество экспериментов для каждой вероятности заражения
     * 
     * @return
     */
    public int getExperimentsCount() {
        return experimentsCount;
    }

    /**
     * Устанавливает количество экспериментов для каждой вероятности заражения
     * 
     * @param experimentsCount
     */
    public void setExperimentsCount(int experimentsCount) {
        this.experimentsCount = experimentsCount;
    }

    /**
     * Возвращает ссылку на индикатор выполнения сценария
     * 
     * @return
     */
    public ScenarioProgressIndicator getIndicator() {
        return indicator;
    }

    /**
     * Устанавливает индикатор выполнения сценария
     * 
     * @param indicator
     */
    public void setIndicator(ScenarioProgressIndicator indicator) {
        this.indicator = indicator;
    }

    /**
     * Прекратить выполненение сценария
     * 
     * @return
     */
    public void stop() {
        stop = true;
    }

    /**
     * Возвращает генератор сети
     * 
     * @return
     */
    public NetGenerator getGenerator() {
        return generator;
    }

    /**
     * Устанавливает генератор сети.
     * 
     * @param generator
     */
    public void setGenerator(NetGenerator generator) {
        this.generator = generator;
    }

    /**
     * Осуществляет эксперимент для заданных параметров.
     * 
     * @param p
     *            верояность заражения узла в сети
     * @param height
     *            высота сети в эксперименте
     * @param width
     *            ширина сети в эксперименте
     * 
     * @return
     */
    protected ExperimentsStatistics makeExperiments(int width, int height,
            double p) {
        ExperimentsStatistics statistics = new ExperimentsStatistics(width*
                height, p);

        for (int i = 0; i < experimentsCount; i++) {
            if (stop) {
                break;
            }

            NetStructureInfo structureInfo = new NetStructureInfo();
            structureInfo.setGenerator(getGenerator());
            structureInfo.setWidth(width);
            structureInfo.setHeight(height);
            net.generate(structureInfo);
            net.infect(p);
            statistics.addData(net);
        }

        return statistics;
    }

}