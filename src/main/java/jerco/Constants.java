package jerco;

public final class Constants {
    public static final int DOUBLE_FRACTION_LENGTH = 1000;
    public static final double DOUBLE_PRECISION = 1e-7;

    /**
     * Сравнивает переданные значения вещественных чисел на основе заданной
     * точности
     * 
     * @param a
     * @param b
     * @return
     */
    public static boolean isEqualsDoubles(double a, double b) {
        return Math.abs(a - b) < DOUBLE_PRECISION;
    }

    /**
     * Осуществляет сравнение чисел
     * 
     * @param a
     * @param b
     * @return 0, если числа равны, 1, если a больше b, -1 если a меньше b
     */
    public static int compare(double a, double b) {
        if (isEqualsDoubles(a, b)) {
            return 0;
        } else {
            if ((a - b) > 0) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    /**
     * Возвращает хэш-код вещественного числа. При этом осуществляется
     * приведение числа к целому на основе заданной точности вещественного
     * числа.
     * 
     * @return
     */
    public static int getDoubleHashCode(double value) {
        return (int) value * DOUBLE_FRACTION_LENGTH;
    }
}
