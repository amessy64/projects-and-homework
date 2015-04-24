import java.util.Scanner;
public class NewCalculator {
	//will size arrays for any string the user inputs
	public static int arraySize(String str){
		int count = 0;
		for (int i = 0; i<str.length();i++) {
			if (!(str.charAt(i)>='0' && str.charAt(i)<='9' || str.charAt(i) == '.')){
			count++;			
			}
		}
		return count;
	}
	//this is a method which returns void and takes as argument an array of doubles and an array of chars representing operators, the method
	//bedmas calculates the string the user entered as input in the order of operations, this method is unfinished but it works. The main problem
	//being it prints out the right answer then prints out more answers after the right answer
	//also this method is recursive, read below for more information
	public static void bedmas(double[] foo, char[] bar) {
		double result = 0;
		double count = 0;
		//this will check wether there are multiplications or divisions remaining in the calculation
		for (int i = 0; i<bar.length; i++) {
			if (bar[i] == '*' || bar[i] == '/') {
				count++;
			}
		}
		//this will do the final computation where there are only additions and substractions left from left to right
		if (count == 0) {
			for (int i = 0; i<bar.length; i++) {
				int j = i+1;
				if (bar[i] == '+' && i == 0) {
					result = foo[i]+foo[j];
				} else {
					if (bar[i] == '-' && i == 0) {
						result = foo[i] - foo[j];
					}
				}
				if (bar[i] == '+' && i != 0) {
					result += foo[j];
				} else {
					if (bar[i] =='-' && i != 0) {
						result -= foo[j];
					}
				}
			}
			System.out.println(result);
			System.exit(0);
		//this looks for the first multiplication or division in the array, computes it and creates two new arrays containing the new number as well as
		//any remaing numbers in the first array and the chars leftover in the second array all in the proper order. These new arrays are
		//one size shorter than the input arrays and are sent back to bedmas for another round of modifications until there no longer are any multiplications
		//or divisions to operate
		} else {
			char[] newOperators = new char[bar.length-1];
			double[] newOperands = new double[bar.length];
			
			for (int i = 0; i<bar.length; i++) {
				if (bar[i] == '+' || bar[i] == '-') {
					newOperators[i] = bar[i];
					newOperands[i] = foo[i];
				} else {
					if (bar[i] == '*') {
						newOperands[i] = foo[i] * foo[i+1];
						for (int j = i+1; j<newOperands.length; j++) {
							newOperands[j] = foo[j+1]; 
							newOperators[j-1] = bar[j];
						}
						bedmas(newOperands, newOperators);
					}
					if (bar[i] == '/') {
						newOperands[i] = foo[i] / foo[i+1];
						for (int j = i+1; j<newOperands.length; j++) {
							newOperands[j] = foo[j+1]; 
							newOperators[j-1] = bar[j];
						}
						bedmas(newOperands, newOperators);
					}
				}
			}
		}
	}
	//parses any string of operands and operators into an array of doubles and chars
	public static void main (String[] args) {
		System.out.println("Eat my shorts Pascal!");
		Scanner keyboard = new Scanner(System.in);
		System.out.println("Enter a calculation:");
		String userInput = keyboard.nextLine();
		//calls arraySize method to properly size the arrays
		char[] operator = new char[arraySize(userInput)];
		double[] doubles = new double[arraySize(userInput)+1];
		//parses a string by concatenating the numerals in between characters, and converting and adding the resulting string to an array of doubles, adding
		//the character to an array of characters and making the container string null so that it may take a new set of numbers
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
		//calls the bedmas method
		bedmas(doubles, operator);
	}
}
