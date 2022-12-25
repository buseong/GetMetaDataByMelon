import com.mpatric.mp3agic.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetMelonInfo {
    public static void main(String[] args) {
        GetMelon getMelon1 = new GetMelon();
//        String url = "https://www.melon.com/song/detail.htm?songId=34819464";
//        String albumUrl = "https://www.melon.com/album/detail.htm?albumId=10903868";
        String url = "https://www.melon.com/song/detail.htm?songId=1017063";
        getMelon1.setUrl(url);
//        getMelon1.setAlbumUrl(albumUrl);
        print.print(getMelon1.getTag(GetMelon.Tag.AlbumName));
        print.print(getMelon1.getTag(GetMelon.Tag.Year));
        print.print(getMelon1.getTag(GetMelon.Tag.Genre));
        print.print(getMelon1.getTag(GetMelon.Tag.Title));
        print.print(getMelon1.getTag(GetMelon.Tag.Artist));
        print.print(getMelon1.getTag(GetMelon.Tag.TrackNum));
        print.print(getMelon1.getTag(GetMelon.Tag.AlbumCover));
        print.print(getMelon1.getTag(GetMelon.Tag.Lyrics));
        print.print(getMelon1.getTag(GetMelon.Tag.AlbumArtist));
    }}

class Scraper {
    Document soup = null;
    String url;
    public void setUrl(String url) {
        this.url = url;
    }

    public void getSoup() {
        try {this.soup = Jsoup.connect(url).get();}
        catch (IOException error) {
            error.printStackTrace();
            this.soup = null;}}

    public Elements selectInSoup(String text){
        return soup.select(text);}}

class GetMelon {
    private final Scraper scraper = new Scraper();
    private String soupType = "";
    private String musicInfo = null;
    final String melonId = "https://www.melon.com/song/detail.htm?songId=";
    final String albumId = "https://www.melon.com/album/detail.htm?albumId=";
    String adultOnly = "19금";
    String musicUrl = null;
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
        AlbumCover}

    public void setUrl(String url){
        this.musicUrl = url;}

    public void setAlbumUrl(String albumUrl){
        this.albumUrl = albumUrl;}

    public String getTag(Tag tag){
        switch (tag){
            case Year -> {
                this.getMusicInfoSoup();
                return utils.findText("발매일 (.+?) 장르", this.musicInfo);}

            case Genre -> {
                this.getMusicInfoSoup();
                String genre;
                if (this.musicInfo.contains("FLAC")){genre = utils.findText("장르 (.+?) FLAC", this.musicInfo);}
                else{genre = utils.findText("장르 (.+?)$", this.musicInfo);}
                return genre;}

            case AlbumName -> {
                this.getMusicInfoSoup();
                return utils.findText("앨범 (.+?) 발매일", this.musicInfo);}

            case Title -> {
                this.getMusicSoup();
                this.title = scraper.selectInSoup(".song_name").get(0).text().replace("곡명 ", "");
                if (this.title.contains(this.adultOnly)) this.title = this.title.replace(this.adultOnly, "").strip();
                return this.title;}

            case TrackNum -> {
                return this.getTrackNum();}

            case AlbumArtist, Artist -> {
                this.getMusicInfoSoup();
                return scraper.selectInSoup(".artist_name").get(0).text();}

            case Lyrics -> {
                this.getMusicSoup();
                Elements lyrics = scraper.selectInSoup(".lyric");
                if (lyrics.size() == 0) return "";
                lyrics.get(0).select("br").append("\\n");
                return lyrics.text();}

            case AlbumCover -> {
                return this.getAlbumCoverUrl();}}

        return "didn't get tag";}

    public void getMusicInfoSoup(){
        if (this.musicInfo != null) return;
        assert (this.musicUrl != null);
        this.getMusicSoup();
        this.musicInfo = scraper.selectInSoup(".list").get(0).text().replace("\n", "");}

    public void getAlbumSoup(){
        if (this.soupType.equals("album")) return;
        if (this.albumUrl == null) this.albumUrl = this.getAlbumUrlByMusicUrl();
        scraper.setUrl(this.albumUrl);
        scraper.getSoup();
        this.soupType = "album";}

    public void getMusicSoup(){
        if (this.soupType.equals("music")) return;
        assert (this.musicUrl != null);
        scraper.setUrl(this.musicUrl);
        scraper.getSoup();
        this.soupType = "music";}

    public String getAlbumCoverUrl(){
        this.getAlbumSoup();
        String saveLocation = "src/test.jpg";
        String imgUrl = utils.findText("content=\"(.+?)\"",
                scraper.selectInSoup("meta[property=\"og:image\"]").get(0).toString());
        try{
            utils.saveImage(imgUrl.replace("500", "1000"), saveLocation);}
        catch (IOException e1){
            try {
                utils.saveImage(imgUrl, saveLocation);
            } catch (IOException e2) {
                print.print("didn't get image");}
        }
        return imgUrl;}

    public String getTrackNum(){
        this.getAlbumSoup();
        Elements soup = scraper.soup.select(".wrap_song_info");
        assert (soup.size() != 0);
        ArrayList <String> titleInMusic = new ArrayList<>();
        //noinspection ReassignedVariable
        String tempTitle;
        for (int i = 0; i < soup.size(); i++){
            {
//            if (i == 0) {
//                tempTitle = utils.findText("\"disabled\">(.+?)</span>", soup.get(0).toString());}
//            else{
            tempTitle = utils.findText("href=\".+?\" title=\".+?\">(.+?)</a>", soup.get(i).toString());}
            if (tempTitle.contains("&amp")){
                tempTitle = tempTitle.replaceAll("&amp", "&");}
//            if (tempTitle.contains("(Inst.)")) continue;
            titleInMusic.add(tempTitle);}
        return String.join(", ", titleInMusic);}

    public String getAlbumUrlByMusicUrl(){
        this.getMusicSoup();
        String soup = utils.findText("javascript:melon.link.goAlbumDetail\\(\\'(.+?)\\'\\)",
                scraper.selectInSoup(".list").get(0).toString().replace("\n", ""));
        return this.albumId + soup;}
    public void saveTAg(){

    }
    public void saveTag3v1(){

    }
}

class utils {
    public static String findText(String text, String target) {
        Pattern pattern = Pattern.compile(text);
        Matcher matcher = pattern.matcher(target);
        //noinspection ReassignedVariable
        String result = null;
        while (matcher.find()) {
            result = matcher.group(1);
            if (result != null) break;}
        return result;}

    public static void saveImage(String imageUrl, String destinationFile) throws IOException {
        URL url = new URL(imageUrl);
        InputStream is = url.openStream();
        OutputStream os = new FileOutputStream(destinationFile);

        byte[] b = new byte[2048];
        //noinspection ReassignedVariable
        int length;

        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);}
        is.close();
        os.close();
    }}

class GetMelonMusicId{
    String title;
    String artist;
    String music;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getMelonId(){
        return "";
    }
}

class print{
    public static <MSG> void print(MSG msg){
        System.out.println(msg);}

}

class SetTag{
    public Mp3File mp3File = null;
    public String mp3Location = null;
    public tagType mp3Type = null;
    public HashMap<tagEnum, String> tag = new HashMap<>();
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
    private enum tagType {
        ID3v1,
        ID3v2,
        Custom
    }

    public void setMp3Location(String mp3Location) throws InvalidDataException, UnsupportedTagException, IOException {
        this.mp3Location = mp3Location;
        if (this.mp3File == null) {
            mp3File = new Mp3File(mp3Location);
            if (this.mp3Type != null) {
                if (this.mp3File.hasId3v1Tag()){
                    this.mp3Type = tagType.ID3v1;
                }else if (this.mp3File.hasId3v2Tag()) {
                    this.mp3Type = tagType.ID3v2;
                } else if (this.mp3File.hasCustomTag()) {
                    this.mp3Type = tagType.Custom;
                } else {
                    ID3v2 id3v2 = new ID3v24Tag();
                    this.mp3File.setId3v2Tag(id3v2);
                    this.mp3Type = tagType.ID3v2;
                }
            }
        }}

    public void setTag(tagEnum tag, String value){
        this.tag.put(tag, value);
    }

    public void saveTag() throws IOException, NotSupportedException {
        switch (this.mp3Type) {
            case ID3v1 -> this.setTag3v1();
            case ID3v2 -> this.setTAg3v2();
            case Custom -> System.out.println("don't support Custom Tag");
            default -> System.out.println("didn't set Tag in mp3-file");
        }}

    public void setTag3v1() throws IOException, NotSupportedException {
        ID3v1 id3v1Tag = mp3File.getId3v1Tag();
        id3v1Tag.setTrack("///track num");
        id3v1Tag.setArtist("///music artist");
        id3v1Tag.setTitle("///music title");
        id3v1Tag.setAlbum("///album name");
        id3v1Tag.setYear("///YYYY");
        id3v1Tag.setGenre(12);// valueOf Integer
        id3v1Tag.setComment("/// music or album comment");
//        for ID3v2:[
//        id3v1Tag.setLyrics("///music lyrics");
//        id3v1Tag.setComposer("///composer");
//        id3v1Tag.setPublisher("///publisher");
//        id3v1Tag.setOriginalArtist("/// another music artist");
//        id3v1Tag.setAlbumArtist("///music artist");
//        id3v1Tag.setUrl("///melon music url");]
        this.mp3File.save(this.mp3Location);}

    public void setTAg3v2() throws IOException, NotSupportedException {
        ID3v2 id3v2Tag = mp3File.getId3v2Tag();
//        id3v2Tag.setTrack("///track num");
//        id3v2Tag.setArtist("///music artist");
//        id3v2Tag.setTitle("///music title");
//        id3v2Tag.setAlbum("///album name");
//        id3v2Tag.setYear("///YYYY");
//        id3v2Tag.setGenre(12);
//        id3v2Tag.setComment("/// music or album comment");
//        id3v2Tag.setLyrics("///music lyrics");
//        id3v2Tag.setComposer("///composer");
//        id3v2Tag.setPublisher("///publisher");
//        id3v2Tag.setOriginalArtist("/// another music artist");
//        id3v2Tag.setAlbumArtist("///music artist");
//        id3v2Tag.setUrl("///melon music url");
        byte[] b = new byte[2048];
        id3v2Tag.setAlbum(this.tag.get(tagEnum.AlbumName));
        id3v2Tag.setArtist(this.tag.get(tagEnum.Artist));
        id3v2Tag.setTrack(this.tag.get(tagEnum.TrackNum));
        id3v2Tag.setTitle(this.tag.get(tagEnum.Title));
        id3v2Tag.setLyrics(this.tag.get(tagEnum.Lyrics));
        id3v2Tag.setGenreDescription(this.tag.get(tagEnum.Genre));
        id3v2Tag.setAlbumImage(b, "image/jpg");
        id3v2Tag.setAlbum(this.tag.get(tagEnum.AlbumName));
        id3v2Tag.setAlbum(this.tag.get(tagEnum.AlbumName));
        this.mp3File.save(this.mp3Location);}
}