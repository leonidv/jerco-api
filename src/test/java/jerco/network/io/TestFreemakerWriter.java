package jerco.network.io;

import jerco.TestUtils;
import jerco.network.Net;
import jerco.network.NetImpl;
import jerco.network.NetStructureInfo;
import jerco.network.Node;
import jerco.network.RegularLattice;
import jerco.network.generators.RectGenerator;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;

public class TestFreemakerWriter {

    private static final String TEMPLATE_NAME = TestUtils.TEST_FILES_FOLDER
            + "/graphviz.ftl";

    @BeforeClass
    public static void createTempDir() throws IOException {
      if (!Files.isDirectory(TestUtils.TEST_TEMP_PATH)) {
          Files.createDirectories(TestUtils.TEST_TEMP_PATH);
      }
    }


    @Test
    public void test2nodes() throws Exception {
        BasicNetGenerator netGenerator = new BasicNetGenerator();
        final Node a = new Node(1);
        final Node b = new Node(2);
        Node.linkNodes(a, b);
        netGenerator.add(a);
        netGenerator.add(b);

        Net net = new NetImpl(netGenerator);

        FreemarkerWriter writer = new FreemarkerWriter(net);
        writer.loadTemplate(TEMPLATE_NAME);
        writer.write(TestUtils.TEST_TEMP_DIR + "/nodes2.dot");
    }

    @Test
    public void testStar() throws Exception {
        BasicNetGenerator netGenerator = new BasicNetGenerator();
        Node[] nodes = new Node[6];
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = new Node(i + 1);
        }

        for (int i = 0; i < nodes.length - 1; i++) {
            Node.linkNodes(nodes[i], nodes[nodes.length - 1]);
        }

        // Связываем 3 и 4 узлы.
        Node.linkNodes(nodes[2], nodes[3]);

        nodes[0].setBound(0);
        nodes[2].setBound(1);

        netGenerator.add(nodes);

        Net net = new NetImpl(netGenerator);

        FreemarkerWriter writer = new FreemarkerWriter(net);
        writer.loadTemplate(TEMPLATE_NAME);
        writer.write(TestUtils.TEST_TEMP_DIR + "/star.dot");
    }

    @Test
    public void testRectangle() throws Exception {
        NetStructureInfo structureInfo = new NetStructureInfo(10, 10,
                new RectGenerator());
        Net net = new RegularLattice(structureInfo);

        FreemarkerWriter writer = new FreemarkerWriter(net);
        writer.loadTemplate(TEMPLATE_NAME);
        writer.write(TestUtils.TEST_TEMP_DIR + "/rect.dot");
    }
}
