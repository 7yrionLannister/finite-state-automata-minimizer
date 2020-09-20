package ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import model.Automaton;

public class Controller {

    private Automaton au;
    @FXML
    private TextArea nStates;

    @FXML
    private ComboBox<String> typeMachine;

    @FXML
    private ScrollPane scrollP1;
    @FXML
    private VBox vBox1;
    @FXML
    private ScrollPane scrollP2;

    private GridPane gridP1;

    private GridPane gridP2;
    @FXML
    public void initialize() {
        typeMachine.getItems().addAll("MOORE", "MEALY");
    }

    @FXML
    void generate(ActionEvent event) {
        int rows = Integer.parseInt(nStates.getText());
        if(rows<=26) {
            if (typeMachine.getValue().equals("MOORE")) {

                gridP1 = new GridPane();
                gridP2 = new GridPane();

                gridP1.setHgap(3);
                gridP1.setVgap(3);

                gridP2.setHgap(3);
                gridP2.setVgap(3);


                scrollP1.setContent(gridP1);
                scrollP2.setContent(gridP2);

                try {


                    String[][] a = createMatrixForReadMoore(rows);


                    for (int i = 0; i < a.length; i++) {
                        for (int j = 0; j < a[0].length; j++) {
                            Label lRow = new Label(a[0][j]);
                            gridP1.add(lRow, 0, j);

                            TextField ta = new TextField(a[i + 1][j]);
                            gridP1.add(ta, i + 1, j);


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
        }else{
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Digite ");
            a.show();
        }
    }

    public String[][] createMatrixForReadMoore(int nstate){
        String[][] matrix= new String[4][nstate];
        for (int i = 0; i < nstate; i++) {
            matrix[0][i]= (char) ((char)65+(i))+"";
        }
        return matrix;
    }

}
