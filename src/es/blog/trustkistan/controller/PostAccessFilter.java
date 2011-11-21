package es.blog.trustkistan.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Intercepta las peticiones de publicar articulos y pide credenciales
 */
public class PostAccessFilter implements Filter {
    String loginForm = null;
    String postForm = null;
    String loginUser = null;
    String loginPass = null;

    public PostAccessFilter() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse)response;
        HttpSession session = httpRequest.getSession();
        Object authenObject = session.getAttribute("authenticated");
        if (authenObject != null && (Boolean)authenObject) {
        	session.setAttribute("admin", "true");
        	if(httpRequest.getRequestURI().contains("/postForm")) {
        		gotoURL(postForm, httpRequest, httpResponse);
        	} else {
        		chain.doFilter(request, response);
        	}
        } else {
            String user = request.getParameter("user");
            String pass = request.getParameter("pass");
            if (user != null && pass != null && user.equals(loginUser) && pass.equals(loginPass)) {
                session.setAttribute("authenticated", true);
                session.setAttribute("admin", "true");
                gotoURL(postForm, httpRequest, httpResponse);
            } else {
            	session.setAttribute("admin", "false");
                httpResponse.sendRedirect(session.getServletContext().getContextPath()+loginForm);
            }
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        try {
            ServletContext context = filterConfig.getServletContext();
            loginForm = context.getInitParameter("loginForm");
            postForm = context.getInitParameter("postForm");
            String webConfigFile = context.getInitParameter("webConfigFile");
            InputStream is = context.getResourceAsStream(webConfigFile);
            Properties webConfig = new Properties();
            webConfig.load(is);
            loginUser = webConfig.getProperty("loginUser");
            loginPass = webConfig.getProperty("loginPass");
        } catch (IOException ex) {
            Logger.getLogger(PostAccessFilter.class.getName()).log(Level.SEVERE,
                    "Error cargando datos admin", ex);
        }
    }

    @Override
    public void destroy() {
    }
    
    /**
     * Permite redireccionar a una URL dada su direccion
     * @param address direccion URL a donde queremos ir
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
    protected void gotoURL(String address, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        RequestDispatcher dispatcher = session.getServletContext().getRequestDispatcher(address);
        dispatcher.forward(request, response);
    }
}