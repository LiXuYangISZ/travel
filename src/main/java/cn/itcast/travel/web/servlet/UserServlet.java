package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {
    //声明全局业务对象.
    private UserService service = new UserServiceImpl();
    /**
     * 登录功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //验证码的校验
        String check = request.getParameter("check");
        HttpSession session = request.getSession();
        String checkcodeServer = (String) session.getAttribute("CHECKCODE_SERVER");
        //进行比较
        if(checkcodeServer==null||!checkcodeServer.equalsIgnoreCase(check)){
            ResultInfo info = new ResultInfo();
            //注册失败
            info.setFlag(false);
            info.setErrorMsg("验证码错误!");
//            ObjectMapper mapper = new ObjectMapper();
//            String json = mapper.writeValueAsString(info);
            String json = writeValueAsString(info);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);
            return;
        }

        //1.获取用户信息,并封装
        Map <String, String[]> map = request.getParameterMap();
        User user = new User();
        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        //2.调用service查询user
        //UserService service = new UserServiceImpl();
        User newUser = service.login(user);
        ResultInfo info = new ResultInfo();

        //3.判断用户是否存在
        if(newUser==null){
            info.setFlag(false);
            info.setErrorMsg("用户名或密码错误");
        }
        //4.判断用户是否激活
        if(newUser!=null&&!"Y".equals(newUser.getStatus())){
            info.setFlag(false);
            info.setErrorMsg("您尚未激活,请先通过邮箱进行激活");
        }
        //5.判断登录成功
        if(newUser!=null&&"Y".equals(newUser.getStatus())){
            //存入用户的信息,以便后期使用.
            request.getSession().setAttribute("user",newUser);
            info.setFlag(true);
        }
        //响应数据
//        ObjectMapper mapper = new ObjectMapper();
//        response.setContentType("application/json;charset=utf-8");
//        mapper.writeValue(response.getOutputStream(),info);//将信息返回前台进行处理
        writeValue(info,response);
    }

    /**
     * 注册功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //验证码的校验
        String check = request.getParameter("check");
        //从session中获取验证码
        HttpSession session = request.getSession();
        String checkcodeServer = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");//为了保证验证码只使用一次
        //进行比较
        if(checkcodeServer==null||!checkcodeServer.equalsIgnoreCase(check)){
            ResultInfo info = new ResultInfo();//将结果存到ResultInfo中返回前台页面
            //注册失败
            info.setFlag(false);
            info.setErrorMsg("验证码错误!");
            System.out.println("验证码错误!");
            //将info序列化成json
//            ObjectMapper mapper = new ObjectMapper();
//            String json = mapper.writeValueAsString(info);
            String json = writeValueAsString(info);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);
            return;
        }

        //1.获取数据
        Map <String, String[]> map = request.getParameterMap();
        //2.封装对象
        User user = new User();
        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //3.调用service完成注册
        //UserService service = new UserServiceImpl();
        boolean flag =  service.regist(user);
        ResultInfo info = new ResultInfo();//将结果存到ResultInfo中返回前台页面
        //4.响应结果
        if(flag){
            //注册成功
            info.setFlag(true);
            System.out.println("注册成功昂");
        }else{
            //注册失败
            info.setFlag(false);
            info.setErrorMsg("用户名已存在,注册失败!");
            System.out.println("用户名已存在,注册失败!");
        }
        //5.将information对象序列化为json,并写回客户端.
//        ObjectMapper mapper = new ObjectMapper();
//        String json = mapper.writeValueAsString(info);
        String json = writeValueAsString(info);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);

    }

    /**
     * 查找一个用户功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
//        response.setContentType("application/json;charset=utf-8");
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.writeValue(response.getOutputStream(),user);
        writeValue(user,response);
    }

    /**
     * 退出功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.销毁session
        request.getSession().invalidate();
        //2.跳转到登录界面
        response.sendRedirect(request.getContextPath()+"/login.jsp");
    }

    /**
     * 激活账号功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取激活码
        String code = request.getParameter("code");
        if(code!=null){
            //2.调用service进行激活

            boolean flag = service.active(code);
            //3.判断标记
            String msg = null;
            if(flag){
                //激活成功
                msg = "激活成功,请<a href='login.jsp'>登录</a>";
            }else{
                msg="激活失败,请联系管理员!";
            }
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(msg);

        }
    }


}
