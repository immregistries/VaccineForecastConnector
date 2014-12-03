import java.util.ArrayList;
import java.util.List;

public class Kid
{
  public enum HairColor {
    RED, ORANGE, BLACK, GREY, BROWN, PINK, GREEN, PEACH, LIGHT_GREEN, WHITE, YELLOW
  };

  public String name = "";
  public int age = 0;
  public List<Toy> toys = new ArrayList<Toy>();

  public static void main(String[] args) {
    Kid max = new Kid();
    max.name = "Max Bunker";
    max.age = 8;
    max.toys.add(Toy.SCOOTER);
    max.toys.add(Toy.BABY_DOLLS);
    printKid(max);
  }

  public static void printKid(Kid kid) {
    System.out.println(kid.name + " is " + kid.age + " years old.");
    System.out.println("Here are " + kid.name + "'s toys:");
    for (Toy toy : kid.toys) {
      System.out.println(" + " + toy);
    }
  }

}
