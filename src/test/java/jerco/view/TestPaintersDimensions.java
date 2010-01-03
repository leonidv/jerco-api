package jerco.view;

import static org.junit.Assert.*;
import jerco.network.RegularLattice;
import jerco.network.NetStructureInfo;
import jerco.network.generators.NetGenerators;
import jerco.network.generators.RectGenerator;
import jerco.view.CircleEdgesPainter;
import jerco.view.ShapesPainter;

import org.junit.Before;
import org.junit.Test;


public class TestPaintersDimensions {
    RegularLattice net;

    @Before
    public void setUp() {
        NetStructureInfo structureInfo = new NetStructureInfo();
        structureInfo.setGenerator(RectGenerator.INSTANCE);
        structureInfo.setWidth(10);
        structureInfo.setHeight(10);
        net = new RegularLattice(structureInfo);
    }

   
    public void testCircleEdgePainter() {
        ShapesPainter painter = new CircleEdgesPainter();
        int width = painter.getDimension(net).width;
        int height = painter.getDimension(net).height;
        assertTrue(186 == width);
        assertTrue(386 == height);
    }

}
