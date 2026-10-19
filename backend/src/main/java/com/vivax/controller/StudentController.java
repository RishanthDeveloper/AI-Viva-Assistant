package com.vivax.controller;

import com.vivax.dto.ApiResponse;
import com.vivax.entity.User;
import com.vivax.entity.Viva;
import com.vivax.entity.VivaAttempt;
import com.vivax.repository.UserRepository;
import com.vivax.repository.VivaAttemptRepository;
import com.vivax.repository.VivaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    private VivaRepository vivaRepository;

    @Autowired
    private VivaAttemptRepository attemptRepository;

    @Autowired
    private UserRepository userRepository;

    private static boolean isFinished(VivaAttempt a) {
        return "SUBMITTED".equals(a.getStatus()) || "AUTO_SUBMITTED".equals(a.getStatus());
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse> getDashboard(Authentication authentication) {
        User student = userRepository.findByEmail(authentication.getName()).orElse(null);
        if (student == null) {
            return ResponseEntity.status(401).body(new ApiResponse(false, "User not found", null));
        }

        List<VivaAttempt> finished = attemptRepository.findByStudentId(student.getId()).stream()
                .filter(StudentController::isFinished)
                .collect(Collectors.toList());
        List<Viva> published = vivaRepository.findByStatus("PUBLISHED");

        Set<Long> doneVivaIds = finished.stream()
                .filter(a -> a.getViva() != null)
                .map(a -> a.getViva().getId())
                .collect(Collectors.toSet());

        LocalDateTime now = LocalDateTime.now();
        long upcoming = published.stream()
                .filter(v -> !doneVivaIds.contains(v.getId()))
                .filter(v -> v.getEndTime() == null || v.getEndTime().isAfter(now))
                .count();

        double avgScore = finished.stream().mapToDouble(VivaAttempt::getPercentage).average().orElse(0.0);

        List<Map<String, Object>> recent = finished.stream()
                .sorted(Comparator.comparing(VivaAttempt::getSubmittedAt,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(5)
                .map(a -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("id", a.getId());
                    m.put("vivaTitle", a.getViva() != null ? a.getViva().getTitle() : "Untitled viva");
                    m.put("submittedAt", a.getSubmittedAt());
                    m.put("score", a.getScore());
                    m.put("percentage", a.getPercentage());
                    return m;
                })
                .collect(Collectors.toList());

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("totalTests", published.size());
        data.put("completed", finished.size());
        data.put("upcoming", upcoming);
        data.put("avgScore", avgScore);
        data.put("recentTests", recent);

        return ResponseEntity.ok(new ApiResponse(true, "Dashboard data fetched", data));
    }

    @GetMapping("/vivas")
    public ResponseEntity<ApiResponse> getVivas() {
        List<Map<String, Object>> vivas = vivaRepository.findByStatus("PUBLISHED").stream().map(v -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", v.getId());
            m.put("title", v.getTitle());
            m.put("subject", v.getSubject() != null ? v.getSubject().getName() : null);
            m.put("startTime", v.getStartTime());
            m.put("endTime", v.getEndTime());
            m.put("durationMinutes", v.getDurationMinutes());
            return m;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse(true, "Vivas fetched", vivas));
    }
}
