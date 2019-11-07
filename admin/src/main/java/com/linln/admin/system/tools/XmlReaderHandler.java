package com.linln.admin.system.tools;

import com.linln.admin.system.domain.Road;
import com.linln.admin.system.domain.Way;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class XmlReaderHandler {

    private static List<Way>ways;
    private static List<Road>roads;
    /**
     *@Description:   XML读入并解析，只有解析之后才能获取到必要的List
     * @author Sumtudou
     * @date 2019/11/5
    */
    public static  void setAll() throws ParserConfigurationException, SAXException, IOException {
        // 加载文件返回文件的输入流
        File f = new File("C://Users//11630//Desktop//XTU.xml");    // 声明File对象
        InputStream input = new FileInputStream(f);

        XmlParseHandler handler = new XmlParseHandler();
        // 1. 得到SAX解析工厂
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        // 2. 让工厂生产一个sax解析器
        SAXParser newSAXParser = saxParserFactory.newSAXParser();
        // 3. 传入输入流和handler，解析
        newSAXParser.parse(input, handler);
        input.close();
        ways = handler.getWays();
        roads = handler.getRoads();
    }
    public static List<Way>getWays(){
        return ways;
    }
    public static List<Road>getRoads(){
        return roads;
    }
}