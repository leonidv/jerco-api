package jerco.network.io;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jerco.network.Node;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.read.biff.WorkbookParser;

/**
 * Класс считывает структуру сети из файла Excel, в котором она задана матрицей
 * инцидентности.
 * 
 * @author Leonid Vygovskiy
 * 
 */
public class ExcelReader implements NetReader {
    private static final int SHEET_BOUNDS_INDEX = 1;

    private static final String SHEET_BOUNDS_NAME = "Ключевые узлы";

    private static final int STRUCTURE_SHEET_INDEX = 0;

    private static final String STRUCTURE_SHEET_NAME = "Структура";

    private final File xls;

    /**
     * Содержит отобажение идентификаторов узлов на узлы. Нужно для быстрого
     * поиска узла по идентификатору в процессе построения.
     */
    private final Map<Integer, Node> nodesIds = new HashMap<Integer, Node>();

    private final Map<String, Node> nodesNames = new HashMap<String, Node>();

    private Workbook workbook;

    public ExcelReader(String fileName) {
        this(new File(fileName));
    }

    public ExcelReader(File file) {
        xls = file;
    }

    public void loadNetwork() throws BiffException, IOException {
        nodesIds.clear();
        nodesNames.clear();

        workbook = WorkbookParser.getWorkbook(xls);
        
        Sheet sheet = findSheet(STRUCTURE_SHEET_NAME, STRUCTURE_SHEET_INDEX);
        readNodesNames(sheet);
        loadStructure(sheet);
        
        sheet = findSheet(SHEET_BOUNDS_NAME, SHEET_BOUNDS_INDEX);
        readBounds(sheet);

    }

    private void loadStructure(Sheet sheet) {
        for (int i = 1; i < sheet.getColumns(); i++) {
            Node a = getNode(i);

            Cell[] cells = sheet.getColumn(i);
            for (int j = 1; j < cells.length; j++) {
                Cell cell = cells[j];
                if (cell.getContents().trim().isEmpty()) {
                    continue;
                }

                Node b = getNode(j);
                Node.linkNodes(a, b);
            }
        }
    }

    /**
     * Считывает из листа структуры имена узлов и заполняет {@link #nodesNames}.
     * 
     * @param sheet
     */
    private void readNodesNames(Sheet sheet) {
        Cell[] titleRow = sheet.getRow(0);
        for (int i = 1; i < titleRow.length; i++) {
            Node node = getNode(i);
            nodesNames.put(titleRow[i].getContents(), node);
        }
    }

    /**
     * Считывает границы сети. Границы начинаются со второго столбика и в одной
     * ячейки имя одного узла границы.
     * 
     * @param sheet
     */
    private void readBounds(Sheet sheet) {
        for (int i = 1; i < sheet.getRows(); i++) {
            Cell[] bound = sheet.getRow(i);
            for (int j = 1; j < bound.length; j++) {
                String name = bound[j].getContents();

                if (name.isEmpty()) {
                    continue;
                }

                Node node = nodesNames.get(name);
                node.setBound(i-1);
            }
        }
    }

    /**
     * Возвращает лист со структурой сети. Это либо 0, либо первый с именем
     * "Структура".
     * @param name TODO
     * @param index TODO
     * 
     * @return
     */
    private Sheet findSheet(String name, int index) {
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheet(i);
            if (sheet.getName().equalsIgnoreCase(name)) {
                return sheet;
            }
        }
        return workbook.getSheet(index);
    }

    /**
     * Возвращает узел по его идентификтору. Если узел еще не был создан,
     * создает его.
     * 
     * @param id
     * @return
     */
    private Node getNode(int id) {
        if (nodesIds.containsKey(id)) {
            return nodesIds.get(id);
        }

        Node node = new Node(id);
        nodesIds.put(id, node);
        return node;
    }

    @Override
    public Set<Node> read() throws JercoReaderException {
        try {
            loadNetwork();
        } catch (BiffException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new HashSet<Node>(nodesIds.values());
    }

}
