package nss;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class ViewHandler {
    
    @RequestMapping("main.go")
    public ModelAndView main(ModelAndView mv) {
        mv.addObject("test-attribute", "test-attribute");
        mv.setViewName("main");
        return mv;
    }
}