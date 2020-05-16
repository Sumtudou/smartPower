package com.linln.admin.system.controller.osm;

import com.linln.admin.system.domain.*;
import com.linln.admin.system.mapper.OsmMapper;
import com.linln.admin.system.repository.RuleRepository;
import com.linln.admin.system.service.RelationService;
import com.linln.admin.system.service.RoadService;
import com.linln.admin.system.service.TagService;
import com.linln.admin.system.service.NodeService;
import com.linln.admin.system.tools.CalculateRes;
import com.linln.admin.system.tools.ToolsUtils;
import com.linln.admin.system.tools.XmlReaderHandler;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;

//import com.github.binarywang.java.emoji.EmojiConverter;

import static java.util.Collections.sort;

/**
 * @author sumtdou
 * @date 2019/11/02
 */
@Controller
@RequestMapping("/system/osmindex")
public class OsmController {

    //private EmojiConverter emojiConverter = EmojiConverter.getInstance();
    @Autowired
    private RuleRepository ruleRepository;
    @Autowired
    private NodeService nodeService;
    @Autowired
    private RoadService roadService;
    @Autowired
    private TagService tagService;
    @Autowired
    private RelationService relationService;
    @Autowired
    private OsmMapper osmMapper;

    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("system:osmindex:index")
    public String index(Model model, Node node) {
        return "/system/osmindex/index";
    }

    @GetMapping("/detail")
    @RequiresPermissions("system:osmindex:index")
    public String detail(Model model, @RequestParam("MinSupport") Double MinSupport,
                         @RequestParam("MinConfidence") Double MinConfidence) {

        System.out.println(MinSupport + "  ggg  " + MinConfidence);
        List<Rule> rules = ruleRepository.getRules(15, "TAGINSIDE");
        //System.out.println(rules);
        //System.out.println(rules.isEmpty());
        for (Rule item : rules) {
            System.out.println(item);
        }


        model.addAttribute("rules", rules);

//        ArrayList<String> types = new ArrayList<>();
//        types.add("KEY");
//        types.add("VALUE");
//        types.add("KV");
//        types.add("TAGINSIDE");
//
//        for (int i = 0; i < 20; i++) {
//            for (String type : types) {
//                List<Rule> the_rules = ruleRepository.getRules(15, "TAGINSIDE");
//                model.addAttribute("rules", the_rules);
//                //返回
//            }
//        }

        return "/system/osmindex/detail";
    }

    @GetMapping("/page")
    @RequiresPermissions("system:osmindex:index")
    public String page(Model model) {
        ArrayList<String> types = new ArrayList<>();
        types.add("KEY");
        types.add("VALUE");
        types.add("KV");
        types.add("TAGINSIDE");

        for (int i = 0; i < 20; i++) {
            for (String type : types) {
                List<Rule> the_rules = ruleRepository.getRules(15, "TAGINSIDE");
                model.addAttribute("rules", the_rules);
                //返回
            }
        }

        return "/system/osmindex/detail";
    }

    @PostMapping("/savesql")
    @RequiresPermissions("system:osmindex:savesql")
    @ResponseBody
    public String SaveSql() throws IOException, SAXException, ParserConfigurationException {
        XmlReaderHandler.setAll();
        List<Node> nodes = XmlReaderHandler.getNodes();
        List<Road> roads = XmlReaderHandler.getRoads();
        List<Tag> tags = XmlReaderHandler.getTags();
        List<Relation> relations = XmlReaderHandler.getRelations();
        osmMapper.truncateTable("osm_node");
        osmMapper.truncateTable("osm_road");
        osmMapper.truncateTable("osm_tag");
        osmMapper.truncateTable("osm_relation");


        CountDownLatch countDownLatch = new CountDownLatch(4);
        Thread thread = new Thread(new ThreadForSaveTag(countDownLatch, tags, tagService));
        thread.start();
        countDownLatch.countDown();

        Thread thread1 = new Thread(new ThreadForSaveNode(countDownLatch, nodes, nodeService));
        thread1.start();
        countDownLatch.countDown();

        Thread thread2 = new Thread(new ThreadForSaveRoad(countDownLatch, roads, roadService));
        thread2.start();
        countDownLatch.countDown();

        Thread thread3 = new Thread(new ThreadForSaveRelation(countDownLatch, relations, relationService));
        thread3.start();
        countDownLatch.countDown();

        return "success";
    }
}


class ThreadForSaveTag implements Runnable {

    private final CountDownLatch countDownLatch;
    private List<Tag> tags;
    private TagService tagService;

    public ThreadForSaveTag(CountDownLatch countDownLatch, List<Tag> tags, TagService tagService) {
        this.countDownLatch = countDownLatch;
        this.tags = tags;
        this.tagService = tagService;
    }

    @Override
    public void run() {
        try {
            for (Tag tag : tags) {
                if (ToolsUtils.containsEmoji(tag.getTagvalue())) {
                    tag.setTagvalue("emoji");
                }
            }
            for (Tag tag : tags) {
                tagService.save(tag);
            }
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class ThreadForSaveNode implements Runnable {

    private final CountDownLatch countDownLatch;
    private List<Node> nodes;
    private NodeService nodeService;

    public ThreadForSaveNode(CountDownLatch countDownLatch, List<Node> nodes, NodeService nodeService) {
        this.countDownLatch = countDownLatch;
        this.nodes = nodes;
        this.nodeService = nodeService;
    }

    @Override
    public void run() {
        try {
            for (Node node1 : nodes) {   //node
                if (ToolsUtils.containsEmoji(node1.getUser())) {
                    node1.setUser("emoji");
                }
                if (ToolsUtils.containsEmoji(node1.getTagvalue())) {
                    node1.setTagvalue("emoji");
                }
            }
            for (Node node1 : nodes) {   //node
                if (node1 != null) {
                    nodeService.save(node1);
                }
            }

            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


class ThreadForSaveRoad implements Runnable {

    private final CountDownLatch countDownLatch;
    private List<Road> roads;
    private RoadService roadService;

    public ThreadForSaveRoad(CountDownLatch countDownLatch, List<Road> roads, RoadService roadService) {
        this.countDownLatch = countDownLatch;
        this.roads = roads;
        this.roadService = roadService;
    }

    @Override
    public void run() {
        try {
            for (Road road : roads) {  //way
                if (ToolsUtils.containsEmoji(road.getUser())) {
                    road.setUser("emoji");
                }
                if (ToolsUtils.containsEmoji(road.getTagvalue())) {
                    road.setTagvalue("emoji");
                }
            }
            for (Road road : roads) {  //way
                if (road != null) {
                    roadService.save(road);
                }
            }
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class ThreadForSaveRelation implements Runnable {

    private final CountDownLatch countDownLatch;
    private List<Relation> relations;
    private RelationService relationService;

    public ThreadForSaveRelation(CountDownLatch countDownLatch, List<Relation> relations, RelationService relationService) {
        this.countDownLatch = countDownLatch;
        this.relations = relations;
        this.relationService = relationService;
    }

    @Override
    public void run() {
        try {
            for (Relation relation : relations) {  //way
                if (relation != null) {
                    relationService.save(relation);
                }
            }
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
