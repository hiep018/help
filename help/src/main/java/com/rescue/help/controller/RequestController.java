package com.rescue.help.controller;

import com.rescue.help.model.Request;
import com.rescue.help.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class RequestController {

    @Autowired
    private RequestService requestService;

    @GetMapping("/get-requests")
    public List<Request> getRequests() {
        return requestService.getAllRequests();
    }

    // --- ĐÂY LÀ PHIÊN BẢN MỚI (GỌN NHẸ & TỐC ĐỘ CAO) ---
    @PostMapping("/submit-request")
    public ResponseEntity<?> submitRequest(@RequestBody Request request) {
        
        // 1. Lưu vào CSDL (Cực nhanh)
        Request savedRequest = requestService.saveRequest(request);

        // 2. Kích hoạt gửi mail ngầm (Fire-and-forget)
        // Web sẽ KHÔNG CHỜ dòng này chạy xong mà đi tiếp ngay
        requestService.sendRescueEmail(savedRequest);

        // 3. Trả về thông báo thành công NGAY LẬP TỨC
        return ResponseEntity.ok(Map.of("message", "Yêu cầu đã được gửi thành công! Đội cứu hộ sẽ liên lạc với bạn."));
    }
}