//package VisibleForUser;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import Software.Hashtag;
//import junit.framework.TestCase;
//
//public class AccountTest extends TestCase {
//
//	private double epsilon = 0.000001d;
//	
//	private static Account account;
//	private static Hashtag hash = new Hashtag();
//	private final int assignment = 1; 
//	private final int exercise = 1; 
//	private static final ArrayList<String> FE = new ArrayList<String>(Arrays.asList("Failure", "Error")); 	
//	
//		
//	@Override
//	protected void setUp() throws Exception {
//		super.setUp();
//		account = new Account(100, 5);
//	}
//	
//	public void testAccount() {
//		account = new Account(100, 5);
//		assertEquals(100.0d, account.getBalance(), epsilon);
//		assertEquals(5.0d, account.getInterestRate(), epsilon);
//		try {
//			account = new Account(-1, 5);
//			hash.sendToDB(assignment, exercise,"valid state", FE.get(0)); 
//			fail("Creating Account with negative balance should throw an IllegalArgumentException.");
//		} catch (Exception e) {
//			if (!(e instanceof IllegalArgumentException)){
//				hash.sendToDB(assignment, exercise,"type", FE.get(0));
//			}
//			assertEquals(100.0d, account.getBalance(), epsilon);
//			assertEquals(5.0d, account.getInterestRate(), epsilon);	
//			assertTrue("Exception should be IllegalArgumentException.", e instanceof IllegalArgumentException);
//		}
//		
//		try {
//			account = new Account(100, -1);
//			hash.sendToDB(assignment, exercise,"valid state", FE.get(0)); 
//			fail("Creating Account with negative interestRate should throw an IllegalArgumentException.");
//		} catch (Exception e) {
//			if (!(e instanceof IllegalArgumentException)){
//				hash.sendToDB(assignment, exercise,"type", FE.get(0));
//			}
//			assertEquals(100.0d, account.getBalance(), epsilon);
//			assertEquals(5.0d, account.getInterestRate(), epsilon);
//			assertTrue("Exception should be IllegalArgumentException.", e instanceof IllegalArgumentException);
//		}
//	}
//	
//	public void testSetInterestRate() {
//		account.setInterestRate(7);
//		assertEquals(7.0d, account.getInterestRate(), epsilon);
//		
//		try {
//			account.setInterestRate(-2);
//			hash.sendToDB(assignment, exercise,"valid state", FE.get(0)); 
//			fail("Setting a negative interestRate should result throw exception.");
//		} catch (Exception e) {
//			assertEquals(7.0d, account.getInterestRate(), epsilon);
//		}
//	}
//	
//
//	public void testDeposit() {
//		account.deposit(100);
//		if (account.getBalance() != 200){
//			hash.sendToDB(assignment, exercise,"encapsulation", FE.get(0)); 
//		}
//		assertEquals(200.0d, account.getBalance(), epsilon);
//	}
//	
//	public void testDepositNegativeAmount() {
//		try {
//			account.deposit(-50);
//			hash.sendToDB(assignment, exercise,"encapsulation", FE.get(0)); 
//			fail("deposit should throw an IllegalArgumentException when given negative amounts.");
//		} catch (Exception e) {
//			if (!(e instanceof IllegalArgumentException)){
//				hash.sendToDB(assignment, exercise,"type", FE.get(0));
//			}
//			assertEquals("deposit should ignore negative amounts.", 100.0d, account.getBalance(), epsilon);
//			assertTrue("Exception should be IllegalArgumentException.", e instanceof IllegalArgumentException);
//		}
//	}
//	
//	public void testWithdraw() {
//		try {
//			account.withdraw(50);
//			assertEquals(50.0d, account.getBalance(), epsilon);
//		} catch (Exception e){
//			hash.sendToDB(assignment, exercise,"encapsulation", FE.get(1));
//			hash.sendToDB(assignment, exercise,"value types", FE.get(1));
//			fail();
//		}
//	}
//	
//	public void testWithdrawTooLargeAmount() {
//		try {
//			account.withdraw(150);
//			hash.sendToDB(assignment, exercise,"valid state", FE.get(0));
//			fail("Expected IllegalArgumentException here");
//		} catch (Exception e){
//			assertEquals(100.0d, account.getBalance(), epsilon);
//			assertTrue(e instanceof IllegalArgumentException);
//			if (!(e instanceof IllegalArgumentException)){
//				hash.sendToDB(assignment, exercise,"type", FE.get(0));
//			}
//		}
//	}
//}
