package jerco.network;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import jerco.network.Layer;
import jerco.network.Node;

import org.junit.Before;
import static org.hamcrest.core.Is.*;

public class TestBase {
    protected static final String FILE_CLUSTER_5X5 = "cluster 5x5 test01.txt";

    @Before
    public void initNodeCounter() {
        Node.resetIdCounter();
    }

    protected Node[] checkLayerLength(Node[] nodes, int length) {
        assertTrue("Длина слоя равна " + nodes.length, nodes.length == length);
        return nodes;
    }

    /**
     * Checked that given node linked with other node. Nodes given as their
     * {@link Node#getId()}
     * 
     * @param node
     *            - checked node, get from generated net
     * @param nodeId
     *            - requirment nodeId
     * @param linkedWith
     *            - array with Ids of linked nodes.
     */
    protected void checkNode(Node node, int nodeId, Integer... linkedWith) {
        assertTrue(String.format(
                "У узла не правильный идентификатор %d вместо %d",
                node.getId(), nodeId), node.getId() == nodeId);
        checkNodes(node, linkedWith);
    }

    static protected File makeTestFile(String fileName) {
        return new File("src/test/resources/" + fileName);
    }

    /**
     * Получает итератор переданной сущности и вызвает метод
     * {@link #checkNodes(Iterator, Integer)}
     * 
     * @param entity
     *            - сущность, узлы которой проверяются
     * @param nodesIDs
     *            - список идентификаторов узлов
     */
    protected void checkNodes(Iterable<Node> entity, Integer... nodesIDs) {
        checkNodes(entity.iterator(), nodesIDs);
    }

    /**
     * Осуществляет проверку, что итератор обходит все нужные узлы. И только с
     * те, с которые нужны.
     * <p/>
     * 
     * Поскольку мы не знаем, в какой последовательности возвращаются узлы,
     * делаем хитрый алгоритм. Тестовые значения преобразовываются в изменяемый
     * список. Получаем очередной связанный узел, и пробуем его удалить из этого
     * списка. Если удалить не получилось, значит ошибка - это лишняя связь, не
     * предусмотренная тестовыми данными. <br/>
     * 
     * Если после удаления всех связанных узлов остались тестовые данные, то это
     * тоже ошибка. Значит узел связан не совсеми узлами, с которыми должен быть
     * связан.
     * 
     * @param node
     *            - проверяемый узел
     * @param linkedWith
     *            - список идентификаторов связанных с узлом
     */

    protected void checkNodes(Iterator<Node> iterator, Integer... nodesIDs) {
        List<Integer> nodeIDs = convertArrayToModifiableList(nodesIDs);

        /*
         * Проверяем на лишнии связи
         */
        while (iterator.hasNext()) {
            Node node = iterator.next();

            boolean removed = nodeIDs.remove(new Integer(node.getId()));
            assertTrue(String.format("Есть лишний узел с идентификатором '%d'",
                    node.getId()), removed);
        }

        /*
         * Проверяем на отсутствующие связи
         */
        String badLinks = "";
        for (Integer badLink : nodeIDs) {
            badLinks += " " + badLink.toString();
        }
        assertTrue(String.format(
                "Нет необходимого узлов с идентификаторами:%s'", badLinks),
                nodeIDs.size() == 0);

    }

    /**
     * Служебная процедура, которая осуществлят преобразование массива данных в
     * изменяемый список. Это связано с тем, что стандартная процедура
     * возвращает неизменяемый список (по крайне мере, оттуда точно нельзя
     * удалить элемент)
     * 
     * @param <T>
     *            - тип элементов массива и списка
     * @param data
     *            - массив данных, преобразовываемых в список
     * @return - изменяемый список (LinkedList) с данными массива
     */
    protected <T> List<T> convertArrayToModifiableList(T... data) {
        List<T> result = new LinkedList<T>();
        for (T var : data) {
            result.add(var);
        }
        return result;
    }

    /**
     * Проверяет структуру сети с количественной точки зрения
     * 
     * @param layers
     *            - список слоев, образующих сеть
     * @param layersCount
     *            - ожидаемое количество слоев в сети
     * @param nodesCount
     *            - ожидаемое количество узлов в сети
     */
    protected void checkNetStructure(List<Layer> layers, int layersCount,
            int nodesCount) {
        assertThat("Количество слоев в сети", layers.size(), is(layersCount));
        assertThat("Количество узлов в сети", Node.getIdCounter(),
                is(nodesCount));
    }

}