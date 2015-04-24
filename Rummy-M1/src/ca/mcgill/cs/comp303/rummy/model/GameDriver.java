package ca.mcgill.cs.comp303.rummy.model;

public class GameDriver
{
	/**
	 * Comment: this runs a command line version of the game with 2 ai players. There is no human player interaction,
	 * although such a feature could be integrated, the main version of the program will be a GUI
	 * 
	 * @param args arguments passed through the command line
	 */
    public static void main(String args[])
    {
    	GameEngine ge = new GameEngine();
    	Player nitel = new SmartAI("Nitel");
    	Player perry = new DumbAI("Perry");
    	Logger l = new ConsoleLogger();

    	ge.addPlayer(nitel);
    	ge.addPlayer(perry);
    	ge.addLogger(l);

    	ge.newGame();

    	ge.play();
    }
}
