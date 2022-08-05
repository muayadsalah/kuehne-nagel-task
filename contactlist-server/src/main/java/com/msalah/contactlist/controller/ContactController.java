package com.msalah.contactlist.controller;

import com.msalah.contactlist.domain.Contact;
import com.msalah.contactlist.service.ContactService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v0/contacts")
@AllArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @GetMapping
    public Page<Contact> getAllContacts(@RequestParam(required = false, name = "q") String query,
                                        Pageable pageable) {
        return contactService.getContacts(query, pageable);
    }
}
