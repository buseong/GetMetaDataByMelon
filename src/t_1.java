import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
        util.pprint(text);
    }
    public static void covertStr2IntPrint(String text){
        Integer num = util.Str2Int(text);
        util.pprint(num);
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
        util.pprint(this.name);
    }
    public void printAge(){
        util.pprint(this.age);
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
    public static <MSG> void pprint(MSG text){
        System.out.println(text);
    }
    public static String Int2Str(Integer num){
        return num.toString();
    }
    public static Integer Str2Int(String text){
        return Integer.valueOf(text);
    }
    public static String findText(String text, String target){
        Pattern pattern = Pattern.compile(text);
        Matcher matcher = pattern.matcher(target);
        //noinspection ReassignedVariable
        String result = null;
        while (matcher.find()) {
            result = matcher.group(1);
            if (result != null) break;
        }
        return result;}
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
        util.pprint(resultStringBuffer);
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
class Scraper {
    Document soup = null;
    String url;

    public void setUrl(String url) {
        this.url = url;
    }

    public void getSoup() {
        try {
            this.soup = Jsoup.connect(url).get();
        } catch (IOException error) {
            error.printStackTrace();
            this.soup = null;
        }
    }

    public Elements findByClass(String elem) {
        assert (soup != null);
        return soup.getElementsByClass(elem);
    }

    public Elements selectElem(String toFind) {
        assert (soup != null);
        return soup.select(toFind);
    }
    public String selectInSoup(String text){
        return soup.select(text).toString();
    }
}
class GetMelon{
    Scraper scraper = new Scraper();
    String url = null;
    private String soupType;
    String musicInfo = null;
    String albumUrl = null;
    String title = null;
    public enum Tag {
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
    public void setUrl(String url){
        this.url = url;
//        scraper.setUrl(this.url);
    }
    public void setAlbumUrl(String albumUrl){
        this.albumUrl = albumUrl;
    }
    public void getMusicDetailInfo(){
        this.musicInfo = scraper.findByClass("list").get(0).text().replace("\n", "");
        util.pprint(this.musicInfo);
    }
    public String findTextOnlySoup(String text){
        if (this.musicInfo == null) this.getMusicDetailInfo();
        Pattern pattern = Pattern.compile(text);
        Matcher matcher = pattern.matcher(this.musicInfo);
        //noinspection ReassignedVariable
        String result = null;
        while (matcher.find()) result = matcher.group(1);
        return result;
    }
    public void getAlbumInfo(){
        assert this.albumUrl != null;
        scraper.setUrl(this.albumUrl);
        scraper.getSoup();
        this.soupType = "album";
    }
    public void getMusicInfo(){
        assert (this.url != null);
        scraper.setUrl(this.url);
        scraper.getSoup();
        this.soupType = "music";
    }
    public String findText(String text, String target){
        Pattern pattern = Pattern.compile(text);
        Matcher matcher = pattern.matcher(target);
        //noinspection ReassignedVariable
        String result = null;
        while (matcher.find()) {
            result = matcher.group(1);
            if (result != null) break;
        }
        return result;
    }
    public String getTrackNum(){
        if (!this.soupType.equals("album")) getAlbumInfo();
        if (this.title == null) this.getTag(Tag.Title);
        assert this.title != null;
        Elements soup = scraper.findByClass("wrap_song_info");
        ArrayList <String> titleInAlbum = new ArrayList<>();
        //noinspection ReassignedVariable
        String tempTitle;
        for (int i = 0; i < soup.size(); i++){
            if (i == 0) {
                tempTitle = findText("\"disabled\">(.+?)</span>", soup.get(0).toString());}
            else{
            tempTitle = findText("href=\".+?\" title=\".+?\">(.+?)</a>", soup.get(i).toString());}
            if (tempTitle.contains("&amp")){
                tempTitle = tempTitle.replaceAll("&amp", "&");
            }
            if (tempTitle.contains("(Inst.)")){
                continue;
            }
            titleInAlbum.add(tempTitle);
        }
        print.print(titleInAlbum);
        print.print(titleInAlbum.indexOf(this.title));
        print.print(titleInAlbum.size());
        return "";
    }
    public String getAlbumCoverImg(){
        if (!this.soupType.equals("album")) getAlbumInfo();
        //noinspection ReassignedVariable
        String imgUrl = scraper.selectElem("meta[property=\"og:image\"]").toString();
        imgUrl = util.findText("content=\"(.+?)\"", imgUrl);
        try{
            imgUrl = imgUrl.replace("500", "1000");}
        catch (Exception exception){
            //pass
        }
        return imgUrl;
    }
    public String getLyrics(){
        if (!this.soupType.equals("music")) this.getMusicInfo();
        Elements soup = scraper.soup.select(".lyric");
        if (soup.size() == 0) {print.print("lyric not exist");
        return "";}
        soup.select("br").append("\\n");
        return soup.text();
    }
    public String getTag(Tag type){
        if (scraper.soup == null) this.getMusicInfo();
        switch (type){
            case Year -> {return findTextOnlySoup("발매일 (.+?) 장르");}
            case Genre -> {
                if (this.musicInfo.contains("FLAC")) return findTextOnlySoup("장르 (.+?) FLAC");
                else{return findTextOnlySoup("장르 (.+?)$");}}
            case AlbumName -> {return findTextOnlySoup("앨범 (.+?) 발매일");}
            case Title -> {this.title = scraper.findByClass("song_name").get(0).text().replace("곡명 ", "");
                return this.title;}
            case Artist, AlbumArtist -> {return scraper.findByClass("artist_name").get(0).text();}
            case TrackNum -> {return getTrackNum();}
            case Lyrics -> {return getLyrics();}
            case AlbumCover -> {return getAlbumCoverImg();}
        }
        return null;
    }
    public void getAlbumByTitleUrl(){
        if (!this.soupType.equals("album")) this.getAlbumInfo();
        assert (scraper.soup != null);

    }
}
class t_3 extends t_1{
    public static void main(String[] args) {
        GetMelon getMelon = new GetMelon();
        String url = "https://www.melon.com/song/detail.htm?songId=34819464";
        String albumUrl = "https://www.melon.com/album/detail.htm?albumId=10903868";
        getMelon.setUrl(url);
        getMelon.setAlbumUrl(albumUrl);
        print.print(getMelon.getTag(GetMelon.Tag.AlbumName));
        print.print(getMelon.getTag(GetMelon.Tag.Year));
        print.print(getMelon.getTag(GetMelon.Tag.Genre));
        print.print(getMelon.getTag(GetMelon.Tag.Title));
        print.print(getMelon.getTag(GetMelon.Tag.Artist));
        print.print(getMelon.getTag(GetMelon.Tag.TrackNum));
        print.print(getMelon.getTag(GetMelon.Tag.AlbumCover));
        print.print(getMelon.getTag(GetMelon.Tag.Lyrics));

    }
}
class print<MSG>{
    public static <MSG> void print(MSG msg) {
        System.out.println(msg);
    }
}