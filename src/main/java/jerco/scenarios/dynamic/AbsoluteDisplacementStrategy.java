package jerco.scenarios.dynamic;

import jerco.Constants;
import jerco.network.InfectedNodeIterator;
import jerco.network.Node;

/**
 * Класс реализует стратегию абсолютного покрытия сети. За один шаг из одного
 * узла происходит протекание только в один другой узел. Узел выбирается по
 * наименьшей вероятности заражения.
 * 
 * @author leonidv
 * 
 */
public class AbsoluteDisplacementStrategy implements DisplacementStrategy {

    /**
     * Находит узел, откуда произойдет протекание вещества из переданного узла.
     * При рассматриваются следующие узлы:
     * <ul>
     * <li>Узел должен быть заражен</li>
     * <li><Узел должен быть заполнен водой/li>
     * </ul>
     * Вещество из текущего узла всегда перетекает в такой узел, который
     * наиболее готов его принять (другими словами, вероятность заражения
     * которого минимальна).
     * 
     * @param source
     * @return
     */
    @Override
    public NextNodesInfo findDestinationNodes(Node sourceNode) {
        NextNodesInfo result = new NextNodesInfo();

        Node destination = null;
        double minProbabality = Double.MAX_VALUE;
        for (Node node : new InfectedNodeIterator(sourceNode.iterator())) {

            if (node.getSubstance() != Node.OXYGEN) {
                continue;
            }

            result.incFindedCount();
            final double p = node.getDisplaceProbability();
            if (Constants.compare(minProbabality, p) == 1) {
                destination = node;
                minProbabality = p;
            }

        }
        result.addNextNode(destination);        
        
        result.setCurrentToNext(result.getFindedCount() > 1);
        
        return result;
    }

}
