package template.graph;

/**
 * 字典树表示的二进制数字
 */
class BitTrie {
    public static final int MAX_BIT = 31;
    static class Node {
        Node[] child = new Node[2];
        int prefix = 0;
        int count = 0;
    }

    Node root;
    public BitTrie() {
        root = new Node();
    }

    public void add(int val) {
        Node node = root;
        int prefix = 0;
        for (int i = MAX_BIT - 1; i >= 0; i--) {
            int bit = val>>i&1;
            if (node.child[bit] == null) {
                node.child[bit] = new Node();
            }
            node = node.child[bit];
            prefix |= bit<<i;
            node.prefix = prefix;
            node.count ++;
        }
    }

    public void remove(int val) {
        Node node = root;
        for (int i = MAX_BIT - 1; i >= 0; i--) {
            int bit = val>>i&1;
            node = node.child[bit];
            node.count --;
        }
    }

    // 求 num^x 最大的x，x是在树中存在的数字
    public int maxXor(int num) {
        Node node = root;
        int ans = 0;
        for (int i = MAX_BIT - 1; i >= 0; i--) {
            int bit = num>>i&1;
            if (node.child[1-bit]!=null && node.child[1-bit].count>0) {
                bit = 1-bit;
                ans |= 1<<i;
            } else if (node.child[bit]!=null && node.child[bit].count>0) {
            } else {
                return -1;
            }
            node = node.child[bit];
        }
        return ans;
    }

    public int minXor(int num) {
        Node node = root;
        int ans = 0;
        for (int i = MAX_BIT - 1; i >= 0; i--) {
            int bit = num>>i&1;
            if (node.child[bit]!=null && node.child[bit].count>0) {
            } else if (node.child[1-bit]!=null && node.child[1-bit].count>0) {
                bit = 1-bit;
                ans |= 1<<i;
            } else {
                return -1;
            }
            node = node.child[bit];
        }
        return ans;
    }
}


/**
 * 向树中添加数字用递归的方式，自顶向下（高位到低位），优势是可以更灵活的统计子树，比如求最大值、最小值，并且支持移除数字，这用迭代方式是实现不了的。
 * 缺点是性能不如迭代方式，常数更大。
 */
class RecurBitTrie {
    public static final int MAX_BIT = 31;

    static class Node {
        Node[] child = new Node[2];
        int prefix = 0;
        int count = 0;
        int max = 0;
        int min = Integer.MAX_VALUE;
    }

    Node root;

    public RecurBitTrie() {
        root = new Node();
    }

    public void add(int val, int cnt) {
        // 和查询时的开始bit务必对上
        add(root, MAX_BIT - 1, val, cnt);
    }

    // node是parent节点，对应 i-1
    private void add(Node node, int i, int val, int cnt) {
        if (i < 0) {
            //node是不包含第i位的前缀
            node.max = node.min = val;
            node.count += cnt;
            return;
        }
        int bit = (val >> i) & 1;
        if (node.child[bit] == null) {
            node.child[bit] = new Node();
            node.child[bit].prefix = node.prefix | (bit << i);
        }
        node.count += cnt;
        add(node.child[bit], i - 1, val, cnt);
        node.max = 0;
        node.min = Integer.MAX_VALUE;
        if (node.child[0] != null && node.child[0].count > 0) {
            node.max = Math.max(node.max, node.child[0].max);
            node.min = Math.min(node.min, node.child[0].min);
        }
        if (node.child[1] != null && node.child[1].count > 0) {
            node.max = Math.max(node.max, node.child[1].max);
            node.min = Math.min(node.min, node.child[1].min);
        }
    }

    // 求 num^x 最大的x，x是在树中存在的数字
    public int maxXor(int num, int lowerBound) {
        Node node = root;
        int ans = 0;
        for (int i = MAX_BIT - 1; i >= 0; i--) {
            int bit = (num >> i) & 1;
            if (node.child[1 - bit] != null && node.child[1 - bit].count > 0
                    && node.child[1 - bit].max >= lowerBound) {
                bit = 1 - bit;
                ans |= 1 << i;
            } else if (node.child[bit] != null && node.child[bit].count > 0
                    && node.child[bit].max >= lowerBound) {
            } else {
                return -1;
            }
            node = node.child[bit];
        }
        return ans;
    }

    public int minXor(int num, int upperBound) {
        Node node = root;
        int ans = 0;
        for (int i = MAX_BIT - 1; i >= 0; i--) {
            int bit = (num >> i) & 1;
            if (node.child[bit] != null && node.child[bit].count > 0
                    && node.child[bit].min <= upperBound) {
            } else if (node.child[1 - bit] != null && node.child[1 - bit].count > 0
                    && node.child[1 - bit].min <= upperBound) {
                bit = 1 - bit;
                ans |= 1 << i;
            } else {
                return -1;
            }
            node = node.child[bit];
        }
        return ans;
    }
}