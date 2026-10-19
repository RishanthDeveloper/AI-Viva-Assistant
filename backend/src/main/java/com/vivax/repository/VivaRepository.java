package com.vivax.repository;
import com.vivax.entity.Viva;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VivaRepository extends JpaRepository<Viva, Long> {
    List<Viva> findByStatus(String status);
    List<Viva> findByTeacherId(Long teacherId);
}
