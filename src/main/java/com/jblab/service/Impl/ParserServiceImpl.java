package com.jblab.service.Impl;

import com.jblab.model.Product;
import com.jblab.service.ParseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by damir on 06.07.17.
 */
@Service
public class ParserServiceImpl implements ParseService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public List<Product> parse(String path) throws IOException, SAXException, ParserConfigurationException {
        logger.info("-------------------------------------");
        logger.info("Parser: started...");
        File inputXml = new File(path);
        logger.info("Parser: working on file: " + inputXml.getAbsolutePath());
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputXml);
        doc.getDocumentElement().normalize();
        List<Product> products = new ArrayList<>();
        NodeList nodeList = doc.getElementsByTagName("offer");
        logger.info("Parser: " + nodeList.getLength() + " products found in XML.");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                Product product = new Product();
                product.setSerial(element.getAttribute("id"));
                product.setName(element.getElementsByTagName("name").item(0).getTextContent());
                product.setCost(element.getElementsByTagName("price").item(0).getTextContent() );
                product.setUrl(element.getElementsByTagName("url").item(0).getTextContent());
                product.setImgUrl(element.getElementsByTagName("image").item(0).getTextContent());
                product.setTitle(element.getElementsByTagName("title").item(0).getTextContent());
                product.setCurrency(element.getElementsByTagName("currencyId").item(0).getTextContent());
                products.add(product);

            }
        }
        if (products.size() == 0) {
            logger.warn("Parser: Something went wrong, no products parsed!");
        }
        logger.info("Parser: Parsing completed.");
        return products;
    }
}
