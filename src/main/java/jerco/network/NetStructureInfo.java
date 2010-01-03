package jerco.network;

import jerco.network.generators.NetGenerator;
import jerco.network.generators.RectGenerator;

/**
 * Класс содержит описание структуры сети.
 * <p>
 * Класс является результатом рефакторинга объединения группы параметров в одну
 * сущность.
 * <p>
 * Класс гарантирует клиентам корректность установленных данных. По умолчанию
 * данные описывают сеть с прямоугольной решеткой {@link RectGenerator} и
 * размерами 100 на 100.
 * 
 * @author leonidv
 * 
 */
public class NetStructureInfo {
    private int width = 100;
    private int height = 100;
    private NetGenerator generator = RectGenerator.INSTANCE;

    /**
     * Создает описание структуры с параметрами по умолчанию.
     */
    public NetStructureInfo() {

    }
    
    /**
     * Создает копию объекта
     * @param structureInfo
     */
    public NetStructureInfo(NetStructureInfo structureInfo) {
        this.width = structureInfo.getWidth();
        this.height = structureInfo.getHeight();
        this.generator = structureInfo.getGenerator();
    }
    
    /**
     * Создает описание структуры с заданными параметрами
     * 
     * @param width -
     *          ширина сети
     * @param height -
     *          высота сети
     * @param generator -
     *          генератор решетки сети
     */

    public NetStructureInfo(int width, int height, NetGenerator generator) {
        this.width = width;
        this.height = height;
        this.generator = generator;
    }

    public int getWidth() {
        return width;
    }

    /**
     * Устанавливает ширину сети
     * 
     * @param width
     * @throws IllegalArgumentException -
     *           в случае, если ширина меньше 1
     */
    public void setWidth(int width) {
        if (width < 1) {
            throw new IllegalArgumentException("Ширина должна быть больше 0");
        }
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * Устанавливает высоту сети
     * 
     * @param height
     */
    public void setHeight(int height) {
        if (height < 1) {
            throw new IllegalArgumentException("Высота должна быть больше 0");
        }
        this.height = height;
    }

    public NetGenerator getGenerator() {
        return generator;
    }

    public void setGenerator(NetGenerator generator) {
        if (generator == null) {
            throw new NullPointerException("generator не может быть null");
        }
        this.generator = generator;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((generator == null) ? 0 : generator.hashCode());
        result = prime * result + height;
        result = prime * result + width;
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
        final NetStructureInfo other = (NetStructureInfo) obj;
        if (generator == null) {
            if (other.generator != null)
                return false;
        } else if (!generator.equals(other.generator))
            return false;
        if (height != other.height)
            return false;
        if (width != other.width)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return String.format("generator = %s, width = %d, height = %d",
                generator, width, height);
    }
}
