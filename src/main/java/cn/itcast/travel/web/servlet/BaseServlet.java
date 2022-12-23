package cn.itcast.travel.web.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class BaseServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //完成方法的分发
        //1.获取方法名称
        String uri = req.getRequestURI();  //   /travle/user/add
        System.out.println("请求uri:"+uri);

        //2.获取方法名称
        String methodName = uri.substring(uri.lastIndexOf("/") + 1);
        System.out.println("方法名称:"+methodName);
        //3.获取方法对象Method
        System.out.println(this);
        try {
            //getDeclaredMethod  获取所有声明的方法,忽略访问修饰符.(一般和暴力反射搭配着使用)   getMethod 只能获取公有方法.
            Method method = this.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
           //暴力反射
            //method.setAccessible(true);
            method.invoke(this,req, resp);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    //因为在Servlet中经常使用到序列化,所有我们可以对  这些重复写的序列化代码 这两个方法进行抽取.

    /**
     * 直接将传入的对象序列化为JSON,并写会客户端.
     * @param obj
     * @param response
     * @throws IOException
     */
    public void writeValue(Object obj,HttpServletResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(),obj);
    }

    /**
     * 将传入的对象序列化为JSON字符串,并返回
     * @param obj
     * @return
     * @throws JsonProcessingException
     */
    public String writeValueAsString(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }
}
