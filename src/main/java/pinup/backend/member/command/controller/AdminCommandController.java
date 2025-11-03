package pinup.backend.member.command.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//관리자 로그인
@Controller
public class AdminCommandController {
    @GetMapping("/admin/login")
    public String loginPage() {
        return "admin/login"; //
    }

//    @GetMapping("/admin/home")
//    public String homePage() {
//        return "admin/home";  //
//    }
}
