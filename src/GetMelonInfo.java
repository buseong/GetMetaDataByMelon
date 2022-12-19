import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class print1<MSG>{
    public static <MSG> void print(MSG msg) {
        System.out.println(msg);
    }}
public class GetMelonInfo {
    public static void main(String[] args) {
        GetMelon1 getMelon1 = new GetMelon1();
        String url = "https://www.melon.com/song/detail.htm?songId=34819464";
        String albumUrl = "https://www.melon.com/album/detail.htm?albumId=10903868";
        getMelon1.setUrl(url);
        getMelon1.setAlbumUrl(albumUrl);
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
    private String adult_only = "19금";
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
//    public void setAlbumUrl(String albumUrl){
//        this.albumUrl = albumUrl;
//    }
//    public void getMusicDetailInfo(){
//        this.musicInfo = scraper.findByClass("list").get(0).text().replace("\n", "");
//        util.pprint(this.musicInfo);
//    }
//    public String findTextOnlySoup(String text){
//        if (this.musicInfo == null) this.getMusicDetailInfo();
//        Pattern pattern = Pattern.compile(text);
//        Matcher matcher = pattern.matcher(this.musicInfo);
//        //noinspection ReassignedVariable
//        String result = null;
//        while (matcher.find()) result = matcher.group(1);
//        return result;
//    }
//    public void getAlbumInfo(){
//        assert this.albumUrl != null;
//        scraper.setUrl(this.albumUrl);
//        scraper.getSoup();
//        this.soupType = "album";
//    }
//    public void getMusicInfo(){
//        assert (this.url != null);
//        scraper.setUrl(this.url);
//        scraper.getSoup();
//        this.soupType = "music";
//    }
//    public String findText(String text, String target){
//        Pattern pattern = Pattern.compile(text);
//        Matcher matcher = pattern.matcher(target);
//        //noinspection ReassignedVariable
//        String result = null;
//        while (matcher.find()) {
//            result = matcher.group(1);
//            if (result != null) break;
//        }
//        return result;
//    }
//    public String getTrackNum(){
//        if (!this.soupType.equals("album")) getAlbumInfo();
//        if (this.title == null) this.getTag(Tag.Title);
//        assert this.title != null;
//        Elements soup = scraper.findByClass("wrap_song_info");
//        ArrayList<String> titleInAlbum = new ArrayList<>();
//        //noinspection ReassignedVariable
//        String tempTitle;
//        for (int i = 0; i < soup.size(); i++){
//            if (i == 0) {
//                tempTitle = findText("\"disabled\">(.+?)</span>", soup.get(0).toString());}
//            else{
//                tempTitle = findText("href=\".+?\" title=\".+?\">(.+?)</a>", soup.get(i).toString());}
//            if (tempTitle.contains("&amp")){
//                tempTitle = tempTitle.replaceAll("&amp", "&");
//            }
//            if (tempTitle.contains("(Inst.)")){
//                continue;
//            }
//            titleInAlbum.add(tempTitle);
//        }
//        print.print(titleInAlbum);
//        print.print(titleInAlbum.indexOf(this.title));
//        print.print(titleInAlbum.size());
//        return "";
//    }
//    public String getAlbumCoverImg(){
//        if (!this.soupType.equals("album")) getAlbumInfo();
//        //noinspection ReassignedVariable
//        String imgUrl = scraper.selectElem("meta[property=\"og:image\"]").toString();
//        imgUrl = util.findText("content=\"(.+?)\"", imgUrl);
//        try{
//            imgUrl = imgUrl.replace("500", "1000");}
//        catch (Exception exception){
//            //pass
//        }
//        return imgUrl;
//    }
//    public String getLyrics(){
//        if (!this.soupType.equals("music")) this.getMusicInfo();
//        Elements soup = scraper.soup.select(".lyric");
//        if (soup.size() == 0) {print.print("lyric not exist");
//            return "";}
//        soup.select("br").append("\\n");
//        return soup.text();
//    }
//    public String getTag(Tag type){
//        if (scraper.soup == null) this.getMusicInfo();
//        switch (type){
//            case Year -> {return findTextOnlySoup("발매일 (.+?) 장르");}
//            case Genre -> {
//                if (this.musicInfo.contains("FLAC")) return findTextOnlySoup("장르 (.+?) FLAC");
//                else{return findTextOnlySoup("장르 (.+?)$");}}
//            case AlbumName -> {return findTextOnlySoup("앨범 (.+?) 발매일");}
//            case Title -> {this.title = scraper.findByClass("song_name").get(0).text().replace("곡명 ", "");
//                return this.title;}
//            case Artist, AlbumArtist -> {return scraper.findByClass("artist_name").get(0).text();}
//            case TrackNum -> {return getTrackNum();}
//            case Lyrics -> {return getLyrics();}
//            case AlbumCover -> {return getAlbumCoverImg();}
//        }
//        return null;
//    }
//    public void getAlbumByTitleUrl(){
//        if (!this.soupType.equals("album")) this.getAlbumInfo();
//        assert (scraper.soup != null);
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
                return scraper.selectInSoup(".song_name").get(0).text().replace("곡명 ", "");}
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
        assert (this.albumUrl != null);
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
        return "미구현 메소드";}}
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
