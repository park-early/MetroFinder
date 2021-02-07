package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a single metro system. Tokyo consists of all the lines in the system as well as their stations.
 * Everything will be initialized by default.
 *
 * Lines are initialized first, then each line will undergo an initialization method that initializes all the stations
 * in the line. If a station appears in more than one line, it will be initialized in the first line it appears in,
 * then the transfers are accounted for in initializations for lines it appears in again.
 *
 * For example, Mita station is initialized in the Asakusa line. When it appears again in the Mita line, the line is
 * added to the station's list of lines while being added to the line.
 */

public class Tokyo {
    private final String name;
    private List<Line> lines;

    //EFFECT: constructs a new tokyo metro system with no lines
    public Tokyo() {
        this.name = "Tokyo";
        lines = new ArrayList<>();
    }

    //getters
    public String getName() {
        return this.name;
    }

    public List<Line> getLines() {
        return lines;
    }

    //MODIFIES: this
    //EFFECT: instantiate all the necessary objects to represent all the lines and stations
    public void initializeTokyo() {
        Line asakusa = new Line("Asakusa", "A|Rose");
        Line mita = new Line("Mita", "I|Blue");
        Line shinjuku = new Line("Shinjuku", "S|Leaf Green");
        initializeAsakusaLines(asakusa);
        this.lines.add(asakusa);
        initializeMitaLines(mita);
        this.lines.add(mita);
        initializeShinjukuLines(shinjuku);
        this.lines.add(shinjuku);
    }

    //MODIFIES: line
    //EFFECT: adds all the stations for the Asakusa Line
    private void initializeAsakusaLines(Line line) {                                // 8/20 stations implemented
        Station nishiMagome = new Station("Nishi-Magome", line);
        Station magome = new Station("Magome", line);
        Station nakanobu = new Station("Nakanobu", line);
        Station togoshi = new Station("Togoshi", line);
        Station gotanda = new Station("Gotanda", line);
        Station takanawadai = new Station("Takawanadai", line);
        Station sengakuji = new Station("Sengakuji", line);
        Station mita = new Station("Mita", line);                             // Transfers to Mita

        line.getStations().addAll(Arrays.asList(nishiMagome, magome, nakanobu, togoshi, gotanda, takanawadai,
                sengakuji, mita));

        nishiMagome.getNextStations().add(magome);
        magome.getNextStations().addAll(Arrays.asList(nishiMagome, nakanobu));
        nakanobu.getNextStations().addAll(Arrays.asList(magome, togoshi));
        togoshi.getNextStations().addAll(Arrays.asList(nakanobu, gotanda));
        gotanda.getNextStations().addAll(Arrays.asList(togoshi, takanawadai));
        takanawadai.getNextStations().addAll(Arrays.asList(gotanda, sengakuji));
        sengakuji.getNextStations().addAll(Arrays.asList(takanawadai, mita));
        mita.getNextStations().add(sengakuji);                                      // Transfers to Mita
    }

    //MODIFIES: line
    //EFFECT: adds all the stations for the Mita Line
    private void initializeMitaLines(Line line) {                                   // 10/27 stations implemented
        Station meguro = new Station("Meguro", line);
        Station shirokanedai = new Station("Shirokanedai", line);
        Station shirokaneTakanawa = new Station("Shirokane-Takanawa", line);
        Station mita = this.getLines().get(0).getStations().get(7);                 // Transfer from Asakusa
        mita.getLine().add(line);
        Station shibakoen = new Station("Shibakoen", line);
        Station onarimon = new Station("Onarimon", line);
        Station uchisaiwaicho = new Station("Uchisaiwaicho", line);
        Station hibiya = new Station("Hibiya", line);
        Station otemachi = new Station("Otemachi", line);
        Station jimbocho = new Station("Jimbocho", line);                     // Transfer to Shinjuku

        line.getStations().addAll(Arrays.asList(meguro, shirokanedai, shirokaneTakanawa, mita, shibakoen, onarimon,
                uchisaiwaicho, hibiya, otemachi, jimbocho));

        meguro.getNextStations().add(shirokanedai);
        shirokanedai.getNextStations().addAll(Arrays.asList(meguro, shirokaneTakanawa));
        shirokaneTakanawa.getNextStations().addAll(Arrays.asList(shirokanedai, mita));
        mita.getNextStations().addAll(Arrays.asList(shirokaneTakanawa, shibakoen)); // Transfer from Asakusa
        shibakoen.getNextStations().addAll(Arrays.asList(mita, onarimon));
        onarimon.getNextStations().addAll(Arrays.asList(shibakoen, uchisaiwaicho));
        uchisaiwaicho.getNextStations().addAll(Arrays.asList(onarimon, hibiya));
        hibiya.getNextStations().addAll(Arrays.asList(uchisaiwaicho, otemachi));
        otemachi.getNextStations().addAll(Arrays.asList(hibiya, jimbocho));
        jimbocho.getNextStations().add(otemachi);                                   // Transfer to Shinjuku
    }

    //MODIFIES: line
    //EFFECT: adds all the stations for the Shinjuku Line
    private void initializeShinjukuLines(Line line) {                               // 6/21 stations implemented
        Station shinjuku = new Station("Shinjuku", line);
        Station shinjukuSanchome = new Station("Shinjuku-Sanchome", line);
        Station akebonobashi = new Station("Akebonobashi", line);
        Station ichigaya = new Station("Ichigaya", line);
        Station kudanshita = new Station("Kudanshita", line);
        Station jimbocho = this.getLines().get(1).getStations().get(9);
        jimbocho.getLine().add(line);                                               // Transfer from Mita

        line.getStations().addAll(Arrays.asList(shinjuku, shinjukuSanchome, akebonobashi, ichigaya, kudanshita,
                jimbocho));

        shinjuku.getNextStations().add(shinjukuSanchome);
        shinjukuSanchome.getNextStations().addAll(Arrays.asList(shinjuku, akebonobashi));
        akebonobashi.getNextStations().addAll(Arrays.asList(shinjukuSanchome, ichigaya));
        ichigaya.getNextStations().addAll(Arrays.asList(akebonobashi, kudanshita));
        kudanshita.getNextStations().addAll(Arrays.asList(ichigaya, jimbocho));
        jimbocho.getNextStations().add(kudanshita);                                 // Transfer from Mita
    }
}
