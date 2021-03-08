package com.dariawan.contactapp.controller;

import com.dariawan.contactapp.domain.Address;
import com.dariawan.contactapp.domain.Contact;
import com.dariawan.contactapp.exception.BadResourceException;
import com.dariawan.contactapp.exception.ResourceAlreadyExistsException;
import com.dariawan.contactapp.exception.ResourceNotFoundException;
import com.dariawan.contactapp.service.ContactService;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ContactController {

	  private final Logger logger = LoggerFactory.getLogger(this.getClass());
	    
	    private final int ROW_PER_PAGE = 5;
	    
	    @Autowired
	    private ContactService contactService;
	    
	    @GetMapping(value = "/contacts", produces = MediaType.APPLICATION_JSON_VALUE)
	    public ResponseEntity<List<Contact>> findAll() {
	            return ResponseEntity.ok(contactService.findAll());  
	    }
	 
	    @GetMapping(value = "/contacts/{contactId}", produces = MediaType.APPLICATION_JSON_VALUE)
	    public ResponseEntity<Contact> findContactById(@PathVariable long contactId) {
	        try {
	            Contact book = contactService.findById(contactId);
	            return ResponseEntity.ok(book);  // return 200, with json body
	        } catch (ResourceNotFoundException ex) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // return 404, with null body
	        }
	    }
	    
	    @PostMapping(value = "/contacts")
	    public ResponseEntity<Contact> addContact(@RequestBody Contact contact) 
	            throws URISyntaxException {
	        try {
	            Contact newContact = contactService.save(contact);
	            return ResponseEntity.created(new URI("/api/contacts/" + newContact.getId()))
	                    .body(contact);
	        } catch (ResourceAlreadyExistsException ex) {
	            // log exception first, then return Conflict (409)
	            logger.error(ex.getMessage());
	            return ResponseEntity.status(HttpStatus.CONFLICT).build();
	        } catch (BadResourceException ex) {
	            // log exception first, then return Bad Request (400)
	            logger.error(ex.getMessage());
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	        }
	    }
	    
	    @PutMapping(value = "/contacts/{contactId}")
	    public ResponseEntity<Contact> updateContact(@RequestBody Contact contact, 
	            @PathVariable long contactId) {
	        try {
	            contact.setId(contactId);
	            contactService.update(contact);
	            return ResponseEntity.ok().build();
	        } catch (ResourceNotFoundException ex) {
	            // log exception first, then return Not Found (404)
	            logger.error(ex.getMessage());
	            return ResponseEntity.notFound().build();
	        } catch (BadResourceException ex) {
	            // log exception first, then return Bad Request (400)
	            logger.error(ex.getMessage());
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	        }
	    }
	    
		/*
		 * @PatchMapping("/contacts/{contactId}") public ResponseEntity<Void>
		 * updateAddress(@PathVariable long contactId,
		 * 
		 * @RequestBody Address address) { try { contactService.updateAddress(contactId,
		 * address); return ResponseEntity.ok().build(); } catch
		 * (ResourceNotFoundException ex) { // log exception first, then return Not
		 * Found (404) logger.error(ex.getMessage()); return
		 * ResponseEntity.notFound().build(); } }
		 */
	    
	    @DeleteMapping(path="/contacts/{contactId}")
	    public ResponseEntity<Void> deleteContactById(@PathVariable long contactId) {
	        try {
	            contactService.deleteById(contactId);
	            return ResponseEntity.ok().build();
	        } catch (ResourceNotFoundException ex) {
	            logger.error(ex.getMessage());
	            return ResponseEntity.notFound().build();
	        }
	    }
}
