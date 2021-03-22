package DatabaseProject;

public class App {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // Instanciate Database controller and establish database connection
            QuestionController QuestionCtrl = new QuestionController();

            // Start application loop
            while (!QuestionCtrl.exit) {
                System.out.println("\n\n");
                try {
                    // Get user input for which useCase that should run
                    Integer useCase = QuestionCtrl.chooseUseCase();

                    // Handle invalid useCase handling
                    if (useCase == 0) {
                        // Set exit value to true
                        QuestionCtrl.exit = true;

                    } else if (useCase == 1) {
                        // Prompt user to continue or not
                        QuestionCtrl.chooseExit();

                    } else { // Throw invalid useCase input error
                        throw new Exception("Invalid useCase input");
                    }
                } catch (Exception appError) { // Catch application errors
                    System.out.println("Error: " + appError);
                }
            } // End application loop

        } catch (

        Exception systemError) { // Catch system errors
            System.out.println("Error: " + systemError);
        }
    }
}
