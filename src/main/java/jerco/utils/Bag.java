package jerco.utils;

import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

public class Bag<T> {
    // Карта хранения сопоставления объектов и их встречающегося количества
    private NavigableMap<T, Integer> data = new TreeMap<T, Integer>();
    
    /**
     * Добавляет объект в сумку. При этом увеличивается количество объектов
     * @param obj
     */
    public void add(T obj) {
        add(obj, 1);
    }

    /**
     * Добавляет объект в сумку. При этом увеличивается количество объектов
     * @param obj
     * @param count TODO
     */
    public void add(T obj, int count) {
        int countInBag;
        if (data.containsKey(obj)) {
            // Уже существует. Добавляем значение
            countInBag = data.get(obj);
            countInBag += count;
        } else {
            // Встречается первый раз
            countInBag = count;
        }
        data.put(obj, countInBag);
    }
        
    /**
     * Возвращает множество пар <Ключ,Значение>, где ключ есть подсчитываемые
     * объекты, а значение - их количество
     * @return
     */
    public Set<Map.Entry<T,Integer>> iterator() {
        return data.entrySet();
    }
    
    /**
     * Возвращает количество вхождение переданного объекта.
     * @param obj
     * @return
     */
    public int getCount(T obj) {
        return data.get(obj);
    }
    
    /**
     * Возвращает неизменяемое множество ключей в сумке
     * @return
     */
    public Set<T> keySet() {
        return data.keySet();
    }
    
    /**
     * Удаляет всю информацию из сумки.
     */
    public void clear(){
        data.clear();
    }
        
    public Entry<T, Integer> firstEntry() {
        return data.firstEntry();
    }

    public T firstKey() {
        return data.firstKey();
    }

    public Entry<T, Integer> lastEntry() {
        return data.lastEntry();
    }

    public T lastKey() {
        return data.lastKey();
    }

    public int size() {
        return data.size();
    }
}
