package jerco.scenarios.dynamic;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.imageio.ImageIO;

import jerco.network.InfectedNodeIterator;
import jerco.network.RegularLattice;
import jerco.network.NetStructureInfo;
import jerco.network.Node;
import jerco.network.generators.CylinderGenerator;
import jerco.scenarios.Scenario;
import jerco.scenarios.ScenarioExecuteException;
import jerco.scenarios.ScenarioProgressIndicator;
import jerco.view.Painter;
import jerco.view.SquarePainter;
import jerco.view.StructureNetRender;
import jerco.view.colorer.DisplacementColorer;


/**
 * Реализует перколяцию замещения. На выход предоставляет информацию шагах
 * работы. Под шагом понимается попытка заражения соседних к каждому узлу фронта
 * узлов. Каждый шаг описывается изображением сети и фронтом.
 * 
 * @author leonidv
 * 
 */
public class DisplacementScenario extends Scenario {

    private static final int NO_PERCOLATION_YET = -1;
    
    /**
     * Точка входа в программу. 
     * @param args
     */
    public static void main(String[] args) {
        // Создаем структуру с информацией об сети
        NetStructureInfo structureInfo = new NetStructureInfo();

        // Устанавливаем генератор цилиндрической решетки
        structureInfo.setGenerator(new CylinderGenerator());

        // Устанавливаем количество узлов по ширине
        structureInfo.setWidth(100);

        // Устанавливаем количество узлов по высоте
        structureInfo.setHeight(100);

        // Создаем граф (сеть) на основе заданной информации
        RegularLattice net = new RegularLattice(structureInfo);

        /*
         * Заражаем сеть с веротностью 0.6 до тех пор, пока не будет создан
         * перколяционный кластер.
         */
        do {
            System.out.println("Попытка генерации перколяционного кластера");
            net.infect(0.6);
        } while (!net.hasPercolationCluster());

        // Создаем сценарий замещения
        DisplacementScenario scenario = new DisplacementScenario();

        // Указываем, какую сеть будет обрабатывать сценарий
        scenario.setNet(net);

        // Устанавливаем стратегию выбора зараженного узла
        scenario.setStrategy(new AbsoluteDisplacementStrategy());

        /*
         * Делаем настройку раскрасщика узла
         */
        scenario.getColorer().setDefaultColor(Color.BLACK);
        scenario.getColorer().setFrontColor(Color.YELLOW);
        scenario.getColorer().setSubstanceColor(Node.OXYGEN, Color.RED);
        scenario.getColorer().setSubstanceColor(Node.WATER, Color.BLUE);

        // Указываем, что при выполнении сценария нужно сохранять картинки
        scenario.setSaveImages(true);

        // Устанавливаем индикатор хода работы, который выводит сообщения
        // в консоль
        scenario.setIndicator(ScenarioProgressIndicator.SYSTEM_OUT_INDICATOR);
        // scenario.setFileNameTemplate("samples/dynamic/500x500_02/%05d.png");
        // Задаем путь к выводу картинок (файлы сохраняются в оперативную
        // память)
        scenario.setFileNameTemplate("/dev/shm/images/%05d.png");

        // Задаем узлов для инъекции. Берется первый зараженный узел в первом
        // слое
        for (Node node : net.getLayers().get(0)) {
            if (node.isInPercolationCluster()) {
                scenario.addInitialNode(node);
                break;
            }
        }

        // Запускаем выполнение сценария
        scenario.doScenario();

        try {
            // Открываем на запись файлы для вывода данных по фронту
            // распространения вируса
            PrintWriter frontSizeFile = new PrintWriter(new BufferedWriter(
                    new FileWriter("/dev/shm/front.dat")));

            // Открываем на запись файлы для вывода данных по общему количеству
            // зараженных узлов
            PrintWriter totalCountFile = new PrintWriter(new BufferedWriter(
                    new FileWriter("/dev/shm/total.dat")));

            // Получаем список с информацией о каждом шаге заражения
            List<StepInfo> result = scenario.getResult();
            
            /*
             * Выводим файлы в информацию о каждом шаге выполнения
             */
            int total = 0;

            for (StepInfo stepInfo : result) {
                total += stepInfo.getFrontSize();
                frontSizeFile.println(stepInfo.getFrontSize());
                totalCountFile.println(total);
            }
            
            /*
             * Закрываем файлы.
             */
            frontSizeFile.close();
            totalCountFile.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static final long serialVersionUID = 6442001063754030036L;

    // Ссылка на обрабатываемую сеть
    private RegularLattice net;

    // Вероятност заражения узла сети воздухом
    private int infectProbability;

    // Средняя вероятность вытеснения воздуха
    private double displaceMean = 0.65;

    // Отклонение от средней вероятности вытеснения воздуха
    private double displaceDeveration = 0.15;

    // Устанавливает страгию выбора узлов для замещения
    private DisplacementStrategy strategy;

    // Нулевой фронт, с которого начинается распространение замещения
    private Collection<Node> initialFront = new HashSet<Node>();

    // Генератор случайных чисел
    private Random random = new Random();

    // Хранит список шагов протекания перколяции
    private List<StepInfo> result;

    // Рисовальщик сети
    private Painter painter = new SquarePainter();

    // Раскрасщик узлов сети
    private DisplacementColorer colorer = new DisplacementColorer();

    // Шаблон пути к выходному файлу набора изображений
    private String fileNameTemplate;

    // Флаг определяет, нужно сохранять изображения или нет.
    private boolean saveImages = false;

    // Флаг определяет, нужно сохранять картинку с начальным состоянием сети
    // или нет
    private boolean saveThumbail = false;

    // Общее количество зараженных узлов в данном сценарии
    private int totalDisplacement;

    // Общее количество узлов, которые могут быть заражены
    private int initInfectableCount;

    // Шаг, на котором образовался перколяционный кластер\
    private int percolationStep = NO_PERCOLATION_YET;

    // Флажок, есть ли хотя бы один узел в верхней границе сети
    private boolean hasNodeInTop = false;

    // Флажок, есть ли хотя бы один узел в нижней границе сети
    private boolean hasNodeInBottom = false;

    /**
     * Создает объект
     */
    public DisplacementScenario() {
        painter.setNodeColorer(colorer);
    }

    @Override
    public void doScenario() {
        if (strategy == null) {
            throw new ScenarioExecuteException(
                    "Для работы сценария необходимо "
                            + "установить стратегию замещения (setStrategy())");
        }

        if (net == null) {
            throw new ScenarioExecuteException(
                    "Для работы сценария необходимо "
                            + "установить сеть (setNet())");
        }

        prepareNet(net);
        getIndicator().init(initInfectableCount);

        result = new ArrayList<StepInfo>();

        Collection<Node> front = initialFront;
        result.add(new StepInfo(front, totalDisplacement));

        int step = 0;
        if (saveThumbail || saveImages) {
            drawImage(net, initialFront, step);
        }
        while ((front.size() > 0) && (!stop)) {
            step++;
            getIndicator().progress(totalDisplacement);
            Collection<Node> nextFront = makeStep(net, front);
            if (saveImages) {
                drawImage(net, nextFront, step);
            }
            result.add(new StepInfo(nextFront, totalDisplacement));
            front = nextFront;

            if ((percolationStep == NO_PERCOLATION_YET) && hasNodeInBottom
                    && hasNodeInTop) {
                percolationStep = step;
            }
        }

        result.add(new StepInfo(front, totalDisplacement));

    }

    /**
     * Осуществляет рисование изображения и сохранение полученной картинки в
     * файл
     * 
     * @param net
     * @param front
     * @param stepCount
     * @return
     */
    private BufferedImage drawImage(RegularLattice net, Collection<Node> front,
            int stepCount) {
        colorer.setFront(front);
        final BufferedImage image = StructureNetRender
                .renderImage(net, painter);
        String fileName = String.format(fileNameTemplate, stepCount);
        try {
            ImageIO.write(image, "png", new File(fileName));
        } catch (IOException e) {
            throw new ScenarioExecuteException(e);
        }
        return image;

    }

    /**
     * Сбрасываем состояние зараженности у узлов, не находящихся в
     * перколяционном кластере. Для каждого кластера, который является
     * зараженным, устанавливаем вещество OXYGEN.
     * 
     * @param net
     */
    void prepareNet(RegularLattice net) {
        for (Node node : new InfectedNodeIterator(net.iterator())) {
            if (node.isInPercolationCluster()) {
                node.setSubstance(Node.OXYGEN);
                node.setDisplaceProbability(generateDisplaceProbability());
                initInfectableCount++;
            } else {
                node.setInfected(false);
            }
        }

        for (Node node : initialFront) {
            node.setSubstance(Node.WATER);
            chekBoundsNode(node);
        }

        totalDisplacement = initialFront.size();
    }

    /**
     * Моделирует процесс заражения узлов сети из заданного фронта.
     * 
     * @param net
     * @param front
     * @return следующий фронт заражения
     */
    Collection<Node> makeStep(RegularLattice net, Collection<Node> front) {
        Set<Node> nextFront = new HashSet<Node>(front.size());

        for (Node frontNode : front) {
            // NextNodesInfo info = findDestinationNode(frontNode);
            NextNodesInfo info = strategy.findDestinationNodes(frontNode);

            // Не нашли ни одного подходящего узла
            if (info.getFindedCount() == 0) {
                continue;
            }

            for (Node nextNode : info.getNextNodes()) {
                if (nextNode.getSubstance() != Node.WATER) {
                    nextNode.setSubstance(Node.WATER);

                    chekBoundsNode(nextNode);

                    totalDisplacement++;
                }

                boolean check = nextFront.add(nextNode);
                assert (check != false);
            }

            // Если были еще кандидаты на добавление во фронт, добавляем
            // текущий узел в следующий фронт. Чтобы осуществить добавление
            // оставшихся возможных узлов в следующий раз
            if (info.getFindedCount() > 1) {
                nextFront.add(frontNode);
            }
        }

        return nextFront;
    }

    /**
     * Проверяет, принадлежит ли узел верхнему или нижнему узлы сети.
     * 
     * @param nextNode
     */
    private void chekBoundsNode(Node nextNode) {
        if (nextNode.isInBottomBound()) {
            hasNodeInBottom = true;
        }

        if (nextNode.isInTopBound()) {
            hasNodeInTop = true;
        }
    }

    /**
     * Рассчитываем вероятность замещения на основе среднего значения и
     * отклонения.
     * <p>
     * Общая идея такая. Получим случайное число, которое обозначает отклонение
     * и лежит в двойном диапазоне
     * 
     * <pre>
     *   (mean-deveration ... mean ... mean+deveration).
     * </pre>
     * 
     * Далее добавляем отклонение к среднему и получаем вероятность замещения.
     * </p>
     * <p>
     * Проблема заключается в том, что датчик случайных чисел может бросить
     * число толко в диапазоне в положительном диапазоне. Поэтому получаем
     * отклонение в диапазоне:
     * 
     * <pre>
     * d' = (0 ... 2*deveration)
     * </pre>
     * 
     * Далее отнимаем полученное число от следующего значения:
     * 
     * <pre>
     * (mean+deviration - d')
     * </pre>
     * 
     * </p>
     * 
     * @return верояность замещения, лежащую в диапазоне
     *         {@link #getDisplaceMean()}+/-{@link #getDisplaceDeveration()}
     */
    double generateDisplaceProbability() {
        final double nextDouble = random.nextDouble();
        double doubleMean = nextDouble * (2 * displaceDeveration);
        return displaceMean + displaceDeveration - doubleMean;
    }

    /**
     * Возвращает ссылку на сеть, на которой осуществляется сценарий
     * 
     * @return
     */
    public RegularLattice getNet() {
        return net;
    }

    public void setNet(RegularLattice net) {
        if (net == null) {
            throw new NullPointerException(
                    "Сеть должна быть существующим объектом (net == null)");
        }
        this.net = net;
    }

    /**
     * Возвращает вероятность первичного заражения узла. Вероятность
     * используется при образование кластера
     * 
     * @return
     */
    public int getInfectProbability() {
        return infectProbability;
    }

    /**
     * Устанавливает вероятность первичного заражения узла. Вероятность
     * используется при образование кластера.
     * 
     * @param infectProbability
     */
    public void setInfectProbability(int infectProbability) {
        this.infectProbability = infectProbability;
    }

    /**
     * Возвращает среднее значение вероятности замещения одного вещества другим
     * 
     * @return
     */
    public double getDisplaceMean() {
        return displaceMean;
    }

    /**
     * Устанавливает среднее значение вероятности замещения одного вещества
     * другим
     * 
     * @param displaceMean
     */
    public void setDisplaceMean(double displaceMean) {
        this.displaceMean = displaceMean;
    }

    /**
     * Возвращает отклонение от средней вероятности замещения одного вещества
     * другим
     * 
     * @return
     */
    public double getDisplaceDeveration() {
        return displaceDeveration;
    }

    /**
     * Устанавливает отклонение от средней вероятности замещения одного вещества
     * другим
     * 
     * @param displaceDeveration
     */
    public void setDisplaceDeveration(double displaceDeveration) {
        this.displaceDeveration = displaceDeveration;
    }

    /**
     * Возвращает указатель на страегию выбора узлов для замещения
     * 
     * @return
     */
    public DisplacementStrategy getStrategy() {
        return strategy;
    }

    /**
     * Устанавливает стратегию выбора узлов для замещения
     * 
     * @param strategy
     */
    public void setStrategy(DisplacementStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Возвращает список узлов, из которых начинает происходит замещение одного
     * вещества другим.
     * 
     * @return
     */
    public Collection<Node> getInitialFront() {
        for (Node node : initialFront) {
            node.setSubstance(Node.WATER);
        }
        return initialFront;
    }

    /**
     * Добавляем узел в начальный фронт. Узел должен принадлежать
     * перколяционному кластеру, иначе будет брошено исключение
     * {@link IllegalArgumentException}
     * 
     * @param node
     */
    public void addInitialNode(Node node) {
        if (!node.isInPercolationCluster()) {
            throw new IllegalArgumentException(
                    "Узел должен принадлежать перколяционному кластеру");
        }
        initialFront.add(node);
    }

    /**
     * Возвращает список шагов протекания процесса перколяции.
     * 
     * @return
     */
    public List<StepInfo> getResult() {
        return result;
    }

    /**
     * Возвращает текущего рисовальщик сети.
     * 
     * @return
     */
    public Painter getPainter() {
        return painter;
    }

    /**
     * Устанавливает нового рисовальщика сети.
     * 
     * @param painter
     */
    public void setPainter(Painter painter) {
        this.painter = painter;
    }

    /**
     * Возвращает текущего раскрасщика узлов
     * 
     * @return
     */
    public DisplacementColorer getColorer() {
        return colorer;
    }

    /**
     * Возвращает шаблон форматирования имени файла получаемых изображений.
     * 
     * @return
     */
    public String getFileNameTemplate() {
        return fileNameTemplate;
    }

    /**
     * Возвращает, будут ли сохранятся изображения при осуществление
     * эксперимента или нет
     * 
     * @return
     */
    public boolean isSaveImages() {
        return saveImages;
    }

    /**
     * Устанавливает, нужно ли сохранять изображения при осущесвлении
     * эксперимента или нет
     * 
     * @param saveImages
     */
    public void setSaveImages(boolean saveImages) {
        this.saveImages = saveImages;
    }

    /**
     * Возвращает, нужно сохранять картинку с начальным состоянием сети
     * 
     * @return
     */
    public boolean isSaveThumbail() {
        return saveThumbail;
    }

    /**
     * Устанавливает, нужно сохранять картинку с начальным состоянием сети или
     * нет.
     * <p>
     * Установка флага приведет к обязательной отрисовке картинки для 0 шага.
     * 
     * @param saveThumbail
     */
    public void setSaveThumbail(boolean saveThumbail) {
        this.saveThumbail = saveThumbail;
    }

    /**
     * Шаблон формирования имени выходного файла с картинкой. Первым аргументов
     * функции {@link String#format(String, Object...)} передается номер
     * очередного шага процесса замещения.
     * 
     * @param fileNameTemplate
     *            - шаблон форматирования имени файла
     */

    public void setFileNameTemplate(String fileNameTemplate) {
        this.fileNameTemplate = fileNameTemplate;
    }

    /**
     * Устанавливает нового раскрасщика узлов.
     * 
     * @param colorer
     */
    public void setColorer(DisplacementColorer colorer) {
        this.colorer = colorer;
        painter.setNodeColorer(colorer);
    }

    /**
     * Возвращает номер шага, на котором был образован перколяционный кластер.
     * 
     * @return
     */
    public int getPercolationStep() {
        return percolationStep;
    }
}
