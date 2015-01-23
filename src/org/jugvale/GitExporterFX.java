/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jugvale;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.scene.Parent;
import javafx.scene.image.WritableImage;
import javafx.util.Duration;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

/**
 *
 * @author william
 */
public class GitExporterFX {

    public static void captureNow(Parent target, int durationInMilis, String outputDirectory, int timeBetweenFramesMS, boolean loopContinuously) throws IOException {
        ImageOutputStream output = new FileImageOutputStream(new File(outputDirectory));
        final GifSequenceWriter gifWriter = new GifSequenceWriter(output, 3, timeBetweenFramesMS, loopContinuously);
        Consumer<Event> run = e -> {
            int w = (int) target.getBoundsInParent().getWidth();
            int h = (int) target.getBoundsInParent().getHeight();
            WritableImage img = new WritableImage(w, h);
            target.snapshot(null, img);
            try {
                gifWriter.writeToSequence(SwingFXUtils.fromFXImage(img, null));
            } catch (IOException ex) {
                Logger.getLogger(GitExporterFX.class.getName()).log(Level.SEVERE, null, ex);
            }
        };

        int keyFrameDuration = durationInMilis / (durationInMilis / timeBetweenFramesMS);
        final KeyFrame oneFrame = new KeyFrame(Duration.millis(keyFrameDuration), run::accept);
        Timeline t = new Timeline(durationInMilis, oneFrame);
        t.setCycleCount(durationInMilis / timeBetweenFramesMS);
        t.setOnFinished(e -> {
            try {                
                gifWriter.close();
                output.close();
            } catch (IOException ex) {
                Logger.getLogger(GitExporterFX.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        t.play();
    }
}
