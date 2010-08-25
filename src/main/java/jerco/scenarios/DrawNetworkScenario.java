package jerco.scenarios;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.swing.ImageIcon;

import jerco.network.NetStructureInfo;
import jerco.network.RegularLattice;
import jerco.view.Painter;
import jerco.view.RectangleLatticeRender;
import jerco.view.SquarePainter;


public class DrawNetworkScenario extends Scenario {
    public static void main(String[] args) {
        DrawNetworkScenario scenario = new DrawNetworkScenario();
        scenario.setPath("samples/");
        scenario.setWidth(100);
        scenario.setHeight(100);
        scenario.setIndicator(ScenarioProgressIndicator.SYSTEM_OUT_INDICATOR);
        scenario.doScenario();
    }

    private static final String IMAGE_NAME_TEMPLATE = "%03d %d %d %f.png";

    /**
     * Ширина миниатюры изображения
     */
    public static int THUMBNAIL_WIDTH = 150;

    /**
     * Высота миниатюры изображения
     */
    public static int THUMBNAIL_HEIGHT = 150;

    /**
     * Отношение ширины миниатюры к высоте
     * 
     */
    public static double THUMBNAIL_RATIO = (double) THUMBNAIL_WIDTH
            / (double) THUMBNAIL_HEIGHT;

    // Количество изображений
    private int imagesCount = 10;

    // Путь к папке сохранения картинок
    private String path = "images/";

    // Ширина сети
    private int width = 500;

    // Высота сети
    private int height = 500;

    // Вероятность заражение сети
    private double probability = 0.593;

    // Рисовальщик
    private Painter painter = new SquarePainter();

    // Набор картинок с результатом
    private List<ImageIcon> thumbnails = new ArrayList<ImageIcon>();

    /**
     * В связи со спецификой сценария, возвращает пустую неизменяемую карту.
     */
    @Override
    public void doScenario() {
        thumbnails = new ArrayList<ImageIcon>();
        getIndicator().init(imagesCount);

        NetStructureInfo structureInfo = new NetStructureInfo();
        structureInfo.setGenerator(generator);
        structureInfo.setWidth(width);
        structureInfo.setHeight(height);
        
        RegularLattice net = new RegularLattice(structureInfo);

        try {
            for (int i = 0; (i < imagesCount) && !stop; i++) {
                net.resetInfected();
                net.infect(probability);

                String dateTemplateName = path                
                        + DateFormat.getDateTimeInstance().format(new Date())
                        + " " + IMAGE_NAME_TEMPLATE;

                String fileName = String.format(dateTemplateName, i+1, width,
                        height, probability);
                fileName = fileName.replace(",", ".");

                BufferedImage image = RectangleLatticeRender.renderToFile(net,
                        painter, fileName);
                addThumbnails(image);

                getIndicator().progress(i + 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        getIndicator().done();
    }

    /**
     * Добавляет переданное изображение в список эскизов. При этом на вход
     * принимается полноценное изображение, которое уменьшается в размерах.
     * 
     * @param image
     */
    private void addThumbnails(BufferedImage image) {
        /*
         * Получаем коэффециент преобразования изображения. Нам нужно выбрать,
         * по какой стороне будет делать масштабирование.
         */
        double imageRatio = (double) image.getWidth() / image.getHeight();
        double scaleCoeff;
        if (Double.compare(imageRatio, THUMBNAIL_RATIO) >= 0) {
            scaleCoeff = (double) THUMBNAIL_WIDTH / image.getWidth();
        } else {
            scaleCoeff = (double) THUMBNAIL_HEIGHT / image.getHeight();
        }

        /*
         * Создаем и применяем преобразование.
         */
        AffineTransform transform = AffineTransform.getScaleInstance(
                scaleCoeff, scaleCoeff);
        AffineTransformOp operation = new AffineTransformOp(transform,
                AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        thumbnails.add(new ImageIcon(operation.filter(image, null)));
    }

    /**
     * Возвращает неизменяемый список эскизов полученных в результате
     * экспримента изображений
     * 
     * @return
     */
    public List<ImageIcon> getThumbnails() {
        return Collections.unmodifiableList(thumbnails);
    }

    /**
     * Возвращает количество картинок
     * 
     * @return
     */
    public int getImagesCount() {
        return imagesCount;
    }

    /**
     * Устанавливает количество картинок
     * 
     * @param imagesCount
     */
    public void setImagesCount(int imagesCount) {
        this.imagesCount = imagesCount;
    }

    /**
     * Возвращает папку сохранения картинок
     * 
     * @return
     */
    public String getPath() {
        return path;
    }

    /**
     * Устанавливает папку сохранения картинок
     * 
     * @param path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Возвращает ширину сети (в узлах)
     * 
     * @return
     */
    public int getWidth() {
        return width;
    }

    /**
     * Устанавливает ширину сети (в узлах)
     * 
     * @param width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Возвращает высоту сети (в узлах)
     * 
     * @return
     */
    public int getHeight() {
        return height;
    }

    /**
     * Устанавливает высоту сети (в узлах)
     * 
     * @param height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Возвращает вероятность заражение
     * 
     * @return
     */
    public double getProbability() {
        return probability;
    }

    /**
     * Устанавливает вероятность заражения
     * 
     * @param probability
     */
    public void setProbability(double probabality) {
        this.probability = probabality;
    }

}
