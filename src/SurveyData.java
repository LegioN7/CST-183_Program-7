import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SurveyData {

    private final int[] facultyIds;
    private final double[] surveyResponse1;
    private final double[] surveyResponse2;
    private final double[] surveyResponse3;
    private final double[] surveyResponse4;

    private SurveyData(int[] facultyIds, double[] surveyResponse1, double[] surveyResponse2,
                       double[] surveyResponse3, double[] surveyResponse4) {
        this.facultyIds = facultyIds;
        this.surveyResponse1 = surveyResponse1;
        this.surveyResponse2 = surveyResponse2;
        this.surveyResponse3 = surveyResponse3;
        this.surveyResponse4 = surveyResponse4;
    }

    public int[] getFacultyIds() {
        return facultyIds;
    }

    public double[] getSurveyResponse1() {
        return surveyResponse1;
    }

    public double[] getSurveyResponse2() {
        return surveyResponse2;
    }

    public double[] getSurveyResponse3() {
        return surveyResponse3;
    }

    public double[] getSurveyResponse4() {
        return surveyResponse4;
    }

    public static SurveyData readSurveyFromFile(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));

        // Assuming the format is: faculty_ID,surveyResponse1,surveyResponse2,surveyResponse3,surveyResponse4
        int lineCount = (int) reader.lines().count();
        reader.close();

        int[] facultyIds = new int[lineCount];
        double[] surveyResponse1 = new double[lineCount];
        double[] surveyResponse2 = new double[lineCount];
        double[] surveyResponse3 = new double[lineCount];
        double[] surveyResponse4 = new double[lineCount];

        reader = new BufferedReader(new FileReader(filePath));
        int index = 0;

        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            facultyIds[index] = Integer.parseInt(parts[0]);
            surveyResponse1[index] = Double.parseDouble(parts[1]);
            surveyResponse2[index] = Double.parseDouble(parts[2]);
            surveyResponse3[index] = Double.parseDouble(parts[3]);
            surveyResponse4[index] = Double.parseDouble(parts[4]);
            index++;
        }

        reader.close();

        return new SurveyData(facultyIds, surveyResponse1, surveyResponse2, surveyResponse3, surveyResponse4);
    }
}