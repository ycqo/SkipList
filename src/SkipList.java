import java.io.*;
import java.util.Comparator;

public class SkipList<K, V> { //定义基于跳表的KV键值对存储数据的类

    public static final int MAX_LEVEL = 32; //该跳表存储数据最大的层数为32

    public static final String FILE = "dump.txt"; // 该跳表存储数据放在dumpfile.txt文件中

    public static final double P = 0.25; //用于产生随机数

    private Comparator<K> comparator; //比较key的比较器

    private int size; // 结点个数

    private int level; //跳表层数

    private Node<K, V> first = new Node<>(null, null, MAX_LEVEL); // 头结点

    public SkipList() {
        this(null);
    }

    public SkipList(Comparator<K> comparator) {
        this.comparator = comparator;
    }

    private int compare(K k1, K k2) { //比较键值对k1，k2的大小
        if(comparator != null) {
            return comparator.compare(k1, k2);
        }
        return ((Comparable<K>)k1).compareTo(k2);
    }

    private void checkKey(K key) throws IllegalAccessException {
        if(key == null) {
            throw new IllegalAccessException("key must not be null.");
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    private int randomLevel(){ // 产生随机层数
        int level = 1;
        while(Math.random() < P && level < MAX_LEVEL){
            level++;
        }
        return level;
    }

    //添加数据
    public V put(K key, V value) {
        try {
            checkKey(key);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        Node<K, V> node = first;
        Node<K, V>[] previousNodes = new Node[level]; //前驱结点

        //找到插入地方前每下降一层时下降的结点并放入前驱结点中
        for(int i = level-1; i >= 0; i--) {
            int cmp = -1;
            //找到第i层下降的结点
            while (null != node.nexts[i] && (cmp = compare(node.nexts[i].getKey(), key))< 0){
                node = node.nexts[i];
            }
            //判断结点是否存在,若存在，则更新成最新数据
            if(cmp == 0){
                V oldValue = node.nexts[i].getValue();
                node.nexts[i].setValue(value);
                return  oldValue;
            }
            //该节点不存在，把第i层下降的结点放在地i给前驱结点数组中
            previousNodes[i] = node;
        }
        int newLevel = randomLevel();
        Node<K, V> newNode = new Node<>(key, value, newLevel);

        //维护前驱和后继
        for(int i = 0; i<newLevel; i++){
            //当新节点层数小于跳表层数时，该节点插入是前面的结点在前驱结点中，否则，该节点前驱结点是头结点
            if(i < previousNodes.length){
                newNode.nexts[i] = previousNodes[i].nexts[i];
                previousNodes[i].nexts[i] = newNode;
            } else {
                first.nexts[i] = newNode;
            }
        }

        size++;
        this.level = Integer.max(newLevel, this.level);
        return null;
    }

    //删除结点
    public V remove(K key) {
        try {
            checkKey(key);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        Node<K, V> node = first;
        Node<K, V>[] previousNodes = new Node[level]; //前驱结点
        boolean flag = false; //判断结点是否存在

        //找到该节点并把该节点前驱结点存储到数组中
        for(int i = level-1; i >= 0; i--) {
            int cmp = -1;
            while(node.nexts[i] != null && (cmp = compare(node.nexts[i].getKey(), key)) < 0){
                node = node.nexts[i];
            }
            if(cmp == 0){
                flag = true;
            }
            previousNodes[i] = node;
        }
        if(!flag){
            return null;
        }

        Node<K, V> removeNode = node.nexts[0]; //删除结点在最底层前驱结点的后面一位
        for(int i = 0; i < removeNode.getLevel(); i++) {
            previousNodes[i].nexts[i] = removeNode.nexts[i];
        }
        size--;
        //当更新后头结点上层后面结点为空时，跳表层数减少
        int newLevel = level;
        while(--newLevel >= 0 && first.nexts[newLevel] == null){
            level--;
        }
        return removeNode.getValue();
    }

    //根据key查询value
    public V get(K key) {
        try {
            checkKey(key);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        Node<K, V> node = first;
        for(int i = level-1; i >= 0; i--) {
            int cmp = -1;
            while(node.nexts[i] != null && (cmp = compare(node.nexts[i].getKey(), key)) < 0){
                node = node.nexts[i];
            }
            if(cmp == 0){
                return node.nexts[i].getValue();
            }
        }
        return null;
    }

    //显示数据
    public void display(){
        System.out.println("--------display--------");
        for(int i = 0; i < level; i++) {
            Node<K, V> node = first.nexts[i];
            System.out.print("Level"+i+":");
            while(node != null){
                System.out.print(node.getKey()+":"+node.getValue()+";");
                node = node.nexts[i];
            }
            System.out.println();
        }
    }

    //数据落盘
    public void dumpFile(){
        System.out.println("--------dumpFile--------");
        Node<K, V> node = first.nexts[0];
        File file = new File(FILE);
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        try {
            fos = new FileOutputStream(file, true);
            osw = new OutputStreamWriter(fos);
            bw = new BufferedWriter(osw);
            while(node != null){
                bw.write(node.getKey()+":"+node.getValue());
                System.out.println(node.getKey()+":"+node.getValue());
                bw.newLine();
                node = node.nexts[0];
            }
            return ;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                osw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //数据加载
    public void loadFile() {
        System.out.println("--------laodFile--------");
        File file = new File(FILE);
        String line;
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            while((line=br.readLine())!=null){
                String[] s=line.split(":");
                K key = (K) s[0];
                V value = (V) s[1];
                put(key, value);
                System.out.println("key:"+s[0]+",value:"+s[1]);
            }
            return ;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                isr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
