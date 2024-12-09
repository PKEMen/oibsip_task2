
import java.awt.*;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;


/**
 * This program implements a Number Guessing Game. Users can select difficulty 
 * levels and try to guess a randomly generated number within a limited number 
 * of attempts. Scores are saved and loaded from a file to track progress 
 * across sessions.
 *
 * @author PK Ewusie-Mensah
 * @version 1.0  2024-Nov-25
 */
public class NumberGuessingGame{

   // defines constants for menu item choices
    private static final int OPT_ONE = 1;
    private static final int OPT_TWO = 2;    
    private static final int OPT_THREE = 3;    
    private static final int OPT_FOUR = 4;
    private static final int OPT_FIVE = 5;
    private static final int QUIT_PROGRAM = 0;
    
    // private constant values
    private static final int LOW_MENU_CHOICE = QUIT_PROGRAM ;
    private static final int HIGH_MENU_CHOICE = 10;
    
    // constant for data file location
    private static final String DATA_FILE_NAME = "data/userScore.txt";
    
    //
    private static int currentScore = 0;
    private static int currentRound = 0;
    
    
   /**
     * Main method to run the online reservation system.
     *
     * @param args command-line arguments
     * @throws AWTException if a problem occurs while clearing the console
     */
   
   public static void main(String[] args) throws AWTException {
      Scanner input = new Scanner(System.in);
      int menuChoice = -1;
      String gameChoice = "";
      
      // Display welcome message and load user data
      displayIntro();
      clear();
      
      loadScores(DATA_FILE_NAME);
      
      while (menuChoice != QUIT_PROGRAM)
      {
         if(gameChoice.equals("no")){
            menuChoice = QUIT_PROGRAM;
         }
         else{
            displayMenu();
            System.out.println();         
            menuChoice = getUserMenuInput(input);
            currentRound++;
         }
         
         switch (menuChoice)
         {
            case OPT_ONE:
                 clear();
                 gameChoice = numberGame(1, 10, 3, input);//EASY LEVEL
                 break;
                 
             case OPT_TWO:
                 clear();
                 gameChoice = numberGame(1, 100, 8, input);//MEDIUM LEVEL
                 break;
                 
             case OPT_THREE:
                 clear();
                 gameChoice = numberGame(1, 1000, 15, input);//HARD LEVEL
                 break;
             
              //EXTRA-HARD LEVEL   
             case OPT_FOUR:
                 clear();
                 gameChoice = numberGame(-1000, 1000, 20, input);
                 break;
                 
              case OPT_FIVE:
                 clear();
                 currentScore = 0;
                 saveScores(DATA_FILE_NAME);
                 System.out.println("The Scoreboard has been resetted!!!");
                 break;
                 
             case QUIT_PROGRAM:
                 System.out.println("Quitting program..."); 
                 saveScores(DATA_FILE_NAME);
                 System.out.println("\nTHANKS FOR PLAYING THE "
                         + "NUMBER GUESSING GAME\n");
                 break;
                 
             default:
                 System.out.println("ERROR: This should not be possible!");
                 break;
         } 
      }
   }
     
   /**
     * Displays the introductory message.
     */
   public static void displayIntro(){
      System.out.println(" __ _ _   _  ___  ___ ___ \n" +
                           " / _` | | | |/ _ \\/ __/ __|\n" +
                           "| (_| | |_| |  __/\\__ \\__ \\\n" +
                           " \\__, |\\__,_|\\___||___/___/\n" +
                           "  __/ |                    \n" +
                           " |___/      ");
      System.out.println("WELOME TO THE NUMBER GUESSING GAME");
   }
   
   /**
     * Displays the game menu.
     */
   private static void displayMenu(){
      System.out.println();
      System.out.println("Choose your preffered level");
      System.out.println("--------------------------------------");
      System.out.println(OPT_ONE + " - Easy Level(Numbers from 1 to 10)");
      System.out.println(OPT_TWO + " - Intermediate Level(Numbers from 1 to 100"
            + ")");
      System.out.println(OPT_THREE + " - Hard Level(Numbers from 1 to 1000)");
      System.out.println(OPT_FOUR + " - Extra-Hard Level(Numbers from -1000 to "
            + "1000)");
      System.out.println("--------------------------------------");
      System.out.println(OPT_FIVE + " - Reset the score board");
      System.out.println( QUIT_PROGRAM + ". Quit Program");
      System.out.println("--------------------------------------");
   }
   
   /**
     * Prompts the user for menu input.
     *
     * @param input the Scanner object
     * @return the validated menu choice
     */
   private static int getUserMenuInput(Scanner input){
        int intChoice = -1;
        
        while (intChoice < LOW_MENU_CHOICE || intChoice > HIGH_MENU_CHOICE)
        {
            System.out.print("Enter menu option number: ");
            String userChoice = input.nextLine();

            while (!isPositiveInteger(userChoice))
            {
                System.out.println("ERROR: Invalid choice!");
                System.out.print("Enter menu option number: ");
                userChoice = input.nextLine();
            }

            intChoice = Integer.parseInt(userChoice);

            if (intChoice < LOW_MENU_CHOICE || intChoice > HIGH_MENU_CHOICE)
            {
                System.out.println("ERROR: Invalid choice!");
            }
        }

        return intChoice;
    }
   
   
   /**
     * Validates whether a string represents a positive integer.
     *
     * @param strNum the input string
     * @return true if valid, false otherwise
     */
    public static boolean isPositiveInteger(String strNum)
    {
        final char LOW_INT_VALUE = '0';
        final char HIGH_INT_VALUE = '9';
        final String BAD_STRING = "";

        // if the string is null or empty, it is not valid
        if (strNum == null || strNum.equals(BAD_STRING))
        {
            return false;
        }

        // check each character, making sure it is between 0 and 9 inclusive
        for (int i = 0; i < strNum.length(); i++)
        {
            if (strNum.charAt(i) < LOW_INT_VALUE
                    || strNum.charAt(i) > HIGH_INT_VALUE)
            {
                return false;
            }
        }

        // all tests passed, so string contains a positive integer
        return true;
    }
   
    /**
     * Executes the number guessing game for the given difficulty level.
     *
     * @param low the lowest possible number
     * @param high the highest possible number
     * @param numGuesses the maximum number of guesses allowed
     * @param input the Scanner object for user input
     * @return a string indicating whether the user wants to play another round
     */
    public static String numberGame(int low, int high, int numGuesses, 
          Scanner input){
      Random randIn = new Random();
      int computerNum = randIn.nextInt(low, high);
      int attemptLeft = numGuesses;
      int userNum = -1;
      
      displayIntro();
       System.out.println("Guessing a number from " + low + " to " + high);
       System.out.println();
       
       while(userNum != computerNum && attemptLeft > 0){
          System.out.printf("%-17s %-25s %-25s\n","Your Score: " + currentScore, 
                "Attempts Remaining: " + attemptLeft, "Round Number: " + 
                      currentRound);
          System.out.print("Guess the number: ");
          userNum = input.nextInt();
          System.out.println();
          
          if(userNum > computerNum){
             System.out.println("The correct number is lower than the guessed "
                   + "number");
             attemptLeft--;
          }
          else if(userNum < computerNum){
             System.out.println("The correct number is higher than the guessed "
                   + "number");
             attemptLeft--;
          }
          else{
            currentScore++;
            attemptLeft = 0;
             System.out.println("YOU GUESSED THE NUMBER CORRECTLY!!!");
             System.out.println("Your current score is: " + currentScore);
          }
          if(attemptLeft == 0){
             System.out.println("YOU ARE ALL OUT OF GUESSES!!!");
          }
       }
       System.out.println("Type 1 or Y to play another round or NO to quit");
       input.nextLine();
       String confirmation = input.nextLine().trim().toLowerCase();
       return confirmation;
    }
    
    
    /**
     * Clears the console screen using the Robot class.
     */
    public static void clear() throws AWTException {
      Robot rob = new Robot();
      try {
        rob.keyPress(KeyEvent.VK_CONTROL); // press "CTRL"
        rob.keyPress(KeyEvent.VK_L); // press "L"
        rob.keyRelease(KeyEvent.VK_L); // unpress "L"
        rob.keyRelease(KeyEvent.VK_CONTROL); // unpress "CTRL"
        Thread.sleep(1000); // add delay in milisecond
      } 
      catch (InterruptedException e) {
      }
   }

    /**
     * Loads the user's score from a file.
     *
     * @param filename the file containing the score
     */
   private static void loadScores(String filename) {
       File loadingFile = new File(filename);
       int score = 0;
       try{
          Scanner loadFile = new Scanner(loadingFile);
          String line = loadFile.nextLine();
          if (line != null) {
                score = Integer.parseInt(line.trim()); 
            }
         currentScore = score;
         loadFile.close();
       }
       catch(FileNotFoundException fnfe){
          System.out.println("ERROR! File could not be opened");
       }
   }

   /**
     * Saves the user's current score to a file.
     *
     * @param filename the file to save the score to
     */
   private static void saveScores(String filename) {
       File saveFile = new File(filename);
       try{
          PrintWriter fileWrite = new PrintWriter(saveFile);
          fileWrite.println(currentScore);
          fileWrite.flush();
          fileWrite.close();
       }
       catch(Exception e){
          System.out.println(filename + " File could not be created");
       }
   }
      
}

   


