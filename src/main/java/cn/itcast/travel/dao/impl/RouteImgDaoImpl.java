package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.RouteImgDao;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class RouteImgDaoImpl implements RouteImgDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    /**
     * 根据rid查询 routeImg,并封装成list
     * @param rid
     * @return
     */
    @Override
    public List <RouteImg> findById(int rid) {
        String sql = "SELECT * FROM tab_route_img WHERE rid = ? ";

        return template.query(sql,new BeanPropertyRowMapper <RouteImg>(RouteImg.class),rid);
    }
}
