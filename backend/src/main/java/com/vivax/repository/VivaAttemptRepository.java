package com.vivax.repository;
import com.vivax.entity.VivaAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface VivaAttemptRepository extends JpaRepository<VivaAttempt, Long> {
    List<VivaAttempt> findByStudentId(Long studentId);
    Optional<VivaAttempt> findByVivaIdAndStudentId(Long vivaId, Long studentId);
    List<VivaAttempt> findByVivaId(Long vivaId);
}
