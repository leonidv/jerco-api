package jerco.network.generators;

/**
 * Определяет заданный по умолчанию набор способов построения сети с регулярной
 * структурой. Сами реализации генераторов спрятаны от пользователя. Для доступа
 * к ним используется метод getGenerator. Начиная с версия 0.5 класс запрещен к
 * использованию. Вместо него нужно использовать генераторы на прямую.
 * 
 * @author leonidv
 */
@Deprecated
public enum NetGenerators {
    Rectangle {
        public NetGenerator getGenerator() {
            return RectGenerator.INSTANCE;
        }
    },
    Triangle {
        public NetGenerator getGenerator() {
            return new TriangGenerator();
        }
    },
    Hexagone {
        public NetGenerator getGenerator() {
            return new HexaGenerator();
        }
    };

    /**
     * Исключение выбрасывается в случае, если в функцию определения типа
     * генератора передана переменная неизвестного класса.
     * 
     * @author leonidv
     * 
     */
    public static class UnknownNetGenerator extends RuntimeException {
        private static final long serialVersionUID = 1668537396916340401L;

        UnknownNetGenerator(NetGenerator generator) {
            super("Неизвестный тип генератора "
                    + generator.getClass().getCanonicalName());
        }

    };

    /**
     * Возвращает тип генератора по экземпляру переменной.
     * 
     * @param generator
     * @return
     */
    public static NetGenerators getGeneratorType(NetGenerator generator) {
        if (generator instanceof RectGenerator) {
            return Rectangle;
        } else if (generator instanceof TriangGenerator) {
            return Triangle;
        } else if (generator instanceof HexaGenerator) {
            return Hexagone;
        } else {
            throw new UnknownNetGenerator(generator);
        }
    }

    /**
     * Возвращает соотвествующий заданному типу генератор сети.
     * 
     * @return
     */
    public abstract NetGenerator getGenerator();
}
