import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FacultyData {

    private int[] facultyIds;
    private String[] facultyNames;
    private String[] facultyEmails;
    private String[] facultyFirstNames;
    private String[] facultyLastNames;
    public static int MAX_FACULTY = 242;

    public FacultyData(int[] facultyIds, String[] facultyFirstNames, String[] facultyLastNames, String[] facultyEmails) {
        this.facultyIds = facultyIds;
        this.facultyFirstNames = facultyFirstNames;
        this.facultyLastNames = facultyLastNames;
        this.facultyEmails = facultyEmails;
    }



    public static FacultyData readFacultyFromFile(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));

        int[] facultyIds = new int[MAX_FACULTY];
        String[] facultyFirstNames = new String[MAX_FACULTY];
        String[] facultyLastNames = new String[MAX_FACULTY];
        String[] facultyEmails = new String[MAX_FACULTY];

        int index = 0;
        String line;
        while ((line = reader.readLine()) != null && index < MAX_FACULTY) {
            String[] parts = line.split("\\s+", 3);
            if (parts.length == 3) {
                facultyIds[index] = Integer.parseInt(parts[0]);

                // Split the name into first and last name
                String[] nameParts = parts[1].split("_");
                facultyFirstNames[index] = nameParts[0];
                facultyLastNames[index] = nameParts.length > 1 ? nameParts[1] : "";

                facultyEmails[index] = parts[2];

                index++;
            }
        }

        reader.close();

        return new FacultyData(facultyIds, facultyFirstNames, facultyLastNames, facultyEmails);
    }



    public int[] getFacultyIds() {
        return facultyIds;
    }

    public String[] getFacultyNames() {
        if (facultyFirstNames != null && facultyLastNames != null) {
            facultyNames = new String[facultyFirstNames.length];
            for (int i = 0; i < facultyFirstNames.length; i++) {
                facultyNames[i] = facultyFirstNames[i] + " " + facultyLastNames[i];
            }
        }
        return facultyNames;
    }

    public String[] getFacultyLastNames() {
        return facultyLastNames;
    }

    public String[] getFacultyEmails() {
        return facultyEmails;
    }
}