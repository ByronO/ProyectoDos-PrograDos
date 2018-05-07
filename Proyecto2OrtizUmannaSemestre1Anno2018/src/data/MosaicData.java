
package data;

import domain.Mosaic;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Byron
 */
public class MosaicData {
    private String path;
    
    public MosaicData() {
        this.path = "Proyects.dat";
    }
    
    public void saveProyects(Mosaic mosaic) throws IOException, ClassNotFoundException{
        File file= new File(this.path);
        List<Mosaic> objetoList= new ArrayList<Mosaic>();
        
        if(file.exists()){
            ObjectInputStream objectInputStream= new ObjectInputStream(new FileInputStream(file));
            Object aux= objectInputStream.readObject();
            objetoList=(List<Mosaic>) aux;
            objectInputStream.close();
        }
        
        objetoList.add(mosaic);
        ObjectOutputStream output= new ObjectOutputStream(new FileOutputStream(file));
        output.writeUnshared(objetoList);
        output.close();
    }
    
    public void saveProyect(Mosaic mosaic, String path) throws IOException, ClassNotFoundException{
        File file= new File(path+".dat");
        
        if(file.exists()){
            ObjectInputStream objectInputStream= new ObjectInputStream(new FileInputStream(file));
            Object aux= objectInputStream.readObject();
            objectInputStream.close();
        }
        
        ObjectOutputStream output= new ObjectOutputStream(new FileOutputStream(file));
        output.writeUnshared(mosaic);
        output.close();
    }
    
     public Mosaic loadProyect(String path) throws IOException, ClassNotFoundException{
        File myFile =new File(path);
        Mosaic mosaic= new Mosaic();
        if(myFile.exists()){
            ObjectInputStream ObjectinputStream=new ObjectInputStream(new FileInputStream(myFile));
            Object aux= ObjectinputStream.readObject();
            mosaic=(Mosaic) aux;
            ObjectinputStream.close();
        }//If       
           
        return mosaic;
    }//obteneObjeto
}
