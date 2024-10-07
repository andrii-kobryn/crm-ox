package com.akobryn.crm.controllers;

import com.akobryn.crm.dto.ClientDTO;
import com.akobryn.crm.dto.ContactDTO;
import com.akobryn.crm.dto.InteractionDTO;
import com.akobryn.crm.dto.TaskDTO;
import com.akobryn.crm.service.ClientService;
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
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final TaskService taskService;
    private final ClientService clientService;
    private final ContactService contactService;
    private final InteractionService interactionService;

    @GetMapping("/{clientId}")
    public String getClientDetails(@PathVariable Long clientId, Model model) {
        List<TaskDTO> tasks = taskService.getTasksByClientId(clientId);
        List<InteractionDTO> interactions = interactionService.getInteractionsByClientId(clientId);
        ClientDTO client = clientService.getClientById(clientId);
        List<ContactDTO> contacts = contactService.getContactsByClientId(clientId);

        model.addAttribute("client", client);
        model.addAttribute("tasks", tasks);
        model.addAttribute("interactions", interactions);
        model.addAttribute("contacts", contacts);

        return "clientDetails";
    }


    @GetMapping("/create")
    public String showCreateClientForm(Model model) {
        model.addAttribute("clientDTO", new ClientDTO());
        return "createClient";
    }

    @PostMapping("/create")
    public String createClient(@ModelAttribute ClientDTO clientDTO) {
        clientService.createClient(clientDTO);
        return "redirect:/clients";
    }

    @GetMapping
    public String getAllClientDetails(Model model) {
        List<ClientDTO> clientDTOS = clientService.getAllClients();
        model.addAttribute("clients", clientDTOS);
        return "clientList";
    }

    @GetMapping("/edit/{id}")
    public String editClient(@PathVariable Long id, Model model) {
        ClientDTO clientDTO = clientService.getClientById(id);
        model.addAttribute("client", clientDTO);
        return "editClient";
    }

    @PostMapping("/edit")
    public String updateClient(@RequestParam Long id, @ModelAttribute ClientDTO clientDTO) {
        clientDTO.setId(id);
        clientService.updateClient(id, clientDTO);
        return "redirect:/clients/" + id;
    }

    @PostMapping("/delete/{id}")
    public String deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return "redirect:/clients";
    }

    @GetMapping("/search")
    public String searchClients(
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) String industry,
            @RequestParam(required = false) String address,
            Model model) {

        List<ClientDTO> clients = clientService.searchClients(companyName, industry, address);
        model.addAttribute("clients", clients);
        model.addAttribute("companyName", companyName);
        model.addAttribute("industry", industry);
        model.addAttribute("address", address);
        return "searchClients";
    }

    @GetMapping("/{clientId}/contacts/search")
    public String searchContacts(@PathVariable Long clientId,
                                 @RequestParam(required = false) String firstName,
                                 @RequestParam(required = false) String lastName,
                                 @RequestParam(required = false) String email,
                                 Model model) {
        List<ContactDTO> contacts = contactService.searchContactsByClientId(clientId, firstName, lastName, email);
        ClientDTO client = clientService.getClientById(clientId);
        List<TaskDTO> tasks = taskService.getTasksByClientId(clientId);
        List<InteractionDTO> interactions = interactionService.getInteractionsByClientId(clientId);

        model.addAttribute("client", client);
        model.addAttribute("contacts", contacts);
        model.addAttribute("tasks", tasks);
        model.addAttribute("interactions", interactions);
        model.addAttribute("firstName", firstName);
        model.addAttribute("lastName", lastName);
        model.addAttribute("email", email);

        return "clientDetails";
    }
}

