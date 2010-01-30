package jerco.network.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jerco.network.Net;
import jerco.network.Node;

public class FreemakerWriter {
    private final Net net;

    private Template template;

    public FreemakerWriter(Net net) {
        this.net = net;
    }

    public void loadTemplate(String templateName) throws IOException {
        File templateFile = new File(templateName);
        File templateDir;
        if (templateFile.isFile()) {
            templateDir = templateFile.getParentFile();
        } else {
            templateDir = templateFile;
        }

        Configuration config = new Configuration();
        config.setDirectoryForTemplateLoading(templateDir);
        config.setObjectWrapper(new DefaultObjectWrapper());

        template = config.getTemplate(templateFile.getName());
    }

    public void write(String exportName)
            throws IOException, TemplateException {

        List<Node> nodes = new ArrayList<Node>();
        for (Node node : net) {
            nodes.add(node);
        }

        Map<String, List<Node>> root = new HashMap<String, List<Node>>();
        root.put("nodes", nodes);
        
        Writer writer = new BufferedWriter(new FileWriter(exportName));
        template.process(root, writer);
    }
}
