public class Node<K, V> { //定义结点[跳表中的结点]

    private K key;

    private V value; //使用KV键值对存储数据

    private int level; //该节点的层数

    public Node<K, V>[] nexts; //跳表中该结点对应每个层数的后面一个结点，如nexts[10]就是在该节点在第十层后面指向的结点

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Node() {
    }

    public Node(K key, V value, int level) {
        this.key = key;
        this.value = value;
        this.level = level;
        this.nexts = new Node[level]; //该节点有多少层就需要多少个结点数组[每层指向后一个结点]
    }

    @Override
    public String toString() {
        return "Node{" +
                "key=" + key +
                ", value=" + value +
                ", level=" + level +
                '}';
    }
}
