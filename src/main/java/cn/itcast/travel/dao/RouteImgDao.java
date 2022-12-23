package cn.itcast.travel.dao;

import cn.itcast.travel.domain.RouteImg;

import java.util.List;

public interface RouteImgDao {
    /**
     * 根据rid查询 routeImg,并封装成list
     * @param rid
     * @return
     */
    List<RouteImg> findById(int rid);
}
