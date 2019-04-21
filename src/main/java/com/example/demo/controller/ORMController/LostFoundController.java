package com.example.demo.controller.ORMController;

import com.example.demo.ORMEntity.LostFound;
import com.example.demo.service.LostFoundServie;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LostFoundController {

    @Autowired
    private LostFoundServie lostFoundServie;

    @RequestMapping("lostfoundinfos")
    public List<LostFound> queryInfoByType(@RequestParam(defaultValue = "1") int pageNo,
                                           @RequestParam(defaultValue = "20") int pageSize ,Integer type) {
        PageHelper.startPage(pageNo,pageSize);
        return lostFoundServie.queryInfoByType(type);
    }

    @RequestMapping("lostfoundinfo")
    public LostFound queryInfoById(Integer id) {
        return lostFoundServie.queryInfoById(id);
    }

    @RequestMapping(value = "addLostFoundInfo",method = RequestMethod.POST)
    public int addLostFound(LostFound lostFound) {
        System.out.println(lostFound);
        System.out.println(lostFound.getTitle());
        return lostFoundServie.addLostFound(lostFound);
    }

}
