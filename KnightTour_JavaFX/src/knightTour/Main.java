package knightTour;

import static java.lang.Integer.parseInt;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import static knightTour.Chessboard.*;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Knight's Tour");
        primaryStage.setScene(new Scene(root, BOARDDIM, BOARDDIM));
        primaryStage.setResizable(false);

        GridPane gpane = new GridPane();
        initChessboard(gpane);

        Knight knight = startupWindow(gpane);

        Scene scene = new Scene(gpane, BOARDDIM, BOARDDIM);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    void heuristicStartup(GridPane gpane, Knight knight) throws Exception{
        gpane.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            knight.moveKnightHeuristically(gpane);
        });
    }

    void manualStartup(GridPane gpane, Knight knight) throws Exception {

        gpane.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            Node clickedNode = e.getPickResult().getIntersectedNode();
            int clickX = GridPane.getColumnIndex(clickedNode);
            int clickY = GridPane.getRowIndex(clickedNode);
            boolean matched = false;


            // If one of the number icons were clicked then move the knight
            for (int i = 0; i < knight.getMoveNumberIcon().length; i++) {
                try {
                    if (!matched) {
                        if (clickX != knight.getPosX() && clickY != knight.getPosY()) {
                            if (GridPane.getColumnIndex(knight.getMoveNumberIcon()[i]) == clickX) {
                                if (GridPane.getRowIndex(knight.getMoveNumberIcon()[i]) == clickY) {
                                    knight.moveKnight(i, gpane);
                                    matched = true;
                                }
                            }
                        }
                    }
                } catch (Throwable e1) {
                    System.out.println(e1);
                }
            }
        });
    }

    Knight startupWindow(GridPane gpane) throws Exception {
        Parent root1 = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Stage startupStage = new Stage();
        startupStage.setScene(new Scene(root1, BOARDDIM - 50, 250));
        startupStage.setAlwaysOnTop(true);
        startupStage.setTitle("Knight's Tour");
        startupStage.setResizable(false);

        BorderPane startupPane = new BorderPane();

        Knight knight = new Knight();
        initStartupWindow(startupStage, startupPane, gpane, knight);

        // Exit program if window is exited
        startupStage.setOnCloseRequest(windowEvent -> {
            try {
                Platform.exit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        startupStage.setScene(new Scene(startupPane, BOARDDIM - 50, 200));
        startupStage.show();

        return knight;
    }

    void initStartupWindow(Stage startupStage, BorderPane startupPane, GridPane gpane, Knight knight) {
        // ------------------- Labels and textfields -------------------
        Label startupTitle = new Label("Welcome to Knight's Tour!");
        startupTitle.setFont(new Font(startupTitle.getFont().toString(), 24));
        startupTitle.setTextFill(Color.BLACK);
        BorderPane.setAlignment(startupTitle, Pos.CENTER);

        Label startupPrompt = new Label("To begin, enter a position for the knight to start on");
        startupPrompt.setFont(new Font(startupPrompt.getFont().toString(), 14));
        startupPrompt.setTextFill(Color.BLACK);
        startupPrompt.setPadding(new Insets(0, 0, 20, 0));
        BorderPane.setAlignment(startupPrompt, Pos.TOP_CENTER);

        Label promptColumn = new Label("Column (X): ");
        promptColumn.setFont(new Font(promptColumn.getFont().toString(), 14));
        promptColumn.setTextFill(Color.BLACK);

        TextInputControl promptColumnTxt = new TextField();
        promptColumnTxt.setMaxWidth(150);

        Label promptRow = new Label("Row (Y): ");
        promptRow.setFont(new Font(promptRow.getFont().toString(), 14));
        promptRow.setTextFill(Color.BLACK);

        TextInputControl promptRowTxt = new TextField();
        promptRowTxt.setMaxWidth(150);

        Pane pane = new Pane();

        HBox hbox = new HBox(10);
        hbox.setPadding(new Insets(0, 0, 85, 40));
        hbox.getChildren().addAll(promptColumn, promptColumnTxt, promptRow, promptRowTxt);


        // ----------- RADIO BUTTONS -----------
        HBox hboxRadioBtns = new HBox(30);
        hboxRadioBtns.setPadding(new Insets(35, 0, 0, 160));
        ToggleGroup startupGroup = new ToggleGroup();

        RadioButton manualStartup = new RadioButton("Manual Startup");
        manualStartup.setToggleGroup(startupGroup);
        manualStartup.setFont(new Font(manualStartup.getFont().toString(), 13));
        manualStartup.setSelected(true);

        RadioButton heuristicStartup = new RadioButton("Heuristic Startup");
        heuristicStartup.setToggleGroup(startupGroup);
        heuristicStartup.setFont(new Font(heuristicStartup.getFont().toString(), 13));
        hboxRadioBtns.getChildren().addAll(manualStartup, heuristicStartup);

        // ----------- START BUTTON -----------
        HBox hboxButton = new HBox();
        hboxButton.setPadding(new Insets(70, 0, 0, 250));
        Button start = new Button("Start");
        start.setFont(new Font(start.getFont().toString(), 14));
        hboxButton.getChildren().addAll(start);

        pane.getChildren().addAll(hbox, hboxRadioBtns, hboxButton);

        start.setOnAction(actionEvent -> {
            try {
                int posX = parseInt(promptColumnTxt.getText());
                int posY = parseInt(promptRowTxt.getText());
                promptColumnTxt.setEditable(true);
                promptRowTxt.setEditable(true);

                boolean validInput = false;

                if (posX >= 0 && posX <= SQSIDENUM - 1) {
                    if (posY >= 0 && posY <= SQSIDENUM - 1) {
                        knight.setPosX(posX);
                        knight.setPosY(posY);

                        knight.initKnight(gpane);
                        validInput = true;

                        startupStage.close();
                    }
                }

                if (!validInput) {
                    pane.getChildren().add(setErrorMessage("Invalid input! Please choose a value from 0 to " + (SQSIDENUM - 1), new Insets(30, 0, 0, 85)));
                }

                if(manualStartup.isSelected()){
                    manualStartup(gpane, knight);
                } else if(heuristicStartup.isSelected()){
                    heuristicStartup(gpane, knight);
                }

            } catch (Throwable e) {
                promptColumnTxt.setEditable(true);
                promptRowTxt.setEditable(true);
                pane.getChildren().add(setErrorMessage("Invalid input! Please choose a value from 0 to " + (SQSIDENUM - 1), new Insets(30, 0, 0, 85)));
            }
        });

        startupPane.setTop(startupTitle);
        startupPane.setCenter(startupPrompt);
        startupPane.setBottom(pane);
    }

    private HBox setErrorMessage(String inputPrompt, Insets msgInset) {
        Text validInputPrompt = new Text(inputPrompt);
        HBox hboxTxt = new HBox();
        hboxTxt.setPadding(msgInset);
        hboxTxt.getChildren().add(validInputPrompt);
        return hboxTxt;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
