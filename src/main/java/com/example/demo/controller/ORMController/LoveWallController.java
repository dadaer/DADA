package com.example.demo.controller.ORMController;

import com.example.demo.ORMEntity.LostFound;
import com.example.demo.ORMEntity.LoveWall;
import com.example.demo.service.LoveWallService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LoveWallController {

    @Autowired
    private LoveWallService loveWallService;

    @RequestMapping("lovewallinfos")
    public List<LoveWall> getList(@RequestParam(defaultValue = "1") int pageNo,
                                  @RequestParam(defaultValue = "20") int pageSize) {
        PageHelper.startPage(pageNo,pageSize);
        return loveWallService.queryInfos();
    }

    @RequestMapping(value = "addLoveWallInfo",method = RequestMethod.POST)
    public int addLoveWall(LoveWall loveWall) {
        System.out.println(loveWall);
        return loveWallService.addLoveWall(loveWall);
    }
}
