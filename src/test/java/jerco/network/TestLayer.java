package jerco.network;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.Test;

import static jerco.TestUtils.*;

public class TestLayer extends TestBase {

	@Test
	public void testLinkNodes() {
		Node a = new Node();
		Node b = new Node();
		Node.linkNodes(a, b);
		Iterator<Node> iterA = a.iterator();
		Iterator<Node> iterB = b.iterator();
		assertTrue("a.hasNext()", iterA.hasNext());
		assertTrue("b.hasNext()", iterB.hasNext());
		assertEquals(b, iterA.next());
		assertEquals(a, iterB.next());

	}
	
	/**
	 * Данный тест проверяет инициализацию нового слоя. Проверяется, что 
	 * было добавлено нужно количество узлов, а также все методы получения узлов.
	 */
	@Test
	public void testLayer() {
		Layer layer = new Layer(3);
		
		/*
		 * Проверяем, что вся длина вернулась правильно
		 */		
		assertEquals(3, layer.size());
		assertEquals(3, layer.toArray().length);		
		
		/*
		 * Проверяем, что все функции доступа возвращают одни и те же узлы
		 */
		Node[] arr = layer.toArray();
		Iterator<Node> iterator = layer.iterator();
		for (int i = 0; i < 3; i++) {
			assertTrue(String.format("i = %d, iterator.hasNext()", i),iterator.hasNext());
			Node x = iterator.next();
			Node y = arr[i];
			Node z = layer.getNode(i);
			assertEquals(String.format("i = %d, x = y",i), x, y);
			assertEquals(String.format("i = %d, x = z",i), x, z);
			assertEquals(String.format("i = %d, y = z",i), y, z);
		}		
	}
	
	/**
	 * Проверяет на связаннось узлов в одном слое
	 * <pre>
	 * 0-1-2
	 * </pre>
	 */
	@Test
	public void testlinkedNeighbors() {
	    Layer layer = new Layer(3);
	    layer.linkNeighbors();
	    checkNode(layer.getNode(0), 0, new Integer[]{1});
	    checkNode(layer.getNode(1), 1, new Integer[]{0,2});
	    checkNode(layer.getNode(2), 2, new Integer[]{1});	    
	}
	
	/**
	 * Check leftmost node.
	 */
	@Test
	public void testLeftmost() {
	    Layer layer = new Layer(1);
	    assertEquals(layer.getNode(0), layer.getLeftmost());
	    
	    layer = new Layer(3);
	    assertEquals(layer.getNode(0), layer.getLeftmost());
	}
	
	/**
	 * Check rightmost node.
	 */
	@Test
	public void testRightmost() {
	    Layer layer = new Layer(1);
	    assertEquals(layer.getNode(0), layer.getRightmost());
	    
	    layer = new Layer(3);
	    assertEquals(layer.getNode(2), layer.getRightmost());
	}
}
