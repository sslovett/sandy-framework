package com.sandy.fw.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sandy.fw.admin.models.TzArea;
import com.sandy.fw.admin.service.TzAreaService;
import com.sandy.fw.common.response.ServerResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("admin/area")
public class AreaController {

    @Autowired
    private TzAreaService tzAreaService;

    @GetMapping("/list")
    public ServerResponseEntity<List<TzArea>> list(TzArea area) {
        List<TzArea> areaList = tzAreaService.list(new LambdaQueryWrapper<TzArea>()
                .like(area.getAreaName() != null, TzArea::getAreaName, area.getAreaName()));
        return ServerResponseEntity.success(areaList);
    }
}
