package jerco.utils;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;


public class TestBag {
    @Test
    public void countTest() {
        Bag<String> bag = new Bag<String>();
        for (int i = 0; i < 5; i++) {
            bag.add("test", 1);
        }
        assertSame(5, bag.getCount("test"));
    }

    @Test
    public void keySetTest() {
        Bag<Integer> bag = new Bag<Integer>();
        List<Integer> testList = new ArrayList<Integer>(10);
        for (int i = 1; i <= 10; i++) {
            for (int j = 0; j < i; j++) {
                bag.add(i, 1);
            }
            testList.add(i);
        }
        
        for (Integer i : bag.keySet()) {
            assertSame(i,bag.getCount(i));
            assertTrue(i.toString(),testList.remove(i));
        }
        assertSame(0, testList.size());
    }
}
