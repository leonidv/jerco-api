package jerco.scenarios;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import static jerco.TestUtils.*;
import jerco.Constants;
import jerco.network.Net;
import jerco.network.NetStructureInfo;
import jerco.network.RegularLattice;
import jerco.network.TestBase;
import jerco.network.RegularLattice.BadNetFileFormatException;
import jerco.network.generators.RectGenerator;

import org.junit.Before;
import org.junit.Test;

public class TestExperimentsStatistics extends TestBase {
    ExperimentsStatistics statistics;
    ExperimentsStatistics statistics2;
    ExperimentsStatistics statistics3;

    @Before
    public void setUp() throws FileNotFoundException, BadNetFileFormatException {
        statistics = new ExperimentsStatistics(5*5, 0.6);
        statistics.addData(loadNet5x5());

        statistics2 = new ExperimentsStatistics(5*5, 0.6);
        for (int i = 0; i < 10; i++) {
            statistics2.addData(loadNet5x5());
        }

        statistics3 = new ExperimentsStatistics(5*5, 0.6);
        final String FILE_NAME = "cluster 5x5 test%02d.txt";
        for (int i = 1; i <= 5; i++) {
            RegularLattice net = new RegularLattice();
            net.load(loadTestFile(String.format(FILE_NAME, i)));
            statistics3.addData(net);
        }
    }

    private Net loadNet5x5() {
        RegularLattice net = new RegularLattice();
        try {
            net.load(loadTestFile(FILE_CLUSTER_5X5));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BadNetFileFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return net;
    }

    @Test
    public void testExperimentsCount() {
        NetStructureInfo structureInfo = new NetStructureInfo();
        structureInfo.setGenerator(RectGenerator.INSTANCE);
        structureInfo.setWidth(5);
        structureInfo.setHeight(5);
        Net net = new RegularLattice(structureInfo);
        statistics.clear();
        for (int i = 0; i < 5; i++) {
            net.infect(0.6);
            statistics.addData(net);
            assertEquals("Количество экспериментов", i + 1, statistics
                    .getExperimentsCount());
        }
    }

    @Test
    public void testAllMetrics() {
        testMeanMaximum_3();
        testClusterInPercolationProbability_3();
    }

    /**
     * Проверяем среднее количество кластеров для 1 первого эксперимента.
     */
    @Test
    public void testMeanMaximum_1() {
        assertEquals(1, statistics.getExperimentsCount());
        assertEquals(5.0, statistics.getMeanMaximumClusterSize(),
                Constants.DOUBLE_PRECISION);
    }

    /**
     * Проверяем среденее количество кластеров для первого эксперимента,
     * дополнительно загруженного 10 раз.
     */
    @Test
    public void testMeanMaximum_2() {
        assertEquals(5.0, statistics2.getMeanMaximumClusterSize(),
                Constants.DOUBLE_PRECISION);
    }

    /**
     * Загружает набор из тестовых файлов. Тесты подготовлены таким образом, что
     * максималный размер кластера растер на 1 по порядковому номеру файла
     * теста.
     * 
     * @throws BadNetFileFormatException
     * @throws FileNotFoundException
     */
    @Test
    public void testMeanMaximum_3() {
        assertEquals(7.0, statistics3.getMeanMaximumClusterSize(),
                Constants.DOUBLE_PRECISION);
        assertEquals(6.99999999, statistics3.getMeanMaximumClusterSize(),
                Constants.DOUBLE_PRECISION);

    }

    @Test
    public void testClusterInPercolationProbability_1() {
        assertEquals(0.2, statistics.getProbabilityClusterInPercolation(),
                Constants.DOUBLE_PRECISION);

    }

    @Test
    public void testClusterInPercolationProbability_2() {
        assertEquals(0.2, statistics2.getProbabilityClusterInPercolation(),
                Constants.DOUBLE_PRECISION);
    }

    @Test
    public void testClusterInPercolationProbability_3() {
        assertEquals(0.28, statistics3.getProbabilityClusterInPercolation(),
                Constants.DOUBLE_PRECISION);
    }

}
