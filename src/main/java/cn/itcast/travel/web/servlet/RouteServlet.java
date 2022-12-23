package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 和route路线相关的Servlet
 */
@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {
    RouteService routeService = new RouteServiceImpl();
    FavoriteService favoriteService = new FavoriteServiceImpl();


    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取前天页面数据
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        String cidStr = request.getParameter("cid");
        String rname = request.getParameter("rname");
        System.out.println(rname);

        // 因为使用的是Tomcat7,所以得处理中文乱码问题
        if(rname!=null&&!"null".equals(rname)){//先要判空,否则会出现空指针异常...
            rname = new String(rname.getBytes("iso-8859-1"),"utf-8");
        }else{//为了避免初次 传入数据为空 则rname中是一个"null"  或者不输入数据为一个""
            rname="";
        }


        System.out.println(rname);
        //2.处理参数
        int cid = 0;
        if(cidStr!=null && cidStr.length()>0&&!"null".equals(cidStr)){
            cid = Integer.parseInt(cidStr);
        }
        int currentPage = 0;
        if(currentPageStr!=null && currentPageStr.length()>0){
            currentPage = Integer.parseInt(currentPageStr);
        }else{
            currentPage = 1;
        }
        int pageSize;
        if(pageSizeStr!=null && pageSizeStr.length() >0){
            pageSize = Integer.parseInt(pageSizeStr);
        }else{
            pageSize = 5;//默认设置成5条
        }

        //3.调用service查询PageBean对象
        PageBean<Route> pb = routeService.pageQuery(cid,currentPage,pageSize,rname);

        //4.将pageBean对象序列化为json,返回

        writeValue(pb,response);


    }

    /**
     * 根据id查询一个旅游线路的详细信息
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.接收id
        String rid = request.getParameter("rid");
        //2.调用service方法查询route对象
        Route route = routeService.findOne(rid);

        //3.将其转为json写回客户端
        writeValue(route,response);
    }

    /**
     * 添加收藏.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void showFovorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String rid = request.getParameter("rid");
        //获取session中的user
        User user = (User)request.getSession().getAttribute("user");
        //判断user是否为null2
        int uid = 0;
        if(user!=null){
            uid = user.getUid();
        }
        //3.调用FavoriteService查询,传递rid,uid;
        boolean flag = favoriteService.isFavorite(rid,uid);

        //4.写回客户端flag标记.
        writeValue(flag,response);
    }
    public void addFovorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String rid = request.getParameter("rid");
        //获取session中的user
        User user = (User)request.getSession().getAttribute("user");
        //判断user是否为null2
        int uid;
        if(user==null){
            return;
        }else{
            uid = user.getUid();
        }
        //调用service
        favoriteService.add(rid,uid);

    }
}
