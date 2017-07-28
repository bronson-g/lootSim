import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JProgressBar;
import javax.swing.JFrame;
import javax.swing.UIManager;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Font;
import java.io.InputStream;
import javax.swing.BorderFactory;
import java.awt.Dimension;

// If I ever make an info page for bosses/items, consider JTabbedPane
public class Main {
  /* In mob/
  mogrify -resize 512x512 -background none -gravity center -extent 512x512 *.png
  */
  /* In item/
  mogrify -resize 64x64 -background none -gravity center -extent 64x64 *.png
  */
  // Find out how to have quantities[] with probabilities on drop() i.e: coins x 1000, 2000

  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
    } catch(Exception ex) {
      ex.printStackTrace();
    }
    Font font;

    try {
      InputStream ttf = new Main().getClass().getResource("assets/client/text-font.ttf").openStream();
      font = Font.createFont(Font.TRUETYPE_FONT, ttf).deriveFont(18f);
      ttf.close();
      setFont(font);
    } catch(Exception ex) {
      System.out.println("Failed to load font.");
      font = new Font(null, Font.PLAIN, 16);
    }

    UIManager.put("ProgressBar.selectionForeground", Color.white);
    UIManager.put("ProgressBar.selectionBackground", Color.white);

    JFrame loader = new JFrame("loading - please wait.");
    loader.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    loader.setLocationRelativeTo(null);

    JProgressBar progress = new JProgressBar(0, 100);
    progress.setValue(50); // remove this later, and increment this as it loads
    progress.setStringPainted(true);
    progress.setOpaque(true);
    progress.setForeground(new Color(128,0,0));
    progress.setBackground(Color.black);
    progress.setBorder(BorderFactory.createLineBorder(new Color(128,0,0)));
    progress.setPreferredSize(new Dimension(250,40));

    loader.setBackground(Color.black);
    loader.add(progress);
    loader.pack();
    loader.setVisible(true);

    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      setFont(font);
    } catch(Exception ex) {
      ex.printStackTrace();
    }

    BufferedImage img;
    try { //Windows / Linux?
      img = ImageIO.read(new Main().getClass().getResource("assets/client/favicon.png"));
      try {//Mac
        String className = "com.apple.eawt.Application";
        Class<?> cls = Class.forName(className);

        Object application = cls.newInstance().getClass().getMethod("getApplication").invoke(null);
        application.getClass().getMethod("setDockIconImage", java.awt.Image.class).invoke(application, img);
        System.setProperty("apple.awt.application.name", "OSRS Loot Simulator");
      } catch(Exception ex) {
        ex.printStackTrace();
      }

      new GUI(initMobs(), initItems(), img, progress, font);
    } catch(Exception ex) {
      ex.printStackTrace();
    }
    loader.dispose();
  }

//have a json file with items, use org.json to parse
  private static List<Item> initItems() {
    ArrayList<Item> items = new ArrayList<>();

    // get items from some config file - either manual entry
    // or read from wikipedia or find some api
    // maybe if stuff is read dynamically from api or wiki
    // just do it once in a while and keep a local file

    return items;
  }

//bosses
  //Start w/ gwd bosses
//demi bosses
//raids/barrows
//popular slayer tasks
//popular off task
//all
  private static List<Mob> initMobs() {
    ArrayList<Mob> mobs = new ArrayList<>();

    Item item = new Item("Abyssal Whip", 1952290, "assets/item/abyssal_whip.png", false);
    Mob mob = new Mob("Abyssal Demon", "Zeah Catacombs", "assets/mob/abyssal_demon.png");
    HashMap<Double, Item> dropTable = new HashMap<>();
    dropTable.put(0.001953125, item); //real
    mob.setDropTable(dropTable);
    mobs.add(mob);

    mobs.add(new Mob("Abyssal Sire", "The Abyss", "assets/mob/abyssal_sire.png"));
    mobs.add(new Mob("General Graardor", "The God Wars Dungeon", "assets/mob/general_graardor.png"));
    mobs.add(new Mob("Kree'Arra", "The God Wars Dungeon", "assets/mob/kree'arra.png"));
    mobs.add(new Mob("K'ril Tsutsaroth", "The God Wars Dungeon", "assets/mob/k'ril_tsutsaroth.png"));
    mobs.add(new Mob("Commander Zilyana", "The God Wars Dungeon", "assets/mob/commander_zilyana.png"));

    return mobs;
  }

  private static void setFont(Font font) {
    for(Object key : UIManager.getLookAndFeel().getDefaults().keySet()) {
      if(key instanceof String && ((String)key).toLowerCase().contains("font")) {
        UIManager.put(key, font);
      }
    }
  }
}
