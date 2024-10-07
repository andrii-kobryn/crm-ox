package com.akobryn.crm.controllers;

import com.akobryn.crm.constants.TaskStatus;
import com.akobryn.crm.dto.TaskDTO;
import com.akobryn.crm.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/create/{contactId}")
    public String createTaskForm(@PathVariable Long contactId, Model model) {
        model.addAttribute("contactId", contactId);
        model.addAttribute("task", new TaskDTO());
        model.addAttribute("statuses", TaskStatus.values());
        return "createTask";
    }

    @PostMapping
    public String createTask(@ModelAttribute TaskDTO taskDTO) {
        taskService.createTask(taskDTO);
        return "redirect:/contacts/" + taskDTO.getContactId();
    }

    @PostMapping("/update-contact")
    public String updateTaskContact(@RequestParam Long taskId, @RequestParam Long contactId) {
        Long oldContactId = taskService.findTaskById(taskId).getContactId();
        taskService.setContactId(taskId, contactId);
        return "redirect:/contacts/" + oldContactId;
    }

    @PostMapping("/update-status")
    public String updateTaskStatus(@RequestParam Long taskId, @RequestParam String status) {
        Long contactId = taskService.findTaskById(taskId).getContactId();
        taskService.updateTaskStatus(taskId, TaskStatus.valueOf(status));
        return "redirect:/contacts/" + contactId;
    }

    @PostMapping("/delete/{taskId}")
    public String deleteTask(@PathVariable Long taskId) {
        Long contactId = taskService.findTaskById(taskId).getContactId();
        taskService.deleteTask(taskId);
        return "redirect:/contacts/" + contactId;
    }
}
