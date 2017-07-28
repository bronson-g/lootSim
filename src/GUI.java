import java.util.List;
import java.util.Random;
import java.util.HashMap;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.JToolTip;
import javax.swing.BorderFactory;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.JCheckBox;
import javax.imageio.ImageIO;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.text.NumberFormat;
import java.math.BigInteger;

class GUI extends JFrame {
  private JLabel loading;
  private boolean ringWealth;
  private List<Mob> lastList;
  private String fontFace;

  private Color primary;
  private Color secondary;
  private Color tertiary;

  private long kills;
  private Mob selection;

  private int screenWidth;
  private int screenHeight;

  private JPanel content;
  private JPanel options;
  private JTextField killsEntry;
  private JCheckBox row;
  private JButton start;
  private JScrollPane loot;
  private JPanel lootPane;
  private List<Item> items;
  private JPanel summary;
  private JLabel totalVal;
  private JPanel sideBar;
  private JTextField search;
  private JScrollPane bosses;
  private List<JToggleButton> bossButtons;
  private List<Mob> mobs;
  private HashMap<Mob, ImageIcon> bossIcons;

  GUI(List<Mob> mobs, List<Item> items, Image img, JProgressBar progress, Font font) {
    super("OSRS Loot Simulator");
    setIconImage(img);
    this.mobs = mobs;
    this.items = items;
    this.kills = 1L;
    this.ringWealth = false;
    this.fontFace = font.getName();

    primary   = new Color(153, 124,  88);
    secondary = new Color(249, 241, 220);
    tertiary  = new Color(113,  92,  65);

    GraphicsEnvironment genv = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice gd = genv.getDefaultScreenDevice();
    genv.registerFont(font);
    screenWidth = gd.getDisplayMode().getWidth();
    screenHeight = gd.getDisplayMode().getHeight();

    UIManager.put("ScrollBar.thumb", tertiary);
    UIManager.put("ScrollBar.track", primary);
    UIManager.put("ToggleButton.select", tertiary);
    UIManager.put("CheckBox.focus", secondary);
    UIManager.put("CheckBox.select", tertiary);

    loading = new JLabel(" Loading - please wait.  ");
    loading.setForeground(Color.white);
    loading.setBackground(Color.black);
    loading.setBorder(BorderFactory.createLineBorder(Color.white));
    loading.setOpaque(true);

    selection = null;
    setLayout(new BorderLayout());

    initSideBar();
    initContent();

    getContentPane().setBackground(primary);

    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        screenWidth = getWidth();
        screenHeight = getHeight();
        loot.setPreferredSize(new Dimension((int)(screenWidth*0.8), (int)(screenHeight*0.8)));
      }
    });

    search("");
    pack();
    setVisible(true);
  }

  private void search(String criteria) {
    selection = null;
    enableOptions(false);
    List<Mob> bossList = new ArrayList<>();
    for(Mob mob : mobs) {
      if(mob.getName().toLowerCase().contains(criteria.toLowerCase())) {
        bossList.add(mob);
      }
    }
    if(bossList.size() == 0) {
      enableOptions(true);
    } else {
      sideBar.remove(bosses);
      genBosses(bossList);
    }
  }

  private void genBosses(List<Mob> bossList) {
    lastList = new ArrayList<>(bossList);
    JPanel bossesPane = new JPanel();
    bossesPane.setBackground(primary);
    bossButtons = new ArrayList<>();
    bossesPane.setLayout(new BoxLayout(bossesPane, BoxLayout.Y_AXIS));

    for(Mob boss : bossList) {
      JToggleButton bossButton = new JToggleButton(bossIcons.get(boss));
      bossButton.setBorderPainted(false);
      bossButton.setBackground(primary);
      bossButton.setToolTipText("<html><font face=\""+fontFace+"\">"+boss.getName()+" </font></html>");
      bossButton.setForeground(Color.orange);
      bossButton.setOpaque(true);
      bossButton.addItemListener(new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
          if(e.getStateChange() == ItemEvent.DESELECTED) {
            selection = null;
            enableOptions(false);
          } else {
            for(JToggleButton tmp : bossButtons) {
              if(!tmp.getToolTipText().equals(bossButton.getToolTipText())) {
                tmp.setSelected(false);
              }
            }
            selection = boss;
            enableOptions(true);
          }
        }
      });
      bossesPane.add(bossButton);
      bossesPane.setBackground(primary);
      bossButtons.add(bossButton); // array
    }
    bosses = new JScrollPane(bossesPane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    bosses.setBackground(primary);
    sideBar.add(bosses, BorderLayout.CENTER);
  }

  private void initSideBar() {
    bossIcons = new HashMap<>();
    for(Mob boss : mobs) {
      try {
        BufferedImage img = ImageIO.read(getClass().getResource(boss.getIcon()));
        int dim = (int)(screenWidth*0.1);
        ImageIcon icon = new ImageIcon(img.getScaledInstance(dim,dim,Image.SCALE_SMOOTH));
        bossIcons.put(boss, icon);
      } catch(Exception ex) {
        ex.printStackTrace();
        //use some default icon??
      }
    }
    sideBar = new JPanel();
    sideBar.setLayout(new BorderLayout());
    search = new JTextField();
    search.setBackground(secondary);
    search.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        search(search.getText());
        sideBar.revalidate();
      }
    });
    sideBar.setBackground(primary);
    sideBar.add(search, BorderLayout.NORTH);
    genBosses(mobs);
    add(sideBar, BorderLayout.WEST);
  }

  private void simulateDrops() {
    Random random = new Random();
    HashMap<Item, Integer> drops = new HashMap<>();

    if(selection != null) {
      for(int i=0; i<kills; i++) {
        Item drop = selection.drop(random.nextDouble());
        if(!drop.equals(new Item())) {
          if(drops.containsKey(drop)) {
            drops.put(drop, drops.get(drop)+1);
          } else {
            drops.put(drop, 1);
          }
        }
      }
    }
    BigInteger totalValue = BigInteger.valueOf(0);
    for(Item drop: drops.keySet()) {
      try {
        BigInteger total = BigInteger.valueOf(drop.getValue()).multiply(BigInteger.valueOf(drops.get(drop)));
        String tipText = "<html><font face=\""+fontFace+"\">"+drop.getName()+"<br>quantity: "+NumberFormat.getIntegerInstance().format(drops.get(drop))+"<br>value: "+NumberFormat.getIntegerInstance().format(drop.getValue())+"<br>total: "+print(total)+"</font></html>";
        BufferedImage img = ImageIO.read(getClass().getResource(drop.getIcon()));
        ImageIcon icon = new ImageIcon(img.getScaledInstance(50,50,Image.SCALE_SMOOTH)); //scale to lootpane??? todo lol
        JLabel itemLabel = new JLabel(icon);
        itemLabel.setToolTipText(tipText);

        if(drops.get(drop) < 100_000) {
          itemLabel.setForeground(Color.yellow);
          itemLabel.setText(""+drops.get(drop));
        } else if(drops.get(drop) < 10_000_000) {
          itemLabel.setForeground(Color.white);
          itemLabel.setText((int)(drops.get(drop)/1_000)+"k");
        } else {
          itemLabel.setForeground(Color.green);
          itemLabel.setText((int)(drops.get(drop)/1_000_000)+"M");
        }
        totalValue = totalValue.add(total);

        itemLabel.setIconTextGap(-50);
        itemLabel.setHorizontalTextPosition(JLabel.RIGHT);
        itemLabel.setVerticalTextPosition(JLabel.TOP);
        itemLabel.setBackground(primary);
        lootPane.add(itemLabel);
      } catch(Exception ex) {
        ex.printStackTrace();
        //use some default.
      }
    }
    totalVal.setText("Total value: "+print(totalValue)+" ");
  }

  private void initContent() {
    content = new JPanel();
    content.setLayout(new BorderLayout());
    content.setBackground(primary);
    options = new JPanel(new GridLayout());
    killsEntry = new JTextField();
    killsEntry.setBackground(secondary);
    row = new JCheckBox("Ring of Wealth");
    row.setForeground(Color.orange);

    row.addItemListener(new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent e) {
        ringWealth = e.getStateChange() == ItemEvent.SELECTED;
      }
    });

    options.add(killsEntry);
    options.add(row);
    options.setBackground(primary);
    start = new JButton("simulate");
    start.setForeground(Color.orange);
    start.setBackground(tertiary);
    enableOptions(false);
    options.add(start);
    content.add(options, BorderLayout.NORTH);
    lootPane = new JPanel(new SpringLayout());

    start.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          kills = Long.valueOf(killsEntry.getText());
        } catch(Exception ex) {
          kills = 1L;
        }

        try {
          Thread th = new Thread() {
            @Override
            public void run() {
              enableOptions(false);
              lootPane.removeAll();
              totalVal.setText("Total value: 0 ");
              lootPane.add(loading);
              lootPane.revalidate();
              lootPane.repaint();
              try {
                SwingUtilities.invokeAndWait(new Runnable() {
                  @Override
                  public void run() {
                    simulateDrops();
                  }
                });
              } catch(Exception ex) {
                ex.printStackTrace();
              }
              lootPane.remove(loading);
              lootPane.revalidate();
              lootPane.repaint();
              enableOptions(true);
            }
          };
          th.start();
        } catch(Exception ex) {
          ex.printStackTrace();
        }
      }
    });
    lootPane.setBackground(new Color(153, 132, 109));
    loot = new JScrollPane(lootPane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    loot.setPreferredSize(new Dimension((int)(screenWidth*0.8), (int)(screenHeight*0.8)));
    loot.setBackground(new Color(153, 132, 109));
    content.add(loot, BorderLayout.CENTER);
    summary = new JPanel(new FlowLayout());
    totalVal = new JLabel("Total value: 0 ");
    totalVal.setBackground(primary);
    totalVal.setForeground(Color.orange);
    summary.add(totalVal);
    summary.setBackground(primary);
    content.add(summary, BorderLayout.SOUTH);
    add(content, BorderLayout.CENTER);
  }

  private void enableOptions(boolean enable) {
    start.setEnabled(enable);
    killsEntry.setEnabled(enable);
    row.setEnabled(enable);
  }

  private String print(BigInteger val) {
    StringBuilder formatted = new StringBuilder(val.toString());
    String backwards = formatted.reverse().toString();
    formatted = new StringBuilder();
    for(int i=0; i<backwards.length(); i++) {
      if(i > 0 && i%3 == 0) {
        formatted.append(",");
      }
      formatted.append(backwards.charAt(i));
    }
    return formatted.reverse().toString();
  }
}
