package jerco.scenarios.dynamic;

import java.awt.image.BufferedImage;
import java.util.Collection;

import jerco.network.Node;


/**
 * Описывает информацию об одном шаге прохождения сценария.
 * 
 */
final public class StepInfo {
    // Иконка для отображению preview анимации
    final private BufferedImage image;
    // Количество фронтов в текущем узле
    final private int frontSize;
    // Общее количество замещенных узлов на текущем шаге
    final private int totalDisplacement;

    /**
     * Создает информацию о шаге с указанным фронтом и
     * 
     * @param front
     * @param totalDisplacement -
     *          общее количество зараженных узлов
     * @param image
     */
    StepInfo(Collection<Node> front, int totalDisplacement, BufferedImage image) {
        this.image = image;
        frontSize = front.size();
        this.totalDisplacement = totalDisplacement;
    }

    /**
     * Создает информацию о шаге без изображения.
     * 
     * @param front
     * @param totalDisplacement
     */
    StepInfo(Collection<Node> front, int totalDisplacement) {
        this(front, totalDisplacement, null);
    }

    public int getFrontSize() {
        return frontSize;
    }

    public int getTotalDisplacement() {
        return this.totalDisplacement;
    }

    public BufferedImage getImage() {
        return image;
    }

    @Override
    public String toString() {
        return String.format("frontSize = %03d, totalDisplacement = %04d",
                frontSize, totalDisplacement);
    }
}