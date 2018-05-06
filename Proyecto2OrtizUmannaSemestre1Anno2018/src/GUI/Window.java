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
import javafx.scene.Node;
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
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
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
    private MenuItem itemNewProyect, itemExportMosaic, itemSaveProyect, itemLoadProyect, itemLoadImage;
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
        menu = new Menu("Mosaic");
        menu1 = new Menu("Proyect");

        itemNewProyect = new MenuItem("New mosaic");
        itemLoadImage = new MenuItem("Load image");
        itemExportMosaic = new MenuItem("Export mosaic");
        itemSaveProyect = new MenuItem("Save proyect");
        itemLoadProyect = new MenuItem("Load proyect");
        images = null;
        scroll2 = null;

        bar.getMenus().addAll(menu, menu1);
        menu.getItems().addAll(itemNewProyect);
        menu1.getItems().addAll(itemLoadProyect);

        this.mosaicData = new MosaicData();

        BorderPane root = new BorderPane();
        String imageBackground = "/assets/a400a3da30d7aa298406759adb958cd0.jpg";
        root.setStyle("-fx-background-image: url('" + imageBackground + "'); "
                + "-fx-background-position: center center; "
        );
        root.setTop(bar);

        //se usa para validar que en el TextField solo se ingresen numeros
        UnaryOperator<Change> integerFilter = change -> {
            String input = change.getText();
            if (input.matches("[0-9]*")) {
                return change;
            }
            return null;
        };

        itemNewProyect.setOnAction((ActionEvent event) -> {

            if (images == null) {
                images = new ArrayList<>();
                imagesPath = new ArrayList<>();

                Pane g = new Pane();

                Button b = new Button("Select");
                TextField field = new TextField();
                field.setTextFormatter(new TextFormatter<String>(integerFilter));
                Label lbl = new Label("Select the size of the blocks in which you will divide the image");
                lbl.setTextFill(Color.WHITE);
                TextField field2 = new TextField();
                field2.setTextFormatter(new TextFormatter<String>(integerFilter));
                Label lbl2 = new Label("Select the size of the Mosaic");
                lbl2.setTextFill(Color.WHITE);

                lbl.relocate(30, 20);
                field.relocate(30, 50);
                lbl2.relocate(30, 85);
                field2.relocate(30, 110);
                b.relocate(80, 145);

                g.getChildren().add(lbl);
                g.getChildren().add(field);
                g.getChildren().add(lbl2);
                g.getChildren().add(field2);
                g.getChildren().add(b);

                root.setLeft(g);

                b.setOnAction((ActionEvent event1) -> {

                    Label lbl3 = new Label("This size is invalid");
                    lbl3.setTextFill(Color.web("RED"));
                    sizePix = Integer.parseInt(field.getText());
                    sizeMosaic = Integer.parseInt(field2.getText());
                    //Limite al tamaño en pixeles de los cadros ya que si es mayor al tamaño de la imagen nola pinta
                    if (sizePix < 30 || sizePix > 500) {
                        lbl3.relocate(210, 53);
                        g.getChildren().add(lbl3);
                    } else if (sizeMosaic < 3) {
                        lbl3.relocate(210, 113);
                        g.getChildren().add(lbl3);
                    } else {
                        FileChooser fileChooser = new FileChooser();
                        File file = fileChooser.showOpenDialog(primaryStage);

                        if (file.isFile() && file.getName().contains(".jpg") || file.getName().contains(".png")) {

                            Image image = new Image("file:" + file.getAbsolutePath());
                            images.add(image);
                            imagesPath.add("file:" + file.getAbsolutePath());
                            this.m = new MosaicInterface(sizePix, sizeMosaic);

                            scroll2 = new ScrollPane(m.Mosaic());
                            scroll2.setPrefSize(600, 600);

                            menu.getItems().add(itemLoadImage);
                            menu.getItems().addAll(itemExportMosaic);
                            menu1.getItems().addAll(itemSaveProyect);

                            root.setLeft(m.image(images));
                            root.setRight(scroll2);
                            root.setTop(bar);
                        }
                    }

                });
            } else {
                int dialogButton = JOptionPane.showConfirmDialog(null, "Do you want to advance? all changes that have not been previously saved will be lost", "Warning", JOptionPane.YES_NO_OPTION);
                if (dialogButton == 0) {
                    root.setRight(null);

                    images = new ArrayList<>();
                    imagesPath = new ArrayList<>();

                    Pane g = new Pane();

                    Button b = new Button("Select");
                    TextField field = new TextField();
                    field.setTextFormatter(new TextFormatter<String>(integerFilter));
                    Label lbl = new Label("Select the size of the blocks in which you will divide the image");
                    lbl.setTextFill(Color.WHITE);
                    TextField field2 = new TextField();
                    field2.setTextFormatter(new TextFormatter<String>(integerFilter));
                    Label lbl2 = new Label("Select the size of the Mosaic");
                    lbl2.setTextFill(Color.WHITE);

                    lbl.relocate(30, 20);
                    field.relocate(30, 50);
                    lbl2.relocate(30, 85);
                    field2.relocate(30, 110);
                    b.relocate(80, 145);

                    g.getChildren().add(lbl);
                    g.getChildren().add(field);
                    g.getChildren().add(lbl2);
                    g.getChildren().add(field2);
                    g.getChildren().add(b);

                    root.setLeft(g);

                    b.setOnAction((ActionEvent event1) -> {

                        Label lbl3 = new Label("This size is invalid");
                        lbl3.setTextFill(Color.web("RED"));
                        sizePix = Integer.parseInt(field.getText());
                        sizeMosaic = Integer.parseInt(field2.getText());
                        //Limite al tamaño en pixeles de los cadros ya que si es mayor al tamaño de la imagen nola pinta
                        if (sizePix < 30 || sizePix > 500) {
                            lbl3.relocate(210, 53);
                            g.getChildren().add(lbl3);
                        } else if (sizeMosaic < 3) {
                            lbl3.relocate(210, 113);
                            g.getChildren().add(lbl3);
                        } else {
                            FileChooser fileChooser = new FileChooser();
                            File file = fileChooser.showOpenDialog(primaryStage);

                            if (file.isFile() && file.getName().contains(".jpg") || file.getName().contains(".png")) {

                                Image image = new Image("file:" + file.getAbsolutePath());
                                images.add(image);
                                imagesPath.add("file:" + file.getAbsolutePath());
                                this.m = new MosaicInterface(sizePix, sizeMosaic);

                                scroll2 = new ScrollPane(m.Mosaic());
                                scroll2.setPrefSize(600, 600);

                                root.setLeft(m.image(images));
                                root.setRight(scroll2);
                                root.setTop(bar);
                            }
                        }

                    });

                }
            }
        });

        itemExportMosaic.setOnAction((event3) -> {
            FileChooser jF1 = new FileChooser();
            jF1.setTitle("Save Image");
            String ruta = "";
            try {
                File ruta1 = jF1.showSaveDialog(null);
                ruta = ruta1.getAbsolutePath();

                if (this.scroll2 != null) {
                    GridPane gaux = (GridPane) scroll2.getContent();
                    gaux.getChildren().forEach((Node panel) -> {
                        panel.setStyle(null);
                    });

                    ScrollPane sAux = new ScrollPane(gaux);
                    SnapshotParameters parameters = new SnapshotParameters();
                    parameters.setFill(Color.TRANSPARENT);
                    WritableImage imageFinal = sAux.getContent().snapshot(parameters, null);
                    File imagePath = new File(ruta + ".png");

                    try {
                        ImageIO.write(SwingFXUtils.fromFXImage(imageFinal, null), "png", imagePath);
                        gaux.getChildren().forEach((Node panel) -> {
                            panel.setStyle("-fx-border-color: red;"
                                    + "-fx-border-width: 1;"
                                    + "-fx-border-style: dotted;");
                        });
                    } catch (IOException ex) {
                        Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        });

        itemLoadImage.setOnAction((eventLoadImage) -> {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(primaryStage);

            if (file.isFile() && file.getName().contains(".jpg") || file.getName().contains(".png")) {

                Image image = new Image("file:" + file.getAbsolutePath());
                images.add(image);
                imagesPath.add("file:" + file.getAbsolutePath());
                root.setLeft(this.m.image(images));
            }
        });

        itemSaveProyect.setOnAction((event6) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Proyect");
            File directory = fileChooser.showSaveDialog(primaryStage);

            
            String path = directory.getPath();
            System.err.println(path);
            this.nameOfProyect = directory.getName();
            System.err.println(this.nameOfProyect);
            File file = new File(path);
            file.mkdirs();
            mosaicData = new MosaicData();

            if (this.scroll2 != null) {
                GridPane gaux = (GridPane) scroll2.getContent();
                    gaux.getChildren().forEach((Node panel) -> {
                        panel.setStyle(null);
                    });

                    ScrollPane sAux = new ScrollPane(gaux);
                    SnapshotParameters parameters = new SnapshotParameters();
                    parameters.setFill(Color.TRANSPARENT);
                    WritableImage imageFinal = sAux.getContent().snapshot(parameters, null);
                    File imagePath = new File(path + "\\"+ this.nameOfProyect+".png");

                    try {
                        ImageIO.write(SwingFXUtils.fromFXImage(imageFinal, null), "png",imagePath);
                        gaux.getChildren().forEach((Node panel) -> {
                            panel.setStyle("-fx-border-color: red;"
                                    + "-fx-border-width: 1;"
                                    + "-fx-border-style: dotted;");
                            });
                        mosaic = new Mosaic(nameOfProyect, sizePix, sizeMosaic, imagesPath, directory.getPath());
                        mosaicData.saveProyect(mosaic,path + "\\"+ this.nameOfProyect);

                } catch (IOException ex) {
                    Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });

        itemLoadProyect.setOnAction((eventLoad) -> {

                images = new ArrayList<>();
                FileChooser fileChooser = new FileChooser();
                File file = fileChooser.showOpenDialog(primaryStage);
                String path = file.getPath();

                if (file.isFile() && file.getName().contains(".dat")) {
                    mosaicData = new MosaicData();

                    ArrayList<Mosaic> aux;
                    try {
                        aux = this.mosaicData.loadProyect(path);
                            mosaic = aux.get(0);


                    if (images == null) {
                        for (int i = 0; i < mosaic.getImages().size(); i++) {
                            Image image = new Image(mosaic.getImages().get(i));
                            images.add(image);
                        }
                    } else {
                        images.clear();
                        for (int i = 0; i < mosaic.getImages().size(); i++) {
                            Image image = new Image(mosaic.getImages().get(i));

                            images.add(image);
                        }
                    }
                    Image mosaicImage = new Image(mosaic.getMosaicPath());
                    imagesPath = mosaic.getImages();
                    this.m = new MosaicInterface(mosaic.getSizePix(), mosaic.getSizeMosaic());

                    scroll2 = new ScrollPane(m.splitMosaic(mosaicImage, mosaic.getSizeMosaic(), mosaic.getSizePix()));
                    scroll2.setMaxHeight(600);
                    scroll2.setMaxWidth(600);
                    root.setLeft(m.image(images));
                    root.setRight(scroll2);
                    menu.getItems().addAll(itemLoadImage, itemExportMosaic);
                    menu1.getItems().addAll(itemSaveProyect);
                    
                    } catch (IOException ex) {
                        Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
                    }

//                    for (int i = 0; i < aux.size(); i++) {
//                        if (aux.get(i).getNameMosaic().equalsIgnoreCase(name)) {
//                            mosaic = aux.get(i);
//                        }
//                    }
//
//                    if (images == null) {
//                        for (int i = 0; i < mosaic.getImages().size(); i++) {
//                            Image image = new Image(mosaic.getImages().get(i));
//                            images.add(image);
//                        }
//                    } else {
//                        images.clear();
//                        for (int i = 0; i < mosaic.getImages().size(); i++) {
//                            Image image = new Image(mosaic.getImages().get(i));
//
//                            images.add(image);
//                        }
//                    }
//                    Image mosaicImage = new Image(mosaic.getMosaicPath());
//                    imagesPath = mosaic.getImages();
//                    this.m = new MosaicInterface(mosaic.getSizePix(), mosaic.getSizeMosaic());
//
//                    scroll2 = new ScrollPane(m.splitMosaic(mosaicImage, mosaic.getSizeMosaic(), mosaic.getSizePix()));
//                    scroll2.setMaxHeight(600);
//                    scroll2.setMaxWidth(600);
//                    root.setLeft(m.image(images));
//                    root.setRight(scroll2);
//                    menu.getItems().addAll(itemLoadImage, itemExportMosaic);
//                    menu1.getItems().addAll(itemSaveProyect);
                    
//                }
//            } catch (IOException ex) {
//                Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (ClassNotFoundException ex) {
//                Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
//            }
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
