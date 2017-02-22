
public class TestClass {
	
	private String word;
	private int number;
	
	public TestClass(String word, int number){
		this.word = word;
		this.number = number;
	}
	
	public void setNumber (int number){
		this.number = number;
	}
	
	public int getNumber (){
		return number;
	}
	
	public void setWord(String word){
		this.word = word;
	}
	
	public String getWord(){
		return word;
	}
	
	public String toString() {
		return "Word: "+getWord()+"\nNumber: "+getNumber();
	}
	
	
	
	public static void main(String[] args) {
		TestClass tc1 = new TestClass("hei", 1);
		TestClass tc2 = new TestClass("hallo",2);
		System.out.println(tc1);
		System.out.println(tc2);
		
	}

}
