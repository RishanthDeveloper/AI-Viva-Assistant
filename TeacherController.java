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

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

    @Autowired
    private VivaRepository vivaRepository;

    @Autowired
    private VivaAttemptRepository attemptRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse> getDashboard(Authentication authentication) {
        User teacher = userRepository.findByEmail(authentication.getName()).orElse(null);
        if (teacher == null) {
            return ResponseEntity.status(401).body(new ApiResponse(false, "User not found", null));
        }

        List<Viva> vivas = vivaRepository.findByTeacherId(teacher.getId());
        long published = vivas.stream().filter(v -> "PUBLISHED".equals(v.getStatus())).count();

        List<VivaAttempt> submissions = new ArrayList<>();
        for (Viva v : vivas) {
            for (VivaAttempt a : attemptRepository.findByVivaId(v.getId())) {
                if ("SUBMITTED".equals(a.getStatus()) || "AUTO_SUBMITTED".equals(a.getStatus())) {
                    submissions.add(a);
                }
            }
        }

        double avgScore = submissions.stream().mapToDouble(VivaAttempt::getPercentage).average().orElse(0.0);

        List<Map<String, Object>> recent = submissions.stream()
                .sorted(Comparator.comparing(VivaAttempt::getSubmittedAt,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(10)
                .map(a -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("id", a.getId());
                    m.put("studentName", a.getStudent() != null ? a.getStudent().getName() : "Unknown");
                    m.put("vivaTitle", a.getViva() != null ? a.getViva().getTitle() : "Untitled viva");
                    m.put("submittedAt", a.getSubmittedAt());
                    m.put("score", a.getScore());
                    m.put("percentage", a.getPercentage());
                    return m;
                })
                .collect(Collectors.toList());

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("totalVivas", vivas.size());
        data.put("publishedVivas", published);
        data.put("totalSubmissions", submissions.size());
        data.put("avgScore", avgScore);
        data.put("recentSubmissions", recent);

        return ResponseEntity.ok(new ApiResponse(true, "Dashboard data fetched", data));
    }
}
