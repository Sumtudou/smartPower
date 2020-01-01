package com.linln.admin.system.tools;

import com.linln.admin.system.domain.Road;
import com.linln.admin.system.domain.Tag;
import com.linln.admin.system.domain.Node;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.util.List;

public class XmlReaderHandler {

    private static List<Node>nodes;
    private static List<Road>roads;
    private static List<Tag>tags;
    /**
     *@Description:   XML读入并解析，只有解析之后才能获取到必要的List
     * @author Sumtudou
     * @date 2019/11/5
    */
    public static  void setAll() throws ParserConfigurationException, SAXException, IOException {
        // 加载文件返回文件的输入流
        File f = new File("C://Users//11630//Desktop//map.xml");    // 声明File对象
        InputStream input = new FileInputStream(f);

        XmlParseHandler handler = new XmlParseHandler();
        // 1. 得到SAX解析工厂
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        // 2. 让工厂生产一个sax解析器
        SAXParser newSAXParser = saxParserFactory.newSAXParser();
        // 3. 传入输入流和handler，解析
        newSAXParser.parse(input, handler);
        input.close();
        nodes = handler.getWays();
        roads = handler.getRoads();
        tags  =handler.getTags();

    }
    public static List<Node>getNodes(){
        return nodes;
    }
    public static List<Road>getRoads(){
        return roads;
    }
    public static List<Tag>getTags(){
        return tags;
    }


  /*  public static InputStream changeToUTF8(InputStream input) throws IOException {

        StringBuffer content = new StringBuffer();
        DataInputStream in = new DataInputStream(input);
        BufferedReader d = new BufferedReader(new InputStreamReader(in, "UTF-8"));// , "UTF-8"
        String line = null;
        while ((line = d.readLine()) != null)
            content.append(line);
        d.close();
        in.close();
        input.close();

    }*/
}