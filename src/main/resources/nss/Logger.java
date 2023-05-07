package nss;

import org.springframework.stereotype.Component;

@Component
public class Logger {
    public void info(String msg){
        System.out.println(msg);
    }
}