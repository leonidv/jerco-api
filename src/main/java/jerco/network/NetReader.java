package jerco.network;

import java.util.Set;

/**
 * Интерфейс определяет считывателя сети.
 * 
 * @author Leonid Vygovskiy
 * 
 */
public interface NetReader {

    /**
     * Метод считывание структуры сети. Возвращает множество связанных между
     * собой узлов, образующих структуру сети.
     * 
     * @return
     */
    public Set<Node> read();
}
