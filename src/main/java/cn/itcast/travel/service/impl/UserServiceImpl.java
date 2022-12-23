package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

public class UserServiceImpl implements UserService {
    private UserDao dao   = new UserDaoImpl();
    /**
     * 用户注册的方法
     * @param user
     * @return
     */
    @Override
    public boolean regist(User user) {
        //1.根据用户名查询用户对象
        User u = dao.findByUsername(user.getUsername());
        //判断u是否为null
        if(u!=null){
            //用户名存在,注册失败

            return false;
        }
        //2.保存用户信息
        //2.1设置激活码,唯一字符串
        user.setCode(UuidUtil.getUuid());
        //2.设置激活状态
        user.setStatus("N");
        dao.save(user);
        //3.激活邮件发送,邮件正文
        String content = "<a href='http://localhost/travel/user/active?code="+user.getCode()+"'>"+"点击进行激活白羊旅游网"+"</a>";
        MailUtils.sendMail(user.getEmail(),content,"账号激活邮件");
        return true;
    }

    /**
     * 邮箱激活方法
     * @param code
     * @return
     */
    @Override
    public boolean active(String code) {
        //1.根据激活码查询用户对象
        User user = dao.findByCode(code);
        if(user!=null){
            //修改激活状态
            dao.updateStatus(user);
            return true;
        }
        //激活失败
        return false;
    }

    /**
     * 用户登录
     * @param user
     * @return
     */
    @Override
    public User login(User user) {
        User u = dao.findByUsernameAndPassword(user.getUsername(),user.getPassword());

        return u;
    }
}
