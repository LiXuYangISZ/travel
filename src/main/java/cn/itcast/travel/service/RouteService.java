package cn.itcast.travel.service;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;

public interface RouteService {

    PageBean<Route> pageQuery(int cid, int currentPage, int pageSize,String rname);

    /**
     * 根据rid查询旅游路线的详细信息
     * @param rid
     * @return
     */
    Route findOne(String rid);
}
