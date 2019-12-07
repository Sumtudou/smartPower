package com.linln.admin.system.tools;

import com.linln.admin.system.domain.Tag;
import com.linln.admin.system.domain.TagSon;
import com.linln.admin.system.service.TagService;
import org.omg.CORBA.INTERNAL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

import static java.util.Collections.*;

//https://www.cnblogs.com/BobXie85/p/8745705.html
@Component
public class CalculateRes {

    @Autowired
    private TagService tagService;
    @Autowired
    private static TagService staticTagService;

    @PostConstruct
    public void init() {
        staticTagService = tagService;
    }

    public static List<TagSon> solve() {
        List<TagSon> allsons = new ArrayList<>();
        ArrayList<Tag> allTags = staticTagService.getAllTags();
        int allTagsSize = allTags.size();

        Map<String, Integer> tagcount = new HashMap<>();
        boolean isFirst = true;
        for (Tag tag : allTags) {
            TagSon item = new TagSon();
            if (isFirst) {
                item.setKey(tag.getTagkey());
                item.setValue(tag.getTagvalue());
                item.setTimes(item.getTimes() + 1);
                allsons.add(item);

                tagcount.put(tag.getTagkey(), 1);

                isFirst = false;
            } else {
                TagSon isLast = allsons.get(allsons.size() - 1);
                //和上一个相同
                if (isLast.getKey().equals(tag.getTagkey()) && isLast.getValue().equals(tag.getTagvalue())) {
                    allsons.get(allsons.size() - 1).setTimes(isLast.getTimes() + 1);
                } else {
                    //和上一个不同
                    item.setKey(tag.getTagkey());
                    item.setValue(tag.getTagvalue());
                    item.setTimes(item.getTimes() + 1);
                    allsons.add(item);
                }
                //map中已经包含了key
                if (tagcount.containsKey(tag.getTagkey())) {
                    Integer times = tagcount.get(tag.getTagkey()) + 1;
                    tagcount.put(tag.getTagkey(), times);
                } else {
                    tagcount.put(tag.getTagkey(), 1);
                }

            }
        }
        for (TagSon ts : allsons) {
            ts.setSupport((double) ts.getTimes() / allTagsSize);
            ts.setConfidence((double) ts.getTimes() / tagcount.get(ts.getKey()));
        }
        return allsons;
    }
}

