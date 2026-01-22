package kr.co.breadfeetserver.global.base;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {

    @GetMapping("/favicon.ico")
    public void returnNoFavicon() {
    }
}
