package jerco.network.io;

import java.util.Set;

import jerco.network.Node;


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
