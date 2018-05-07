/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.transform.Rotate;

/**
 *
 * @author byron
 */
public class MosaicInterface {

    private GridPane g;
    private int aux;
    private Button b1, b2;
    private int sizePix, sizeMosaic, blocksX, blocksY;
    private WritableImage writable; //convierte pixeles en una imagen
    private PixelReader pixel; //se encarga de leer pixel por pixel
    public Image subImage;
    private MenuItem item, item1, item2, item3, item4;

    public MosaicInterface(int sizePix, int sizeMosaic) {
        b1 = new Button("<");
        b2 = new Button(">");
        g = new GridPane();
        this.sizePix = sizePix;
        this.sizeMosaic = sizeMosaic;
        item = new MenuItem("Left rotate");
        item1 = new MenuItem("Right rotate");
        item2 = new MenuItem("Invert Y");
        item3 = new MenuItem("Invert X");
        item4 = new MenuItem("Delete");

    }

    /*crea un gridpane con los dos botones que controlan el ArrayList de imagenes 
    y en medio de ellos pega el gridpane que retorna el metodo splitImage con la 
    imagen ya dividida*/
    public GridPane image(ArrayList<Image> images) {
        //aux es el indice de la ultima image que se cargo
        aux = images.size() - 1;

        g.getChildren().clear();

        ScrollPane scroll = new ScrollPane(splitImage(images.get(aux)));
        scroll.setMaxSize(600, 600);

        g.add(b1, 0, 0);
        g.add(scroll, 1, 0);
        g.add(b2, 2, 0);

        b1.setOnAction((ActionEvent event) -> {
            if (aux > 0) {
                aux--;
                scroll.setContent(splitImage(images.get(aux)));
            }
        });

        b2.setOnAction((ActionEvent event) -> {
            if (aux < images.size() - 1) {
                aux++;
                scroll.setContent(splitImage(images.get(aux)));
            }
        });

        return g;
    }

    /*recive la imagen que tiene que dividir, hace el procedimiento y retorna el
    Gridpane con la imagen lista*/
    public GridPane splitImage(Image a) {

        /*calcula la cantidad de veces que se puede dividir la imagen con el
        tamaño de los cuadros ingresado al principio*/
        blocksX = (int) a.getWidth() / sizePix;
        blocksY = (int) a.getHeight() / sizePix;

        /*Controla si el tamaño de los cuadros es mayor al tamaño
        de la imagen*/
        if (sizePix > a.getWidth()) {
            blocksX = 1;
            sizePix = (int) a.getWidth();
        }
        if (sizePix > a.getHeight()) {
            blocksY = 1;
            sizePix = (int) a.getHeight();

        }

        GridPane g = new GridPane();
        int x = 0;
        int y = 0;

        /*estos for recorren el GridPane como si fuera una matriz corriente del tamaño
        [blocksX][blocksY] y va agregando en cada posicion el hBox con el trozo de imagen*/
        for (int i = 0; i <= blocksX; i++) {
            for (int j = 0; j <= blocksY; j++) {
                //if controla si ya se llego al ultimo cuadro en el que se puede dividir la imagen
                if (x + sizePix <= a.getWidth() && y + sizePix <= a.getHeight()) {
                    this.pixel = a.getPixelReader(); //recibe los pixeles de la imagen
                    this.writable = new WritableImage(this.pixel, x, y, sizePix, sizePix);//parte la imagen en el x,y del tamaño ingresado 
                    ImageView imageS = new ImageView(writable);

                    /*cada ImageView con el pedazo de imagen que le toca se agrega a un
                    Hbox con un borde para que se vea cuadriculado*/
                    HBox hBox_outter = new HBox();
                    String style_outter = "-fx-border-color: yellow;"
                            + "-fx-border-width: 1;"
                            + "-fx-border-style: dotted;";
                    hBox_outter.setStyle(style_outter);
                    hBox_outter.getChildren().add(imageS);

                    //Obtiene la imagen que tiene el ImageView que esta dentro del Hbox al que se le de click
                    hBox_outter.setOnMouseClicked((event) -> {
                        this.subImage = imageS.getImage();
                    });

                    g.add(hBox_outter, i, j);

                    //mueve y hasta el siguiente punto donde tiene que empezar a cortar
                    y += sizePix;
                }
            }
            //mueve x hasta el siguiente punto donde tiene que empezar a cortar
            x += sizePix;
            //vuelve a y al inicio para volver a recortar desde arriba hacia abajo
            y = 0;
        }

        return g;
    }

    //Crea y retorna un GridPane conn HBox vacios en el que se monta el mosaico
    public GridPane Mosaic() {
        GridPane g = new GridPane();
        for (int i = 0; i < sizeMosaic; i++) {
            for (int j = 0; j < sizeMosaic; j++) {
                ImageView imageS = new ImageView();
                HBox hBox_outter = new HBox();
                
                String style_outter = "-fx-border-color: red;"
                        + "-fx-border-width: 1;"
                        + "-fx-border-style: dotted;";
                hBox_outter.setStyle(style_outter);
                
                hBox_outter.setPrefSize(sizePix, sizePix);


                /*Se le da al HBox vacio al que se le de click un ImageView con el atributo 
                subImage que tiene el trozo de imagen obtenido al darle click a una parte de la imagen*/
                hBox_outter.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            imageS.setRotate(0);
                            imageS.setImage(subImage);
                            imageS.getTransforms().clear();

                            //limpia el imageView en caso de que tenga algo dentro porque sino tira error
                            hBox_outter.getChildren().clear();
                            //agrega el trozo de imagen
                            hBox_outter.getChildren().add(imageS);
                        } else if (event.getButton() == MouseButton.SECONDARY) {
                            ContextMenu context = new ContextMenu();
                            context.getItems().addAll(item, item1, item2, item3, item4);

                            item.setOnAction((eventRotateL) -> {
                                imageS.setRotate(imageS.getRotate() - 90);
                            });
                            item1.setOnAction((eventRotateR) -> {
                                imageS.setRotate(imageS.getRotate() + 90);
                            });
                            item2.setOnAction((eventInvertY) -> {
                                imageS.getTransforms().add(new Rotate(180, 0, sizePix / 2, 0, Rotate.X_AXIS));
                            });
                            item3.setOnAction((eventInvertX) -> {
                                imageS.getTransforms().add(new Rotate(180, sizePix / 2, 0, 0, Rotate.Y_AXIS));
                            });
                            item4.setOnAction((eventDelete) -> {
                                imageS.setImage(null);
                                String style_outter = "-fx-border-color: red;"
                                        + "-fx-border-width: 1;"
                                        + "-fx-border-style: dotted;";
                                hBox_outter.setStyle(style_outter);
                            });

                            context.show(hBox_outter, event.getScreenX(), event.getScreenY());
                        }

                    }
                });

                g.add(hBox_outter, i, j);
            }
        }

        return g;
    }

//metodo para dividir la imagen del mosaico que se va a cargar
    public GridPane splitMosaic(Image a, int sizeMosaic, int sizePix) {

        GridPane g = new GridPane();
        int x = 0;
        int y = 0;
        
        /*estos for recorren el GridPane como si fuera una matriz corriente del tamaño
        [sizeMosaic][sizeMosaic] y va agregando en cada posicion el hBox con el trozo de imagen*/
        for (int i = 0; i <= sizeMosaic; i++) {
            for (int j = 0; j <= sizeMosaic; j++) {
                //controla si ya se llego al ultimo cuadro en el que se puede dividir la imagen
                if (x + sizePix <= a.getWidth() && y + sizePix <= a.getHeight()) {
                    PixelReader pixel = a.getPixelReader(); //recibe los pixeles de la imagen
                    this.writable = new WritableImage(pixel, x, y, sizePix, sizePix);//parte la imagen en el x,y del tamaño ingresado 
                    ImageView imageS = new ImageView(writable);

                    HBox hBox_outter = new HBox();
                    String style_outter = "-fx-border-color: red;"
                            + "-fx-border-width: 1;"
                            + "-fx-border-style: dotted;";
                    hBox_outter.setStyle(style_outter);
                    hBox_outter.getChildren().add(imageS);

                    hBox_outter.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            if (event.getButton() == MouseButton.PRIMARY) {
                                imageS.setRotate(0);
                                imageS.setImage(subImage);
                                imageS.getTransforms().clear();

                                //limpia el imageView en caso de que tenga algo dentro porque sino tira error
                                hBox_outter.getChildren().clear();
                                //agrega el trozo de imagen
                                hBox_outter.getChildren().add(imageS);
                            } else if (event.getButton() == MouseButton.SECONDARY) {
                                ContextMenu context = new ContextMenu();
                                context.getItems().addAll(item, item1, item2, item3, item4);

                                item.setOnAction((eventRotateL) -> {
                                    imageS.setRotate(imageS.getRotate() - 90);
                                });
                                item1.setOnAction((eventRotateR) -> {
                                    imageS.setRotate(imageS.getRotate() + 90);
                                });
                                item2.setOnAction((eventInvertY) -> {
                                    imageS.getTransforms().add(new Rotate(180, 0, sizePix / 2, 0, Rotate.X_AXIS));
                                });
                                item3.setOnAction((eventInvertX) -> {
                                    imageS.getTransforms().add(new Rotate(180, sizePix / 2, 0, 0, Rotate.Y_AXIS));
                                });
                                item4.setOnAction((eventDelete) -> {
                                    imageS.setImage(null);
                                    String style_outter = "-fx-border-color: red;"
                                            + "-fx-border-width: 1;"
                                            + "-fx-border-style: dotted;";
                                    hBox_outter.setStyle(style_outter);
                                });

                                context.show(hBox_outter, event.getScreenX(), event.getScreenY());
                            }
                        }
                    });

                    g.add(hBox_outter, i, j);

                    //mueve y hasta el siguiente punto donde tiene que empezar a cortar
                    y += sizePix;
                }
            }
            //mueve x hasta el siguiente punto donde tiene que empezar a cortar
            x += sizePix;
            //vuelve a y al inicio para volver a recortar desde arriba hacia abajo
            y = 0;
        }

        return g;
    }

}
