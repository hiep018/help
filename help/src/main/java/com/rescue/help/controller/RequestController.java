package com.rescue.help.controller;

import com.rescue.help.model.Request;
import com.rescue.help.repository.RequestRepository;
import com.rescue.help.service.RequestService;

// Import các thư viện Spring cần thiết
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

// THÊM CÁC IMPORT ĐỂ GỬI MAIL
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@RestController
public class RequestController {

    @Autowired
    private RequestService requestService;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${rescue.team.email}")
    private String rescueTeamEmail;

    @GetMapping("/get-requests")
    public List<Request> getRequests() {
        return requestService.getAllRequests();
    }

    // --- ĐÂY LÀ PHIÊN BẢN ĐÃ SỬA CHÍNH XÁC ---
    @PostMapping("/submit-request")
    public ResponseEntity<?> submitRequest(@RequestBody Request request) {
        
        // 1. Lưu yêu cầu vào CSDL
        Request savedRequest = requestRepository.save(request);

        // 2. Gửi thông báo EMAIL cho đội cứu hộ
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            
            mailMessage.setFrom(fromEmail); // Gửi từ
            mailMessage.setTo(rescueTeamEmail); // Gửi tới
            mailMessage.setSubject("[YÊU CẦU CỨU HỘ MỚI] - " + savedRequest.getHelpType()); // Tiêu đề
            
            // --- NỘI DUNG EMAIL ĐÃ SỬA ---
            // Chuỗi định dạng hiện sử dụng 7 tham số (2 String, 5 Float/Double/String)
            String messageBody = String.format(
                "Một yêu cầu cứu hộ mới vừa được gửi:\n\n" +
                "Loại yêu cầu: %s\n" +              // 1. helpType
                "Mô tả: %s\n" +                    // 2. description
                "Vị trí (Lat, Lng): %f, %f\n" +    // 3. latitude, 4. longitude
                "Thời gian: %s\n\n" +              // 5. createdAt
                
                // Sửa ở đây: Link này sẽ sử dụng 2 tham số cuối cùng (%f, %f)
                // Thêm %f, %f vào cuối đường link để nhận tọa độ
                "Link Google Maps:\nhttp://googleusercontent.com/maps.google.com/?q=%f,%f",
                
                savedRequest.getHelpType(),         // 1
                savedRequest.getDescription(),      // 2
                savedRequest.getLatitude(),         // 3
                savedRequest.getLongitude(),        // 4
                savedRequest.getCreatedAt().toString(), // 5
                
                // Hai tham số này được dùng cho link Google Maps
                savedRequest.getLatitude(),         // 6
                savedRequest.getLongitude()         // 7
            );
            // --- KẾT THÚC PHẦN SỬA ---
            // Update fix mail cuoi cung
            mailMessage.setText(messageBody);
            
            // Gửi mail
            javaMailSender.send(mailMessage);
            
            System.out.println("Đã gửi Email thông báo cứu hộ thành công!");

        } catch (Exception e) {
            System.err.println("Lỗi nghiêm trọng khi gửi Email: " + e.getMessage());
        }

        // 3. Trả về thông báo thành công cho người dùng web
        return ResponseEntity.ok(Map.of("message", "Yêu cầu đã được gửi thành công! Đội cứu hộ sẽ liên lạc với bạn."));
    }
    // (Đã xóa phương thức submitRequest bị lặp lại)
}