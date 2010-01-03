package jerco.network;

import static jerco.Constants.DOUBLE_PRECISION;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import jerco.network.generators.NetGenerator;
import jerco.network.generators.NetGenerators;


/**
 * Сеть с регулярной структурой. Позволяет создавать сеть с заданным правилом генерации структуры. Например, квадратную сеть или треугольную. Для задания правила создания структуры применяется шаблон проектирования Стратегия. Поведение генератора описано в интерфейсе NetGenerator. <p> Реализует интефрейс Iterabla<Node> при этом обходятся все узлы, которые есть в сети. Существует также возможность получить обход только зараженных узлов и только узлов, принадлежащих перколяционному кластеру.
 * @author     leonidv
 */
public class RegularLattice implements Iterable<Node> {
    // Собственное исключение, генерируемое в случае плохого формата файла сети
    public static class BadNetFileFormatException extends Exception {
        private static final long serialVersionUID = 1L;

        BadNetFileFormatException() {
            super();
        }

        BadNetFileFormatException(File file) {
            super(file.getAbsolutePath());
        }
    };

    /**
     * Итератор перебора всех узлов в сети.
     * 
     */
    class NodeIterator implements Iterator<Node> {
        // Индекс текущего рассматриваемого слоя
        private int layerIndex = 0;

        // Индекс текущего рассматриваемого узла
        private int nodeIndex = 0;

        @Override
        public boolean hasNext() {
            return (layerIndex < layers.size())
                    && (nodeIndex < layers.get(layerIndex).size());
        }

        @Override
        public Node next() {
            if (hasNext()) {
                Node node = layers.get(layerIndex).getNode(nodeIndex);
                nodeIndex++;
                if (nodeIndex >= layers.get(layerIndex).size()) {
                    layerIndex++;
                    nodeIndex = 0;
                }
                return node;
            } else {
                throw new NoSuchElementException();
            }
        }

        /**
         * Операция не поддерживается.
         */
        @Override
        public void remove() {
            throw new UnsupportedOperationException();

        }

    }

    /**
     * Структура сети
     */
    private NetStructureInfo structureInfo = new NetStructureInfo();

    /**
     * Флажок, хранящий - генерировали мы уже решетку или нет. Предназначен
     * для оптимизации - генерация решетки это длительный процесс в обработке
     * данных.
     */
    private boolean generated;
    
    /**
     * Вероятность заражения сети
     * @uml.property   name="infectProbability"
     */
    private double infectProbability = Double.NaN;

    /**
     * Слои сети
     * @uml.property   name="layers"
     */
    private List<Layer> layers = new ArrayList<Layer>();

    /**
     * Кластеры в сети
     * @uml.property   name="clusters"
     */
    private List<Cluster> clusters;

    public RegularLattice() {
        reset();
    }

    /**
     * Создает сеть с заданной структурой. При этом сразу осуществляется
     * генерация решетки.
     * 
     * @param structureInfo
     */
    public RegularLattice(NetStructureInfo structureInfo) {
        generate(structureInfo);
    }

    /**
     * Создает новую сеть, содержащую переданное количество узлов.
     * 
     * @throws IllegalStateException -
     *           в случае, если генератор сети не был установлен
     */
    public void generate(NetStructureInfo structureInfo) {
        reset();
        // Если структура решетки такая же, как и переданная - заново
        // генерировать не надо
        if (generated && this.structureInfo.equals(structureInfo)) {
            return;
        }
        
        this.structureInfo = new NetStructureInfo(structureInfo);
        layers = getGenerator().generate(getWidth(), getHeight());
        generated = true;   
    }

    /**
     * Возвращает итератор перебора всех узлов в сети.
     * 
     * @return
     */
    public NodeIterator iterator() {
        return new NodeIterator();
    }

    /**
     * Осуществляет заражение из одной точки
     * 
     * @param firstNode
     */
    public void infect(Node firstNode, double treshold) {
    }

    /**
     * Осуществляет заражение сети по методу <font color="red">Бернулли</font>
     * <p/> При этом методе перколяция осуществляется следующим способом. Каждый
     * узел с заданной вероятностью подвергается возможности заражения.
     * 
     * @param p
     *          вероятнось заражения узла.
     */
    strictfp public void infect(double p) {
        infectProbability = p;
        for (Node node : this) {
            boolean infected = (infectProbability - Math.random()) > DOUBLE_PRECISION;
            node.setInfected(infected);
        }
        findClusters();
    }

    /**
     * Осуществляет поиск кластеров. В конце поиска кластеры ранжируются по
     * возрастанию мощности (размера) кластера.
     * 
     * @return количество кластеров в сети
     */
    public int findClusters() {
        resetClusters();
        for (Node node : this) {
            if (!node.isInCluster() && node.isInfected()) {
                Cluster cluster = new Cluster(node);
                cluster.build();
                clusters.add(cluster);
            }
        }
        Collections.sort(clusters);
        return clusters.size();
    }

    /**
     * Осуществляет сброс кластеров сети. При этом создается новый список
     * кластеров, а все узлы помечаются как не находящиеся в каком-либо кластере
     */
    public void resetClusters() {
        clusters = new ArrayList<Cluster>();
        for (Node node : this) {
            node.setInCluster(false);
        }
    }

    public NetStructureInfo getStructureInfo() {
        return this.structureInfo;
    }

    /**
     * Возвращает ширину сети
     * 
     * @return
     * @uml.property name="width"
     */
    public int getWidth() {
        return structureInfo.getWidth();
    }

    /**
     * Возвращает генератор решетки
     * 
     * @return - генератор решетки
     * @uml.property name="generator"
     */
    public NetGenerator getGenerator() {
        return structureInfo.getGenerator();
    }

    /**
     * Возвращает высоту сети
     * 
     * @return
     * @uml.property name="height"
     */
    public int getHeight() {
        return structureInfo.getHeight();
    }
    
    public int size() {
        int size = 0;
        for (Layer layer : layers) {
            size += layer.size();
        }
        
        return size;
    }
    
    /**
     * Возвращает вероятность заражения узла сети, которая была установлена при
     * последнем заражении.
     * 
     * @return вероятность, с который были заражены узлы в последний раз.
     *         Double.NaN, в случае, если операция заражения не осуществлялась.
     * @uml.property name="infectProbability"
     */
    public double getInfectProbability() {
        return infectProbability;
    }

    /**
     * Осуществляет сбор флага "посещено" у всех узлов сети.
     */
    public void resetVisited() {
        for (Node node : this) {
            node.setVisited(false);
        }
    }

    /**
     * Осуществляет сбор флага "заражен" у всех узлов сети. Эта операция также
     * приводит к сбросу списка кластеров в сети
     */
    public void resetInfected() {
        for (Node node : this) {
            node.setInfected(false);
        }
        clusters.clear();
    }

    /**
     * Возвращает неизменяемый список слоев сети
     * 
     * @return - неизменяемый список слоев
     * @uml.property name="layers"
     */
    public List<Layer> getLayers() {
        return Collections.<Layer> unmodifiableList(layers);
    }

    /**
     * Возвращает неизменяемое множество кластеров сети
     * 
     * @return - неизменяемое множество кластеров
     * @uml.property name="clusters"
     */
    public List<Cluster> getClusters() {
        return Collections.<Cluster> unmodifiableList(clusters);
    }

    /**
     * Возвращает истину, если имеется хотя бы один перколяционный кластер.
     * 
     * @return
     */
    public boolean hasPercolationCluster() {
        for (Cluster cluster : clusters) {
            if (cluster.isPercolation()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Осуществляет полный сброс информации о сети.
     */
    public void reset() {
        //layers = new ArrayList<Layer>();
        resetClusters();
        resetVisited();
        resetInfected();
    }

    /**
     * Сохраняет структуру сети на жеский диск. При этом информация о связях
     * между узлами не сохраняется, а сохраняется тип генератора. При этом
     * сохраняется информация о зараженности узлов. <br/>
     * 
     * <pre>
     * Тип генератора
     * Ширина (количество узлов в слое)
     * Высота (количество слоев в сети)
     * 0 1 0 1 1 1 0 0 0 - строки описание слоев сети. Одна строка - один слой.
     * 1 означает зараженный узел, 0 означает не зараженный узел.
     * </pre>
     * 
     * Например:
     * 
     * <pre>
     * Rectangle
     * 3
     * 2
     * 1 0 1
     * 0 1 0
     * </pre>
     * 
     * @param file -
     *          имя файла для сохранения
     * @throws IOException -
     *           в случае проблемы работы с файлом
     */
    public void save(File file) throws IOException {
        if (!file.getParentFile().exists()) {
            if (!file.getParentFile().mkdirs()) {
                throw new IOException(
                        "Не могу создать несуществующий каталог для файла");
            }
        }

        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(
                file)));

        /*
         * Выводим имя генератора решетки
         */
        String generatorName = NetGenerators.getGeneratorType(getGenerator())
                .toString();
        writer.println(generatorName);

        /*
         * Выводим ширину и высоту решетки
         */
        writer.println(getWidth());
        writer.println(getHeight());

        /*
         * Выводим информацию об узлах
         */
        for (Layer layer : layers) {
            for (Node node : layer) {
                if (node.isInfected()) {
                    writer.print("1 ");
                } else {
                    writer.print("0 ");
                }
            }
            writer.println();
        }

        writer.flush();
        writer.close();
    }

    /**
     * Загружает структуру сети из файла заданного формата ({@link #save(File)}.
     * После загрузки автоматически осуществляется поиск кластера.
     * 
     * @param file
     * @throws FileNotFoundException -
     *           в случае, если переданный файл не найден.
     * @throws BadNetFileFormatException -
     *           в случае, если структура переданного файла не соотвествует
     *           заданной
     */
    public void load(File file) throws FileNotFoundException,
            BadNetFileFormatException {
        Scanner scanner = new Scanner(new BufferedReader(new FileReader(file)));

        // Считываем генератор структуры сети
        NetStructureInfo structureInfo = new NetStructureInfo();
        structureInfo.setGenerator(NetGenerators.valueOf(scanner.next())
                .getGenerator());
        /*
         * Считываем ширину и высоту решетки и создаем структуру сети
         */
        structureInfo.setWidth(scanner.nextInt());
        structureInfo.setHeight(scanner.nextInt());
        generate(structureInfo);

        /*
         * Для каждого узла считываем, инфицирован он или нет
         */
        for (Node node : this) {
            int temp = scanner.nextInt();
            switch (temp) {
            case 0:
                node.setInfected(false);
                break;
            case 1:
                node.setInfected(true);
                break;
            default:
                throw new BadNetFileFormatException(file);
            }

        }

        scanner.close();

        /*
         * Осуществляем поиск кластеров
         */
        findClusters();
    }

    /**
     * Осуществляет сравнение сети. Две сети считаются равными, если совпадает
     * метод их генерации, сходятся все количественные характеристики слоев, и
     * они одинаково заражены.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RegularLattice)) {
            return false;
        }

        RegularLattice other = (RegularLattice) obj;
        if (getGenerator().getClass() != other.getGenerator().getClass()) {
            return false;
        }

        if (getWidth() != other.getWidth()) {
            return false;
        }

        if (getHeight() != other.getHeight()) {
            return false;
        }

        for (int i = 0; i < getLayers().size(); i++) {
            Layer myLayer = getLayers().get(i);
            Layer otherLayer = other.getLayers().get(i);

            if (myLayer.size() != otherLayer.size()) {
                return false;
            }

            for (int j = 0; j < myLayer.size(); j++) {
                Node myNode = myLayer.getNode(j);
                Node otherNode = otherLayer.getNode(j);

                if (myNode.isInfected() != otherNode.isInfected()) {
                    return false;
                }
            }
        }

        return true;
    }

    void printClusters(PrintStream out) {
        for (Cluster cluster : clusters) {
            out.println(cluster);
        }
    }
}
