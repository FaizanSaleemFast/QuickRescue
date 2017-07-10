package com.globalrescue.QuickRescue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.globalrescue.QuickRescue.entities.Account;
import com.globalrescue.QuickRescue.entities.Contact;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HibernateUtilsTest {
	HibernateUtils utils;
	
	
	@Before
	public void setup() {
		utils = new HibernateUtils();
	}
	
	@Test
	public void test1_AddAccount() {
        Integer acc1 = utils.addAccount(new Account("Faizan", "gmail.com", "Islamabad"));
        assertNotNull(acc1);
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void test2_DeleteAccount() {
        Integer acc1 = utils.addAccount(new Account("Faizan1", "gmail.com", "Islamabad"));
        
        utils.deleteAccount(acc1);
        Account account = utils.findAccountById(acc1);
        assertNull(account);
	}
	
	@Test
	public void test3_UpdateAccount() {
	  System.err.println("\nUpdating Account");
		  
	  Account accountToUpdate = utils.findAccountByName("Faizan");
      
	  assertNotNull(accountToUpdate);
	  
      // Updating some parameters' value new account object.
      accountToUpdate.setName("Ali Raza");
        
      utils.updateAccount(accountToUpdate);
      
      assertEquals("Ali Raza", accountToUpdate.getName());
      
	}
	
	
	@Test
	public void test4_UpdateAccountByAddingContact() {
	  System.err.println("\nUpdating Contact By Adding Contact");
		  
	  Account accountToUpdate = utils.findAccountByName("Ali Raza");
      
      Contact con = new Contact("Ali Raza", "Contact1" , "ali@gmail.com", 'M', "0900-78601", 1, "A-6", "Taxila", "Punjab", "Pakistan");
      con.setAccount(accountToUpdate);
      accountToUpdate.getContacts().add(con);
      
      utils.updateAccount(accountToUpdate);
      
      Contact con1 = utils.findContactByName("Ali Raza", "Contact1");
      
      assertNotNull(con1);
      
	}
	
	
	@Test
	public void test5_AddContact() {
		Account account = utils.findAccountByName("Ali Raza");
		Contact con = new Contact("John", "Contact1" , "johnd@yahoo.com", 'M', "0900-78601", 1, "A-6", "Taxila", "Punjab", "Pakistan");
		
		utils.addContact(account, con);
		
		Contact con1 = utils.findContactByName("John", "Contact1");
		assertNotNull(con1);
	}
	
	@Test
	public void testDeleteContact() {
		
		Contact con1 = utils.findContactByName("Mughal", "Mughal");
		utils.deleteContact(con1);
		
		
		con1 = utils.findContactByName("Mughal", "Mughal");
		assertNull(con1);
	}
	
	@Test
	public void test6_UpdateContact() {
		
		System.err.println("\nUpdating Contact");
		  
		  Contact contactToUpdate = utils.findContactByName("John", "Contact1");
	      
	      // Updating some parameters' value new account object.
	      contactToUpdate.setFirstName("Mughal");
	      contactToUpdate.setLastName("Faisal");
	      
	      utils.updateContact(contactToUpdate);
	      
	      assertEquals("Mughal", contactToUpdate.getFirstName());
	      assertEquals("Faisal", contactToUpdate.getLastName());
	}
	
	

}
