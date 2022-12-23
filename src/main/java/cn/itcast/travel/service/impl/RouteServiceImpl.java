package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.RouteImgDao;
import cn.itcast.travel.dao.SellerDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.dao.impl.RouteImgDaoImpl;
import cn.itcast.travel.dao.impl.SellerDaoImpl;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.domain.Seller;
import cn.itcast.travel.service.RouteService;

import java.util.List;

public class RouteServiceImpl implements RouteService {
   private RouteDao routeDao= new RouteDaoImpl();
   private RouteImgDao routeImgDao = new RouteImgDaoImpl();
   private SellerDao sellerDao = new SellerDaoImpl();
   private FavoriteDao favoriteDao = new FavoriteDaoImpl();
    /**
     * 分页查询
     * @param cid
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    public PageBean <Route> pageQuery(int cid, int currentPage, int pageSize,String rname) {
        PageBean <Route> pb = new PageBean <Route>();
        //设置当前页码
        pb.setCurrentPage(currentPage);//设置当前页码
        pb.setPageSize(pageSize);//设置当前显示的条目数.
        int totalCount = routeDao.findTotalCount(cid, rname);
        pb.setTotalCount(totalCount);
        //设置当前显示的数据集合
        List <Route> list = routeDao.findByPage(cid, currentPage, pageSize,rname);
        pb.setList(list);
        //设置总的页码数
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize+1 ;
        pb.setTotalPage(totalPage);
        return pb;
    }

    /**
     * 根据id查询旅游路线的详细信息
     * @param rid
     * @return
     */
    @Override
    public Route findOne(String rid) {
        //1.根据id 去route表中route对象
        Route route = routeDao.findOne(Integer.parseInt(rid));
        List<RouteImg> routeImgs = routeImgDao.findById(route.getRid());
        route.setRouteImgList(routeImgs);
        Seller seller = sellerDao.findById(route.getSid());
        route.setSeller(seller);
        //设置收藏次数
        int count = favoriteDao.findCountByRid(route.getRid());
        route.setCount(count);
        return route;
    }
}
