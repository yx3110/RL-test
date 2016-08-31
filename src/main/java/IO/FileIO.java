package IO;

import Util.StateActionPair;
import Util.Values;
import javafx.beans.property.ObjectProperty;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Yang Xu on 2016/8/26.
 */
@SuppressWarnings("ALL")
public class FileIO {

    public static void saveMap(Map<StateActionPair, Values> map){
        try{
            FileOutputStream fileOut = new FileOutputStream("data.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(map);
            out.close();
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static HashMap<StateActionPair,Values> loadMap(String addr){
        HashMap<StateActionPair,Values> res = null;
        try{
            FileInputStream fileInputStream = new FileInputStream(addr);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            res = (HashMap<StateActionPair,Values>) objectInputStream.readObject();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        System.out.println("Map is load");
        System.out.println("Map size:"+res.size());
        return res;
    }
}
