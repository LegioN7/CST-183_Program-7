import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;

public class FacultyReviewProgram {

    // Finals to use for the question numbers
    private static final int QUESTION_1_INDEX = 0;
    private static final int QUESTION_4_INDEX = 3;

    // Class usages for the data
    private FacultyData facultyData;
    private SurveyData surveyData;


    public FacultyReviewProgram() {
        // This class loads the faculty data from the files provided at the start of the program launch
        try {
            facultyData = FacultyData.readFacultyFromFile("faculty.txt");

            // Print faculty names for debugging
            System.out.println("Faculty Names: " + Arrays.toString(facultyData.getFacultyNames()));

            // Initialize the surveyData object and read survey information
            surveyData = SurveyData.readSurveyFromFile("facultyReviewData.txt");

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Creating a Method for the GUI instead of having it in the main method
        createAndShowGUI();
    }


    // Main Method using the GUI Method
    public static void main(String[] args) {
        // Create an instance of FacultyReviewProgram
        SwingUtilities.invokeLater(FacultyReviewProgram::new);
    }


    /// Method to
    private void createAndShowGUI() {

        // Title of the Program to display in the Java Swing Window
        JFrame frame = new JFrame("Faculty Review Program");

        // When you close the swing window, exit the program
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Creating a 1080p window as this is the standard resolution for most monitors
        frame.setSize(1920, 1080);

        // Creating the buttons to handle the requests
        JButton button1 = new JButton("Search by Faculty ID");
        JButton button2 = new JButton("Search by Faculty Last Name");
        JButton button3 = new JButton("Search by Faculty Email");
        JButton button4 = new JButton("Find Highest Average for a Question");
        JButton button5 = new JButton("Find the Overall Average for a Question");

        // Creating a GridBagLayout to handle the buttons
        // https://docs.oracle.com/javase/tutorial/uiswing/layout/gridbag.html
        JPanel panel = new JPanel(new GridBagLayout());

        // Creating the constraints for the buttons
        // Configuring the Gridbag Layout
        // Did I mention I don't care for GridBag? hah.
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(10, 10, 10, 10);

        // Adding the buttons to the panel
        constraints.gridx = 0;
        panel.add(button1, constraints);

        constraints.gridx = 1;
        panel.add(button2, constraints);

        constraints.gridx = 2;
        panel.add(button3, constraints);

        constraints.gridx = 3;
        panel.add(button4, constraints);

        constraints.gridx = 4;
        panel.add(button5, constraints);

        // Reducing the button code by switching from Active Listeners to Lambda Expressions
        // When you click the button, the program will run the method associated with the button
        button1.addActionListener(e -> displayIDAverage());
        button2.addActionListener(e -> displayNameAverage());
        button3.addActionListener(e -> displayEmailAverage());
        button4.addActionListener(e -> {
            String questionNumberString = JOptionPane.showInputDialog("Enter Question Number (1-4):");

            try {
                int questionNumber = Integer.parseInt(questionNumberString);

                if (questionNumber >= 1 && questionNumber <= 4) {
                    questionHighestAverage(questionNumber);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid question number. Please enter a number between 1 and 4.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid question number.");
            }
        });
        button5.addActionListener(e -> calculateAndDisplayOverallAverage());

        // Center the Panel in the Frame
        frame.getContentPane().add(BorderLayout.CENTER, panel);

        // Center the Frame on the Screen
        frame.setLocationRelativeTo(null);

        // Make the Frame visible
        frame.setVisible(true);
    }

    private void displayIDAverage() {
        // Get user input for Faculty ID
        String facultyIDString = JOptionPane.showInputDialog("Enter Faculty ID:");

        try {
            // Convert the input to an integer
            int facultyID = Integer.parseInt(facultyIDString);

            // Find the index of the faculty member in the data
            int index = findFacultyIndexByID(facultyID);

            // Check if the faculty member is found
            if (index != -1) {
                // Format the averages to two decimal places
                String formattedAverage1 = String.format("%.2f", calculateQuestionAverage(surveyData.getSurveyResponse1(), index));
                String formattedAverage2 = String.format("%.2f", calculateQuestionAverage(surveyData.getSurveyResponse2(), index));
                String formattedAverage3 = String.format("%.2f", calculateQuestionAverage(surveyData.getSurveyResponse3(), index));
                String formattedAverage4 = String.format("%.2f", calculateQuestionAverage(surveyData.getSurveyResponse4(), index));

                // Display information using JOptionPane or update your GUI components
                JOptionPane.showMessageDialog(null,
                        "Faculty Member ID: " + facultyData.getFacultyIds()[index] + "\n" +
                                "Faculty Member Name: " + facultyData.getFacultyNames()[index] + "\n\n" +
                                "Question Averages:\n" +
                                "Question Average 1: " + formattedAverage1 + "\n" +
                                "Question Average 2: " + formattedAverage2 + "\n" +
                                "Question Average 3: " + formattedAverage3 + "\n" +
                                "Question Average 4: " + formattedAverage4);
            } else {
                JOptionPane.showMessageDialog(null, "Faculty member not found.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid Faculty ID.");
        }
    }

    private void displayNameAverage() {
        // Get user input for Faculty Last Name
        String facultyLastName = JOptionPane.showInputDialog("Enter the Faculty Member's Last Name:");

        // Find the index of the faculty member with the given last name
        int index = findFacultyIndexByLastName(facultyLastName);

        // Check if the faculty member is found
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

    private void displayEmailAverage() {
        // Get user input for Faculty Email
        String facultyEmail = JOptionPane.showInputDialog("Enter the Faculty Member's Email:");

        // Find the index of the faculty member with the given email
        int index = findFacultyIndexByEmail(facultyEmail);

        // Check if the faculty member is found
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


    private void questionHighestAverage(int questionNumber) {
        if (questionNumber < QUESTION_1_INDEX || questionNumber > QUESTION_4_INDEX) {
            System.out.println("Invalid question number. Please enter a number between 1 and 4.");
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
    }

    private void calculateAndDisplayOverallAverage() {
        // Get user input for the question number
        String questionNumberString = JOptionPane.showInputDialog("Enter Question Number (1-4):");

        try {
            // Convert the input to an integer
            int questionNumber = Integer.parseInt(questionNumberString);

            // Validate the question number
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

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid question number.");
        }
    }


    private int findFacultyIndexByID(int facultyID) {
        int[] facultyIds = facultyData.getFacultyIds();
        for (int i = 0; i < facultyIds.length; i++) {
            if (facultyIds[i] == facultyID) {
                return i; // Faculty member found, return the index
            }
        }
        return -1; // Faculty member not found
    }

    // Helper method to find the index of a faculty member by ID
    private String getFacultyNameById(int facultyId) {
        int[] facultyIds = facultyData.getFacultyIds();

        for (int i = 0; i < facultyIds.length; i++) {
            if (facultyIds[i] == facultyId) {
                // Use the existing getFacultyNames method to get the full name
                return facultyData.getFacultyNames()[i];
            }
        }

        return "Faculty Name Not Found";
    }

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

    private int findFacultyIndexByEmail(String email) {
        email = email.trim(); // Trim leading and trailing spaces
        String[] facultyEmails = facultyData.getFacultyEmails();

        for (int i = 0; i < facultyEmails.length; i++) {
            if (facultyEmails[i].equalsIgnoreCase(email)) {
                return i; // Faculty member found, return the index
            }
        }
        return -1; // Faculty member not found
    }


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
        return Arrays.stream(facultyResponses).average().orElse(Double.NaN);
    }

    private double calculateAverage(double[] values) {

        double sum = 0.0;
        for (double value : values) {
            sum += value;
        }

        return sum / values.length;
    }

    private double calculateOverallAverageForQuestion(double[] questionResponses) {
        double sum = 0;

        for (double response : questionResponses) {
            sum += response;
        }

        return questionResponses.length > 0 ? sum / questionResponses.length : Double.NaN;
    }

    private void displayAveragesOutput(int facultyId, String facultyName, double[] averages) {
        StringBuilder questionAveragesDisplayMessage = new StringBuilder();
        questionAveragesDisplayMessage.append("Faculty Member ID: ").append(facultyId).append("\n")
                .append("Faculty Member Name: ").append(facultyName).append("\n\n")
                .append("Question Averages\n")
                .append("Question Average 1: ").append(String.format("%.2f", averages[0])).append("\n")
                .append("Question Average 2: ").append(String.format("%.2f", averages[1])).append("\n")
                .append("Question Average 3: ").append(String.format("%.2f", averages[2])).append("\n")
                .append("Question Average 4: ").append(String.format("%.2f", averages[3]));


        JOptionPane.showMessageDialog(null, questionAveragesDisplayMessage.toString(), "Faculty Averages", JOptionPane.INFORMATION_MESSAGE);
    }

    private void displayQuestionHighestAverageResult(int questionNumber, double highestAverage, int facultyIdWithHighestAverage) {
        StringBuilder highestAverageDisplayMessage = new StringBuilder();
        highestAverageDisplayMessage.append("Faculty Member ID: ").append(facultyIdWithHighestAverage).append("\n")
                .append("Faculty Member Name: ").append(getFacultyNameById(facultyIdWithHighestAverage)).append("\n")
                .append("Faculty Member Email: ").append(getFacultyEmailById(facultyIdWithHighestAverage)).append("\n")
                .append("Question ").append(getQuestionText(questionNumber)).append("\n")
                .append("Question Highest Average: ").append(String.format("%.2f", highestAverage));

        JOptionPane.showMessageDialog(null, highestAverageDisplayMessage.toString(), "Question Highest Average", JOptionPane.INFORMATION_MESSAGE);
    }


    private void displayOverallAverageOutput(int questionNumber, double overallAverage) {
        StringBuilder questionAverageDisplayMessage = new StringBuilder();
        questionAverageDisplayMessage.append(getQuestionText(questionNumber)).append("\n")
                .append("Question Average: ").append(String.format("%.2f", overallAverage));

        JOptionPane.showMessageDialog(null, questionAverageDisplayMessage.toString(), "Overall Question Average", JOptionPane.INFORMATION_MESSAGE);
    }

}