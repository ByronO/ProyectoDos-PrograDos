/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author byron
 */
public class Window extends Application {

    private MenuBar bar;
    private Menu menu;
    private MenuItem item;
    private int sizePix, sizeMosaic;
    
    private Images i;

    //va guardando las diferentes imagenes que se carguen para el mosaico 
    private ArrayList<Image> images;

    @Override
    public void start(Stage primaryStage) throws Exception {

        bar = new MenuBar();
        menu = new Menu("Image");
        item = new MenuItem("Load image");
        images = null;

        bar.getMenus().addAll(menu);
        menu.getItems().addAll(item);

        BorderPane root = new BorderPane();
        root.setTop(bar);

        //se usa para validar que en el TextField solo se ingresen numeros
        UnaryOperator<Change> integerFilter = change -> {
            String input = change.getText();
            if (input.matches("[0-9]*")) {
                return change;
            }
            return null;
        };

        item.setOnAction((ActionEvent event) -> {

            if (images == null) {
                images = new ArrayList<>();
                Pane g = new Pane();

                Button b = new Button("Select");
                TextField field = new TextField();
                field.setTextFormatter(new TextFormatter<String>(integerFilter));

                Label lbl = new Label("Select the size of the blocks in which you will divide the image");

                lbl.relocate(30, 20);
                field.relocate(30, 50);
                b.relocate(80, 90);

                g.getChildren().add(lbl);
                g.getChildren().add(field);
                g.getChildren().add(b);

                root.setLeft(g);

                b.setOnAction((ActionEvent event1) -> {

                    sizePix = Integer.parseInt(field.getText());
                    Label lbl3 = new Label("This size is invalid");
                    //Limite al tamaño en pixeles de los cadros ya que si es mayor al tamaño de la imagen nola pinta
                    if (sizePix < 10 || sizePix > 999) {
                        lbl3.relocate(210, 53);
                        lbl3.setTextFill(Color.web("RED"));
                        g.getChildren().add(lbl3);
                    } else {
                        Pane g2 = new Pane();

                        TextField field2 = new TextField();
                        field2.setTextFormatter(new TextFormatter<String>(integerFilter));
                        Label lbl2 = new Label("Select the size of the Mosaic");
                        Button b2 = new Button("Select");

                        b2.relocate(80, 90);
                        lbl2.relocate(30, 20);
                        field2.relocate(30, 50);

                        g2.getChildren().add(lbl2);
                        g2.getChildren().add(field2);
                        g2.getChildren().add(b2);

                        root.setLeft(g2);

                        b2.setOnAction((ActionEvent event2) -> {
                            sizeMosaic = Integer.parseInt(field2.getText());
                            Label lbl4 = new Label("This size is invalid");
                            /*limite al tamaño minimo del mosaico, se puede quitar
                            o agregar un limite para el tamaño maximo*/
                            if (sizeMosaic < 3) {
                                lbl4.relocate(210, 53);
                                lbl4.setTextFill(Color.web("RED"));
                                g2.getChildren().add(lbl4);
                            } else {

                                FileChooser fileChooser = new FileChooser();
                                File file = fileChooser.showOpenDialog(primaryStage);

                                if (file.isFile() && file.getName().contains(".jpg") || file.getName().contains(".png")) {

                                    Image image = new Image("file:" + file.getAbsolutePath());
                                    images.add(image);
                                    this.i = new Images(sizePix, sizeMosaic);
                                    ScrollPane scroll2 = new ScrollPane(i.Mosaic());
                                    scroll2.setMaxSize(600, 600);
                                    root.setLeft(i.image(images));
                                    root.setRight(scroll2);
                                }
                            }

                        });

                    }
                });
            } else {
                FileChooser fileChooser = new FileChooser();
                File file = fileChooser.showOpenDialog(primaryStage);

                if (file.isFile() && file.getName().contains(".jpg") || file.getName().contains(".png")) {

                    Image image = new Image("file:" + file.getAbsolutePath());
                    images.add(image);
                    root.setLeft(this.i.image(images));
                }
            }
        });

        Scene scene = new Scene(root, 1200, 700);

        primaryStage.setTitle(
                "Window");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();

        setUserAgentStylesheet(STYLESHEET_CASPIAN);

    }

}
