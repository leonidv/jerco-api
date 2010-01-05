package jerco.network;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

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
    private static final String STRUCTURE_SHEET_NAME = "Структура";

    private final File xls;

    /**
     * Содержит отобажение идентификаторов узлов на узлы. Нужно для быстрого
     * поиска узла по идентификатору в процессе построения.
     */
    private final Map<Integer, Node> nodesIds = new TreeMap<Integer, Node>();

    public ExcelReader(String fileName) {
        this(new File(fileName));
    }

    public ExcelReader(File file) {
        xls = file;
    }

    public void loadNetwork() throws BiffException, IOException {
        nodesIds.clear();

        Workbook workbook = WorkbookParser.getWorkbook(xls);
        Sheet sheet = getStructureSheet(workbook);

        for (int i = 1; i < sheet.getColumns(); i++) {
            Node a = getNode(i);

            Cell[] cells = sheet.getColumn(i);
            for (int j = 1; j < cells.length; j++) {
                Cell cell = cells[j];
                if (cell.getContents().trim().isEmpty()) {
                    continue;
                }

                Node b = getNode(j);
                a.linkTo(b);
            }
        }

    }

    /**
     * Возвращает лист со структурой сети. Это либо 0, либо первый с именем
     * "Структура".
     * 
     * @param workbook
     * @return
     */
    private Sheet getStructureSheet(Workbook workbook) {
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheet(i);
            if (sheet.getName().equalsIgnoreCase(STRUCTURE_SHEET_NAME)) {
                return sheet;
            }
        }
        return workbook.getSheet(0);
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
    public Set<Node> read() {
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
