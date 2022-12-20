import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class print1 {
    public static <MSG> void print(MSG msg) {
        System.out.println(msg);
    }}
public class GetMelonInfo {
    public static void main(String[] args) {
        GetMelon1 getMelon1 = new GetMelon1();
        String url = "https://www.melon.com/song/detail.htm?songId=34819464";
//        String albumUrl = "https://www.melon.com/album/detail.htm?albumId=10903868";
        getMelon1.setUrl(url);
//        getMelon1.setAlbumUrl(albumUrl);
        print1.print(getMelon1.getTag(GetMelon1.Tag.AlbumName));
        print1.print(getMelon1.getTag(GetMelon1.Tag.Year));
        print1.print(getMelon1.getTag(GetMelon1.Tag.Genre));
        print1.print(getMelon1.getTag(GetMelon1.Tag.Title));
        print1.print(getMelon1.getTag(GetMelon1.Tag.Artist));
        print1.print(getMelon1.getTag(GetMelon1.Tag.TrackNum));
        print1.print(getMelon1.getTag(GetMelon1.Tag.AlbumCover));
        print1.print(getMelon1.getTag(GetMelon1.Tag.Lyrics));
        print1.print(getMelon1.getTag(GetMelon1.Tag.AlbumArtist));
    }}
class Scraper1{
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
class GetMelon1{
    Scraper1 scraper = new Scraper1();
    String musicUrl = null;
    String soupType = "";
    String musicInfo = null;
    String albumUrl = null;
    String title = null;
    String melonId = "https://www.melon.com/song/detail.htm?songId=";
    String albumId = "https://www.melon.com/album/detail.htm?albumId=";
    String adultOnly = "19금";
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
        this.musicUrl = url;
//        scraper.setUrl(this.url);
    }
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
        return null;}
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
        Elements imgUrl_ = scraper.selectInSoup("meta[property=\"og:image\"]");
        //noinspection ReassignedVariable
        String imgUrl = imgUrl_.get(0).toString();
        imgUrl = util.findText("content=\"(.+?)\"", imgUrl).replace("500", "1000");
        return imgUrl;}
    public String getTrackNum(){
        this.getAlbumSoup();
        Elements soup = scraper.soup.select(".wrap_song_info");
        assert (soup.size() != 0);
        ArrayList <String> titleInMusic = new ArrayList<>();
        //noinspection ReassignedVariable
        String tempTitle;
        for (int i = 0; i < soup.size(); i++){
            if (i == 0) {
                tempTitle = utils.findText("\"disabled\">(.+?)</span>", soup.get(0).toString());}
            else{
                tempTitle = utils.findText("href=\".+?\" title=\".+?\">(.+?)</a>", soup.get(i).toString());}
            if (tempTitle.contains("&amp")){
                tempTitle = tempTitle.replaceAll("&amp", "&");}
            if (tempTitle.contains("(Inst.)")){
                continue;}
            titleInMusic.add(tempTitle);}
        return String.join(", ", titleInMusic);}
    public String getAlbumUrlByMusicUrl(){
        this.getMusicSoup();
        String soup = utils.findText("javascript:melon.link.goAlbumDetail\\(\\'(.+?)\\'\\)",
                scraper.selectInSoup(".list").get(0).toString().replace("\n", ""));
        return this.albumId + soup;}}
class utils{
    public static String findText(String text, String target){
        Pattern pattern = Pattern.compile(text);
        Matcher matcher = pattern.matcher(target);
        //noinspection ReassignedVariable
        String result = null;
        while (matcher.find()) {
            result = matcher.group(1);
            if (result != null) break;}
        return result;}
    public static void getImgByUrl(String url){
        // check 1000img exist, not exist: return 500img
        // bytes return?
        try{}
        catch (Exception e){}
        }
}
