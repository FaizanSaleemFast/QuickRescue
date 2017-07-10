package com.globalrescue.QuickRescue;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import com.globalrescue.QuickRescue.entities.Account;
import com.globalrescue.QuickRescue.entities.Contact;

public class HibernateUtils {

	private static SessionFactory factory;
	private static HibernateUtils utils;


    public static SessionFactory getFactory() {
		return factory;
	}

	public static void setFactory(SessionFactory factory) {
		HibernateUtils.factory = factory;
	}
	
	public HibernateUtils() {
        try {
        	factory = new Configuration().configure().buildSessionFactory();
        	utils = new HibernateUtils();
        }
        catch(Throwable e) {
        	System.err.println("Failed to create criteriaFactory object." + e);
        	throw new ExceptionInInitializerError(e);
        }
	}

	/*
	public static void main( String[] args )
    {
        System.err.println( "Quick Rescue - Assignment 1" );
        
        utils = new HibernateUtils();
        
        System.out.println(System.getProperty("user.dir"));
        
//        Integer acc1 = MA.addAccount(new Account("Faizan", "gmail.com", "Islamabad"));
//        Integer acc2 = MA.addAccount(new Account("Ali", "yahoo.com", "Lahore"));
//        Integer acc3 = MA.addAccount(new Account("Usman", "rocketmail.com", "Karachi"));
//        Integer acc4 = MA.addAccount(new Account("John", "web.com", "Taxila"));
//        Integer acc5 = MA.addAccount(new Account("Doe", "www.com", "Wah Cantt"));
//       
//        Account ac6 = new Account("Sarah", "test.com", "Boston");
//        Contact con6 = new Contact("FaisalNewest", "Shahzad" , "faisal@shahzad.com", 'M', "0900-78601", 1, "A-6", "Taxila", "Punjab", "Pakistan", ac6);
//        ac6.getContacts().add(con6);
//        Integer acc6 = MA.addAccount(ac6);
        
        
//        MC.addContact(factory, con6);
        
        System.err.println("LISTING ALL THE ACCOUNTS");
        utils.viewAccounts();
        
        System.err.println("\nLISTING ALL THE CONTACTS");
        utils.viewAllContacts();

//        System.err.println("Adding a contact to an account");
//        Account accountToUpdate = utils.findAccountByName("Sarah");
//        Contact newContactToAdd = new Contact("NewContact", "" , "faisal@shahzad.com", 'M', "0900-78601", 1, "A-6", "Taxila", "Punjab", "Pakistan");
//        utils.addContact(accountToUpdate, newContactToAdd);
        
//        Contact contact = utils.findAccountByName("Sarah").getContacts().iterator().next();
//        utils.deleteContact(contact);
        

//        utils.deleteAccount("Sarah");

        
        System.err.println("\nUpdating Contact");
        Account account = utils.findAccountByName("Sarah");
        Contact contactToUpdate = (Contact)account.getContacts().toArray()[0];
        Contact updatedContact = new Contact();
        utils.copyContact(updatedContact, contactToUpdate);
        
        updatedContact.setFirstName("Privy");
        
//        utils.updateContact(contactToUpdate, updatedContact);
        
        System.err.println("\nLISTING ALL THE ACCOUNTS");
        utils.viewAccounts();
        
        System.err.println("\nLISTING ALL THE CONTACTS");
        utils.viewAllContacts();
        
        
        
        System.err.println("\nLISTING CONTACTS OF AN ACCOUNT");
        utils.viewContactOfAccount(account);
    }
	*/
	
	
	
	
	/*
	 * ###################################################
	 * Functions related to Account Management
	 * ###################################################
	 */
	
    /*
     * Function to add account using provided factory and account objects.
     */
    public Integer addAccount(SessionFactory factory, Account account) {
    	Session session = factory.openSession();
    	Transaction tx = null;
    	Integer accountId = null;

    	try {
    		tx = session.beginTransaction();
    		
    		Account checkAccount = (Account) session.load(Account.class, account.getAccountId());    		
    		
    		if(checkAccount == null)
    			accountId = (Integer) session.save(account);
    		tx.commit();
    	}
    	catch(HibernateException e) {
    		if(tx != null)
    			tx.rollback();
    	}
    	finally {
    		session.close();
    	}
    	
    	return accountId;
    }

    
    /*
     * A helper function to add account using only the account object.
     */
    public Integer addAccount(Account account) {
    	return addAccount(factory,account);
    }
    
    
   
    /*
     * Function to get a account object by given name and using provided factory object.
     */
    public Account findAccountByName(SessionFactory factory, String accountName) {
    	Session session = factory.openSession();
    	Transaction tx = null;
    
    	Account accountToReturn = null;
    	
    	try {
    		
    		tx = session.beginTransaction();
    		CriteriaBuilder crBuilder = session.getCriteriaBuilder();
    		CriteriaQuery<Account> criteria = crBuilder.createQuery(Account.class);
    		
    		Root<Account> accountRoot = criteria.from(Account.class);
    		criteria.select(accountRoot);
    		
    		criteria.where(crBuilder.equal(accountRoot.get("name"), accountName));
    		
    		accountToReturn = session.createQuery(criteria).getSingleResult();
    		
    		tx.commit();
    		return accountToReturn;
    	}
    	catch(HibernateException e) {
    		
    		System.err.println(e.getMessage());
    		
    		
    		if (tx != null)
    			tx.rollback();
    	}
    	finally {
    		session.close();
    		return accountToReturn;
    	}
    }
    
    /*
     * A helper function to find contact by name.
     */
    
    public Account findAccountByName(String accountName) {
    	return findAccountByName(factory, accountName);
    }
    
    
    /*
     * Function to find an account by id
     */
    
    public Account findAccountById(SessionFactory factory, Integer accountId) {
    	
    	Session session = factory.openSession();
    	Transaction tx = null;
    	Account accountToFind = null;
    	try {
    		tx = session.beginTransaction();
    		CriteriaBuilder crBuilder = session.getCriteriaBuilder();
    		CriteriaQuery<Account> criteria = crBuilder.createQuery(Account.class);
    		
    		Root<Account> accountRoot = criteria.from(Account.class);
    		criteria.select(accountRoot);
    		
    		criteria.where(crBuilder.equal(accountRoot.get("accountId"), accountId));
    		
    		
    		
    		List<Account> accountList = session.createQuery(criteria).getResultList();
    		
    			accountToFind = accountList.get(0);
    		
//			System.out.println(accountToDelete.getAccountId() + " " + accountToDelete.getName() + " " + accountToDelete.getEmailDomain() + " " + accountToDelete.getTimeZoneCity());
//    		session.delete(accountToDelete);
    		tx.commit();
    	}
    	catch(HibernateException e) {
    		System.err.println(e.getLocalizedMessage());
    		
    		if (tx != null)
    			tx.rollback();
    	}
    	finally {
    		session.close();
    	}
    	return accountToFind;
    }
    
    
    public Account findAccountById(Integer accountId) {
    	return findAccountById(factory, accountId);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    /*
     * A function to view all the accounts on record using provided factory object.
     */
    
    public void viewAccounts(SessionFactory factory) {
    	Session session = factory.openSession();
    	Transaction tx = null;
    	
    	try {
    		tx = session.beginTransaction();
    		CriteriaBuilder crBuilder = session.getCriteriaBuilder();
    		CriteriaQuery<Account> criteria = crBuilder.createQuery(Account.class);
    		
    		Root<Account> accountRoot = criteria.from(Account.class);
    		criteria.select(accountRoot);
    		
    		List<Account> accountList = session.createQuery(criteria).getResultList();
    		
    		for(Account acc : accountList) {
    			System.out.println(acc.getAccountId() + " " + acc.getName() + " " + acc.getEmailDomain() + " " + acc.getTimeZoneCity());
    		}
    		
    		tx.commit();
    	}
    	catch(HibernateException e) {
    		if (tx != null)
    			tx.rollback();
    	}
    	finally {
    		session.close();
    	}
    }
    
    /*
     * A function to view all the accounts on record.
     */
    
    public void viewAccounts() {
    	viewAccounts(factory);
    }
    
    
    /*
     * Function to delete account by given account name and using provided factory object.
     */
    
	 public void deleteAccount(SessionFactory factory, Integer accountId) {
	    	
	    	Session session = factory.openSession();
	    	Transaction tx = null;
	    	
	    	try {
	    		tx = session.beginTransaction();
	    		CriteriaBuilder crBuilder = session.getCriteriaBuilder();
	    		CriteriaQuery<Account> criteria = crBuilder.createQuery(Account.class);
	    		
	    		Root<Account> accountRoot = criteria.from(Account.class);
	    		criteria.select(accountRoot);
	    		
	    		criteria.where(crBuilder.equal(accountRoot.get("accountId"), accountId));
	    		
	    		Account accountToDelete = session.createQuery(criteria).getResultList().get(0);
	    		
				System.out.println(accountToDelete.getAccountId() + " " + accountToDelete.getName() + " " + accountToDelete.getEmailDomain() + " " + accountToDelete.getTimeZoneCity());
	    		session.delete(accountToDelete);
	    		tx.commit();
	    	}
	    	catch(HibernateException e) {
	    		
	    		
	    		System.err.println(e.getLocalizedMessage());
	    		
	    		if (tx != null)
	    			tx.rollback();
	    	}
	    	finally {
	    		session.close();
	    	}
	    	
	    }
    
    /*
     * Function to delete account by given account name and using provided factory object.
     */
    
	 public void deleteAccount(SessionFactory factory, String accountName) {
	    	
	    	Session session = factory.openSession();
	    	Transaction tx = null;
	    	
	    	try {
	    		tx = session.beginTransaction();
	    		CriteriaBuilder crBuilder = session.getCriteriaBuilder();
	    		CriteriaQuery<Account> criteria = crBuilder.createQuery(Account.class);
	    		
	    		Root<Account> accountRoot = criteria.from(Account.class);
	    		criteria.select(accountRoot);
	    		
	    		criteria.where(crBuilder.equal(accountRoot.get("name"), accountName));
	    		
	    		Account accountToDelete = session.createQuery(criteria).getResultList().get(0);
	    		
				System.out.println(accountToDelete.getAccountId() + " " + accountToDelete.getName() + " " + accountToDelete.getEmailDomain() + " " + accountToDelete.getTimeZoneCity());
	    		session.delete(accountToDelete);
	    		tx.commit();
	    	}
	    	catch(HibernateException e) {
	    		
	    		
	    		System.err.println(e.getLocalizedMessage());
	    		
	    		if (tx != null)
	    			tx.rollback();
	    	}
	    	finally {
	    		session.close();
	    	}
	    	
	    }
    
 /*
  * A function to delete account by using give account name.
  */
 
    public void deleteAccount(String accountName) {
    	deleteAccount(factory, accountName);
    }
    
    public void deleteAccount(Integer accountId) {
    	deleteAccount(factory, accountId);
    }
    
    
    /*
     * A function update/edit an account with all the updated information in a new account object
     */
    
    public void updateAccount(Account accountToUpdate) {
    	
    	Session session = factory.openSession();
    	Transaction tx = null;
    	
    	try {
    		tx = session.beginTransaction();
    		CriteriaBuilder crBuilder = session.getCriteriaBuilder();
    		CriteriaQuery<Account> criteria = crBuilder.createQuery(Account.class);
    		
    		Root<Account> accountRoot = criteria.from(Account.class);
    		criteria.select(accountRoot);
    		
    		session.saveOrUpdate(accountToUpdate);
    		
    		tx.commit();
    	}
    	catch(HibernateException e) {
    		
    		System.out.println(e.getMessage());
    		
    		if (tx != null)
    			tx.rollback();
    	}
    	finally {
    		session.close();
    	}
    }
    
    
    /*
     * A function to for deep copying from one account object to another.
     */
    public void copyAccount(Account sourceAccount, Account destinationAccount) {
    	
    	
    	destinationAccount.setName(sourceAccount.getName());
    	destinationAccount.setEmailDomain(sourceAccount.getEmailDomain());
    	destinationAccount.setTimeZoneCity(sourceAccount.getTimeZoneCity());
    	destinationAccount.setContacts(sourceAccount.getContacts());
    }
    
    
	/*
	 * ###################################################
	 * Functions related to Contact Management
	 * ###################################################
	 */
    
    /*
     * Function to add a contact to an account.
     */
    
    public Integer addContact(Account account, Contact contact) {
    	Session session = factory.openSession();
    	Transaction tx = null;
    	Integer contactId = null;

    	try {
    		tx = session.beginTransaction();
    		
    		contact.setAccount(account);
    		account.getContacts().add(contact);
    		session.saveOrUpdate(account);
    		
//    		contactId = (Integer) session.save(account);
    		tx.commit();
    	}
    	catch(HibernateException e) {
    		
    		
    		System.err.println(e.getLocalizedMessage());
    		
    		if(tx != null)
    			tx.rollback();
    	}
    	finally {
    		session.close();
    	}
    	
    	return contactId;
    }
    
    
    
    
    
    
    
    
    
    
    
    /*
     * Function to get a account object by given name and using provided factory object.
     */
    public Contact findContactByName(SessionFactory factory, String contactFirstName, String contactLastName) {
    	Session session = factory.openSession();
    	Transaction tx = null;
    
    	Contact contactToReturn = null;
    	
    	try {
    		
    		tx = session.beginTransaction();
    		CriteriaBuilder crBuilder = session.getCriteriaBuilder();
    		CriteriaQuery<Contact> criteria = crBuilder.createQuery(Contact.class);
    		
    		Root<Contact> contactRoot = criteria.from(Contact.class);
    		criteria.select(contactRoot);
    		
    		
    		Predicate nameRestriction = crBuilder.and(
    				crBuilder.equal( contactRoot.get("firstName"), contactFirstName ),
    				crBuilder.equal( contactRoot.get("lastName"), contactLastName )
    			);
    		
    		criteria.where(nameRestriction);
    		
    		contactToReturn = session.createQuery(criteria).getSingleResult();
    		
    		tx.commit();
    		
    	}
    	catch(HibernateException e) {
    		
    		System.err.println(e.getMessage());
    		
    		if (tx != null)
    			tx.rollback();
    	}
    	finally {
    		session.close();
    		return contactToReturn;
    	}
    	
    	
    }
    
    /*
     * A helper function to find contact by name.
     */
    
    public Contact findContactByName(String contactFirstName, String contactLastName) {
    	return findContactByName(factory, contactFirstName, contactLastName);
    }
    
    
    /*
     * Function to find an account by id
     */
    
    public Contact findContactById(SessionFactory factory, Integer contactId) {
    	
    	Session session = factory.openSession();
    	Transaction tx = null;
    	Contact contactToFind = null;
    	try {
    		tx = session.beginTransaction();
    		CriteriaBuilder crBuilder = session.getCriteriaBuilder();
    		CriteriaQuery<Contact> criteria = crBuilder.createQuery(Contact.class);
    		
    		Root<Contact> accountRoot = criteria.from(Contact.class);
    		criteria.select(accountRoot);
    		
    		criteria.where(crBuilder.equal(accountRoot.get("accountId"), contactId));
    		
    		
    		
    		List<Contact> accountList = session.createQuery(criteria).getResultList();
    		
    		contactToFind = accountList.get(0);
    		
//			System.out.println(accountToDelete.getAccountId() + " " + accountToDelete.getName() + " " + accountToDelete.getEmailDomain() + " " + accountToDelete.getTimeZoneCity());
//    		session.delete(accountToDelete);
    		tx.commit();
    	}
    	catch(HibernateException e) {
    		System.err.println(e.getLocalizedMessage());
    		
    		if (tx != null)
    			tx.rollback();
    	}
    	finally {
    		session.close();
    	}
    	return contactToFind;
    }
    
    
    public Contact findContactById(Integer contactId) {
    	return findContactById(factory, contactId);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /*
     * A function to view all the contacts of an account.
     */
    
    public void viewContactOfAccount(Account account) {
    	Session session = factory.openSession();
    	EntityManager entityManager = session.getEntityManagerFactory().createEntityManager();
    	Transaction tx = null;
    	
    	try {
    		tx = session.beginTransaction();
    		
    		
    		CriteriaBuilder crBuilder = session.getCriteriaBuilder();
    		CriteriaQuery<Contact> criteria = crBuilder.createQuery(Contact.class);
    		
    		Root<Contact> contactRoot = criteria.from(Contact.class);
    		criteria.select(contactRoot);
    		
    		Integer accountId = utils.findAccountByName(account.getName()).getAccountId();
    		Account pAccount = utils.findAccountByName(account.getName());
//    		criteria.where(crBuilder.equal(contactRoot.get(Contact.class.getDeclaredField("accountId").getName()), accountId));

    		criteria.where(crBuilder.equal(contactRoot.get("account"), pAccount));
    		
    		List<Contact> contactList = session.createQuery(criteria).getResultList();
    		
    		
    		/*
    		// Retrieving contacts of an account using native sql. 
    		Integer accountId = utils.findAccountByName(account.getName()).getAccountId();
    		
    		String sql = "from Contact where accountId=:accountId";
    		entityManager.getTransaction().begin();
    		javax.persistence.Query query = entityManager.createQuery(sql);
    		query.setParameter("accountId", accountId);
    		List<Contact> contactList = query.getResultList();
    		*/
    		
    		for(Contact contact : contactList) {
    			
    			System.out.println(contact.getContactId() + " " + contact.getFirstName() + " " + contact.getLastName()  + " " + contact.getEmailAddress()  + " " + contact.getGender()  + " " + contact.getPhone()  + " " + contact.getStatus()  + " " + contact.getStreetAddress() + " " + contact.getCity()  + " " + contact.getState()  + " " + contact.getCountry() + " " + contact.getAccount().getName());
    		}
    		
//    		entityManager.getTransaction().commit();
    		
    		tx.commit();
    		
    	}
    	catch(HibernateException e) {
    		if (tx != null)
    			tx.rollback();
    	} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	finally {
    		session.close();
    		entityManager.close();
    	}
    }
    
    
    
    /*
     * A function to view all the contacts on record.
     */
    
    public void viewAllContacts() {
    	Session session = factory.openSession();
    	Transaction tx = null;
    	
    	try {
    		tx = session.beginTransaction();
    		CriteriaBuilder crBuilder = session.getCriteriaBuilder();
    		CriteriaQuery<Contact> criteria = crBuilder.createQuery(Contact.class);
    		
    		Root<Contact> contactRoot = criteria.from(Contact.class);
    		criteria.select(contactRoot);
    		
    		Query<Contact> query = session.createQuery(criteria);
    		
    		List<Contact> contactList = session.createQuery(criteria).getResultList();
    		
    		for(Contact contact : contactList) {
    			System.out.println(contact.getContactId() + " " + contact.getFirstName() + " " + contact.getLastName()  + " " + contact.getEmailAddress()  + " " + contact.getGender()  + " " + contact.getPhone()  + " " + contact.getStatus()  + " " + contact.getStreetAddress() + " " + contact.getCity()  + " " + contact.getState()  + " " + contact.getCountry() + " " + contact.getAccount().getName());
    		}
    		
    		tx.commit();
    	}
    	catch(HibernateException e) {
    		if (tx != null)
    			tx.rollback();
    	}
    	finally {
    		session.close();
    	}
    }
    
    /*
     * A function to delete an account using provided contact object.
     */    
    
 public void deleteContact(Contact contact) {
    	
	Session session = factory.openSession();
 	Transaction tx = null;
 	Integer contactId = null;

 	try {
 		tx = session.beginTransaction();

 		session.remove(contact);
 		
 		tx.commit();
 	}
 	catch(HibernateException e) {
 		
 		System.err.println(e.getLocalizedMessage());
 		
 		if(tx != null)
 			tx.rollback();
 	}
 	finally {
 		session.close();
 	}
    	
    }
 
 
 /*
  * A function update/edit a contact.
  */
 
 public void updateContact(Contact contactToUpdate) {
 	
 	Session session = factory.openSession();
 	Transaction tx = null;
 	
 	try {
 		tx = session.beginTransaction();
 		CriteriaBuilder crBuilder = session.getCriteriaBuilder();
 		CriteriaQuery<Contact> criteria = crBuilder.createQuery(Contact.class);
 		
 		Root<Contact> accountRoot = criteria.from(Contact.class);
 		criteria.select(accountRoot);
 		
// 		Contact fetchedContact = session.get(Contact.class, contactToUpdate.getContactId());
// 		copyContact(fetchedContact, updatedcontact);
 		
 		session.update(contactToUpdate);
 		
 		tx.commit();
 	}
 	catch(HibernateException e) {
 		
 		System.out.println(e.getMessage());
 		
 		if (tx != null)
 			tx.rollback();
 	}
 	finally {
 		session.close();
 	}
 }
 
 
 /*
  * A function to for deep copying from one contact object to another.
  */
 public void copyContact(Contact sourceContact, Contact destinationContact) {
	 
	 destinationContact.setFirstName(sourceContact.getFirstName());
	 destinationContact.setLastName(sourceContact.getLastName());
	 destinationContact.setEmailAddress(sourceContact.getEmailAddress());
	 destinationContact.setStatus(sourceContact.getStatus());
	 destinationContact.setGender(sourceContact.getGender());
	 destinationContact.setPhone(sourceContact.getPhone());
	 destinationContact.setCity(sourceContact.getCity());
	 destinationContact.setStreetAddress(sourceContact.getStreetAddress());
	 destinationContact.setState(sourceContact.getState());
	 destinationContact.setCountry(sourceContact.getCountry());
	 destinationContact.setAccount(destinationContact.getAccount());
 }
 
 
 
 
 
 
 
 
 
 
 
	
	
}
