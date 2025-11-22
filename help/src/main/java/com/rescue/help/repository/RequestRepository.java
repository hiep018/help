package com.rescue.help.repository;

import com.rescue.help.model.Request;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    // Phương thức để tự động xóa
    void deleteByCreatedAtBefore(LocalDateTime timestamp);
}