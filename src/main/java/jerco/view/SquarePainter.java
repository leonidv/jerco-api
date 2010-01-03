package jerco.view;

import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class SquarePainter extends ShapesPainter {
    
    public SquarePainter() {
        shapeSize = 2;
        edgeLength = shapeSize * 2;
    }
    
    @Override
    protected Point2D.Double getShapeCenter(int layerIndex, int nodeIndex) {
        double x = shapeSize + nodeIndex * edgeLength;
        double y = shapeSize + layerIndex * edgeLength;
        return new Point2D.Double(x, y);
    }

    @Override
    protected Shape makeShape(Node2D node) {
        Point2D.Double center = getShapeCenter(node.getLayerIndex(), node
                .getNodeIndex());
        double x = center.x - shapeSize;
        double y = center.y - shapeSize;
        return new Rectangle2D.Double(x, y, shapeSize * 2, shapeSize * 2);
    }

    @Override
    protected double calculateDimension(int nodeCount) {
        return (nodeCount-1) * edgeLength + shapeSize * 2.2;
    }

}
