package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.dao.impl.CategoryDaoImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.util.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class CategoryServiceImpl implements CategoryService {
    private CategoryDao dao= new CategoryDaoImpl();

    /**
     * 查询所有
     * @return
     */
    @Override
    public List <Category> findAll() {
        //1.从redis中查询.
        //1.1获取jedis客户端.
        Jedis jedis = JedisUtil.getJedis();
        //1.2使用sortedSet排序查询  先从redis中进行查询,查不到再从mysql中进行查询

        //由于jedis.zrange只能查询到value,而下面我们需要用cid,所以我们得使用其他方法.
        //Set <String> categorys =  jedis.zrange("category", 0, -1);
        Set <Tuple> categorys = jedis.zrangeWithScores("category", 0, -1);
        List<Category> cs = null;
        //判断集合是否为空
        if(categorys==null||categorys.size()==0){
            System.out.println("从数据库中查询...");
            //3.1
            cs = dao.findAll();
            //3.2将集合数据存储到redis中的category的key
            for(int i = 0; i< cs.size();i++ ){
                jedis.zadd("category",cs.get(i).getCid(),cs.get(i).getCname());
            }
        }else{
            System.out.println("从redis中查询...");
            //4.如果不为空  将set中的数据存入list  (因为最后我们要的是list类型的数据)
            cs = new ArrayList <Category>();
            for (Tuple tuple : categorys) {
                Category category = new Category();
                category.setCname(tuple.getElement());
                category.setCid((int) tuple.getScore());
                cs.add(category);
            }

        }
        return cs;
    }
}
