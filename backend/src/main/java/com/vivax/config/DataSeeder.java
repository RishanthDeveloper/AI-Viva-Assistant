package com.vivax.config;

import com.vivax.entity.Role;
import com.vivax.entity.Subject;
import com.vivax.entity.User;
import com.vivax.entity.Viva;
import com.vivax.entity.VivaAttempt;
import com.vivax.repository.SubjectRepository;
import com.vivax.repository.UserRepository;
import com.vivax.repository.VivaRepository;
import com.vivax.repository.VivaAttemptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private SubjectRepository subjectRepository;
    
    @Autowired
    private VivaRepository vivaRepository;
    
    @Autowired
    private VivaAttemptRepository attemptRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            User student = new User();
            student.setName("RISHANTH P");
            student.setEmail("student@test.com");
            student.setPassword(passwordEncoder.encode("password"));
            student.setRole(Role.STUDENT);
            userRepository.save(student);

            User teacher = new User();
            teacher.setName("Teacher");
            teacher.setEmail("teacher@test.com");
            teacher.setPassword(passwordEncoder.encode("password"));
            teacher.setRole(Role.TEACHER);
            userRepository.save(teacher);
            
            Subject sub1 = new Subject();
            sub1.setName("Data Visualization");
            sub1.setCode("CBB1403");
            subjectRepository.save(sub1);
            
            Subject sub2 = new Subject();
            sub2.setName("UDSA");
            sub2.setCode("CBB1401");
            subjectRepository.save(sub2);
            
            Viva v1 = new Viva();
            v1.setTitle("DV Viva Voice");
            v1.setSubject(sub1);
            v1.setTeacher(teacher);
            v1.setStatus("PUBLISHED");
            v1.setStartTime(LocalDateTime.now().minusDays(1));
            v1.setEndTime(LocalDateTime.now().plusDays(1));
            vivaRepository.save(v1);
            
            Viva v2 = new Viva();
            v2.setTitle("CBB1403 - DATA VISUALIZATION SSA 2");
            v2.setSubject(sub1);
            v2.setTeacher(teacher);
            v2.setStatus("PUBLISHED");
            v2.setStartTime(LocalDateTime.now().minusDays(2));
            v2.setEndTime(LocalDateTime.now().plusDays(1));
            vivaRepository.save(v2);

            VivaAttempt attempt1 = new VivaAttempt();
            attempt1.setStudent(student);
            attempt1.setViva(v1);
            attempt1.setStatus("SUBMITTED");
            attempt1.setPercentage(90.0);
            attempt1.setScore(9.0);
            attempt1.setSubmittedAt(LocalDateTime.now().minusHours(2));
            attemptRepository.save(attempt1);

            VivaAttempt attempt2 = new VivaAttempt();
            attempt2.setStudent(student);
            attempt2.setViva(v2);
            attempt2.setStatus("SUBMITTED");
            attempt2.setPercentage(96.7);
            attempt2.setScore(29.0);
            attempt2.setSubmittedAt(LocalDateTime.now().minusDays(1));
            attemptRepository.save(attempt2);
        }
    }
}
