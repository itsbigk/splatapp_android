package io.skulltah.splatapp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by max on 10/21/15.
 */
public class SplatoonNet {
    public static class Nnid {
        public String iconUrl;
        public String miiName;

        public Nnid() {

        }

        public Nnid(String html) {
            Document doc = Jsoup.parse(html);
            iconUrl = doc.select("img[src]").attr("src");
            miiName = doc.select("div[class=mii-name-wrapper]").text();
        }
    }

//    public static class Rank {
//        public ArrayList<RankItem> regular;
//        public ArrayList<RankItem> ranked;
//
//        public Rank() {
//
//        }
//
//        public Rank(String html) {
//            Document doc = Jsoup.parse(html);
//            String regular_html = doc.select("div[class=ranklist-regular]").html();
//            Elements regular_elements = Jsoup.parse(regular_html).getElementsByClass("rank-detail");
//
////            iconUrl = doc.select("img[src]").attr("src");
////            miiName = doc.select("div[class=mii-name-wrapper]").text();
//        }
//
//        public class RankItem {
//            public int rankNumber;
//            public String miiIcon;
//            public String miiUsername;
//            public int miiScore;
//            public String miiWeapon;
//            public String miiHat;
//            public String miiShirt;
//            public String miiShoes;
//        }
//    }
}
