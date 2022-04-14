/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.filter;

import entity.Student;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import util.enumeration.AccessRightEnum;

/**
 *
 * @author raihan
 */
@WebFilter(filterName = "SecurityFilter", urlPatterns = {"/*"})
public class SecurityFilter implements Filter {
     
    FilterConfig filterConfig;
    
    private static final String CONTEXT_ROOT = "/NUSociety-war";
    
   

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }



    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        HttpServletResponse httpServletResponse = (HttpServletResponse)response;
        HttpSession httpSession = httpServletRequest.getSession(true);
        String requestServletPath = httpServletRequest.getServletPath();        
        
        if(httpSession.getAttribute("isLogin") == null) {
            httpSession.setAttribute("isLogin", false);
        }

        Boolean isLogin = (Boolean)httpSession.getAttribute("isLogin");
        
        
        
        if(!excludeLoginCheck(requestServletPath)) {
            if(isLogin == true) {
                Student currentStudent = (Student)httpSession.getAttribute("currentStudent");
                
                if(true)
                {
                    chain.doFilter(request, response);
                }
                else
                {
                    httpServletResponse.sendRedirect(CONTEXT_ROOT + "/accessRightError.xhtml");
                }
            }
            else
            {
                httpServletResponse.sendRedirect(CONTEXT_ROOT + "/accessRightError.xhtml");
            }
        }
        else
        {
            chain.doFilter(request, response);
        }
    }



    public void destroy()
    {

    }
    
    
    
    private Boolean checkAccessRight(String path, AccessRightEnum accessRight)
    {        
        if(accessRight.equals(AccessRightEnum.MEMBER))
        {            
//            if(path.equals("/cashierOperation/checkout.xhtml") ||
//                path.equals("/cashierOperation/voidRefund.xhtml") ||
//                path.equals("/cashierOperation/viewMySaleTransactions.xhtml"))
//            {
                return true;
//            }
//            else
//            {
//                return false;
//            }
        }
        else if(accessRight.equals(AccessRightEnum.LEADER))
        {
//            if(path.equals("/cashierOperation/checkout.xhtml") ||
//                path.equals("/cashierOperation/voidRefund.xhtml") ||
//                path.equals("/cashierOperation/viewMySaleTransactions.xhtml") ||
//                path.equals("/systemAdministration/staffManagement.xhtml") ||
//                path.equals("/systemAdministration/productManagement.xhtml") ||
//                path.equals("/systemAdministration/searchProductsByName.xhtml") ||
//                path.equals("/systemAdministration/filterProductsByCategory.xhtml") ||
//                path.equals("/systemAdministration/filterProductsByTags.xhtml"))
//            {
                return true;
//            }
//            else
//            {
//                return false;
//            }
        }
        
        return false;
    }



    private Boolean excludeLoginCheck(String path)
    {
        if(path.equals("/index.xhtml") ||
            path.equals("/accessRightError.xhtml") ||
            path.startsWith("/javax.faces.resource"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
}
