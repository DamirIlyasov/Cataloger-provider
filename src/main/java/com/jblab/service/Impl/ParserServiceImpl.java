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
public class ParserServiceImpl implements ParseService {
    private final Transliter transliter;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ParserServiceImpl(Transliter transliter) {
        this.transliter = transliter;
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

        logger.info("Searching for categories");
        Map<String, String> categories = new HashMap<>();

        NodeList nodeListCategories = doc.getElementsByTagName("category");
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
        String shopName = doc.getElementsByTagName("name").item(0).getTextContent();
        NodeList nodeListProducts = doc.getElementsByTagName("offer");
        logger.info(nodeListProducts.getLength() + " products found in XML.");

        for (int i = 0; i < nodeListProducts.getLength(); i++) {
            Node node = nodeListProducts.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String category = categories.get(element.getElementsByTagName("categoryId").item(0).getTextContent());
                String name = element.getElementsByTagName("name").item(0).getTextContent();
                name = name.replaceAll("&QUOT;"," ");
                String price = element.getElementsByTagName("price").item(0).getTextContent();
                String url = element.getElementsByTagName("url").item(0).getTextContent();
                String currency = element.getElementsByTagName("currencyId").item(0).getTextContent();
                String description;
                try {
                    description = element.getElementsByTagName("description").item(0).getTextContent();
                } catch (NullPointerException e) {
                    description = name;
                }
                Map<String, String> params = new HashMap<>();
                try {
                    NodeList paramsNodeList = element.getElementsByTagName("param");
                    for (int j = 0; j < paramsNodeList.getLength(); j++) {
                        Node paramNode = paramsNodeList.item(j);
                        Element paramElement = (Element) paramNode;
                        String paramName = paramElement.getAttribute("name");
                        String paramValue = paramElement.getTextContent();
                        params.put(paramName, paramValue);
                    }
                } catch (NullPointerException e) {
                    params = null;
                }
                System.out.println(params.size());
                String readableName = transliter.toTranslit(name);
                String readableCategory = transliter.toTranslit(category);
                List<String> imgUrls = new ArrayList<>();
                String mainImgUrl = null;
                try {
                    NodeList picsNodeList = element.getElementsByTagName("picture");
                    mainImgUrl = picsNodeList.item(0).getTextContent();
                    for (int j = 0; j < picsNodeList.getLength(); j++) {
                        imgUrls.add(picsNodeList.item(j).getTextContent());
                    }
                } catch (NullPointerException e) {
                    imgUrls = null;
                }

                Product product = new Product();
                product.setParams(params);
                product.setMainImgUrl(mainImgUrl);
                product.setImgUrls(imgUrls);
                product.setReadableName(readableName);
                product.setReadableCategory(readableCategory);
                product.setCategory(category);
                product.setName(name);
                product.setPrice(price);
                product.setUrl(url);
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
