package jerco.network;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import jerco.network.RegularLattice.BadNetFileFormatException;
import jerco.network.generators.RectGenerator;
import static jerco.TestUtils.*;

import org.junit.Before;
import org.junit.Test;


public class TestNet extends TestBase {
    private NetStructureInfo structureInfo;
    private RegularLattice net;
    
    @Before
    public void initStructurInfo() {
        structureInfo = new NetStructureInfo();
        structureInfo.setGenerator(RectGenerator.INSTANCE);
        structureInfo.setWidth(3);
        structureInfo.setHeight(3);
        
        net = new RegularLattice(structureInfo);
    }
    
    /**
     * Проверяем различные комбинации ширины и высоты генерируемой решетки на
     * правильность возвращаемых значений. Т.е. чтобы если попросили решетку
     * шириной 3 и высотой 5 соотвествующие методы вернули именно эти значения
     */
    @Test
    public void testGoodWidthHeight() {
        RegularLattice net = new RegularLattice();
        
        structureInfo.setWidth(3);
        structureInfo.setHeight(3);        
        net.generate(structureInfo);
        assertSame(net.getWidth(), 3);
        assertSame(net.getHeight(), 3);

        structureInfo.setWidth(1);
        structureInfo.setHeight(1);
        net.generate(structureInfo);
        assertSame(1, net.getWidth());
        assertSame(1, net.getHeight());

        structureInfo.setWidth(3);
        structureInfo.setHeight(5);
        net.generate(structureInfo);
        assertSame(3, net.getWidth());
        assertSame(5, net.getHeight());

    }

    /**
     * Проверяем правильность работы итератора узлов сети.
     */
    @Test
    public void testNodeIterator() {
        Set<Node> set = new HashSet<Node>();
        for (Node node : net) {
            set.add(node);
        }
        assertEquals(9, set.size());
    }

    @Test
    public void testEquals() {
        Net a = new RegularLattice(structureInfo);
        Net b = new RegularLattice(structureInfo);

        assertEquals("Равные сети не равны", a, b);

        for (Node node : b) {
            node.setInfected(true);
        }

        assertFalse("Не равные сети равны", a.equals(b));
    }

    /**
     * Проверяем, что метод сброса посещенных узлов работает.
     */
    @Test
    public void testResetVisits() {
        for (Node node : net) {
            node.setVisited(true);
        }

        net.resetVisited();

        for (Node node : net) {
            assertFalse(node.isVisited());
        }
    }

    /**
     * Проверяем корректность работы сброса инфицированных узлов
     */
    @Test
    public void testResetInfected() {
        for (Node node : net) {
            node.setInfected(true);
        }
        net.resetInfected();
        for (Node node : net) {
            assertFalse(node.isInfected());
        }
        assertEquals(0, net.getClusters().size());
    }

    /**
     * Проверяет алгоритм работы заражения сети с вероятностью 1. Ожидается, что
     * с такой вероятностью окажутся зараженными все узлы сети.
     */
    @Test
    public void testInfectWith1() {
        net.infect(1);
        for (Node node : net) {
            assertTrue(node.isInfected());
        }
    }

    /**
     * Проверяет алгоритм работы заражения сети с вероятностью 0. Ожидается, что
     * с такой вероятностью не будет заражен ни один узел.
     */
    @Test
    public void testInfectWith0() {
        // Сначала заражаем полностью, чтобы потом убрать заражение
        net.infect(1);
        net.infect(0);
        for (Node node : net) {
            assertFalse(node.isInfected());
        }

    }

    @Test
    public void testSaveNotInfected3x4() throws FileNotFoundException {
        File file = new File("target/test-temp-results/not infected 3x3.txt");
        System.out.println(file.getAbsolutePath());
        structureInfo.setHeight(4);
        net = new RegularLattice(structureInfo);
        try {
            net.save(file);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Ошибка при доступе к файлу");
        }

        BufferedReader reader = new BufferedReader(new FileReader(file));

        try {
            assertEquals("Rectangle", reader.readLine());
            assertEquals("3", reader.readLine());
            assertEquals("4", reader.readLine());
            for (int i = 0; i < 4; i++) {
                assertEquals("0 0 0 ", reader.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testSaveInfected3x4() throws FileNotFoundException {
        File file = new File("target/test-temp-results/infected 3x4.txt");
        System.out.println(file.getAbsolutePath());
        structureInfo.setHeight(4);
        net = new RegularLattice(structureInfo);
        net.infect(1);
        try {
            net.save(file);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Ошибка при доступе к файлу");
        }

        BufferedReader reader = new BufferedReader(new FileReader(file));

        try {
            assertEquals("Rectangle", reader.readLine());
            assertEquals("3", reader.readLine());
            assertEquals("4", reader.readLine());
            for (int i = 0; i < 4; i++) {
                assertEquals("1 1 1 ", reader.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testSaveNotInfected1x1() throws FileNotFoundException {
        File file = new File("target/test-temp-results/not infected 1x1.txt");
        System.out.println(file.getAbsolutePath());
        structureInfo.setWidth(1);
        structureInfo.setHeight(1);
        RegularLattice net = new RegularLattice(structureInfo);
        try {
            net.save(file);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Ошибка при доступе к файлу");
        }

        BufferedReader reader = new BufferedReader(new FileReader(file));

        try {
            assertEquals("Rectangle", reader.readLine());
            assertEquals("1", reader.readLine());
            assertEquals("1", reader.readLine());
            for (int i = 0; i < 1; i++) {
                assertEquals("0 ", reader.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testSaveAndLoadInfected100x100() throws FileNotFoundException {
        File file = new File("target/test-temp-results/infected save and load 100x100.txt");
        
        structureInfo.setWidth(100);
        structureInfo.setHeight(100);
        RegularLattice a = new RegularLattice(structureInfo);
        a.infect(0.6);
        try {
            a.save(file);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Ошибка при сохранение файла");
        }

        RegularLattice b = new RegularLattice();
        try {
            b.load(file);
        } catch (BadNetFileFormatException e) {
            e.printStackTrace();
        }

        assertEquals(a, b);
    }

    @Test
    public void testClustersOrder() {
        structureInfo.setWidth(100);
        structureInfo.setHeight(100);
        Net net = new RegularLattice(structureInfo);
        net.infect(0.6);

        Iterator<Cluster> iterator = net.getClusters().iterator();
        Cluster previous = iterator.next();
        while (iterator.hasNext()) {
            Cluster current = iterator.next();
            assertFalse(previous.compareTo(current) > 0);
            previous = current;
        }

    }
    
    @Test
    public void testFindClusters() {
        RegularLattice net = new RegularLattice();
        try {
            net.load(loadTestFile(FILE_CLUSTER_5X5));
            //net.printClusters(System.out);
            assertSame(4, net.findClusters());
            
            assertSame(1, net.getClusters().get(0).size());
            assertSame(3, net.getClusters().get(1).size());
            assertSame(5, net.getClusters().get(2).size());
            assertSame(5, net.getClusters().get(3).size());
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BadNetFileFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
