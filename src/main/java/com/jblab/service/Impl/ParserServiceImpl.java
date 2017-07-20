package com.jblab.service.Impl;

import com.jblab.model.Product;
import com.jblab.service.ParseService;
import com.jblab.util.Transliter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@PropertySource(value = "classpath:parser.properties")
public class ParserServiceImpl implements ParseService {
    private final Transliter transliter;
    private final Environment environment;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ParserServiceImpl(Transliter transliter, Environment environment) {
        this.transliter = transliter;
        this.environment = environment;
    }

    public List<Product> parse(String path, String fileName) throws IOException, SAXException, ParserConfigurationException, NullPointerException {
        logger.info("------------------------------------------");
        logger.info("Parser: " + fileName + "...");
        File inputXml = new File(path);
        logger.info("Working on file: " + inputXml.getAbsolutePath());
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputXml);
        doc.getDocumentElement().normalize();

        logger.info("Reading parser.properties...");
        String categoryTag = environment.getProperty(fileName + ".category");
        String offerTag = environment.getProperty(fileName + ".offer");
        String offerCategoryIdTag = environment.getProperty(fileName + ".categoryId");
        String offerNameTag = environment.getProperty(fileName + ".name");
        String offerPriceTag = environment.getProperty(fileName + ".price");
        String referalUrlTag = environment.getProperty(fileName + ".url");
        String pictureUrlTag = environment.getProperty(fileName + ".picture");
        String currencyTag = environment.getProperty(fileName + ".currency");
        String descriptionTag = environment.getProperty(fileName + ".description");
        String shopNameTag = environment.getProperty(fileName + ".shopName");
        logger.info("Reading parser.properties completed success!");

        logger.info("Searching for cathegories");
        Map<String, String> categories = new HashMap<>();

        NodeList nodeListCategories = doc.getElementsByTagName(categoryTag);
        logger.info(nodeListCategories.getLength() + " categories found in XML.");
        for (int i = 0; i < nodeListCategories.getLength(); i++) {
            Node node = nodeListCategories.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String cathegoryName = element.getTextContent().replaceAll("/", "-");
                String cathegoryIdName = element.getAttribute("id");
                categories.put(cathegoryIdName, cathegoryName);
            }
        }
        logger.info("Parsing categories completed!");
        logger.info("Parsing products started!");
        List<Product> products = new ArrayList<>();
        String shopName = doc.getElementsByTagName(shopNameTag).item(0).getTextContent();
        NodeList nodeListProducts = doc.getElementsByTagName(offerTag);
        logger.info(nodeListProducts.getLength() + " products found in XML.");

        for (int i = 0; i < nodeListProducts.getLength(); i++) {
            Node node = nodeListProducts.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String category = categories.get(element.getElementsByTagName(offerCategoryIdTag).item(0).getTextContent());
                String name = element.getElementsByTagName(offerNameTag).item(0).getTextContent();
                String price = element.getElementsByTagName(offerPriceTag).item(0).getTextContent();
                String url = element.getElementsByTagName(referalUrlTag).item(0).getTextContent();
                String currency = element.getElementsByTagName(currencyTag).item(0).getTextContent();
                String description;
                try {
                    description = element.getElementsByTagName(descriptionTag).item(0).getTextContent();
                } catch (NullPointerException e) {
                    description = name;
                }
                String readableName = transliter.toTranslit(name);
                String readableCategory = transliter.toTranslit(category);

                Product product = new Product();
                product.setReadableName(readableName);
                product.setReadableCategory(readableCategory);
                product.setCategory(category);
                product.setName(name);
                product.setPrice(price);
                product.setUrl(url);
                try {
                    List<String> pictures = new ArrayList<>();
                    NodeList picsNodeList = element.getElementsByTagName(pictureUrlTag);
                    product.setMainImgUrl(picsNodeList.item(0).getTextContent());
                    for (int j = 0; j < picsNodeList.getLength(); j++) {
                        pictures.add(picsNodeList.item(j).getTextContent());
                    }
                    product.setImgUrls(pictures);
                } catch (NullPointerException e) {
                    product.setImgUrls(null);
                }
                product.setDescription(description);
                product.setCurrency(currency);
                products.add(product);
            }
        }
        logger.info("Parsing products completed!");
        if (products.size() == 0) {
            logger.warn("Something went wrong, no products parsed!");
        }

        return products;
    }
}
