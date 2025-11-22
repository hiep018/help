package com.rescue.help.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // <-- Lưu ý: đây là @Controller, không phải @RestController
public class WebController {

    /**
     * Phương thức này xử lý các yêu cầu đến trang chủ (ví dụ: http://localhost:3040/)
     * và trả về tệp "index.html" từ thư mục /templates.
     */
    @GetMapping("/")
    public String index() {
        return "index"; // Trả về tên của tệp template (index.html)
    }
}