package com.linln.admin.system.tools;

import com.linln.admin.system.domain.Road;
import com.linln.admin.system.domain.Way;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

//https://blog.csdn.net/lp284558195/article/details/79136322  sax
//https://blog.csdn.net/scy411082514/article/details/7484497  osm数据结构
public class XmlParseHandler extends DefaultHandler {

    private List<Way> ways;  //node的集合……前期打错了字，就一错到此了
    private List<Road>roads;  // way的集合
    private String currentTag; // 记录当前解析到的节点名称
    private Way way; // 记录当前的node
    private Road road;   //记录当前的way
    //记录node下tag的k，v
    private String keys = "";
    private String values = "";
    //记录way下tag的k，v以及nd
    private String Rkey = "";
    private String Rvalue ="";
    private String Rnd ="";

    /**
     * 文档解析开始调用
     */
    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        System.out.println("----startDocument----");
        ways = new ArrayList<Way>();
        roads = new ArrayList<Road>();
    }

    /**
     * 文档解析结束后调用
     */
    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
        System.out.println("----endDocument----");
    }

    /**
     * 节点解析结束后调用
     *
     * @param uri       : 命名空间的uri
     * @param localName : 标签的名称
     * @param qName     : 带命名空间的标签名称
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        //System.out.println(qName + "-endElement()");
        if ("node".equals(qName)) {
            if (way != null) {
                ways.add(way);
            }
            keys = "";
            values ="";
            way = null;
            currentTag = null;
        }
        if ("way".equals(qName)) {
            if (road != null) {
                //System.out.println(road.toString());
                roads.add(road);
            }
            Rkey = "";
            Rvalue ="";
            Rnd = "";
            road = null;
            currentTag = null;
        }
    }


    /**
     * 节点解析开始调用
     *
     * @param uri       : 命名空间的uri
     * @param localName : 标签的名称
     * @param qName     : 带命名空间的标签名称
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        super.startElement(uri, localName, qName, attributes);
        //System.out.println(qName + "-StartElement()");
        //System.out.println("uri:" + uri + "  localName:" + localName + "  qName:" + qName + "  Attributes:" + attributes.toString());
        if ("node".equals(qName)) { // 一个node节点
            way = new Way();
            for (int i = 0; i < attributes.getLength(); i++) {
                /**********************/
                /*int length = attributes.getQName(i).length();
                System.out.print("attribute_name：" + attributes.getQName(i));
                for (int j = 0; j < 20 - length; j++) System.out.print(" ");
                 System.out.println("attribute_value：" + attributes.getValue(i));*/
                /************************/
                String name = attributes.getQName(i);
                String value = attributes.getValue(i);

                switch (name) {
                    case "id":
                        if (!value.equals("")) way.setNid(value);
                        break;
                    case "visible":
                        if (!value.equals("")) way.setVisible(value);
                        break;
                    case "version":
                        if (!value.equals("")) way.setVersion(Integer.valueOf(value));
                        break;
                    case "changeset":
                        if (!value.equals("")) way.setChangeset(value);
                        break;
                    case "timestamp":
                        if (!value.equals("")) {
                            try {
                                way.setTimestamp(TimeUtils.StampToDate(value));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case "user":
                        if (!value.equals("")) way.setUser(value);
                        break;
                    case "uid":
                        if (!value.equals("")) way.setUid(Integer.valueOf(value));
                        break;
                    case "lat":
                        if (!value.equals("")) way.setLat(value);
                        break;
                    case "lon":
                        if (!value.equals("")) way.setLon(value);
                        break;
                    default:
                        break;
                }
            }
          //  System.out.println("node-id = " + way.getNid());
            currentTag = "node"; // 把当前标签记录下来
        }
        if ("node".equals(currentTag) && "tag".equals(qName)) {
            keys = keys + attributes.getValue("k") + ';';
            values = values + attributes.getValue("v") + ';';
            way.setTagkey(keys);
            way.setTagvalue(values);
           // System.out.println("key = " + keys + "   value = " + values);
        }
        if ("way".equals(qName)) { // 一条road
            road = new Road();
            for (int i = 0; i < attributes.getLength(); i++) {
                String name = attributes.getQName(i);
                String value = attributes.getValue(i);
                switch (name) {
                    case "id":
                        if (!value.equals("")) road.setNid(value);
                        break;
                    case "visible":
                        if (!value.equals("")) road.setVisible(value);
                        break;
                    case "version":
                        if (!value.equals("")) road.setVersion(Integer.valueOf(value));
                        break;
                    case "changeset":
                        if (!value.equals("")) road.setChangeset(value);
                        break;
                    case "timestamp":
                        if (!value.equals("")) {
                            try {
                                road.setTimestamp(TimeUtils.StampToDate(value));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case "user":
                        if (!value.equals("")) road.setUser(value);
                        break;
                    case "uid":
                        if (!value.equals("")) road.setUid(Integer.valueOf(value));
                        break;
                    default:
                        break;
                }
            }
            //System.out.println("road-id = " + road.getNid());
            currentTag = "way"; // 把当前标签记录下来
        }
        //解析《way》下的tag
        if ("way".equals(currentTag) && "tag".equals(qName)) {
            Rkey = Rkey + attributes.getValue("k") + ';';
            Rvalue = Rvalue + attributes.getValue("v") + ';';
            road.setTagkey(Rkey);
            road.setTagvalue(Rvalue);
            //System.out.println("Rkey = " + Rkey + "   Rvalue = " + Rvalue);
        }
        //解析《way》下的nd
        if ("way".equals(currentTag) && "nd".equals(qName)) {
            Rnd = Rnd + attributes.getValue("ref") + ';';
            road.setNd(Rnd);
        }
    }
    /**
     *@Description:  作用是解析闭合标签，如<name>Sumtudou<name/>,得到里面的Sumtudou
     * @author Sumtudou
     * @date 2019/11/5
    */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
    }
    public List<Way> getWays() {
        return ways;
    }
    public List<Road>getRoads(){
        return roads;
    }
}
