package pinup.backend.member.command.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminCommandController {
    @GetMapping("/admin/login")
    public String loginPage() {
        return "admin/login"; // ✅ templates/admin/login.html
    }

    @GetMapping("/admin/home")
    public String homePage() {
        return "admin/home";  // ✅ templates/admin/home.html
    }
}
