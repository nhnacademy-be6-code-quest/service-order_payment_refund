package com.nhnacademy.orderpaymentrefund.context;


import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ClientHeaderContext {

    private static final String X_USER_ID = "X-User-ID";

    private static final ThreadLocal<Long> clientId = new ThreadLocal<>();
    private static final Logger log = LoggerFactory.getLogger(ClientHeaderContext.class);

    public void setClientId(HttpServletRequest request){
        if(request.getHeader(X_USER_ID) != null){
            clientId.set(Long.parseLong(request.getHeader(X_USER_ID)));
        }
    }

    public boolean isClient(){
        return clientId.get() != null;
    }

    public Long getClientId(){
        return clientId.get();
    }

    public void clear(){
        clientId.remove();
    }

}
