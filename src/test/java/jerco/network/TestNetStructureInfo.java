package jerco.network;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import jerco.network.generators.RectGenerator;

import org.junit.Test;


public class TestNetStructureInfo {
    private NetStructureInfo structureInfo = new NetStructureInfo();

    /**
     * Проверяем поведение при неправильно заданной ширине решетки
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIllegalWidth() {
        structureInfo.setWidth(0);
    }

    /**
     * Проверяем поведение при неправильно заданной высоте решетки
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIllegalHeight() {
        structureInfo.setHeight(0);
    }
    
    @Test
    public void testEquals() {
        NetStructureInfo a = new NetStructureInfo(300, 200,
                RectGenerator.INSTANCE);
        NetStructureInfo b = new NetStructureInfo(300, 200,
                RectGenerator.INSTANCE);
        assertTrue(a.equals(a));
        assertTrue(a.equals(b));
        assertTrue(b.equals(a));
        
        b.setWidth(400);
        assertFalse(a.equals(b));
        assertFalse(b.equals(a));
        assertTrue(b.equals(b));
    }
}
