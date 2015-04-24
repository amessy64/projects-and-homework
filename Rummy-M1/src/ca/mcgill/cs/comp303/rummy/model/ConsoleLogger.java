package ca.mcgill.cs.comp303.rummy.model;

public class ConsoleLogger implements Logger 
{
	@Override
	public void printStatement(String pString)
	{
		System.out.println(pString);
	}
	
}