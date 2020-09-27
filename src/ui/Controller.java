package ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.Automaton;
import model.MooreMachine;

import java.util.ArrayList;

public class Controller {

    private Automaton au;
    @FXML
    private TextArea nStates;
    @FXML
    private TextArea stimulus;
    @FXML
    private ComboBox<String> typeMachine;
    @FXML
    private ScrollPane scrollP1;
    @FXML
    private VBox vboxG;
    @FXML
    private ScrollPane scrollP2;

    private GridPane gridP1;

    private GridPane gridP2;
    private int rows;
    private int columns;
    private TextField[][] tf;
    private String[] arrStimulus;

    @FXML
    public void initialize() {
        typeMachine.getItems().addAll("MOORE", "MEALY");
    }

    @FXML
    public void generate(ActionEvent event) {
        rows = Integer.parseInt(nStates.getText());
        arrStimulus = stimulus.getText().split(",");
        columns = arrStimulus.length;
        if (rows <= 26) {
            if (typeMachine.getValue().equals("MOORE")) {

                HBox hb = new HBox();

                gridP1 = new GridPane();
                gridP2 = new GridPane();
                hb.getChildren().add(scrollP1);

                GridPane gpS = new GridPane();

                gridP1.setHgap(3);
                gridP1.setVgap(3);

                gpS.setHgap(3);
                gpS.setVgap(3);

                gridP2.setHgap(3);
                gridP2.setVgap(3);

                scrollP1.setContent(gridP1);
                scrollP2.setContent(gridP2);
                vboxG.getChildren().add(gpS);
                vboxG.getChildren().add(hb);

                try {
                    tf = new TextField[columns + 1][rows];

                    for (int i = 1; i < columns + 1; i++) {
                        TextField ta = new TextField(arrStimulus[i - 1]);
                        ta.setEditable(false);
                        gridP1.add(ta, i, 0);
                    }
                    for (int i = 1; i < rows + 1; i++) {
                        TextField ta = new TextField((char) (i + 64) + "");
                        ta.setEditable(false);
                        gridP1.add(ta, 0, i);
                        for (int j = 1; j < columns + 2; j++) {
                            ta = new TextField("");
                            //ta.setPrefWidth(15);
                            //ta.setStyle("#0000");
                            tf[j - 1][i - 1] = ta;
                            gridP1.add(ta, j, i);
                        }
                    }
                } catch (NegativeArraySizeException | IllegalArgumentException e) {
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setContentText(e.getMessage());
                    a.show();
                }
            } else if (typeMachine.getValue().equals("MEALY")) {
                System.out.println("MEALY");
            }
        } else {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Pruebe con menos estados");
            a.show();
        }
    }

    @FXML
    public void readMachine(ActionEvent event) {
        readMoore(tf, columns, rows);
    }

    public void readMoore(TextField[][] moore, int columns, int rows) {
        String[][] matrix = new String[columns + 1][rows];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j] = moore[i][j].getText();
            }
        }

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
        a.setContentText("El automata fue minimizado.\nAhora contiene " + minStates + " estados");
        a.show();

        for (int i = 1; i < columns + 1; i++) {
            TextField ta = new TextField(arrStimulus[i - 1]);
            ta.setEditable(false);
            gridP2.add(ta, i, 0);
        }
        ArrayList<Character> minStatesList = mooreMachine.getVertices();
        for (int i = 1; i < minStates + 1; i++) {
            char current = minStatesList.get(i - 1);
            TextField ta = new TextField(current + "");
            ta.setEditable(false);
            gridP2.add(ta, 0, i);
            ta = new TextField("" + mooreMachine.getResponse(current));
            ta.setEditable(false);
            gridP2.add(ta, columns + 1, i);
            for (int j = 1; j < columns + 1; j++) {
                ta = new TextField("" + mooreMachine.stateTransitionFunction(current, arrStimulus[j - 1].charAt(0)));
                System.out.println(current + ":" + mooreMachine.getResponse(current) + "--" + arrStimulus[j - 1] + "-->" + ta.getText());
                ta.setEditable(false);
                //ta.setPrefWidth(15);
                //ta.setStyle("#0000");
                tf[j - 1][i - 1] = ta;
                gridP2.add(ta, j, i);
            }
        }
    }
}
