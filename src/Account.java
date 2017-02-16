

public class Account {

	double balance;
	double interestRate;
	
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
	
	void deposit(double amount) {
		
			balance = balance + amount;
	
	}
	void addInterest() {
		deposit(balance * interestRate / 100);		
	}
}
