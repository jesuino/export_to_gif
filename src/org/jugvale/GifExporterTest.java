/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jugvale;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author william
 */
public class GifExporterTest extends Application {

    Transition master;

    @Override
    public void start(Stage stage) {

        Label lblTarget = new Label("JavaFX");
        StackPane spContainer = new StackPane(lblTarget);
        Button btnStart = new Button("Start");
        Button btnStop = new Button("Stop");
        CheckBox chkExport = new CheckBox("Export to gif");
        VBox root = new VBox(10, spContainer, new HBox(30, btnStart, btnStop, chkExport));

        spContainer.setPrefSize(300, 250);
        lblTarget.setFont(Font.font("Tahoma", FontWeight.BOLD, FontPosture.REGULAR, 40));
      //  lblTarget.setTextFill(Color.DARKGRAY);

        Scene scene = new Scene(root, 600, 500);
        createAnimations(lblTarget);

        stage.setTitle("Hello World!");
        stage.setScene(scene);
        stage.show();

        btnStart.setOnAction(e -> {
            try {
                master.playFromStart();
                if(chkExport.isSelected())
                    GitExporterFX.captureNow(spContainer, (int) master.getTotalDuration().toMillis(), "/home/wsiqueir/jfx2.gif", 100, true);
            } catch (IOException ex) {
                Logger.getLogger(GifExporterTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        btnStop.setOnAction(e -> master.stop());
        btnStart.disableProperty().bind(master.statusProperty().isEqualTo(Transition.Status.RUNNING));
    }

    void createAnimations(Node target) {
        Duration firstDuration = Duration.millis(2000);
        ScaleTransition st = new ScaleTransition(firstDuration, target);
        st.setFromX(0.1);
        st.setToX(1);
        st.setFromY(0.1);
        st.setToY(1);
        st.setInterpolator(Interpolator.LINEAR);

        RotateTransition rt = new RotateTransition(firstDuration.divide(2), target);
        rt.setByAngle(360);
        rt.setInterpolator(Interpolator.LINEAR);
        rt.setCycleCount(2);

        FadeTransition ft = new FadeTransition(Duration.millis(300), target);
        ft.setFromValue(1);
        ft.setToValue(0.1);
        ft.setCycleCount(2);
        ft.setAutoReverse(true);

        ParallelTransition pt = new ParallelTransition(st, rt);
        master = new SequentialTransition(pt, ft);
    }

}
