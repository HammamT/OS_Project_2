/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package os_project_2;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author Hammam Tawafshah
 */
public class FXMLController implements Initializable {

    @FXML
    private Button generate;

    @FXML
    private Button load;

    @FXML
    private Button excute;

    @FXML
    private TextArea txta1;

    @FXML
    private TextArea txta2;

    @FXML
    private TextField txtf;

    @FXML
    private TextField txtf1;

    @FXML
    private TextField txtf2;

    @FXML
    private ChoiceBox<Integer> cbox;

    @FXML
    private ChoiceBox<String> cbox1;

    ArrayList<Integer> a = new ArrayList<Integer>();
    ArrayList<String> b = new ArrayList<String>();

    @FXML
    private void handleLoad(ActionEvent e) throws FileNotFoundException {

        a.clear();
        FileChooser fc = new FileChooser();
        File f = fc.showOpenDialog(null);
        if (f != null) {
            Scanner input = new Scanner(f);
            String s = input.nextLine();
            input.close();
            String[] s0 = s.split(" ");

            for (int i = 0; i < s0.length; i++) {
                a.add(Integer.parseInt(s0[i]));
            }

            int count = 0;
            txta1.clear();
            for (int i = 0; i < 350; i++) {
                txta1.appendText(a.get(i).toString() + "," + " ");
                count++;
                if (count == 10) {
                    txta1.appendText("\n");
                    count = 0;
                }
            }
        }else{
            System.out.println("");
        }
    }

    @FXML
    private void handleGenerate(ActionEvent e) {

        a.clear();
        Random random = new Random();
        int r;
        int max = Integer.parseInt(txtf.getText());
        for (int i = 0; i < 350; i++) {
            r = random.nextInt(max);
            a.add(r);
        }
        int count = 0;
        txta1.clear();
        for (int i = 0; i < 350; i++) {
            txta1.appendText(a.get(i).toString() + "," + " ");
            count++;
            if (count == 10) {
                txta1.appendText("\n");
                count = 0;
            }
        }
    }

    @FXML
    private void handleExcute(ActionEvent e) {

        txta2.clear();

        int[] frames = new int[cbox.getValue()];

        for (int i = 0; i < frames.length; i++) {
            frames[i] = -1;
        }

        int num = 0;
        int missCount = 0;
        int hitCount = 0;
        int index;

        txta2.appendText("Request  ");
        for (int i = 0; i < frames.length; i++) {
            txta2.appendText("[" + i + "] ");
        }
        txta2.appendText("      HIT/MISS");
        txta2.appendText("\n");

        for (int i = 0; i < 350; i++) {

            if (checkFrame(frames, a.get(i))) {
                hitCount++;
                printFrames(frames, a.get(i), 1);

            } else {

                if (num < frames.length) {
                    frames[num] = a.get(i);
                    num++;
                    missCount++;
                    printFrames(frames, a.get(i), 0);

                } else if (cbox1.getValue().equals("OPT")) {
                    index = findOptimal(frames, i + 1);
                    frames[index] = a.get(i);
                    missCount++;
                    printFrames(frames, a.get(i), 0);

                } else if (cbox1.getValue().equals("LRU")) {
                    index = findLRU(frames, i - 1);
                    frames[index] = a.get(i);
                    missCount++;
                    printFrames(frames, a.get(i), 0);
                }
            }
        }
        txtf1.setText(Integer.toString(missCount));
        txtf2.setText(Integer.toString(hitCount));
    }

    private void printFrames(int[] frames, int request, int b) {
        txta2.appendText("    " + request + "          ");
        for (int i = 0; i < frames.length; i++) {
            if (frames[i] != -1) {
                txta2.appendText(frames[i] + "    ");
            } else {
                txta2.appendText('-' + "    ");
            }
        }
        if (b == 1) {
            txta2.appendText("     HIT \n");
        } else {
            txta2.appendText("     MISS \n");
        }
    }

    private boolean checkFrame(int[] frames, int f) {
        for (int i = 0; i < frames.length; i++) {
            if (frames[i] == f) {
                return true;
            }
        }
        return false;
    }

    private int findOptimal(int[] frames, int f) {

        int[] index = new int[frames.length];
        for (int i = 0; i < index.length; i++) {
            index[i] = -1;
        }

        boolean[] b = new boolean[frames.length];
        for (int i = 0; i < b.length; i++) {
            b[i] = true;
        }

        for (int i = f; i < a.size(); i++) {
            for (int j = 0; j < frames.length; j++) {
                if (frames[j] == a.get(i) && b[j]) {
                    index[j] = i;
                    b[j] = false;
                }
            }
        }

        for (int i = 0; i < index.length; i++) {
            if (index[i] == -1) {
                return i;
            }
        }

        int max = index[0];
        int in = 0;
        for (int i = 1; i < index.length; i++) {
            if (index[i] > max) {
                max = index[i];
                in = i;
            }
        }
        return in;
    }

    private int findLRU(int[] frames, int f) {

        int[] index = new int[frames.length];
        for (int i = 0; i < index.length; i++) {
            index[i] = -1;
        }

        boolean[] b = new boolean[frames.length];
        for (int i = 0; i < b.length; i++) {
            b[i] = true;
        }
        int i = f;
        while (i != 0) {
            for (int j = 0; j < frames.length; j++) {
                if (frames[j] == a.get(i) && b[j]) {
                    index[j] = i;
                    b[j] = false;
                }
            }
            i--;
        }

        for (int j = 0; j < index.length; j++) {
            if (index[j] == -1) {
                return j;
            }
        }

        int min = index[0];
        int in = 0;
        for (int j = 1; j < index.length; j++) {
            if (index[j] < min) {
                min = index[j];
                in = j;
            }
        }
        return in;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        int a = 4;
        int b = 5;
        int c = 7;

        cbox.getItems().addAll(a, b, c);

        String s = "OPT";
        String s1 = "LRU";

        cbox1.getItems().addAll(s, s1);
    }
}
