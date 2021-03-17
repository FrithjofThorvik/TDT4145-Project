package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class QuestionController {
  public boolean exit = false;
  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

  // Prompts user for useCases to pick from, and handles them
  public Integer chooseUseCase() {
    // Print out questions for useCases
    System.out.print("Choose a question from 1-5" + "\n\n0 - Exit" + "\n1 - (UseCase 1) User login"
        + "\n2 - (UseCase 2) XXX" + "\n3 - (UseCase 3) Reply to post" + "\n4 - (UseCase 4) Search for posts"
        + "\n5 - (UseCase 5) XXX" + "\n\nYour choice: ");

    // List of all useCases that exists in integer values
    List<Integer> useCases = new ArrayList<Integer>();
    Collections.addAll(useCases, 0, 1, 2, 3, 4, 5);

    try {
      // Get user input and parse into Integer
      Integer useCase;
      useCase = Integer.parseInt(this.reader.readLine());

      // Check if input was valid
      if (useCases.contains(useCase)) {
        // Return value from handling the use case (1, 0 , -1)
        return this.handleUseCase(useCase);

      } else { // Return -1 if invalid user input
        return -1;
      }

    } catch (IOException e1) { // Return -1 if invalid user input
      e1.printStackTrace();
      return -1;
    }
  }

  // Handle use cases. Returns 0 => exit, 1 => successful handling, -1 => error
  private Integer handleUseCase(Integer useCase) {
    // Initialize controllers
    DBController DBCtrl = new DBController();

    // Handle (UseCase 0)
    if (useCase == 0) {
      this.exit = true;
      return 0; // Exit program
    }

    // Handle (UseCase 1)
    else if (useCase == 1) {
      // Instantiate input variables for email & password prompt
      String email = "";
      String password = "";

      try {
        // Display current users
        System.out.println(DBCtrl.handleQuery("SELECT * FROM user"));

        // Prompt user for email
        System.out.print("Email: ");
        email = this.reader.readLine();

        // Prompt user for password
        System.out.print("Password: ");
        password = this.reader.readLine();

        // Validate user authentication
        Integer userAuth = DBCtrl.handleUserLogin(email, password);

        // Handle user authentication
        if (userAuth == 1) {
          System.out.println("User authenticated!");
          return 1;
        } else if (userAuth == 0) {
          System.out.println("User not authenticated!");
          return 1;
        } else {
          return -1;
        }

      } catch (IOException e1) {
        e1.printStackTrace();
        return -1;
      }
    }
    // Handle (UseCase 2 - Steven)
    // Handle (UseCase 3 - Frithjof)
    else if (useCase == 3) {
      // Instantiate input variables for email & password prompt
      String postId = "";
      String postText = "";

      try {
        // Display current users
        System.out.println(DBCtrl.handleQuery("SELECT * FROM Post"));

        // Prompt user for postId
        System.out.print("Pick PostID: ");
        postId = this.reader.readLine();

        // Prompt user for postId
        System.out.print("Reply: ");
        postText = this.reader.readLine();

        // Handle insert of postId
        return DBCtrl.handlePostReply(postId, postText);

      } catch (IOException e1) {
        e1.printStackTrace();
        return -1;
      }
    }
    // Handle (UseCase 4 - Frithjof)
    else if (useCase == 4) {
      // Instantiate input variables for email & password prompt
      String text = "";

      try {
        // Prompt user for text
        System.out.print("Search for Posts: ");
        text = this.reader.readLine();

        // Handle search for posts
        return DBCtrl.handlePostSearch(text);

      } catch (IOException e1) {
        e1.printStackTrace();
        return -1;
      }
    }
    // Handle (UseCase 5 - Steven)
    // Handle (Default)
    else {
      return -1;
    }

  }

  // Prompts user for choice to exit or continue (y/n)
  public void chooseExit() {
    // Print out questions for useCases
    System.out.print("Would you like to continue? (y/n) ");

    try {
      // Get user input and parse into Integer
      String choice;
      choice = this.reader.readLine();

      // Check if input was valid
      if (choice.equals("n")) {
        this.exit = true;
      }
    } catch (IOException e1) { // Return error if invalid user input
      e1.printStackTrace();
    }
  }
}
