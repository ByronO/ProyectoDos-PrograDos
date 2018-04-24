/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author byron
 */
public class Mosaic implements Serializable{
    
    private String nameMosaic, mosaicPath;
    private String [][] imagesPath;
    private int sizePix, sizeMosaic;
    
    private ArrayList<String> images;

    public Mosaic(String nameOfProyect, int sizePix, int sizeMosaic, ArrayList<String> images, String mosaicPath) {
        this.nameMosaic = nameOfProyect;
//        this.imagesPath = new String[a][a];
        this.sizePix = sizePix;
        this.images = images;
        this.sizeMosaic = sizeMosaic;
        this.mosaicPath = "file:" + mosaicPath;
    }
    public String getNameMosaic() {
        return nameMosaic;
    }

    public void setNameMosaic(String nameMosaic) {
        this.nameMosaic = nameMosaic;
    }

    public String[][] getImagesPath() {
        return imagesPath;
    }

    public void setImagesPath(int a, int b, String image) {
        this.imagesPath [a][b]= image;
    }

    public int getSizePix() {
        return sizePix;
    }

    public void setSizePix(int sizePix) {
        this.sizePix = sizePix;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public int getSizeMosaic() {
        return sizeMosaic;
    }

    public void setSizeMosaic(int sizeMosaic) {
        this.sizeMosaic = sizeMosaic;
    }

    public String getMosaicPath() {
        return mosaicPath;
    }

    public void setMosaicPath(String mosaicPath) {
        this.mosaicPath = mosaicPath;
    }
    
    
    
}
