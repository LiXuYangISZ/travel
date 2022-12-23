package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class RouteDaoImpl implements RouteDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    /**
     * 根据cid查询总记录数
     * @param cid
     * @return
     */
    @Override
    public int findTotalCount(int cid,String rname) {
        //多条件分页查询
        //String sql = "SELECT COUNT(*) FROM tab_route WHERE cid = ? ";
        //1.定义sql模板
        String sql = "SELECT COUNT(*) FROM tab_route WHERE 1 = 1 ";
        StringBuilder sb = new StringBuilder(sql);
        List <Object> params = new ArrayList <>();

        //2.判断参数是否有值 ,因为可能一个有值,可能两个有值
        if(cid != 0){
            sb.append(" and cid = ? ");
            params.add(cid);

        }
        if(rname != null){
            sb.append(" and rname like ? ");
            params.add("%"+rname+"%");
        }
        sql = sb.toString();
        System.out.println(sql);



        return template.queryForObject(sql,Integer.class,params.toArray());
    }

    /**
     * 根据cid,start,pageSize查询当前页的数据集合.
     * @param cid
     * @param start
     * @param pageSize
     * @return
     */
    @Override
    public List <Route> findByPage(int cid, int start, int pageSize,String rname) {
        //String sql = "select * from tab_route where cid = ? limit ? , ? ";
        //1.定义sql模板
        String sql = "SELECT * FROM tab_route WHERE 1 = 1 ";
        StringBuilder sb = new StringBuilder(sql);
        List <Object> params = new ArrayList <>();

        //2.判断参数是否有值 ,因为可能一个有值,可能两个有值
        if(cid != 0){
            sb.append(" and cid = ? ");
            params.add(cid);
        }
        if(rname != null && rname.length() > 0){
            sb.append(" and rname like ?");
            params.add("%"+rname+"%");
        }
        sb.append(" limit ? , ? ");
        params.add(start);
        params.add(pageSize);
        sql = sb.toString();
        System.out.println(sql);
        return template.query(sql,new BeanPropertyRowMapper <Route>(Route.class),params.toArray());
    }
    /**
     * 根据rid查询旅游路线的详细信息.
     * @param rid
     * @return
     */
    @Override
    public Route findOne(int rid) {
        String sql = "SELECT * FROM tab_route WHERE rid = ? ";
        return template.queryForObject(sql,new BeanPropertyRowMapper <Route>(Route.class),rid);
    }
}
