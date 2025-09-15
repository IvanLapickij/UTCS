package loginController;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class Helper {

    public static <T> T getBean(String beanName, Class<T> type) {

        // Get the FacesContext object.
        FacesContext context = FacesContext.getCurrentInstance();

        // Get the Application object for the current context.
        Application application = context.getApplication();

        // Evaluate an EL expression, to get the bean with the specified name.
        return application.evaluateExpressionGet(
        		context, 
        		"#{" + beanName + "}", 
        		type);
    }
    
    public static void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context != null) {
            context.addMessage(null, new FacesMessage(severity, summary, detail));
        } else {
            // For unit testing
        	System.out.println("FacesContext == null");
        }
    }
    
    public static void addMessage(String clientId, FacesMessage message) {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context != null) {
        	context.addMessage(clientId, message);
        } else {
        	// For unit testing
        	System.out.println("FacesContext == null");
        }
    }
}
