package com.linln.admin.system.controller.osm;
import com.linln.admin.system.domain.*;
import com.linln.admin.system.mapper.OsmMapper;
import com.linln.admin.system.service.RoadService;
import com.linln.admin.system.service.TagService;
import com.linln.admin.system.service.NodeService;
import com.linln.admin.system.tools.CalculateRes;
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

import static java.util.Collections.sort;

/**
 * @author sumtdou
 * @date 2019/11/02
 */
@Controller
@RequestMapping("/system/osmindex")
public class OsmController {

    @Autowired
    private NodeService nodeService;
    @Autowired
    private RoadService roadService;
    @Autowired
    private TagService tagService;

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
    public String detail(Model model , @RequestParam("MinSupport") Double MinSupport,
                         @RequestParam("MinConfidence") Double MinConfidence) {

        System.out.println(MinSupport+"  ggg  "+MinConfidence);

        List<TagSon> tagsons = CalculateRes.solve();
        sort(tagsons,new SortBySupport());
        List<TagSon>  ggg = new ArrayList<>();

        int sum = 0;
        for(TagSon item:tagsons){
            if(item.getSupport()>MinSupport && item.getConfidence()>MinConfidence){
                ggg.add(item);
                sum++;
            }
            if(sum>14) break;
        }
        model.addAttribute("tagsons",ggg);
        return "/system/osmindex/detail";
    }

    @PostMapping("/savesql")
    @RequiresPermissions("system:osmindex:savesql")
    @ResponseBody
    public String SaveSql() throws IOException, SAXException, ParserConfigurationException {
        XmlReaderHandler.setAll();
        List<Node> nodes = XmlReaderHandler.getNodes();
        List<Road> roads =XmlReaderHandler.getRoads();
        List<Tag>tags  = XmlReaderHandler.getTags();

        osmMapper.truncateTable("osm_node");
        osmMapper.truncateTable("osm_road");
        osmMapper.truncateTable("osm_tag");

//        String str = "任意字符串";
//        str = new String(str.getBytes("gbk"),"utf-8");

//        for(Tag tag:tags){
//            tag.setTagvalue(new String(tag.getTagvalue().getBytes("gbk"),"utf-8"));
//            tag.setTagkey(new String(tag.getTagkey().getBytes("gbk"),"utf-8"));
//        }

        for(Tag tag:tags){
            tagService.save(tag);
        }
        System.out.println("Tag save over");
        for (Node node1 : nodes) {   //node
            if (node1 != null){
                nodeService.save(node1);
            }
        }
        for (Road road : roads) {  //way
            if (road != null){
                roadService.save(road);
            }
        }

        return "success";
    }
}
class SortBySupport implements Comparator {
    public int compare(Object o1, Object o2) {
        TagSon ts1 = (TagSon) o1;
        TagSon ts2 = (TagSon) o2;
        Integer times1 = ts1.getTimes();
        Integer times2 = ts2.getTimes();
        return times1.compareTo(times2)*(-1);
    }
}

class SortByConfidence implements Comparator {
    public int compare(Object o1, Object o2) {
        TagSon ts1 = (TagSon) o1;
        TagSon ts2 = (TagSon) o2;
        Double times1 = ts1.getConfidence();
        Double times2 = ts2.getConfidence();
        return times1.compareTo(times2)*(-1);
    }
}