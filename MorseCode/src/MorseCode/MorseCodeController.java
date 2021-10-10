/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MorseCode;

import static MorseCode.MorseCode.stage;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXToggleButton;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.HashMap;
import java.util.Map;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import animatefx.animation.FadeInUp;
import animatefx.animation.FadeOut;
import animatefx.animation.Shake;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * FXML Controller class
 *
 * @author yassin
 */
public class MorseCodeController implements Initializable {

    private final String GITHUB = "https://github.com/yassindaboussi";
    private final String FACEBOOK = "https://www.facebook.com/yassdaboussi";
    private final String GMAIL = "mailto:yassin.daboussi@esprit.tn";
    private final String YOUTUBE = "https://www.youtube.com/user/dabyain";
    public static String SwitchModeActuel = "White";
    private JFXDialogTool dialogAboutMe;
    private static final Stage stagee = new Stage();
    public static final BoxBlur BOX_BLUR_EFFECT = new BoxBlur(3, 3, 3);
    private int clickedAboutMe = 1;

    @FXML
    private JFXTextArea txtMorseText;
    @FXML
    private JFXTextArea txtMorseCode;
    @FXML
    private Label PutOnly;
    @FXML
    private Label txtisCopied;
    @FXML
    private Pane PaneCopied;
    @FXML
    private JFXToggleButton SwitchMode;
    @FXML
    private Pane GreenPage;
    @FXML
    private AnchorPane containerAboutMe;
    @FXML
    private ImageView Yassin;
    @FXML
    private Separator separator2;
    @FXML
    private Separator separator1;
    @FXML
    private Text developer;
    @FXML
    private StackPane stckMorse;
    @FXML
    private AnchorPane rootMorse;
    @FXML
    private MaterialDesignIconView google;
    @FXML
    private MaterialDesignIconView github;
    @FXML
    private MaterialDesignIconView facebook;
    @FXML
    private MaterialDesignIconView youtube;

    File file = new File(System.getProperty("user.home") + "/Desktop/saveMorse.txt");
    Desktop desktop = Desktop.getDesktop();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        isTxTChanged(txtMorseText);
        setURL(); //Url AboutMe
    }

    void isTxTChanged(JFXTextArea JFXTextArea) {
        JFXTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("textfield changed from " + oldValue + " to " + newValue);
            txtMorseCode.setText(translateToMorse(txtMorseText.getText().toLowerCase()));
        });
    }

    public static void copyToClipboard(String text) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(new StringSelection(text), null);
    }

    @FXML
    private void CopyTheOutPut(MouseEvent event) {
        copyToClipboard(txtMorseCode.getText());
        if (txtMorseCode.getText().isEmpty()) {;
            txtisCopied.setText("Empty  :(");
            new Thread(() -> {
                try {
                    Platform.runLater(() -> PaneCopied.setVisible(true));
                    Thread.sleep(600);
                    Platform.runLater(() -> PaneCopied.setVisible(false));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        } else {
            PaneCopied.setVisible(true);
            txtisCopied.setText("Copied  :)");
            Timeline timelinee = new Timeline();
            timelinee.getKeyFrames().add(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    PaneCopied.setVisible(false);
                    timelinee.stop();
                }
            }));
            // Repeat indefinitely until stop() method is called.
            timelinee.setCycleCount(Animation.INDEFINITE);
            timelinee.setAutoReverse(true);
            timelinee.play();
        }
    }

    Map<Character, String> mapping = new HashMap<Character, String>();

    char[] letter = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
        'v', 'w', 'x', 'y', 'z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', ' ', '.', ',', '?', '!', '/', '(', ')', '&', ':', ';', '=', '+', '-', '_', '"', '$', '@', '¿', '¡'};

    String[] morse = {".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..",
        ".---", "-.-", ".-..", "--", "-.", "---", ".---.", "--.-", ".-.",
        "...", "-", "..-", "...-", ".--", "-..-", "-.--", "--..", ".----",
        "..---", "...--", "....-", ".....", "-....", "--...", "---..", "----.",
        "-----", "/", ".-.-.-", "--..--", "..--..", "-.-.--", "-..-.", "-.--.", "-.--.-", ".-...", "---...", "-.-.-.", "-...-", ".-.-.", "-....-", "..--.-", ".-..-.", "...-..-", ".--.-.", "..-.-", "--...-"};

    public MorseCodeController() {
        setupMap();
    }

    private void setupMap() {
        for (int i = 0; i < letter.length; i++) {
            mapping.put(letter[i], morse[i]);
        }
    }

    public String translateToMorse(String string) {
        char[] characters = string.toCharArray();
        StringBuilder morseString = new StringBuilder();

        for (char character : characters) {
            if ((mapping.get(character)) == null) {
                //   morseString.append("\n");
                PutOnly.setVisible(true);
            } else {
                PutOnly.setVisible(false);

                morseString.append(mapping.get(character));
            }

        }
        return String.valueOf(morseString);
    }

    @FXML
    private void minimize_app(ActionEvent event) {
        MorseCode.stage.setIconified(true);
    }

    @FXML
    private void close_app(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void switchMode(MouseEvent event) {

        if (SwitchMode.isSelected()) {
            GreenPage.setStyle("-fx-background-color:  linear-gradient(to bottom, #485461 0%, #28313b 74%);-fx-background-radius:1em;");
            txtMorseCode.getStyleClass().clear();
            txtMorseCode.getStyleClass().add("text-areaDarkMode");
            txtMorseText.getStyleClass().clear();
            txtMorseText.getStyleClass().add("text-areaDarkMode");
            SwitchModeActuel = "Dark";
        } else {
            GreenPage.setStyle("-fx-background-color:  linear-gradient(to right top, #11998e 0%, #38ef7d 100%);-fx-background-radius:1em;");
            txtMorseCode.getStyleClass().clear();
            txtMorseCode.getStyleClass().add("text-areaWhiteMode");
            txtMorseText.getStyleClass().clear();
            txtMorseText.getStyleClass().add("text-areaWhiteMode");
            SwitchModeActuel = "White";
        }
    }

    ///// About me
    private void url(String url, Node node) {
        node.setOnMouseClicked(ev -> {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(new URI(url));
            } catch (IOException | URISyntaxException ex) {
                ex.getStackTrace();
            }
        });
    }

    private void setURL() {
        url(GITHUB, github);
        url(FACEBOOK, facebook);
        url(GMAIL, google);
        url(YOUTUBE, youtube);
    }

    private void setAnimations() {
        transition(developer, 1);
        transition(separator1, 2);
        transition(Yassin, 3);
        transition(separator2, 4);
        transition(facebook, 5);
        transition(youtube, 6);
        transition(github, 7);
        transition(google, 8);
    }

    private void transition(Node node, int duration) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), node);
        scaleTransition.setFromX(1.0);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToX(1.2);
        scaleTransition.setToY(1.2);

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(3000), node);
        fadeTransition.setFromValue(2);
        fadeTransition.setToValue(0.5);

        PauseTransition pauseTransition = new PauseTransition();
        pauseTransition.setDuration(Duration.seconds(duration));
        pauseTransition.setOnFinished(ev -> {
            PauseTransition pauseTransition2 = new PauseTransition();
            pauseTransition2.setDuration(Duration.seconds(0.1));
            pauseTransition2.setOnFinished(ev2 -> {
                node.setVisible(true);
            });

            pauseTransition2.play();
            fadeInUp(node);
            fadeTransition.play();
        });

        pauseTransition.play();

        node.setOnMouseEntered(ev -> {
            fadeTransition.setToValue(1);
            fadeTransition.playFromStart();

            scaleTransition.setRate(1.0);
            scaleTransition.play();
        });

        node.setOnMouseExited(ev -> {
            fadeTransition.setDuration(Duration.millis(100));
            fadeTransition.setToValue(0.5);
            fadeTransition.playFromStart();

            scaleTransition.setRate(-1.0);
            scaleTransition.play();
        });
    }

    @FXML
    private void AboutMeClicked(MouseEvent event) {
        rootMorse.setEffect(BOX_BLUR_EFFECT);
        containerAboutMe.setVisible(true);
        dialogAboutMe = new JFXDialogTool(containerAboutMe, stckMorse);
        dialogAboutMe.show();
        if (clickedAboutMe == 1) {
            setAnimations();
        }
        if (clickedAboutMe == 2) {
            fadeInUp(containerAboutMe);
        }
        dialogAboutMe.setOnDialogClosed(ev -> {
            fadeOut(containerAboutMe);
            clickedAboutMe = 2;
            closeStage();
            rootMorse.setEffect(null);
        });
    }

    @FXML
    private void CloseAboutMe(MouseEvent event) {
        fadeOut(containerAboutMe);
        clickedAboutMe = 2;
        if (dialogAboutMe != null) {
            dialogAboutMe.close();
        }
    }

    public static void closeStage() {
        if (stagee != null) {
            stagee.hide();
        }
    }

    public static void fadeInUp(Node node) {
        new FadeInUp(node).play();
    }

    public static void fadeOut(Node node) {
        new FadeOut(node).play();
    }

    public static void shake(Node node) {
        new Shake(node).play();
    }

    public void SAVE() {      //Save the UserName and Password (for one user)
        try {
            if (!file.exists()) {
                file.createNewFile();  //if the file !exist create a new one
            }
            BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsolutePath()));
            bw.write("+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
            bw.newLine(); //leave a new Line
            bw.write("This file is on your desktop (SaveMorse.txt)"); //write the Morse Text
            bw.newLine(); //leave a new Line
            bw.write("+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
            bw.newLine(); //leave a new Line
            bw.write("Morse TexT : " + txtMorseText.getText()); //write the Morse Text
            bw.newLine(); //leave a new Line
            bw.write("Morse Code : " + txtMorseCode.getText()); //write the Morse Code
            bw.newLine(); //leave a new Line
            bw.write("+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
            bw.newLine(); //leave a new Line
            bw.write("Email : Yassin.daboussi@esprit.tn && facebook : yassdaboussi"); //write the Morse Text
            bw.newLine(); //leave a new Line
            bw.write("+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
            bw.close(); //close the BufferdWriter

            if (file.exists()) //checks file exists or not  
            {
                desktop.open(file); //opens the specified file   
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }//End Of Save

    @FXML
    private void SaveTheOutPut(MouseEvent event) {
        SAVE();
    }

}
