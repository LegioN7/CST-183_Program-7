import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;

public class FacultyReviewProgram {


    // Class usages for the data
    private FacultyData facultyData;
    private SurveyData surveyData;


    public FacultyReviewProgram() {
        // This class loads the faculty data from the files provided at the start of the program launch
        try {
            facultyData = FacultyData.readFacultyFromFile("faculty.txt");

            // Debug Statements!
            // Print faculty names for debugging
            // System.out.println("Faculty Names: " + Arrays.toString(facultyData.getFacultyNames()));
            // System.out.println("Faculty IDs: " + Arrays.toString(facultyData.getFacultyIds()));
            // System.out.println("Faculty Emails: " + Arrays.toString(facultyData.getFacultyEmails()));


            // Initialize the surveyData object and read survey information
            surveyData = SurveyData.readSurveyFromFile("facultyReviewData.txt");

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Creating a Method for the GUI instead of having it in the main method
        createGUI();
    }


    // Main Method using the GUI Method
    // I used this and found information on the GUI and SwingUtilities
    // https://www.javamex.com/tutorials/threads/invokelater.shtml
    public static void main(String[] args) {
        // Create an instance of FacultyReviewProgram
        SwingUtilities.invokeLater(FacultyReviewProgram::new);
    }


    /// Method to
    private void createGUI() {
        // Frame Creation
        JFrame frame = new JFrame("Faculty Review Program");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Frame Size
        frame.setSize(280, 400);

        // Grid Layout
        JPanel buttonPanel = new JPanel(new GridLayout(6, 1, 10, 10));

        // Button Panel Border
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        // Adding the buttons with the addButton Method (see below)
        // Search faculty information by ID
        addButton(buttonPanel, "Search by Faculty ID", e -> displayIDAverage());

        // Search faculty information by last name
        addButton(buttonPanel, "Search by Faculty Last Name", e -> displayNameAverage());

        // Search faculty information by email
        addButton(buttonPanel, "Search by Faculty Email", e -> displayEmailAverage());

        // Finds the highest average for a question input by the user, and displays the faculty member with the highest average
        addButton(buttonPanel, "Find Highest Average for a Question", e -> questionHighestAverage());

        // Displays the overall average for a question input by the user
        addButton(buttonPanel, "Find Overall Average for a Question", e -> calculateAndDisplayOverallAverage());

        // Creates a Faculty Table
        addButton(buttonPanel, "Display Faculty Information", e -> displayFacultyTable());


        frame.getContentPane().add(BorderLayout.WEST, buttonPanel);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // I changed the UI from a GrabBag to a GridLayout
    // I used add button to simplify the GUI placement
    private void addButton(JPanel panel, String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        panel.add(button);
    }

    private void displayIDAverage() {
        // Get user input for Faculty ID
        String facultyIDString = JOptionPane.showInputDialog("Enter Faculty ID:");


        // Exit the method if the response is null
        // I don't want to throw an error on cancel
        if (facultyIDString == null) {
            return;  //
        }

        try {
            // Convert the input to an integer
            int facultyID = Integer.parseInt(facultyIDString);

            // Find the index of the faculty member in the data
            int index = findFacultyIndexByID(facultyID);

            // Check method for documentation!
            facultyCheck(index);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid Faculty ID.");
        }
    }

    private void displayNameAverage() {
        // Get user input for Faculty Last Name
        String facultyLastName = JOptionPane.showInputDialog("Enter the Faculty Member's Last Name:");

        // Exit the method if the response is null
        // I don't want to throw an error on cancel
        if (facultyLastName == null) {
            return;  //
        }

        // Find the index of the faculty member with the given last name
        int index = findFacultyIndexByLastName(facultyLastName);

        // Check method for documentation!
        facultyCheck(index);
    }

    private void displayEmailAverage() {
        // Get user input for Faculty Email
        String facultyEmail = JOptionPane.showInputDialog("Enter the Faculty Member's Email:");

        // Exit the method if the response is null
        // I don't want to throw an error on cancel
        if (facultyEmail == null) {
            return;  //
        }

        // Find the index of the faculty member with the given email
        int index = findFacultyIndexByEmail(facultyEmail);

        // Check method for documentation!
        facultyCheck(index);
    }

    // Intellij allowed me to refactor this code
    // I use the refactor IDE method to create this
    // It checks if the faculty member exists. Technology is great!
    private void facultyCheck(int index) {
        if (index != -1) {
            double[] averages = {
                    calculateQuestionAverage(surveyData.getSurveyResponse1(), index),
                    calculateQuestionAverage(surveyData.getSurveyResponse2(), index),
                    calculateQuestionAverage(surveyData.getSurveyResponse3(), index),
                    calculateQuestionAverage(surveyData.getSurveyResponse4(), index)
            };

            displayAveragesOutput(facultyData.getFacultyIds()[index], facultyData.getFacultyNames()[index], averages);
        } else {
            JOptionPane.showMessageDialog(null, "Faculty member not found.");
        }
    }


    private void questionHighestAverage() {
        // Get user input for the question number
        String questionNumberString = JOptionPane.showInputDialog("Enter Question Number (1-4):");

        // Exit the method if the response is null
        // I don't want to throw an error on cancel
        if (questionNumberString == null) {
            return;  //
        }

        try {
            // Convert the input to an integer
            int questionNumber = Integer.parseInt(questionNumberString);

            // Validate the question number
            if (questionNumber < 1 || questionNumber > 4) {
                JOptionPane.showMessageDialog(null, "Invalid question number. Please enter a number between 1 and 4.");
                return;
            }

            double highestAverage = -1;
            int facultyIdWithHighestAverage = -1;

            for (int i = 0; i < surveyData.getFacultyIds().length; i++) {
                double[] responsesForQuestion = getResponsesForQuestion(surveyData, questionNumber);
                double average = calculateAverage(responsesForQuestion);

                if (average > highestAverage) {
                    highestAverage = average;
                    facultyIdWithHighestAverage = surveyData.getFacultyIds()[i];
                }
            }

            // Display the result
            displayQuestionHighestAverageResult(questionNumber, highestAverage, facultyIdWithHighestAverage);

            // If entry isn't a number, throw an exception
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid question number.");
        }
    }

    private void calculateAndDisplayOverallAverage() {
        // Get user input for the question number
        String questionNumberString = JOptionPane.showInputDialog("Enter Question Number (1-4):");

        // Exit the method if the response is null
        // I don't want to throw an error on cancel
        if (questionNumberString == null) {
            return;  //
        }

        try {
            // Convert the input to an integer
            int questionNumber = Integer.parseInt(questionNumberString);


            if (questionNumber < 1 || questionNumber > 4) {
                JOptionPane.showMessageDialog(null, "Invalid question number. Please enter a number between 1 and 4.");
                return;
            }

            // Get the responses for the specified question
            double[] questionResponses = getResponsesForQuestion(surveyData, questionNumber);

            // Calculate the overall average for the question
            double overallAverage = calculateOverallAverageForQuestion(questionResponses);

            // Display the result
            displayOverallAverageOutput(questionNumber, overallAverage);

            // If entry isn't a number, throw an exception
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid question number.");
        }
    }

    // I wanted to create a method for each way to find a faculty member as needed
    // This one gets faculty by ID
    private int findFacultyIndexByID(int facultyID) {
        int[] facultyIds = facultyData.getFacultyIds();
        for (int i = 0; i < facultyIds.length; i++) {
            if (facultyIds[i] == facultyID) {
                return i;
            }
        }
        return -1;
    }


    // I wanted to create a method for each way to find a faculty member as needed
    // This one gets faculty by full name
    private String getFacultyNameById(int facultyId) {
        int[] facultyIds = facultyData.getFacultyIds();

        for (int i = 0; i < facultyIds.length; i++) {
            if (facultyIds[i] == facultyId) {
                return facultyData.getFacultyNames()[i];
            }
        }

        return "Faculty Name Not Found";
    }


    // I wanted to create a method for each way to find a faculty member as needed
    // This one gets faculty by last name
    private int findFacultyIndexByLastName(String lastName) {
        lastName = lastName.trim();  // Trim leading and trailing spaces
        String[] facultyLastNames = facultyData.getFacultyLastNames();

        for (int i = 0; i < facultyLastNames.length; i++) {
            if (facultyLastNames[i].equalsIgnoreCase(lastName)) {
                return i; // Faculty member found, return the index
            }
        }
        return -1; // Faculty member not found
    }

    // I wanted to create a method for each way to find a faculty member as needed
    // This one gets email and ID together
    private String getFacultyEmailById(int facultyId) {
        int[] facultyIds = facultyData.getFacultyIds();

        for (int i = 0; i < facultyIds.length; i++) {
            if (facultyIds[i] == facultyId) {
                // Use the existing getFacultyEmails method to get the email
                return facultyData.getFacultyEmails()[i];
            }
        }

        return "Faculty Email Not Found";
    }


    // I wanted to create a method for each way to find a faculty member as needed
    // This one gets faculty by email
    private int findFacultyIndexByEmail(String email) {
        email = email.trim();
        String[] facultyEmails = facultyData.getFacultyEmails();

        for (int i = 0; i < facultyEmails.length; i++) {
            if (facultyEmails[i].equalsIgnoreCase(email)) {
                return i;
            }
        }
        return -1;
    }

    // Used to grab the data for the responses for a specific question
    private double[] getResponsesForQuestion(SurveyData surveyData, int questionNumber) {
        return switch (questionNumber) {
            case 1 -> surveyData.getSurveyResponse1();
            case 2 -> surveyData.getSurveyResponse2();
            case 3 -> surveyData.getSurveyResponse3();
            case 4 -> surveyData.getSurveyResponse4();
            default -> null;
        };
    }

    private String getQuestionText(int questionNumber) {
        return switch (questionNumber) {
            // I don't like how these were worded in the assignment, so I am changing it to simplify
            // We already know we are pulling instructor IDs etc. from the faculty data, so we don't need to specify that
            case 1 -> "Quality of teaching";
            case 2 -> "Qualifications and knowledge level to teach subject";
            case 3 -> "Fairness in grading";
            case 4 -> "Overall recommendation of instructor";
            default -> "Unknown question";
        };
    }


    // Helper method to calculate the average for a specific question and faculty member
    private double calculateQuestionAverage(double[] questionResponses, int facultyIndex) {
        double[] facultyResponses = new double[questionResponses.length];
        for (int i = 0; i < questionResponses.length; i++) {
            facultyResponses[i] = questionResponses[i + facultyIndex * questionResponses.length];
        }

        return calculateAverage(facultyResponses);
    }

    private double calculateAverage(double[] values) {
        double sum = 0.0;

        for (double value : values) {
            sum += value;
        }

        return sum / values.length;
    }

    private double calculateOverallAverageForQuestion(double[] questionResponses) {
        double sum = 0.0;

        for (double response : questionResponses) {
            sum += response;
        }

        return questionResponses.length > 0 ? sum / questionResponses.length : 0;
    }

    // Can you send me a note as to why Intellij prefers strings?
    // Please see below
    // 'StringBuilder questionAveragesDisplayMessage' can be replaced with 'String' and it will work the same
    private void displayAveragesOutput(int facultyId, String facultyName, double[] averages) {
        StringBuilder questionAveragesDisplayMessage = new StringBuilder();
        questionAveragesDisplayMessage.append("Faculty Member ID: ").append(facultyId).append("\n")
                .append("Faculty Member Name: ").append(facultyName).append("\n\n")
                .append("Question Averages\n")
                .append(getQuestionText(1)).append(": ").append(String.format("%.2f", averages[0])).append("\n")
                .append(getQuestionText(2)).append(": ").append(String.format("%.2f", averages[1])).append("\n")
                .append(getQuestionText(3)).append(": ").append(String.format("%.2f", averages[2])).append("\n")
                .append(getQuestionText(4)).append(": ").append(String.format("%.2f", averages[3])).append("\n");


        JOptionPane.showMessageDialog(null, questionAveragesDisplayMessage.toString(), "Faculty Averages", JOptionPane.INFORMATION_MESSAGE);
    }

    // I love StringBuilder but Intellij wants me to switch to a string!
    private void displayQuestionHighestAverageResult(int questionNumber, double highestAverage, int facultyIdWithHighestAverage) {
        StringBuilder highestAverageDisplayMessage = new StringBuilder();
        highestAverageDisplayMessage.append("Faculty Member ID: ").append(facultyIdWithHighestAverage).append("\n")
                .append("Faculty Member Name: ").append(getFacultyNameById(facultyIdWithHighestAverage)).append("\n")
                .append("Faculty Member Email: ").append(getFacultyEmailById(facultyIdWithHighestAverage)).append("\n")
                .append("\n").append(getQuestionText(questionNumber)).append("\n")
                .append("Question Highest Average: ").append(String.format("%.2f", highestAverage));

        JOptionPane.showMessageDialog(null, highestAverageDisplayMessage.toString(), "Question Highest Average", JOptionPane.INFORMATION_MESSAGE);
    }


    // I love StringBuilder but Intellij wants me to switch to a string!
    private void displayOverallAverageOutput(int questionNumber, double overallAverage) {
        StringBuilder questionAverageDisplayMessage = new StringBuilder();
        questionAverageDisplayMessage.append(getQuestionText(questionNumber)).append("\n")
                .append("Question Average: ").append(String.format("%.2f", overallAverage));

        JOptionPane.showMessageDialog(null, questionAverageDisplayMessage.toString(), "Overall Question Average", JOptionPane.INFORMATION_MESSAGE);
    }


    // this method will display Faculty Information in a Table
    // I'll be honest because this is not covered in class. I looked it up online
    // I found this on StackOverflow: https://stackoverflow.com/questions/10620448/most-simple-code-to-populate-jtable-from-resultset
    // I also used Oracles Text: https://docs.oracle.com/javase/tutorial/uiswing/components/table.html
    private void displayFacultyTable() {
        // Create a DefaultTableModel with columns
        DefaultTableModel model = new DefaultTableModel(
                new Object[][]{},
                new Object[]{"ID", "First Name", "Last Name", "Email"}
        );

        // Populate the model with faculty data
        // Loop through the faculty data and add each row to the model
        for (int i = 0; i < facultyData.getFacultyIds().length; i++) {
            model.addRow(new Object[]{
                    facultyData.getFacultyIds()[i],
                    facultyData.getFacultyFirstNames()[i],
                    facultyData.getFacultyLastNames()[i],
                    facultyData.getFacultyEmails()[i]
            });
        }

        // This will create a JTable with the DefaultTableModel
        // https://docs.oracle.com/javase/tutorial/uiswing/components/table.html
        JTable facultyTable = new JTable(model);

        // This will create a JScrollPane to the table with a scrolling option
        // https://docs.oracle.com/javase/tutorial/uiswing/components/scrollpane.html
        JScrollPane scrollPane = new JScrollPane(facultyTable);

        // Create JFrame to display the table
        JFrame frame = new JFrame("Faculty Information Table");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 400); // Adjust the size as needed

        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
    }


}