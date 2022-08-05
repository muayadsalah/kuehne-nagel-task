package com.msalah.contactlist.service;

import com.msalah.contactlist.domain.Contact;
import com.msalah.contactlist.repository.ContactRepository;
import com.msalah.contactlist.utils.FileUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;

    @PostConstruct
    public void loadAllContacts() throws IOException {
        List<Contact> contactList = FileUtils.parseContactsListCSVFile("people.csv");
        contactRepository.saveAll(contactList);
    }

    public Page<Contact> getContacts(Pageable pageable) {
        return contactRepository.findAll(pageable);
    }

}
