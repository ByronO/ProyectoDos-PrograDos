/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 *
 * @author byron
 */
public class MosaicInterface {

    private GridPane g;
    private int sizePix, sizeMosaic;

    public MosaicInterface(int sizePix, int sizeMosaic) {
        g = new GridPane();
        this.sizePix = sizePix;
        this.sizeMosaic = sizeMosaic;
    }
    
    public GridPane Mosaic() {

        for (int i = 0; i < sizeMosaic; i++) {
            for (int j = 0; j < sizeMosaic; j++) {
                ImageView imageS = new ImageView();
                HBox hBox_outter = new HBox();
                String style_outter = "-fx-border-color: black;"
                        + "-fx-border-width: 1;"
                        + "-fx-border-style: dotted;";
                hBox_outter.setStyle(style_outter);

                hBox_outter.getChildren().add(imageS);
                hBox_outter.setPrefSize(sizePix, sizePix);

                g.add(hBox_outter, i, j);

            }
        }
        
        return g;
    }

}
