import java.util.Scanner;
public class Calculator {
	//adjusts the size of the arrays for any string the user inputs
	public static int arraySize(String str){
		int count = 0;
		for (int i = 0; i<str.length();i++) {
			if (!(str.charAt(i)>='0' && str.charAt(i)<='9' || str.charAt(i) == '.')){
			count++;			
			}
		}
		return count;
	}
	//note: this actually doesnt work because it won't handle operations which are more than 2 operands long, also doesn't do bedmas
	public static void operations(double[] foo, char[] bar) {
		double num1, num2;
		double result = 0;
		for (int i=0; i<foo.length-1; i++) {
			int j=i+1;
			num1 = foo[i];
			num2 = foo[j];
			
			Operations f = new Operations();
		
			if (bar[i] == '+') {
				result = f.add(num1, num2);
			}
			if (bar[i] == '-') {
				result = f.substract(num1, num2);
			}
			if (bar[i] == '*') {
				result = f.multiply(num1, num2);
			}
			if (bar[i] == '/') {
				result = f.divide(num1, num2);
			}
		}
		System.out.println(result);
	}
	//parses a string of operands and operators into two arrays, this is the only working part of the program!
	public static void main (String[] args) {
		System.out.println("Eat my shorts Pascal!");
		Scanner keyboard = new Scanner(System.in);
		System.out.println("Enter a calculation:");
		String userInput = keyboard.nextLine();
		
		char[] operator = new char[arraySize(userInput)];
		double[] doubles = new double[arraySize(userInput)+1];
		
		int j=0;
		String container = "";
		for (int i=0; i<userInput.length(); i++){
			if (userInput.charAt(i)>='0' && userInput.charAt(i)<='9' || userInput.charAt(i) == '.') {
				container += userInput.charAt(i); 
			} else {
				operator[j] = userInput.charAt(i);
				Double fromString = new Double(container);
				doubles[j] = fromString;
				j++;
				container = "";
			}
		}
		Double fromString = new Double(container);
		doubles[j] = fromString;	

		operations(doubles, operator);
	}
}
