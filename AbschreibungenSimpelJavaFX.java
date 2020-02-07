import abschreibungen.src.de.htwsaar.stl.winf.abschreibungen.core.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Java Fx Applikation zu dem Abschreibungsprogramm
 * @author Schwanzer
 *
 * @version 1.1
 */
public class AbschreibungenSimpelJavaFX extends Application {
    private static final String LINEAR = "Linear";
    private static final String GEOMETRISCH_DEGRESSIV = "Geometrisch-degressiv";
    private static final String LINEAR_GEOMETRISCH_DEGRESSIV = "Wandelnd";
    //StringBuilder für die Texte
    private static StringBuilder readme = new StringBuilder();
    private static StringBuilder license = new StringBuilder();
    private static StringBuilder linearErklaerung = new StringBuilder();
    private static StringBuilder geometrischDegressivErklaerung = new StringBuilder();
    private static StringBuilder linearGeometrischDegressivErklaerung = new StringBuilder();
    private static StringBuilder abschreibungErklaerung = new StringBuilder();
    private static final String filePath = "Pfad zu den Texten";
    //Dark Mode toggle. Ohne Dark Mode wäre es keine richtige Applikation :)
    private ToggleButton dark_mode = new ToggleButton("Dark Mode off");

    private BorderPane root = new BorderPane();
    private Scene scene = new Scene(root, 850, 550);
    private Vermoegensgegenstand maschine;

    public static void main(String[] args) {
        launch(args);
    }

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?"); //Matched einer Ganzzahl oder einer Gleitkommazahl
    }

    private static String readFile(String filePath) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(filePath));
        return new String(encoded, StandardCharsets.UTF_8);
    }

    /**
     * Ueberprueft die Laenge des Strings
     * @param s der StringBuilder
     * @return boolscher Ausdruck, ob der String die Laenge 0 hat.
     */
    private static boolean checkStringLoaded(StringBuilder s) {
        return s.length() == 0;
    }

    /**
     * Die Start Methode, hier werden bereits TextArea Eigenschaften festgelegt
     * @param primaryStage die Primary Stage fuer das Hauptfenster
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Abschreibung JavaFX (simpel)");

        TextArea textArea = new TextArea();

        //Center of Frame
        textArea.setMaxSize(400, 500);
        root.setLeft(textArea);

        createCenter(root, textArea);
        createMenuBar(root, primaryStage);

        primaryStage.setScene(scene);
        scene.getStylesheets().add(AbschreibungenSimpelJavaFX.class.getResource("Abschreibungen.css").toExternalForm());
        primaryStage.show();
    }

    /**
     * Erzeugt die Maschine, von der abgeschrieben wird
     * @param anschaffungskostenText das Anschaffungskosten-TextField
     * @param nutzungsdauerText das Nutzungsdauer-TextField
     * @param result
     */
    private void createMaschine(TextField anschaffungskostenText, TextField nutzungsdauerText, String result) {
        maschine = new VermoegensgegenstandImpl(checkStringContainsDouble(anschaffungskostenText.getCharacters().toString()),
                checkStringContainsInteger(nutzungsdauerText.getCharacters().toString()));
        maschine.setAbschreibungsverfahren(getCorrectAbschreibungsverfahren(result));
        maschine.abschreiben();
    }

    private Vermoegensgegenstand.Abschreibungsverfahren getCorrectAbschreibungsverfahren(String res) {
        switch (res) {
            case GEOMETRISCH_DEGRESSIV:
                return Vermoegensgegenstand.Abschreibungsverfahren.GEOMETRISCH_DEGRESSIV;
            case LINEAR_GEOMETRISCH_DEGRESSIV:
                return Vermoegensgegenstand.Abschreibungsverfahren.GEOMETRISCH_DEGRESSIV_LINEAR;
            default:
                return Vermoegensgegenstand.Abschreibungsverfahren.LINEAR;
        }
    }

    /**
     * Check Methode, die überprüft, ob der Text im TextField eine Double ist, gibt sonst eine Exception (Listener Button)
     * @param ueberpruefenderText der Text des TextFields
     * @return 0 wenn falsch, wenn richtig die geparste double Zahl
     */
    private double checkStringContainsDouble(String ueberpruefenderText) {
        if (!isNumeric(ueberpruefenderText)) {
            return 0;
        }
        return Double.parseDouble(ueberpruefenderText);
    }

    /**
     * Check Methode, die ueberprueft, ob der Text im TextField eine Double ist, gibt sonst eine Exception (Listener Button)
     * @param ueberpruefenderText der Text des TextFields
     * @return 0 wenn falsch, wenn die gesparste integer Zahl
     */
    private int checkStringContainsInteger(String ueberpruefenderText) {
        if (!isNumeric(ueberpruefenderText)) {
            return 0;
        }
        return Integer.parseInt(ueberpruefenderText);
    }

    /**
     * Updated den Text im Button
     * @param btn der Button
     * @param anschaffungskosten die Anschaffungskosten
     * @param nutzungsdauer die Nutzungsdauer
     */
    private void updateButton(Button btn, double anschaffungskosten, int nutzungsdauer) {
        btn.setText("Abschreiben (" + anschaffungskosten + " EUR, " + nutzungsdauer + " Jahre)");
    }

    /**
     * Das Fenster, welches bei Klick auf Menuitems geöffnet wird
     * @param text der Text, der angezeigt werden soll
     * @param primaryStage die HauptStage der Anwendung
     */
    private void createWindow(String text, Stage primaryStage) {
        int i = text.indexOf("\n");
        String title = text.substring(0, i);


        Label secondLabel = new Label(text);
        ScrollPane root = new ScrollPane();
        root.setContent(secondLabel);

        Scene secondScene = new Scene(root, 230, 100);
        root.setContent(secondLabel);

        // New window (Stage)
        Stage newWindow = new Stage();
        newWindow.setTitle(title);
        newWindow.initModality(Modality.APPLICATION_MODAL);

        secondLabel.setWrapText(true);
        secondLabel.prefWidthProperty().bind(newWindow.widthProperty().subtract(15));

        // Set position of second window, related to primary window.
        newWindow.setX(primaryStage.getX() + 200);
        newWindow.setY(primaryStage.getY() + 100);
        newWindow.setWidth(450);
        newWindow.setHeight(600);

        newWindow.setScene(secondScene);
        if (!dark_mode.isSelected()) {
            newWindow.getScene().getStylesheets().add(this.getClass().getResource("andereFenster.css").toExternalForm());
        }
        newWindow.show();
    }

    /**
     * Erstellt die MenueBar (top)
     * @param root die BorderPane, die die Elemente auf der primaryStage ordnet
     * @param primaryStage die primary Stage, die zum Erstellen des Fenster benoetigt wird
     */
    private void createMenuBar(BorderPane root, Stage primaryStage) {

        //MenuBar erstellen
        MenuBar menuBar = new MenuBar();

        // einzelne Menus
        Menu m1 = new Menu("Abschreibung");
        Menu m2 = new Menu("Abschreibungsverfahren");
        Menu m3 = new Menu("About");

        //Abschreibungsverfahren
        MenuItem abschreibungen = new MenuItem("Abschreibung");
        MenuItem linear = new MenuItem("Linear");
        MenuItem geometrischDegressiv = new MenuItem("Geometrisch-Degressiv");
        MenuItem linearGeometrischDegressiv = new MenuItem("Wandelnd");

        //About Items
        MenuItem licensingMenuItem = new MenuItem("Licensing");
        MenuItem readMeMenuItem = new MenuItem("Readme");

        //Dark Mode

        //Menues zu Bar hinzufügen
        menuBar.getMenus().add(m1);
        menuBar.getMenus().add(m2);
        menuBar.getMenus().add(m3);

        //Menuitems zum Menuepunkt "Abschreibungsverfahren" hinzufügen
        m2.getItems().add(linear);
        m2.getItems().add(geometrischDegressiv);
        m2.getItems().add(linearGeometrischDegressiv);
        m1.getItems().add(abschreibungen);

        //Menueitems zum Menuepunkt "About" hinzufuegen
        m3.getItems().add(licensingMenuItem);
        m3.getItems().add(readMeMenuItem);

        //Listeners zu den MenuItems
        licensingMenuItem.setOnAction(e -> createWindow(loadStringFileContent("LICENSE.txt", license), primaryStage));
        readMeMenuItem.setOnAction(e -> createWindow(loadStringFileContent("README.md", readme), primaryStage));

        linear.setOnAction(e -> createWindow(loadStringFileContent("Linear.txt", linearErklaerung), primaryStage));
        geometrischDegressiv.setOnAction(e -> createWindow(loadStringFileContent("GeometrischDegressiv.txt", geometrischDegressivErklaerung), primaryStage));
        linearGeometrischDegressiv.setOnAction(e -> createWindow(loadStringFileContent("Wandelnd.txt", linearGeometrischDegressivErklaerung), primaryStage));
        linearGeometrischDegressiv.setOnAction(e -> createWindow(loadStringFileContent("GeometrischDegressiv.txt", abschreibungErklaerung), primaryStage));

        abschreibungen.setOnAction(e -> createWindow(loadStringFileContent("Abschreibungen.txt", linearErklaerung), primaryStage));

        root.setTop(menuBar);

    }

    /**
     * Ueberprueft, ob der Button gedrueckt wurde, wenn ja, dann wird das Stylesheet geloescht. Es entsteht ein "Light Theme"
     * @param pressed boolean Ausdruck, ob der Toggle Button gedrueckt wurde
     */
    private void Dark_Mode(boolean pressed) {
        //System.out.println(pressed);
        if (pressed) {
            scene.getStylesheets().clear();
        } else {
            scene.getStylesheets().add(this.getClass().getResource("Abschreibungen.css").toExternalForm());

        }
    }

    /**
     * Das Center der BorderPane wird hier erstellt enthaelt alle Buttons/TextFields
     * @param root die BorderPane
     * @param textArea die TextArea
     */
    private void createCenter(BorderPane root, TextArea textArea) {

        double anschaffungskosten = 10000;
        int nutzungsdauer = 10;

        GridPane rightGridPane = new GridPane();
        //Combobox Items
        ObservableList<String> options = FXCollections.observableArrayList(LINEAR, GEOMETRISCH_DEGRESSIV, LINEAR_GEOMETRISCH_DEGRESSIV);

        //Combobox
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setItems(options);
        comboBox.setValue(LINEAR);

        //Textfields
        TextField anschaffungskostenText = new TextField();
        anschaffungskostenText.insertText(0, Double.toString(anschaffungskosten));
        TextField nutzungsdauerText = new TextField();
        nutzungsdauerText.insertText(0, Integer.toString(nutzungsdauer));

        //Buttons
        Button abschreibeButton = new Button();
        updateButton(abschreibeButton, anschaffungskosten, nutzungsdauer);

        //Listener zu Textfields hinzufügen
        anschaffungskostenText.textProperty().addListener(observable -> updateButton(abschreibeButton, checkStringContainsDouble(anschaffungskostenText.getCharacters().toString()),
                checkStringContainsInteger(nutzungsdauerText.getCharacters().toString())));
        nutzungsdauerText.textProperty().addListener(observable -> updateButton(abschreibeButton, checkStringContainsDouble(anschaffungskostenText.getCharacters().toString()),
                checkStringContainsInteger(nutzungsdauerText.getCharacters().toString())));

        //die einzelnen Labels
        Label properties = new Label("Eigenschaften");
        Label abschreibungsverfahren = new Label("Abschreibungsverfahren: ");
        Label anschaffungskostenLabel = new Label("Anschaffungskosten: ");
        Label nutzungsdauerLabel = new Label("Nutzungsdauer: ");
        //Label Eigenschaften:
        rightGridPane.add(properties, 1, 0);
        //Label Abschreibungsverfahren
        rightGridPane.add(abschreibungsverfahren, 0, 1);
        //Combobox
        rightGridPane.add(comboBox, 1, 1);

        rightGridPane.add(anschaffungskostenLabel, 0, 3);
        rightGridPane.add(nutzungsdauerLabel, 0, 5);
        rightGridPane.add(anschaffungskostenText, 1, 3);
        rightGridPane.add(nutzungsdauerText, 1, 5);

        rightGridPane.add(abschreibeButton, 0, 6);


        dark_mode.selectedProperty().addListener((observable -> Dark_Mode(dark_mode.isSelected())));
        rightGridPane.add(dark_mode, 0, 7);

        abschreibeButton.setOnAction(event -> {
            String abschreibungsArt = comboBox.getValue();
            createMaschine(anschaffungskostenText, nutzungsdauerText, abschreibungsArt);
            textArea.appendText(maschine.toString());
            textArea.appendText("\n" + "-----------------------------------------------------------" + "\n");
        });

        //GridPane in Center of Border Pane
        root.setCenter(rightGridPane);
    }

    /**
     * Ueberprueft, ob der String bereits geladen wurde, dann wird nur der bereits geladene String zurueckgegeben,
     * sonst wird er aus der Datei ausgelesen und zurueckgegeben
     * @param fileName Der Dateiname
     * @param content der StringBuilder der jeweiligen Datei
     * @return der String mit dem Content
     */
    private String loadStringFileContent(String fileName, StringBuilder content) {
        if (!checkStringLoaded(content)) return content.toString();
        try {
            content.append(readFile(filePath + fileName));
        } catch (Exception e) {
            String error="Der Pfad wurde nicht korrekt auf das Verzeichnis Texte gesetzt: "+filePath;
            showErrorDialog(e,error);
        }
        return content.toString();
    }

    /**
     * zeigt einen Java FX Alert an
     * @param e die Exception
     * @param error der zusaetzliche Hinweis als String (wird vor der Exception ausgegeben)
     */
    private void showErrorDialog(Exception e,String error) {
        double alertDialogSize=1000;

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ein Fehler ist aufgetreten");

        alert.setHeaderText(e.toString());

        //CharacterStream schreibt output in StringBuffer
        StringWriter sw = new StringWriter();

        //PrintWriter: Prints formatted representations of objects to a text-output stream.
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw); //prints this throwable and its backtrace to the specified print stream

        String exceptionmsg = sw.toString();
        TextArea textArea = new TextArea();
        textArea.appendText(error+"\n"+exceptionmsg);

        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);


        Label label = new Label("Der Stacktrace lautet:");

        GridPane expContent = new GridPane();
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        expContent.setMinWidth(textArea.getWidth());
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(expContent);
        alert.getDialogPane().setMinWidth(alertDialogSize);
        alert.showAndWait();
    }
}
