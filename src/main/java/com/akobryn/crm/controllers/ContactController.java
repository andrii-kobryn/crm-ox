package com.akobryn.crm.controllers;

import com.akobryn.crm.constants.TaskStatus;
import com.akobryn.crm.dto.ContactDTO;
import com.akobryn.crm.dto.InteractionDTO;
import com.akobryn.crm.dto.TaskDTO;
import com.akobryn.crm.service.ContactService;
import com.akobryn.crm.service.InteractionService;
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

import java.util.List;

@Controller
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;
    private final TaskService taskService;
    private final InteractionService interactionService;

    @GetMapping("/create")
    public String showCreateContactForm(@RequestParam Long clientId, Model model) {
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setClientId(clientId);
        model.addAttribute("contact", contactDTO);
        model.addAttribute("clientId", clientId);
        return "createContact";
    }


    @PostMapping("/create")
    public String createContact(@ModelAttribute ContactDTO contactDTO) {
        contactService.createContact(contactDTO);
        return "redirect:/clients/" + contactDTO.getClientId();
    }

    @GetMapping("/{contactId}")
    public String getContactDetails(@PathVariable Long contactId, Model model) {
        ContactDTO contact = contactService.getContactById(contactId);
        List<TaskDTO> taskDTOS = taskService.getTasksByContactId(contactId);
        List<ContactDTO> allContacts = contactService.getAllContacts();
        List<InteractionDTO> interactions = interactionService.getInteractionsByContactIdAndClientId(contactId, contact.getClientId());
        model.addAttribute("contact", contact);
        model.addAttribute("tasks", taskDTOS);
        model.addAttribute("allContacts", allContacts);
        model.addAttribute("statuses", TaskStatus.values());
        model.addAttribute("interactions", interactions);
        return "contactDetails";
    }

    @GetMapping("/edit/{id}")
    public String editContact(@PathVariable Long id, Model model) {
        ContactDTO contactDTO = contactService.getContactById(id);
        model.addAttribute("contact", contactDTO);
        return "editContact";
    }

    @PostMapping("/edit/{id}")
    public String updateContact(@PathVariable Long id, @ModelAttribute ContactDTO contactDTO) {
        contactDTO.setId(id);
        contactService.updateContact(id, contactDTO);
        return "redirect:/contacts/" + id;
    }

    @PostMapping("/delete/{id}")
    public String deleteContact(@PathVariable Long id, @RequestParam Long clientId) {
        contactService.deleteContact(id);
        return "redirect:/clients/" + clientId;
    }
}
