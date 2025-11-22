package com.rescue.help.service;

import com.rescue.help.model.Request;
import com.rescue.help.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Async; // Quan trọng
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RequestService {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private JavaMailSender javaMailSender; // Chuyển từ Controller sang

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${rescue.team.email}")
    private String rescueTeamEmail;

    public Request saveRequest(Request request) {
        return requestRepository.save(request);
    }

    public List<Request> getAllRequests() {
        return requestRepository.findAll();
    }
    
    // --- ĐÂY LÀ PHẦN QUAN TRỌNG: GỬI MAIL NGẦM ---
    @Async // Lệnh này giúp web KHÔNG PHẢI CHỜ, gửi mail chạy riêng một luồng
    public void sendRescueEmail(Request savedRequest) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            
            mailMessage.setFrom(fromEmail);
            mailMessage.setTo(rescueTeamEmail);
            mailMessage.setSubject("[YÊU CẦU CỨU HỘ MỚI] - " + savedRequest.getHelpType());
            
            // Nội dung mail (Đã có fix link Google Maps chuẩn)
            String messageBody = String.format(
                "Một yêu cầu cứu hộ mới vừa được gửi:\n\n" +
                "Loại yêu cầu: %s\n" +
                "Mô tả: %s\n" +
                "Vị trí (Lat, Lng): %f, %f\n" +
                "Thời gian: %s\n\n" +
                "Link Google Maps:\nhttp://googleusercontent.com/maps.google.com/?q=%f,%f", 
                
                savedRequest.getHelpType(),
                savedRequest.getDescription(),
                savedRequest.getLatitude(),
                savedRequest.getLongitude(),
                savedRequest.getCreatedAt().toString(),
                savedRequest.getLatitude(),
                savedRequest.getLongitude()
            );
            
            mailMessage.setText(messageBody);
            javaMailSender.send(mailMessage);
            
            System.out.println(">>> (Async) Đã gửi mail thành công!");

        } catch (Exception e) {
            // Lỗi thì in ra Log server, không làm phiền người dùng web
            System.err.println(">>> (Async) Lỗi gửi mail: " + e.getMessage());
        }
    }

    // Tự động xóa sau 5 ngày
    @Scheduled(cron = "0 0 1 * * *")
    public void deleteOldRequests() {
        LocalDateTime fiveDaysAgo = LocalDateTime.now().minusDays(5);
        requestRepository.deleteByCreatedAtBefore(fiveDaysAgo);
    }
}