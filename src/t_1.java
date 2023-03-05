public class t_1 {
    public static void main(String[] args) {
        GetMelonId getMelonId = new GetMelonId();
        String title = "after like";
        String artist = "";
        print.print(getMelonId.searchMelonId(title, artist));

        print.print(getMelonId.makeMelonUrlByTitleNArtist(title, artist));

    }
}
