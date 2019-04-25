package com.example.demo.controller.ORMController;

import com.example.demo.ORMEntity.LoveWall;
import com.example.demo.service.LoveWallService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoveWallController {

    @Autowired
    private LoveWallService loveWallService;

    @RequestMapping("lovewallinfos")
    public PageInfo<LoveWall> getList(@RequestParam(defaultValue = "1") int pageNo,
                                  @RequestParam(defaultValue = "20") int pageSize) {
        PageHelper.startPage(pageNo,pageSize);
        PageInfo<LoveWall> loveWallPageInfo = new PageInfo<>(loveWallService.queryInfos());
        return loveWallPageInfo;
    }

    @RequestMapping(value = "addLoveWallInfo",method = RequestMethod.POST)
    public int addLoveWall(LoveWall loveWall) {
        System.out.println(loveWall);
        return loveWallService.addLoveWall(loveWall);
    }
}
