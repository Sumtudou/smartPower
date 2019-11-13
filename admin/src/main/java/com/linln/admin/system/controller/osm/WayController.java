package com.linln.admin.system.controller.osm;
import com.linln.admin.system.domain.Road;
import com.linln.admin.system.domain.Tag;
import com.linln.admin.system.domain.Way;
import com.linln.admin.system.mapper.OsmMapper;
import com.linln.admin.system.service.RoadService;
import com.linln.admin.system.service.TagService;
import com.linln.admin.system.service.WayService;
import com.linln.admin.system.tools.XmlReaderHandler;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

/**
 * @author sumtdou
 * @date 2019/11/02
 */
@Controller
@RequestMapping("/system/osmindex")
public class WayController {

    @Autowired
    private WayService wayService;
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
    public String index(Model model, Way way) {
        return "/system/osmindex/index";
    }

    @PostMapping("/savesql")
    @RequiresPermissions("system:osmindex:savesql")
    @ResponseBody
    public String SaveSql() throws IOException, SAXException, ParserConfigurationException {
        XmlReaderHandler.setAll();
        List<Way> ways = XmlReaderHandler.getWays();
        List<Road> roads =XmlReaderHandler.getRoads();
        List<Tag>tags  = XmlReaderHandler.getTags();

        osmMapper.truncateTable("osm_way");
        osmMapper.truncateTable("osm_road");
        osmMapper.truncateTable("osm_tag");

        for (Way way1 : ways) {
            if (way1 != null){
                wayService.save(way1);
            }
        }
        for (Road road : roads) {
            if (road != null){
                roadService.save(road);
            }
        }
        for(Tag tag:tags){
            tagService.save(tag);
        }
        //System.out.println("哈哈哈");
        return "success";
    }
}