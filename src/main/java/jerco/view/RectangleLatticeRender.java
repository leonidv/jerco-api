package jerco.view;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import jerco.network.Layer;
import jerco.network.Node;
import jerco.network.RegularLattice;


/**
 * @author leonidv
 */
public class RectangleLatticeRender {

    /**
     * Сохраняет представление сети в заданный файл.
     * 
     * @param net
     *          сеть, представление которой будет сохранено
     * @param painter
     *          способ рисования сети
     * @param fileName
     *          имя файла, в которую будет сохранено изображение представления
     *          сети
     * @throws IOException
     *           в случае ошибки создания каталога или записи изображения в файл
     */
    public static BufferedImage renderToFile(RegularLattice net, Painter painter,
            String fileName) throws IOException {

        BufferedImage image = renderImage(net, painter);

        File imageFile = new File(fileName);
        if ((imageFile.getParentFile() != null)
                && (!imageFile.getParentFile().isDirectory())) {
            if (!imageFile.getParentFile().mkdirs()) {
                throw new IOException("Make directory error");
            }
        }

        ImageIO.write(image, "PNG", imageFile);

        return image;
    }

    /**
     * Осуществляет отображение переданной сети заданный отображателем
     * 
     * @param net -
     *          отображаемая сеть;
     * @param painter -
     *          рисовальщик сети;
     * @return - нарисованное изображение сети.
     */
    public static BufferedImage renderImage(RegularLattice net, Painter painter) {
        RectangleLatticeRender render = new RectangleLatticeRender(net, painter);

        BufferedImage image = new BufferedImage(render.getImageWidth(), render
                .getImageHeight(), BufferedImage.TYPE_INT_RGB);
        render.render(image.createGraphics());
        return image;
    }

    // Список слоев, который отображаются
    final private RegularLattice network;

    // Стратегия рисования
    private Painter painter;

    /**
     * Принимает на вход список слоев, которые необходимы для отображения.
     * 
     * @param RegularLattice
     *          отображаемая сеть
     * @throws NullPointerException -
     *           в случае, если передан указатель null
     */
    RectangleLatticeRender(RegularLattice network, Painter painter) {
        if (network == null) {
            throw new NullPointerException(
                    "Параметр network не может быть равен null");
        }

        if (painter == null) {
            throw new NullPointerException(
                    "Параметр painter не может быть равен null");

        }

        this.network = network;
        this.painter = painter;
    }

    /**
     * Осуществляет отображение структурированной сети
     * 
     * @param g
     *          канва, на которой осуществляется рисование
     */
    public void render(Graphics2D g) {
        if (g == null) {
            throw new NullPointerException("g не может быть равна null");
        }
        Map<Node, Node2D> nodes2D = createNode2DMap();
        Collection<Edge2D> edges = createEdgesCollection(nodes2D);

        painter.prepare(g, network);

        for (Edge2D edge : edges) {
            painter.paintEdge(edge, g);
        }

        for (Node2D node2D : nodes2D.values()) {
            painter.paintNode(node2D, g);
        }
    }

    /**
     * Сопоставляет каждому узлу описание двухмерного узла Node2D
     * 
     * @return
     */
    private Map<Node, Node2D> createNode2DMap() {
        Map<Node, Node2D> data = new HashMap<Node, Node2D>();

        int layerIndex = -1;
        for (Layer layer : network.getLayers()) {
            layerIndex++;
            int nodeIndex = -1;
            for (Node node : layer) {
                nodeIndex++;
                Node2D node2D = new Node2D(node, layerIndex, nodeIndex);
                data.put(node, node2D);
            }
        }
        return data;
    }

    /**
     * Строит список ребер. Поскольку ребра направленными, для каждой пары
     * узел-узел строит ребро.
     * 
     * @param nodes2D
     * @return
     */
    private Collection<Edge2D> createEdgesCollection(Map<Node, Node2D> nodes2D) {
        Collection<Edge2D> edges = new ArrayList<Edge2D>();
        network.resetVisited();
        for (Node node : network) {
            Node2D node2D = nodes2D.get(node);
            for (Node linkedNode : node) {
                Node2D linkedNode2D = nodes2D.get(linkedNode);

                Edge2D edge = new Edge2D(node2D, linkedNode2D);
                edges.add(edge);
            }
        }
        return edges;
    }

    /**
     * Возвращает объект рисовальщик сети.
     * 
     * @return
     * @uml.property name="painter"
     */
    public Painter getPainter() {
        return painter;
    }

    /**
     * Устанавливает новую стратегию отображения сети.
     * 
     * @param painter
     * @uml.property name="painter"
     */
    public void setPainter(Painter painter) {
        this.painter = painter;
    }

    /**
     * Возвращает минимальную ширину канвы, которая необходима для отображения
     * сети.
     * 
     * @return
     */
    public int getImageWidth() {
        return painter.getDimension(network).width;
    }

    /**
     * Возвращает минимальную высоту канвы, которая необходима для отображения
     * сети.
     */
    public int getImageHeight() {
        return painter.getDimension(network).height;
    }
}
