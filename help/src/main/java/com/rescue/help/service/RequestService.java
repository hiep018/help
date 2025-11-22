package com.rescue.help.service;

import com.rescue.help.model.Request;
import com.rescue.help.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.scheduling.annotation.Scheduled;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RequestService {

    @Autowired
    private RequestRepository requestRepository;

    // Lưu yêu cầu
    public Request saveRequest(Request request) {
        return requestRepository.save(request);
    }

    // Lấy tất cả yêu cầu
    public List<Request> getAllRequests() {
        return requestRepository.findAll();
    }
    
    // PHƯƠNG THỨC TỰ ĐỘNG XÓA:
    /**
     * Tác vụ này sẽ tự động chạy vào lúc 1 giờ sáng mỗi ngày.
     */
    @Scheduled(cron = "0 0 1 * * *")
    public void deleteOldRequests() {
        // Lấy thời điểm cách đây 5 ngày
        LocalDateTime fiveDaysAgo = LocalDateTime.now().minusDays(5);
        
        System.out.println("Đang chạy tác vụ xóa yêu cầu cũ hơn 5 ngày (trước ngày " + fiveDaysAgo + ")...");
        
        // Gọi phương thức trong repository
        requestRepository.deleteByCreatedAtBefore(fiveDaysAgo);
        
        System.out.println("Đã xóa xong yêu cầu cũ.");
    }
}