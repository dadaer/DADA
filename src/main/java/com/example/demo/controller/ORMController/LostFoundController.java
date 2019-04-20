package com.example.demo.controller.ORMController;

import com.example.demo.ORMEntity.LostFound;
import com.example.demo.service.LostFoundServie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LostFoundController {

    @Autowired
    private LostFoundServie lostFoundServie;

    @RequestMapping("lostfoundinfos")
    public List<LostFound> queryInfoByType(Integer type) {
        return lostFoundServie.queryInfoByType(type);
    }

    @RequestMapping("lostfoundinfo")
    public LostFound queryInfoById(Integer id) {
        return lostFoundServie.queryInfoById(id);
    }

    @RequestMapping(value = "addLostFoundInfo",method = RequestMethod.POST)
    public int queryInfoByType(LostFound lostFound) {
        System.out.println(lostFound);
//        LostFound lostFound1 = new LostFound();
//        lostFound1.setId(lostFound.getId());
//        lostFound1.setType(lostFound.getType());
//        lostFound1.setTime(lostFound.getTime());
//        lostFound1.setPlace(lostFound.getPlace());
//        lostFound1.setContactInfo(lostFound.getContactInfo());
//        lostFound1.setTitle(lostFound.getTitle());
//        lostFound1.setContent(lostFound.getContent());
//        lostFound1.setImgUrl(lostFound.getImgUrl());
        System.out.println(lostFound.getTitle());
        return lostFoundServie.addLostFound(lostFound);
    }

}
