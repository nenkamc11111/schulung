package com.dariawan.contactapp.service;

import static org.junit.jupiter.api.Assertions.*;
import com.dariawan.contactapp.domain.Contact;
import com.dariawan.contactapp.exception.ResourceNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
public class ContactServiceJPATest {

    @Autowired 
    private DataSource dataSource;
    
    @Autowired 
    private ContactService contactService;
    
   
    @Test
    public void testSaveUpdateDeleteContact() throws Exception{
        Contact c = new Contact();
        c.setName("Portgas D. Ace");
        c.setPhone("09012345678");
        c.setEmail("ace@whitebeard.com");
        c.setAddress1("Essen");
        
        contactService.save(c);
        assertNotNull(c.getId());
        
        Contact findContact = contactService.findById(c.getId());
        assertEquals("Portgas D. Ace", findContact.getName());
        assertEquals("ace@whitebeard.com", findContact.getEmail());
        
         //update record
        c.setEmail("cedric@whitebeardpirat.es");
        contactService.update(c);
        
        // test after update
        //findContact = contactService.findById(c.getId());
        //assertEquals("ace@whitebeardpirat.es", findContact.getEmail());
        
        // test delete
        //contactService.deleteById(c.getId());
        
        // query after delete
        //exceptionRule.expect(ResourceNotFoundException.class);
        //contactService.findById(c.getId());
    }    
}
