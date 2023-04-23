package nss;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewHandler {

    @RequestMapping("/main.go")
    public String main(Model model) {
        model.addAttribute("test-attribute", "test-attribute");
        return "main";
    }
}
