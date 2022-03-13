public class Main {
    public static void main(String[] args) {

        SkipList<String, String> skiplist = new SkipList<>();
        skiplist.put("1", "qwer");
        skiplist.put("12", "asd");
        skiplist.put("4", "ghj");
        skiplist.put("10", "b");
        skiplist.put("7", "g");
        skiplist.put("8", "t");
        skiplist.put("9", "hg");
        skiplist.put("2", "rt");
        skiplist.loadFile();
        skiplist.display();
    //    skiplist.dumpFile();
        System.out.println(skiplist.get("4"));
        System.out.println("skiplist size:"+skiplist.size());

    }
}
