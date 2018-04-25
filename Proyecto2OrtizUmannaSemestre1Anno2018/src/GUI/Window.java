/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import data.MosaicData;
import domain.Mosaic;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
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
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
 *
 * @author byron
 */
public class Window extends Application {

    private MenuBar bar;
    private Menu menu, menu1;
    private MenuItem item, item1, item2, item3;
    private int sizePix, sizeMosaic;
    private String nameOfProyect;
    private ScrollPane scroll2;
    private MosaicInterface m;

    private Mosaic mosaic;
    private MosaicData mosaicData;

    //va guardando las diferentes imagenes que se carguen para el mosaico 
    private ArrayList<Image> images;
    private ArrayList<String> imagesPath;

    @Override
    public void start(Stage primaryStage) throws Exception {

        bar = new MenuBar();
        menu = new Menu("Image");
        menu1 = new Menu("Proyect");

        item = new MenuItem("Load image");
        item1 = new MenuItem("End Mosaic");
        item2 = new MenuItem("Save Proyect");
        item3 = new MenuItem("Load Proyect");
        images = null;
        scroll2 = null;

        bar.getMenus().addAll(menu, menu1);
        menu.getItems().addAll(item);
        menu1.getItems().addAll(item3);

        this.mosaicData = new MosaicData();

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
                imagesPath = new ArrayList<>();

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
                    if (sizePix < 30 || sizePix > 500) {
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
                                    imagesPath.add("file:" + file.getAbsolutePath());
                                    this.m = new MosaicInterface(sizePix, sizeMosaic);

                                    scroll2 = new ScrollPane(m.Mosaic());
                                    scroll2.setMaxHeight(600);
                                    scroll2.setMaxWidth(600);

                                    menu.getItems().addAll(item1);
                                    menu1.getItems().addAll(item2);

                                    int auxI = 0;
                                    item1.setOnAction((event3) -> {

                                    });

                                    root.setLeft(m.image(images));
                                    root.setRight(scroll2);
                                    root.setTop(bar);
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
                    imagesPath.add("file:" + file.getAbsolutePath());
                    root.setLeft(this.m.image(images));
                }
            }
        });

        item2.setOnAction((event6) -> {

            Pane g3 = new Pane();

            TextField field5 = new TextField();
            Label lbl5 = new Label("Enter the name of the proyect");
            Button b5 = new Button("Select");

            lbl5.relocate(100, 10);
            field5.relocate(100, 40);
            b5.relocate(100, 70);

            g3.getChildren().add(lbl5);
            g3.getChildren().add(field5);
            g3.getChildren().add(b5);

            root.setBottom(g3);

            b5.setOnAction((ActionEvent eventSave) -> {
                this.nameOfProyect = field5.getText();
                mosaicData = new MosaicData();

                if (this.scroll2 != null) {
                    ScrollPane sAux = new ScrollPane(scroll2.getContent());
//
//                    gaux.getChildren().forEach((Node panel) -> {
//                        String style_outter = "-fx-border-color: black;"
//                                + "-fx-border-width: 1;"
//                                + "-fx-border-style: dotted;";
//                        panel.setStyle(style_outter);
//
//                    });

                    WritableImage imageFinal = sAux.getContent().snapshot(new SnapshotParameters(), null);
                    File imagePath = new File(nameOfProyect + ".png");

                    try {
                        ImageIO.write(SwingFXUtils.fromFXImage(imageFinal, null), "png", imagePath);
                    } catch (IOException ex) {
                        Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    mosaic = new Mosaic(nameOfProyect, sizePix, sizeMosaic, imagesPath, imagePath.getAbsolutePath());
                }

                try {
                    mosaicData.saveProyect(mosaic);
                } catch (IOException ex) {
                    Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
                }

            });

        });

        item3.setOnAction((eventLoad) -> {
            try {

                ArrayList<Image> images = new ArrayList<>();

                String name = JOptionPane.showInputDialog("IAIAIA");
                mosaicData = new MosaicData();

                ArrayList<Mosaic> aux = this.mosaicData.loadProyect();

                for (int i = 0; i < aux.size(); i++) {
                    if (aux.get(i).getNameMosaic().equalsIgnoreCase(name)) {
                        mosaic = aux.get(i);
                    }
                }

                if (images == null) {
                    for (int i = 0; i < mosaic.getImages().size(); i++) {
                        Image image = new Image(mosaic.getImages().get(i));
                        images.add(image);
                    }
                } else {
                    images.clear();
                    for (int i = 0; i < mosaic.getImages().size(); i++) {
                        Image image = new Image(mosaic.getImages().get(i));
                        System.out.println(mosaic.getImages().get(i));

                        images.add(image);
                    }
                }
                Image mosaicImage = new Image(mosaic.getMosaicPath());
                
                this.m = new MosaicInterface(mosaic.getSizePix(), mosaic.getSizeMosaic());
                
                ScrollPane scroll3 = new ScrollPane(m.splitMosaic(mosaicImage, mosaic.getSizeMosaic(), mosaic.getSizePix()));
                scroll3.setMaxHeight(600);
                scroll3.setMaxWidth(600);

                
                System.out.println(mosaic.getMosaicPath());
                
                root.setLeft(m.image(images));
                root.setRight(scroll3);

            } catch (IOException ex) {
                Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
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
