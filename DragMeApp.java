/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.drag.me;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 *
 * @author christian
 */
public class DragMeApp extends Application {

    private Map<String, ImageView> sampleMap = new HashMap<>();
    
    private static final String KEY = "Bruce Wayne";
    
    private Parent getRoot() {
        
        Rectangle field = new Rectangle(0, 0, 300, 300);
        field.setFill(Color.ORANGERED);
      
        Image batLogoImage = new Image("batman.png");
        ImageView batLogo = new ImageView(batLogoImage);
  
        sampleMap.put(KEY, batLogo);
        
        batLogo.setX(300);
        batLogo.setY(50);

                
        batLogo.setOnDragDetected(mouseEvent -> {
            ImageView batman = (ImageView) mouseEvent.getSource();
            
            System.out.println("DRAG DETECTED ---> " + batman);
            
            Dragboard dragboard = batman.startDragAndDrop(TransferMode.MOVE);
            
            ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.putImage(batman.getImage());
            
            clipboardContent.putString(KEY);
            
            dragboard.setContent(clipboardContent);
            
            batman.setVisible(false);
            
            mouseEvent.consume();
            
        });
        
        
        AtomicBoolean sysOutDragOverOnce = new AtomicBoolean(false);
        
        field.setOnDragOver(dragEvent -> {
        
            if (!sysOutDragOverOnce.getAndSet(true)) {
                System.out.println("DRAG OVER FIELD ---> " + dragEvent.getSource());
            }           
            
            if (KEY.equals(dragEvent.getDragboard().getString())) {
                dragEvent.acceptTransferModes(TransferMode.MOVE);
            }
            
            dragEvent.consume();
            
        });
        
        field.setOnDragEntered(dragEvent -> {
            
            System.out.println("DRAG ENTERED ---> " + dragEvent.getSource());
            
            sysOutDragOverOnce.set(false);
            
            dragEvent.consume();
            
        });
        
        field.setOnDragExited(dragEvent -> {
            
            System.out.println("DRAG EXITED ---> " + dragEvent.getSource());
            
            dragEvent.consume();
            
        });
        
        field.setOnDragDropped(dragEvent -> {

            System.out.println("DRAG DROPPED ---> " + dragEvent.getSource());

            Dragboard dragboard = dragEvent.getDragboard();
            
            ClipboardContent clipboardContent = new ClipboardContent();
            
            clipboardContent.putString("a1 as coordinate or so");
            
            dragboard.setContent(clipboardContent);
            
            dragEvent.setDropCompleted(true);
            
            dragEvent.consume();
             
        });

        batLogo.setOnDragDone(dragEvent -> {
            
            ImageView batman = (ImageView) dragEvent.getSource();

            System.out.println("DRAG DONE ---> " + batman);
            
            System.out.println("Given field coordinate = " + dragEvent.getDragboard().getString());
            
            batman.setVisible(true);
            
            dragEvent.consume();
        });
        
        Group group = new Group(field, batLogo);              
        
        StackPane pane = new StackPane(group);        
        
        return pane;
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {

        Scene scene = new Scene(getRoot(), 1000, 750);
        
        primaryStage.setScene(scene);
        primaryStage.setTitle("The Bat Drag");
        primaryStage.show();
        

    }
    
    public static void main(String... args) {
        launch(args);
    }
    
}
