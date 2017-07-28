import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map;

//NOTE: hava a hashmap of item to another hashmap of probabilities to quantity
//that way a mob can drop 100gp, 200gp etc, and have seperate probabilities for each quantity.
// you'd get the item drop, then roll again and select quantities.get(item).get(prob) = quantity
class Mob {
  private SortedMap<Double, Item> dropTable;
  private Map<Item, Map<Double, Integer>> quantities; //NOTE here
  private String name;
  private String location;
  private String icon;

  Mob(String n, String loc, String ico) {
    name = n;
    location = loc;
    icon = ico;
  }

  String getName() {
    return name;
  }
  String getIcon() {
    return icon;
  }

  void setDropTable(Map<Double, Item> drops) {
    dropTable = new TreeMap<>();
    double rate = 0.0;
    for(Double key : drops.keySet()) {
      rate += key;
      dropTable.put(rate, drops.get(key));
    }
    if(rate != 1.0) {
      System.out.println(rate+" is not 100%.");
    }
  }

  boolean rareDropTable() {
    return dropTable.containsValue(new Item("rare drop table"));
  }

  Item drop(double rate) {
    for(Double key : dropTable.keySet()) {
        if(key >= rate) {
          return dropTable.get(key);
        }
    }
    return new Item();
  }

  // somehow worry about acheivement diaries affecting drop rate

  @Override
  public boolean equals(Object other) {
    if(other instanceof Mob) {
      return name.equals(((Mob)other).name) && location.equals(((Mob)other).location);
    }
    return false;
  }
}
