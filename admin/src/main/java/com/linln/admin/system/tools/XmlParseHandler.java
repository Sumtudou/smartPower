package com.linln.admin.system.tools;

import com.linln.admin.system.domain.Road;
import com.linln.admin.system.domain.Tag;
import com.linln.admin.system.domain.Node;
import com.linln.admin.system.domain.Relation;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

//https://blog.csdn.net/lp284558195/article/details/79136322  sax
//https://blog.csdn.net/scy411082514/article/details/7484497  osm数据结构
public class XmlParseHandler extends DefaultHandler {

    private List<Node> nodes;  //node的集合
    private List<Road> roads;  // way的集合
    private List<Relation> relations; //relation的集合
    private String currentTag; // 记录当前解析到的节点名称
    private Node node; // 记录当前的node
    private Road road;   //记录当前的way
    private Relation relation;   //记录当前的relation

    //记录node下tag的k，v
    private String keys = "";
    private String values = "";
    //记录way下tag的k，v以及nd
    private String Rkey = "";
    private String Rvalue = "";
    private String Rnd = "";

    //记录relation下tag的k，v
    private String ReKey = "";
    private String ReValue = "";

    //单独的tag表内数据
    private List<Tag> tags;
    private Tag tag;
    private String fatherId = "";

/*文档解析开始调用*/
@Override
public void startDocument() throws SAXException {
    super.startDocument();
    System.out.println("----startDocument----");
    nodes = new ArrayList<Node>();
    roads = new ArrayList<Road>();
    tags = new ArrayList<Tag>();
    relations = new ArrayList<Relation>();
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
            if (node != null) {
                nodes.add(node);
            }
            keys = "";
            values = "";
            node = null;
            currentTag = null;
        }
        if ("way".equals(qName)) {
            if (road != null) {
                //System.out.println(road.toString());
                roads.add(road);
            }
            Rkey = "";
            Rvalue = "";
            Rnd = "";
            road = null;
            currentTag = null;
        }
        if ("relation".equals(qName)) {
            if (relation != null) {
                relations.add(relation);
            }

            ReKey = null;
            ReValue = null;
            relation = null;
            fatherId = "";
            currentTag = null;
        }
        if ("tag".equals(qName)) {
            if (tag != null) {
                //System.out.println(road.toString());
                tags.add(tag);
            }
            tag = null;
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
    // 解析node节点
    if ("node".equals(qName)) {
        node = new Node();
        for (int i = 0; i < attributes.getLength(); i++) {
            String name = attributes.getQName(i);
            String value = attributes.getValue(i);
            switch (name) {
                case "id":
                    if (!value.equals("")) node.setNid(value);
                    break;
                case "visible":
                    if (!value.equals("")) node.setVisible(value);
                    break;
                /*省略其他case*/
                default:
                    break;
            }
        }
        currentTag = "node"; // 把当前标签记录下来
    }
    //省略其他标签的解析
}

    /**
     * @Description: 作用是解析闭合标签，如<name>Sumtudou</name>,得到里面的Sumtudou
     * @author Sumtudou
     * @date 2019/11/5
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
    }

    protected List<Node> getWays() {
        return nodes;
    }

    protected List<Road> getRoads() {
        return roads;
    }

    protected List<Relation> getRelations() {
        return relations;
    }

    protected List<Tag> getTags() {
        return tags;
    }

}
