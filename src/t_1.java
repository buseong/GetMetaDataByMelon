import com.mpatric.mp3agic.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class t_1 {

    public static void main(String[] args) {
        T_3 t3 = new T_3();
        t3.main();
    }
}

class T_3{

    public T_2 obj = new T_2();

    public void main() {
        this.obj.setTag(T_2.tagEnum.Artist, "1111");
        this.obj.setTag(T_2.tagEnum.AlbumName, "2222");

        System.out.println(this.obj.getTag(T_2.tagEnum.Artist));
        System.out.println(this.obj.getTag(T_2.tagEnum.AlbumName));
    }}
class T_2{

    public enum tagEnum {
        Year,
        Genre,
        AlbumName,
        Title,
        TrackNum,
        Artist,
        AlbumArtist,
        Lyrics,
        AlbumCover
    }

    public HashMap<tagEnum, String> tag = new HashMap<>();

    public void setTag(tagEnum tag, String value){
        this.tag.put(tag, value);
    }

    public String getTag(tagEnum tag){
        return this.tag.get(tag);
    }

}