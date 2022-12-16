import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class t_1{
    public static void main(String[] args) {
//       String text = "123456789";
//       Integer num = 1243425455;
//       covertInt2StrPrint(num);
//       covertStr2IntPrint(text);
        t_filed temp = new t_filed();
        temp.strBuffer();
    }
    public static void start(){
        String name = "puppy";
        int age = 1;
        Start.start(name, age);
    }
    public static void covertInt2StrPrint(Integer num){
        String text = util.Int2Str(num);
        util.strPrint(text);
    }
    public static void covertStr2IntPrint(String text){
        Integer num = util.Str2Int(text);
        util.intPrint(num);
    }
}
class Animal{
    String name;
    int age;
    public void setName(String name){
        this.name = name;
    }
    public void setAge(int age){
        this.age = age;
    }
    public void printName(){
        util.strPrint(this.name);
    }
    public void printAge(){
        util.intPrint(this.age);
    }
    public void set(String name){
        this.setName(name);
    }
    public void set(Integer age){
        this.setAge(age);
    }
}
class Start{
    public static void start(String name, Integer age) {
        Animal a = new Animal();
        a.set(name);
        a.set(age);
        a.printName();
        a.printAge();
    }
}
class util{
    public static void intPrint(Integer num){
        System.out.println(num);
    }
    public static void strPrint(String text){
        System.out.println(text);
    }
    public static String Int2Str(Integer num){
        return num.toString();
    }
    public static Integer Str2Int(String text){
        return Integer.valueOf(text);
    }
}
class t_filed{
    StringBuffer arr = new StringBuffer();
    public void strBuffer(){
        bufferAppender("hello");
        bufferAppender("world");
        bufferAppender("in");
        bufferAppender("java");
        printStrBuffer(arr);
        input();
        printStrBuffer(arr);
    }
    public static void printStrBuffer(StringBuffer stringBuffer){
        String resultStringBuffer = stringBuffer.toString();
        util.strPrint(resultStringBuffer);
    }
    public void bufferAppender(String string){
        arr.append(string);
        arr.append(" ");
    }
    public void input(){
        Scanner scanner = new Scanner(System.in);
        bufferAppender(scanner.nextLine());
    }
}
class Scraper{
    Document soup = null;
    String url;
    public void setUrl(String url) {
        this.url = url;
    }
    public void getSoup() {
        try {
            this.soup = Jsoup.connect(url).get();
        }
         catch (IOException error){
            error.printStackTrace();
            this.soup = null;
        }
    }
    public Elements findByClass(String elem){
        assert soup != null;
        return soup.getElementsByClass(elem);
    }
}
class GetMelon{
    Scraper scraper = new Scraper();
    String url;
    public void setUrl(String url){
        this.url = url;
        scraper.setUrl(url);
        scraper.getSoup();
    }
    public String getArtist(){
        return scraper.findByClass("artist_name").get(0).text();
    }
    public String getTitle(){
        return scraper.findByClass("song_name").get(0).text().replace("곡명 ", "");
    }
    public void getAlbumName(){
        System.out.println(scraper.findByClass("list").get(0));

    }
}
class t_3 extends t_1{
    public static void main(String[] args) {
        GetMelon getMelon = new GetMelon();
        String url = "https://www.melon.com/song/detail.htm?songId=34819463";
        getMelon.setUrl(url);

        String artist = getMelon.getArtist();
        util.strPrint(artist);

        String title = getMelon.getTitle();
        util.strPrint(title);

        getMelon.getAlbumName();
//        String albumName = getMelon.getAlbumName();
//        util.strPrint(albumName);
    }
}