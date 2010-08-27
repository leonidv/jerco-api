package jerco.network.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import jerco.network.Node;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Support import graphs from <a
 * href="http://graphml.graphdrawing.org/">graphml</a> format. Only small subset
 * of full format supported.
 * 
 * @author Leonid Vygovskiy
 * 
 */
public class GraphMLReader implements NetReader {
    private static final Logger logger = LoggerFactory
            .getLogger(GraphMLReader.class);

    private static XMLInputFactory xmlFactory = XMLInputFactory.newInstance();
    private XMLEventReader reader;

    private Map<String, Node> nodesByLabel;

    private Set<Link> links;

    public GraphMLReader(String fileName) throws IOException,
            XMLStreamException {
        this(new File(fileName));
    }

    /**
     * Prepare reader. Throws {@link IOException} if file can't be read.
     * 
     * @param fileName
     * @throws IOException
     * @throws XMLStreamException
     */
    public GraphMLReader(File file) throws IOException, XMLStreamException {

        if (!file.isFile()) {
            throw new IOException("File not found " + file.getAbsolutePath());
        }

        if (!file.canRead()) {
            throw new IOException("Can't read file " + file.getAbsolutePath());
        }

        init(new FileInputStream(file));

    }

    public GraphMLReader(InputStream input) throws XMLStreamException {
        init(input);
    }

    private void init(InputStream input) throws XMLStreamException {
        reader = xmlFactory.createXMLEventReader(input);
    }

    @Override
    public Set<Node> read() throws JercoReaderException {
        nodesByLabel = new HashMap<String, Node>();
        links = new HashSet<Link>();
        
        // init nodesByLabel and links. 
        parseXML();

        return makeNet(nodesByLabel, links);
    }

    private Set<Node> makeNet(Map<String, Node> nodesByLabel, Set<Link> links) throws JercoReaderException {
        Set<Node> nodes = new HashSet<Node>();

        for (Link link : links) {
            Node a = getNodeByLabel(link.getA());
            Node b = getNodeByLabel(link.getB());
            
            Node.linkNodes(a, b);
            nodes.add(a);
            nodes.add(b);
        }

        return nodes;
    }

    /**
     * This is a small trap. Map {@link #nodesByLabel} and {@link #links}
     * contains nodes that has same label, but different objects instations.
     * Main object contains in {@link #nodesByLabel}, so this method get him.
     * <p>
     * This is behaviour required because graphml don't require declare node
     * before reference to it in edges.
     * 
     * @param node
     * @throws JercoReaderException
     */
    private Node getNodeByLabel(Node node) throws JercoReaderException {
        Node n = nodesByLabel.get(node.getLabel());
        if (n == null) {
            throw new JercoReaderException("edge contains undefined node "
                    + node.getLabel());
        }
        return n;
    }

    /**
     * Parse xml with {@link #reader} and fill {@link #nodes},
     * {@link #nodesByLabel}, {@link #links}
     * 
     * @throws JercoReaderException
     */
    private void parseXML() throws JercoReaderException {
        try {
            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();
                if (event.isStartElement()) {
                    StartElement tag = event.asStartElement();
                    String name = tag.getName().getLocalPart();

                    if ("node".equalsIgnoreCase(name)) {
                        try {
                            final Node node = readNode(tag, reader);
                            nodesByLabel.put(node.getLabel(), node);
                        } catch (Exception e) {
                            String msg = "Can't read node from tag " + tag;
                            logger.error(msg, e);
                            throw new JercoReaderException(msg, e);
                        }

                    } else if ("edge".equalsIgnoreCase(name)) {
                        try {
                            links.add(readLink(tag));
                        } catch (Exception e) {
                            String msg = "Can't read link from tag " + tag;
                            logger.error(msg, e);
                            throw new JercoReaderException(msg, e);
                        }
                    }
                }
            }

        } catch (XMLStreamException e) {
            logger.error("Can't parse xml", e);
        } finally {
            try {
                reader.close();
            } catch (XMLStreamException e) {
                logger.error("Can't close XMLEventReader");
            }
        }
    }

    
    private Node readNode(StartElement tag, XMLEventReader reader) throws XMLStreamException,
            JercoReaderException {
        String id = readAttribute(tag, "id");
        Node node = new Node(id);
        
        XMLEvent xmlEvent = reader.peek();
        
        if (xmlEvent.isCharacters()) {
            reader.next();
            xmlEvent = reader.peek();
        }
        
        if (!xmlEvent.isStartElement()) {
            return node;
        }
        
        StartElement data = xmlEvent.asStartElement();
        String name = data.getName().getLocalPart();
        if (!"data".equalsIgnoreCase(name)) {
            return node;
        }
        
        String key = readAttribute(data, "key");
        if (!"bound".equalsIgnoreCase(key)) {
            return node;
        }
        // This our xmlEvent. Move reader to it.
        reader.next();
        
        int bound = Integer.valueOf(reader.getElementText());
        node.setBound(bound);
        
        // we did peek, so skip element
        reader.next();
        
        return node;
    }

    private String readAttribute(StartElement tag, final String attributeName)
            throws JercoReaderException {
        Attribute attribute = tag.getAttributeByName(new QName(attributeName));
        if (attribute == null) {
            throw new JercoReaderException("can't get required attribute "
                    + attributeName);
        }
        
        String id = attribute.getValue();
        if (id == null || id.isEmpty()) {
            throw new JercoReaderException("required attribute value is empty "
                    + attributeName);
        }
        return id;
    }

    private Link readLink(StartElement tag) throws XMLStreamException,
            JercoReaderException {

        String aId = readAttribute(tag, "source");
        String bId = readAttribute(tag, "target");
        
        Node a = giveNodeForLink(aId);
        Node b = giveNodeForLink(bId);

        return new Link(a, b);
    }

    /**
     * When edge is read, it can reference to already defined node or not. So,
     * if node is defined it's loaded from {@link #nodesByLabel} otherwise
     * returned new node.
     * <p>
     * This is allow to save memory on huge graphs.
     * 
     * @param label
     * @return
     */
    private Node giveNodeForLink(String label) {
        if (nodesByLabel.containsKey(label)) {
            return nodesByLabel.get(label);
        } else {
            return new Node(label);
        }
    }
}
