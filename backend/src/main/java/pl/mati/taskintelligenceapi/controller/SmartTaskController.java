package pl.mati.taskintelligenceapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.mati.taskintelligenceapi.dto.TaskResponseDTO;
import pl.mati.taskintelligenceapi.service.SmartTaskService;

import java.security.Principal;

@RestController
@RequestMapping("/api/smart-tasks")
@RequiredArgsConstructor
public class SmartTaskController {

    private final SmartTaskService smartTaskService;

    @GetMapping("/smartTasks")
    public ResponseEntity<Page<TaskResponseDTO>> getAllSmartTasks(Principal principal, Pageable pageable){
        return ResponseEntity.ok(smartTaskService.getSmartTaskList(principal.getName(), pageable));
    }
}
