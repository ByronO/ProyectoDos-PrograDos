/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 *
 * @author byron
 */
public class Images {

    private GridPane g;
    private int aux;
    private Button b1, b2;
    private int sizePix, blocksX, blocksY;
    private WritableImage writable; //convierte pixeles en una imagen
    private PixelReader pixel; //se encarga de leer pixel por pixel

    public Images(int sizePix) {
        b1 = new Button("<");
        b2 = new Button(">");
        g = new GridPane();
//        g.setPadding(new Insets(0, 300, 200, 0));
        this.sizePix = sizePix;
    }

    public GridPane image(ArrayList<Image> images) {
        aux = images.size() - 1;

        g.getChildren().clear();

        ScrollPane scroll = new ScrollPane(splitImage(images.get(aux)));
        scroll.setMaxSize(600, 600);
        g.add(scroll, 1, 0);
        g.add(b1, 0, 0);
        g.add(b2, 2, 0);

        b1.setOnAction((ActionEvent event) -> {
            if (aux > 0) {
                aux--;
                ScrollPane scroll2 = new ScrollPane(splitImage(images.get(aux)));
                scroll2.setMaxSize(600, 600);
                g.add(scroll2, 1, 0);
            }
        });

        b2.setOnAction((ActionEvent event) -> {
            if (aux < images.size() - 1) {
                aux++;
                ScrollPane scroll2 = new ScrollPane(splitImage(images.get(aux)));
                scroll2.setMaxSize(600, 600);
                g.add(scroll2, 1, 0);
            }
        });

        

        return g;
    }

    public GridPane splitImage(Image a) {
        blocksX = (int) a.getWidth() / sizePix;
        blocksY = (int) a.getHeight() / sizePix;

        if (sizePix > a.getWidth()) {
            blocksX = 1;
            sizePix = (int) a.getWidth();
        } else if (sizePix > a.getHeight()) {
            blocksY = 1;
            sizePix = (int) a.getHeight();

        }

//        ImageView[][] imageS = new ImageView[blocksX][blocksY];
        GridPane g = new GridPane();
        int x = 0;
        int y = 0;
        for (int i = 0; i <= blocksX; i++) {
            for (int j = 0; j <= blocksY; j++) {

                if (x + sizePix < a.getWidth() && y + sizePix < a.getHeight()) {
                    this.pixel = a.getPixelReader(); //recibe los pixeles de la imagen
                    this.writable = new WritableImage(this.pixel, x, y, sizePix, sizePix);
                    ImageView imageS = new ImageView(writable);
//                //Outter border
                    HBox hBox_outter = new HBox();
                    String style_outter = "-fx-border-color: yellow;"
                            + "-fx-border-width: 1;"
                            + "-fx-border-style: dotted;";
                    hBox_outter.setStyle(style_outter);

                    hBox_outter.getChildren().add(imageS);
                    g.add(hBox_outter, i, j);

                    y += sizePix;
                }
            }
            x += sizePix;
            y = 0;
        }

        return g;
    }

    

}
