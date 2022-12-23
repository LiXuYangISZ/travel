package cn.itcast.travel.dao;

import cn.itcast.travel.domain.User;

public interface UserDao {
    /**
     * 根据用户名查找用户信息
     * @param username
     * @return
     */
    public User findByUsername(String username);
    public void save(User user);

    /**
     * 根据激活码查找用户
     * @param code 作为用户唯一的表示,相比较与uid更加复杂.
     * @return
     */
    User findByCode(String code);

    /**
     * 修改用户激活状态
     * @param user
     */
    void updateStatus(User user);

    /**
     * 通过用户名和密码查找用户
     * @param username
     * @param password
     * @return
     */
    User findByUsernameAndPassword(String username, String password);
}
