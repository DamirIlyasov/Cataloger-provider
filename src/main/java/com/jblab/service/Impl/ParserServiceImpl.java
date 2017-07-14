package com.jblab.service.Impl;

import com.jblab.model.Product;
import com.jblab.service.ParseService;
import com.jblab.util.AliexpressParser;
import com.jblab.util.PleerParser;
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
    private final AliexpressParser aliexpressParser = new AliexpressParser();
    private final PleerParser pleerParser = new PleerParser();
    public List<Product> parse(String path, String fileName) throws IOException, SAXException, ParserConfigurationException {
        logger.info("Looking for parser for file with name "+ fileName +"...");
        if (fileName.equals("aliexpress.xml")){
            return aliexpressParser.parse(path);
        }
        if (fileName.equals("pleer.xml")){
            return pleerParser.parse(path);
        }
        logger.warn("No parser found!");
        return null;
    }
}
