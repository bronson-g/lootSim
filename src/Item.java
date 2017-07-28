class Item {
  private int value;
  private String name;
  private String icon;
  private boolean stackable;

  Item() {
    this("nothing", 0, "/dev/null", true);
  }

  Item(String n) {
    name = n;
  }

  Item(String n, int val, String ico, boolean stacks) {
    this(n);
    value = val;
    icon = ico;
    stackable = stacks;
  }

  public String getName() {
    return name;
  }
  public String getIcon() {
    return icon;
  }
  public int getValue() {
    return value;
  }

  @Override
  public boolean equals(Object other) {
    if(other instanceof Item) {
      return name.equals(((Item)other).name);
    }
    return false;
  }
}
