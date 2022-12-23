package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.util.JDBCUtils;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Template;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class UserDaoImpl implements UserDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    /**
     * 根据用户名查找用户信息
     * @param username
     * @return
     */
    @Override
    public User findByUsername(String username) {
        User user = null;
        //下面这个try...catch本来是不需要加的,但是由于templete查询可能会出现错误,到时候会抛异常,为了避免出现,我们加个这,那么到时候如果查询出现异常,user就不会被赋值就是一个null,程序仍然可以继续进行.
        try {
            //定义sql
            String sql = "SELECT * FROM tab_user WHERE username = ? ";
            //执行sql
            user = template.queryForObject(sql, new BeanPropertyRowMapper <User>(User.class), username);
        } catch (Exception e) {

        }
        return user;
    }

    /**
     * 保存用户信息
     * @param user
     */
    @Override
    public void save(User user) {
        //定义sql
        //这里这样写后序说是会有问题,再看吧.
        //String sql = "INSERT INTO tab_user VALUES(NULL,?,?,?,?,?,?,?,NULL,NULL) ";
        String sql="INSERT INTO tab_user(username,PASSWORD,NAME,birthday,sex,telephone,email,STATUS,CODE) VALUES(?,?,?,?,?,?,?,?,? )";
        //执行sql
        template.update(sql,
                user.getUsername(),
                user.getPassword(),
                user.getName(),
                user.getBirthday(),
                user.getSex(),
                user.getTelephone(),
                user.getEmail(),
                user.getStatus(),
                user.getCode());
    }

    /**
     * 通过激活码查找用户
     * @param code 作为用户唯一的表示,相比较与uid更加复杂.
     * @return
     */
    @Override
    public User findByCode(String code) {
        String sql = "select * from tab_user where code = ?";
        User user = null;
        try {
            user = template.queryForObject(sql, new BeanPropertyRowMapper <User>(User.class), code);
        } catch (Exception e) {
            System.out.println("不存在该用户...恶作剧昂");
        }
        return user;
    }

    /**
     * 修改用户激活状态
     * @param user
     */
    @Override
    public void updateStatus(User user) {
        String sql="update tab_user set status = 'Y' where uid = ? ";
        template.update(sql,user.getUid());
    }

    /**
     * 通过用户名和密码查找用户
     * @param username
     * @param password
     * @return
     */
    @Override
    public User findByUsernameAndPassword(String username, String password) {
        User user = null;
        //下面这个try...catch本来是不需要加的,但是由于templete查询可能会出现错误,到时候会抛异常,为了避免出现,我们加个这,那么到时候如果查询出现异常,user就不会被赋值就是一个null,程序仍然可以继续进行.
        try {
            //定义sql
            String sql = "SELECT * FROM tab_user WHERE username = ? and password = ? ";
            //执行sql
            user = template.queryForObject(sql, new BeanPropertyRowMapper <User>(User.class), username,password);
        } catch (Exception e) {

        }
        return user;
    }


}
