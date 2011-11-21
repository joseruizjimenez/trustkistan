package es.blog.trustkistan.controller;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet que implementa una serie de metodos utiles si la clase es extendida
 * @author Jose Ruiz Jimenez
 */
@SuppressWarnings("serial")
public abstract class BasicUtilitiesServlet extends HttpServlet {
      
    //Recursos accesibles sin credenciales
    protected String frontPage = null;
    protected String pageArticle = null;
    protected String readArticle = null;
    protected String loginForm = null;
    protected String showIP = null;
    //Recursos accesibles solo con credenciales
    protected String postForm = null;
    protected String postImage = null;
    //Solicitud incorrecta por parte del cliente o error por excepcion
    protected String errorForm = null;
    protected String exceptionError = null;
    //Servlets de la aplicacion
    protected String frontController = null;
    protected String getArticleServlet = null;
    protected String deleteArticleServlet = null;
    protected String getPageServlet = null;
    protected String postArticleServlet = null;
    protected String addImageServlet = null;
    protected String mailpostHandlerServlet = null;
    protected String cacheCronServlet = null;
    //Modelo de persistencia empleado
    protected String persistenceMechanism = null;
    //Fichero con los datos de configuracion
    protected String webConfigFile = null;

    protected abstract void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;
    
    /**
     * Permite redireccionar a un servlet dada su direccion
     * @param address direccion del Servlet
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
    protected void gotoNamedResource(String address, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = getServletContext().getNamedDispatcher(address);
        dispatcher.forward(request, response);
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
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(address);
        dispatcher.forward(request, response);
    }

    @Override
    public void init() {
        ServletConfig config = getServletConfig();
        ServletContext context = config.getServletContext();
        frontPage = context.getInitParameter("frontPage");
        pageArticle = context.getInitParameter("pageArticle");
        readArticle = context.getInitParameter("readArticle");
        loginForm = context.getInitParameter("loginForm");
        showIP = context.getInitParameter("showIP");
        postForm = context.getInitParameter("postForm");
        postImage = context.getInitParameter("postImage");
        errorForm = context.getInitParameter("errorForm");
        exceptionError = context.getInitParameter("exceptionError");
        frontController = context.getInitParameter("frontController");
        getArticleServlet = context.getInitParameter("getArticleServlet");
        deleteArticleServlet = context.getInitParameter("deleteArticle");
        getPageServlet = context.getInitParameter("getPageServlet");
        postArticleServlet = context.getInitParameter("postArticleServlet");
        addImageServlet = context.getInitParameter("addImageServlet");
        mailpostHandlerServlet = context.getInitParameter("mailpostHandlerServlet");
        cacheCronServlet = context.getInitParameter("cacheCronServlet");
        persistenceMechanism = context.getInitParameter("persistenceMechanism");
        webConfigFile = context.getInitParameter("webConfigFile");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
   
    /**
     * Valida que la cadena contiene campos permitidos para un nombre,
     * apellidos... solo caracteres alfabeticos y con acentos
     * @param data datos a validar
     * @param minAcceptable tamanyo minimo de la cadena
     * @param maxAcceptable tamanyo maximo de la cadena
     * @return true si es valida, false en caso contrario
     */
    protected boolean validateName(String data, int minAcceptable, int maxAcceptable) {
        boolean isGoodLength = data!=null && checkLength(data, minAcceptable, maxAcceptable);
        String namePattern = "([a-zA-ZÃƒÂ±Ãƒâ€˜ÃƒÂ¡ÃƒÂ©ÃƒÂ­ÃƒÂ³ÃƒÂºÃƒÂ�Ãƒâ€°ÃƒÂ�Ãƒâ€œÃƒÅ¡ÃƒÂ§ ])+";
        return isGoodLength && data.matches(namePattern);
    }
    
    /**
     * Valida que la cadena contiene solo caracteres alfabeticos y numericos
     * @param data datos a validar
     * @param minAcceptable tamanyo minimo de la cadena
     * @param maxAcceptable tamanyo maximo de la cadena
     * @return true si es valida, false en caso contrario
     */
    protected boolean validateAlphaNumeric(String data, int minAcceptable, int maxAcceptable) {
        boolean isGoodLength = data!=null && checkLength(data, minAcceptable, maxAcceptable);
        String namePattern = "([0-9a-zA-Z])+";
        return isGoodLength && data.matches(namePattern);
    }
    
    /**
     * Valida que la cadena contiene solo caracteres numericos
     * @param data datos a validar
     * @param minAcceptable tamanyo minimo de la cadena
     * @param maxAcceptable tamanyo maximo de la cadena
     * @return true si es valida, false en caso contrario
     */
    protected boolean validateNumeric(String data, int minAcceptable, int maxAcceptable) {
        boolean isGoodLength = data!=null && checkLength(data, minAcceptable, maxAcceptable);
        String namePattern = "([0-9])+";
        return isGoodLength && data.matches(namePattern);
    }
    
    /**
     * Valida que la cadena contiene solo caracteres validos para un email
     * @param data datos a validar
     * @return true si es valida, false en caso contrario
     */
    protected boolean validateEmail(String data) {
        String emailPattern = "\\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b";
        Pattern p = Pattern.compile(emailPattern,Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(data);
        return (data!=null && m.matches());
    }
    
    /**
     * Valida que la cadena contiene solo caracteres alfabeticos, acentuados
     * y numericos y algunos caracteres especiales. Direcciones, descripciones...
     * @param data datos a validar
     * @param minAcceptable tamanyo minimo de la cadena
     * @param maxAcceptable tamanyo maximo de la cadena
     * @return true si es valida, false en caso contrario
     */
    protected boolean validateFreeText(String data, int minAcceptable, int maxAcceptable) {
        boolean isGoodLength = data!=null && checkLength(data, minAcceptable, maxAcceptable);
        String namePattern = "([0-9a-zA-ZÃƒÂ±Ãƒâ€˜ÃƒÂ¡ÃƒÂ©ÃƒÂ­ÃƒÂ³ÃƒÂºÃƒÂ�Ãƒâ€°ÃƒÂ�Ãƒâ€œÃƒÅ¡._,;:()/\\?\\!\\Ã‚Â¿\\Ã‚Â¡ \\-])*";
        return isGoodLength && data.matches(namePattern);
    }
    
    /**
     * Valida una fecha de nacimiento dada por:
     * @param day dia
     * @param month mes
     * @param year anho
     * @return true si es valida, false en caso contrario
     */
    protected boolean validateDate(String year, String month, String day) {
        if (checkLength(day, 1, 2) && checkLength(month, 1, 2) && checkLength(year, 4, 4)) {
            if (day.matches("([0-9])*") && month.matches("([0-9])*") && year.matches("([0-9])*")) {
            int dayInt = Integer.parseInt(day);
            int monthInt = Integer.parseInt(month);
            int yearInt = Integer.parseInt(year);
            return ( (dayInt <= 31) && (dayInt > 0) && (monthInt <= 12) && (monthInt > 0)
                    && (yearInt <= 2200) && (yearInt >= 1900) );
            }
        }
        return false;
    }

    protected boolean checkLength(String data, int minAcceptable, int maxAcceptable) {
        if(data == null)
            return false;
        int length = data.length();
        boolean isGoodLength = length >= minAcceptable && length <= maxAcceptable;
        return isGoodLength;
    }
}
