package com.jblab.util;

import com.jblab.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by damir on 14.07.17.
 */
public class PleerParser {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public List<Product> parse(String path) throws ParserConfigurationException, IOException, SAXException {
        logger.info("-------------------------------------");
        logger.info("Pleer parser started...");
        File inputXml = new File(path);
        logger.info("Working on file: " + inputXml.getAbsolutePath());
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputXml);
        doc.getDocumentElement().normalize();

        logger.info("Searching for cathegories");
        Map<String, String> categories = new HashMap<>();


        NodeList nodeListCategories = doc.getElementsByTagName("category");
        logger.info(nodeListCategories.getLength() + " categories found in XML.");
        for (int i = 0; i < nodeListCategories.getLength(); i++) {
            Node node = nodeListCategories.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                categories.put(element.getAttribute("id"), element.getTextContent());
            }
        }
        logger.info("Parsing categories completed!");
        logger.info("Parsing products started!");
        List<Product> products = new ArrayList<>();
        NodeList nodeListProducts = doc.getElementsByTagName("offer");
        logger.info(nodeListProducts.getLength() + " products found in XML.");
        for (int i = 0; i < nodeListProducts.getLength(); i++) {
            Node node = nodeListProducts.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                Product product = new Product();
                product.setSerial(element.getAttribute("id"));
                product.setCategory(categories.get(element.getElementsByTagName("categoryId").item(0).getTextContent()));
                try {
                    product.setName(element.getElementsByTagName("model").item(0).getTextContent());
                } catch (NullPointerException e) {
                    product.setName(element.getElementsByTagName("name").item(0).getTextContent());
                }
                product.setCost(element.getElementsByTagName("price").item(0).getTextContent());
                product.setUrl(element.getElementsByTagName("url").item(0).getTextContent());
                try {
                    product.setImgUrl(element.getElementsByTagName("picture").item(0).getTextContent());
                }catch (NullPointerException e){
                    product.setImgUrl("");
                }
                product.setTitle(element.getElementsByTagName("name").item(0).getTextContent());
                product.setCurrency(element.getElementsByTagName("currencyId").item(0).getTextContent());
                products.add(product);
            }
        }
        logger.info("Parsing products completed!");
        if (products.size() == 0) {
            logger.warn("Something went wrong, no products parsed!");
        }
        logger.info("Pleer parser: parsing completed.");
        return products;
    }
}
