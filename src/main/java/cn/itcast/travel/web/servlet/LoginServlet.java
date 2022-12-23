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

@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(info);
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
        UserService service = new UserServiceImpl();
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
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(),info);//将信息返回前台进行处理


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
