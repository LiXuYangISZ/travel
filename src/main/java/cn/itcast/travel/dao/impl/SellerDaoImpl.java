package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.SellerDao;
import cn.itcast.travel.domain.Seller;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class SellerDaoImpl implements SellerDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    /**
     * 根据sid查找商家信息
     * @param sid
     * @return
     */
    @Override
    public Seller findById(int sid) {
        String sql = "SELECT * FROM tab_seller WHERE sid = ?";
        return template.queryForObject(sql,new BeanPropertyRowMapper <Seller>(Seller.class),sid);
    }
}
