package ie.atu.sw;

import java.util.Scanner;

/**
 * Runner has the main here the user is given the menu and input options to enter Text file Directory
 * Dictionary, Common words, Output file and to execute and exit.
 * 
 * @author Rohan Sikder
 *
 */

public class Runner {

	public static void main(String[] args) throws Exception {
		// You should put the following code into a menu or Menu class
		System.out.println(ConsoleColour.YELLOW);
		System.out.println("************************************************************");
		System.out.println("*       ATU - Dept. Computer Science & Applied Physics     *");
		System.out.println("*                                                          *");
		System.out.println("*              Virtual Threaded Text Indexer               *");
		System.out.println("*                                                          *");
		System.out.println("*                 Rohan Sikder - G00389052                 *");
		System.out.println("*                                                          *");
		System.out.println("************************************************************");
		System.out.println("(1) Specify Text File");
		System.out.println("(2) Configure Dictionary - (Not Required - Dictionary provided)");
		System.out.println("(3) Configure Common Words - (Not Required - Google Top 1000 Word's provided)");
		System.out.println("(4) Specify Output File - (Not Required - Default will be output.txt)");
		System.out.println("(5) Execute");
		System.out.println("(6) Quit");

		// Variables
		int choice;
		String fileDir = "";
		String dictionary = "./Dictionary.csv";
		String ignoreWords = "./google-1000.txt";
		String outputFileName = "output";
		Scanner scanner = new Scanner(System.in);
		
		
		// Menu choice for user
		do {
			//Output a menu of options and solicit text from the user
			System.out.print(ConsoleColour.WHITE);
			System.out.print("Select Option [1-6]>");
			System.out.println();
			choice = scanner.nextInt();

			if (choice == 1) {
				
				System.out.println("Please enter full directory of Text File:");
				fileDir = scanner.next();
				//"./SampleTextFiles/biblegod.txt"
				
			} else if (choice == 2) {
				
				System.out.println("Please enter full directory of Dictionary File:");
				dictionary = scanner.next();
				
			} else if (choice == 3) {
				System.out.println("Please enter full directory of Ignore word File:");
				ignoreWords = scanner.next();
			
			} else if (choice == 4) {
				
				System.out.println("Please enter a name which the output file will be named:");
				outputFileName = scanner.next();
				
			} else if (choice == 5) {
				new ProcessFiles().go(fileDir, dictionary, ignoreWords, outputFileName);
			} else if (choice == 6) {
				System.out.println("Exiting!");
			}else {
				System.out.println("Please enter a valid option!!!");
			}
		} while (choice != 6);
		
		
		scanner.close();//Closing scanner
		
		// You may want to include a progress meter in you assignment!
		System.out.print(ConsoleColour.RED); // Change the colour of the console text
		int size = 100; // The size of the meter. 100 equates to 100%
		for (int i = 0; i < size; i++) { // The loop equates to a sequence of processing steps
			printProgress(i + 1, size); // After each (some) steps, update the progress meter
			Thread.sleep(10); // Slows things down so the animation is visible
		}
	}

	/**
	 * Terminal Progress Meter ----------------------- You might find the progress
	 * meter below useful. The progress effect works best if you call this method
	 * from inside a loop and do not call System.out.println(....) until the
	 * progress meter is finished.
	 * 
	 * Please note the following carefully:
	 * 
	 * 1) The progress meter will NOT work in the Eclipse console, but will work on
	 * Windows (DOS), Mac and Linux terminals.
	 * 
	 * 2) The meter works by using the line feed character "\r" to return to the
	 * start of the current line and writes out the updated progress over the
	 * existing information. If you output any text between calling this method,
	 * i.e. System.out.println(....), then the next call to the progress meter will
	 * output the status to the next line.
	 * 
	 * 3) If the variable size is greater than the terminal width, a new line escape
	 * character "\n" will be automatically added and the meter won't work properly.
	 * 
	 * 
	 */
	public static void printProgress(int index, int total) {
		if (index > total)
			return; // Out of range
		int size = 50; // Must be less than console width
		char done = '-'; // Change to whatever you like.
		char todo = '+'; // Change to whatever you like.

		// Compute basic metrics for the meter
		int complete = (100 * index) / total;
		int completeLen = size * complete / 100;

		/*
		 * A StringBuilder should be used for string concatenation inside a loop.
		 * However, as the number of loop iterations is small, using the "+" operator
		 * may be more efficient as the instructions can be optimized by the compiler.
		 * Either way, the performance overhead will be marginal.
		 */
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < size; i++) {
			sb.append((i < completeLen) ? done : todo);
		}

		/*
		 * The line feed escape character "\r" returns the cursor to the start of the
		 * current line. Calling print(...) overwrites the existing line and creates the
		 * illusion of an animation.
		 */
		System.out.print("\r" + sb + "] " + complete + "%");

		// Once the meter reaches its max, move to a new line.
		if (done == total)
			System.out.println("\n");
	}
}