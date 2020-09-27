package ui;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.Automaton;
import model.MealyMachine;
import model.MooreMachine;

import java.util.ArrayList;
import java.util.Arrays;

public class Controller {
    /**
     * The association with the model Automaton class.
     */
    private Automaton au;
    /**
     * The TextArea asks for the number of states for the machine you want to create the user.
     */
    @FXML
    private TextArea nStates;
    /**
     * The TextArea asks for the stimulus for the machine you want to create the user.
     */
    @FXML
    private TextArea stimulus;
    /**
     * The ComboBox shows the user what type of machine they can choose
     */
    @FXML
    private ComboBox<String> typeMachine;
    /**
     * The ScrollPane is the area where the machine is created
     */
    @FXML
    private ScrollPane scrollP1;
    /**
     * The ScrollPane is the result area of the minimized automata
     */
    @FXML
    private ScrollPane scrollP2;
    /**
     * The GridPane represents the view matrix for the machine
     */
    private GridPane gridP1;
    /**
     * The GridPane represents the result matrix for the automaton
     */
    private GridPane gridP2;

    /**
     * The rows represents the number of states that the machine will have
     */
    private int rows;
    /**
     * The columns represents the number of stimuli that the machine will have
     */
    private int columns;
    /**
     * The TextField represents the matrix of text fields that the user has to type
     */
    private TextField[][] tf;
    /**
     * The arrStimulus represents the arrangement of stimuli that the user types
     */
    private String[] arrStimulus;

    /**
     * <b>Description:</b>
     * The initialize function of the fxml file, called as soon as the graphical interface is loaded.
     */
    @FXML
    public void initialize() {
        typeMachine.getItems().addAll("MOORE", "MEALY");
    }

    /**
     * <b>Description:</b>
     * this function generates the input matrix according to the conditions given by the user
     */
    @FXML
    public void generate(ActionEvent event) {
        rows = Integer.parseInt(nStates.getText());
        arrStimulus = stimulus.getText().split(",");
        columns = arrStimulus.length;
        if (rows <= 26) {
            gridP1 = new GridPane();
            gridP2 = new GridPane();

            gridP1.setHgap(3);
            gridP1.setVgap(3);

            gridP2.setHgap(3);
            gridP2.setVgap(3);

            scrollP1.setContent(gridP1);
            scrollP2.setContent(gridP2);

            try {
                tf = new TextField[columns + 1][rows];

                fillHeaders(gridP1);

                for (int i = 1; i < rows + 1; i++) {
                    TextField ta = new TextField((char) (i + 64) + "");
                    ta.setEditable(false);
                    ta.setStyle(ta.getStyle() + "\n-fx-background-color: rgba(125, 156, 205, 0.84)");
                    ta.setPrefWidth(45);
                    gridP1.add(ta, 0, i);
                    for (int j = 1; j < columns + 1; j++) {
                        ta = new TextField("");
                        ta.setPrefWidth(45);
                        ta.setPromptText("A" + (typeMachine.getValue().equals("MEALY") ? ",a" : ""));
                        tf[j - 1][i - 1] = ta;
                        gridP1.add(ta, j, i);
                    }
                    if (typeMachine.getValue().equals("MOORE")) {
                        ta = new TextField("");
                        ta.setPrefWidth(45);
                        ta.setStyle(ta.getStyle() + "\n-fx-background-color:  rgba(70, 214, 70, 0.75)");
                        ta.setPromptText("a");
                        tf[columns][i - 1] = ta;
                        gridP1.add(ta, columns + 1, i);
                    }
                }
            } catch (NegativeArraySizeException | IllegalArgumentException e) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText(e.getMessage());
                a.show();
            }
        } else {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Pruebe con menos estados");
            a.show();
        }
    }
    /**
     * <b>Description:</b>
     * This function takes the data entered by the user to send it to the model and minimize the machine
     * and then show it in the resulting matrix.
     */
    @FXML
    public void minimizeMachine(Event event) {
        fillHeaders(gridP2);
        if(typeMachine.getValue().equals("MOORE")) {
            readMoore();
        } else {
            readMealy();
        }
    }
    /**
     * <b>Description:</b>
     * This function creates the relationship with the moore machine of the model,
     * then reads the data entered by the user and records them in the machine.
     */
    public void readMoore() {
        String[][] matrix = readTextFields("MOORE");

        MooreMachine<Character, Character, Character> mooreMachine = new MooreMachine<Character, Character, Character>('A', matrix[matrix.length - 1][0].charAt(0));

        for (int j = 0; j < matrix[0].length; j++) {
            char temp = (char) (j + 65);
            mooreMachine.insertState(temp, matrix[matrix.length - 1][j].charAt(0));
        }
        for (int i = 0; i < matrix.length - 1; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                char temp = (char) (j + 65);
                mooreMachine.relate(temp, matrix[i][j].charAt(0), arrStimulus[i].charAt(0));
            }
        }

        mooreMachine = mooreMachine.minimize();
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        int minStates = mooreMachine.getOrder();
        a.setContentText("El automata de Moore fue minimizado.\nAhora contiene " + minStates + " estados");
        a.show();

        ArrayList<Character> minStatesList = mooreMachine.getVertices();
        for (int i = 1; i < minStates + 1; i++) {
            char current = minStatesList.get(i - 1);
            TextField ta = new TextField(current + "");
            ta.setEditable(false);
            ta.setPrefWidth(45);
            ta.setStyle(ta.getStyle() + "\n-fx-background-color: rgba(125, 156, 205, 0.84)");
            gridP2.add(ta, 0, i);
            ta = new TextField("" + mooreMachine.getResponse(current));
            ta.setStyle(ta.getStyle() + "\n-fx-background-color:  rgba(70, 214, 70, 0.75)");
            ta.setEditable(false);
            ta.setPrefWidth(45);
            gridP2.add(ta, columns + 1, i);
            for (int j = 1; j < columns + 1; j++) {
                ta = new TextField("" + mooreMachine.stateTransitionFunction(current, arrStimulus[j - 1].charAt(0)));
                ta.setEditable(false);
                ta.setPrefWidth(45);
                //ta.setStyle("#0000");
                tf[j - 1][i - 1] = ta;
                gridP2.add(ta, j, i);
            }
        }
    }
    /**
     * <b>Description:</b>
     * This function creates the relationship with the mealy machine of the model,
     * then reads the data entered by the user and records them in the machine.
     */
    public void readMealy() {
        String[][] matrix = readTextFields("MEALY");

        for(int i = 0; i < matrix.length; i++) {
            System.out.println(Arrays.toString(matrix[i]));
        }

        MealyMachine<Character, Character, Character> mealyMachine = new MealyMachine<>('A');

        for (int j = 1; j < matrix[0].length; j++) {
            char temp = (char) (j + 65);
            mealyMachine.insertState(temp);
        }
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                char temp = (char) (j + 65);
                String[] cell = matrix[i][j].split(",");
                mealyMachine.relate(temp, cell[0].charAt(0), arrStimulus[i].charAt(0), cell[1].charAt(0));
            }
        }

        mealyMachine = mealyMachine.minimize();
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        int minStates = mealyMachine.getOrder();
        a.setContentText("El automata de Mealy fue minimizado.\nAhora contiene " + minStates + " estados");
        a.show();

        ArrayList<Character> minStatesList = mealyMachine.getVertices();
        for (int i = 1; i < minStates + 1; i++) {
            char current = minStatesList.get(i - 1);
            TextField ta = new TextField(current + "");
            ta.setEditable(false);
            ta.setPrefWidth(45);
            ta.setStyle(ta.getStyle() + "\n-fx-background-color: rgba(125, 156, 205, 0.84)");
            gridP2.add(ta, 0, i);
            for (int j = 1; j < columns + 1; j++) {
                char stimulus = arrStimulus[j - 1].charAt(0);
                String cell = "" + mealyMachine.stateTransitionFunction(current, stimulus);
                cell += "," + mealyMachine.getResponse(current, stimulus);
                ta = new TextField(cell);
                ta.setEditable(false);
                ta.setPrefWidth(45);
                //ta.setStyle("#0000");
                tf[j - 1][i - 1] = ta;
                gridP2.add(ta, j, i);
            }
        }
    }
    /**
     * <b>Description:</b>
     * this function places the header of the stimuli typed by the user to provide a better view to the user
     * @param grid represents the gridpane to edit the header.
     */
    private void fillHeaders(GridPane grid) {
        for (int i = 1; i < columns + 1; i++) {
            TextField ta = new TextField(arrStimulus[i - 1]);
            ta.setEditable(false);
            ta.setStyle(ta.getStyle() + "\n-fx-background-color: rgba(205, 125, 125, 0.84)");
            ta.setPrefWidth(45);
            grid.add(ta, i, 0);
        }
    }

    /** Reads the input the user put in the matrix taking into account if it was a Moore or a Mealy machine as the number of columns differ
     * @param type Whether the machine is a Moore machine or a Mealy machine
     * @return The values of the text field matrix the user gave as input
     * */
    private String[][] readTextFields(String type) {
        String[][] matrix = new String[type.equals("MOORE") ? columns + 1 : columns][rows];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j] = tf[i][j].getText();
            }
        }
        return matrix;
    }
}
