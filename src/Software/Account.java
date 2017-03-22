package Software;

public class Account {

	private double balance;
	private double interestRate;
	
	public String toString() {
		return String.format("[Account balance=%f interestRate=%f", balance, interestRate);
	}
	
	double getBalance() {
		return balance;
	}
	
	double getInterestRate() {
		return interestRate;
	}
	
	void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}
	
	// Error inserted, allows negative balance
	void deposit(double amount) {
		balance = balance + amount;
	}
	
	void addInterest() {
		deposit(balance * interestRate / 100);		
	}
	
}
