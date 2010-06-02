package jerco.network.generators;

import java.util.List;

import jerco.network.Layer;

public class LeftRightBoundsWrapper implements RegularWrapper{
public  void  wrap(List<Layer> layers)
{
	// Устанавливаем у узлов признак нахождения в левой и правой границе решётки
    for (Layer tlayer: layers)
    {
 	   tlayer.getLeftmost().setBound(NetGenerator.LEFT_BOUNDS);
 	   tlayer.getRightmost().setBound(NetGenerator.RIGHT_BOUNDS);
    }
}
}
