package ui;

import com.sun.xml.internal.bind.v2.runtime.output.StAXExStreamWriterOutput;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.Automaton;

import java.util.ArrayList;
import java.util.List;

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
    private GridPane gpQ;
    private List<TextField> listTextField;
    @FXML
    public void initialize() {
        listTextField = new ArrayList<>();
        typeMachine.getItems().addAll("MOORE", "MEALY");
    }

    @FXML
    void generate(ActionEvent event) {
        int rows = Integer.parseInt(nStates.getText());
        String[] arrStimulus = stimulus.getText().split(",");
        int colums = arrStimulus.length;
        if(rows<=26) {
            if (typeMachine.getValue().equals("MOORE")) {
                HBox hb= new HBox();


                gridP1 = new GridPane();
                gridP2 = new GridPane();
                gpQ = new GridPane();
                hb.getChildren().add(gpQ);
                hb.getChildren().add(scrollP1);

                GridPane gpS = new GridPane();

                gridP1.setHgap(3);
                gridP1.setVgap(3);

                gpQ.setHgap(3);
                gpQ.setVgap(3);

                gpS.setHgap(3);
                gpS.setVgap(3);

                gridP2.setHgap(3);
                gridP2.setVgap(3);

                gridP2.getChildren().add(gpQ);


                scrollP1.setContent(gridP1);
                scrollP2.setContent(gpQ);
                vboxG.getChildren().add(gpS);
                vboxG.getChildren().add(hb);

                try {
                    String[][] b= createQForMoore(rows);
                    String[][] a = new String[colums+1][rows];
                    for (int i = 0; i < b.length; i++) {
                        for (int j = 0; j < b[0].length; j++) {
                            Label lRow = new Label(b[i][j]);
                            gpQ.add(lRow, j,i);
                        }
                    }

                    for (int i = 0; i < a.length; i++) {
                        for (int j = 0; j < a[0].length; j++) {

                            TextField ta = new TextField(a[i][j]);
                            ta.setPrefWidth(10);
                            listTextField.add(ta);
                            gridP1.add(ta, i , j);
                        }
                    }
                    readMoore(a);

                } catch (NegativeArraySizeException | IllegalArgumentException e) {
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setContentText(e.getMessage());
                    a.show();
                }
            } else if (typeMachine.getValue().equals("MEALY")) {
                System.out.println("MEALY");
            }
        }else{
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Digite ");
            a.show();
        }
    }

    public void readMoore(String[][] moore){
        String[][] matrix= new String[moore.length][moore[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length ; j++) {
                matrix[i][j]=moore[i][j];
            }
        }

        //return matrix;
    }

    public String[][] createQForMoore(int nstate){
        String[][] matrix= new String[nstate][1];
        for (int i = 0; i < nstate; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j]= (char) ((char)65+(i))+"";
            }

        }
        return matrix;
    }

}
