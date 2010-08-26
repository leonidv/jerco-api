package jerco.network.io;

import static jerco.TestUtils.loadTestFile;

import org.junit.Test;

public class TestExcelReader extends TestReaders {

    @Test
    public void test2node() throws Exception {
        ExcelReader reader = new ExcelReader(loadTestFile("2nodes.xls"));
        test2Nodes(reader);
    }


    @Test
    public void testStar() throws Exception {
        ExcelReader reader = new ExcelReader(loadTestFile("star.xls"));
        testStar(reader);

    }
}
