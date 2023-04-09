import java.io.*;

public class MainD {
    static StreamTokenizer in = new StreamTokenizer(new BufferedReader(new InputStreamReader(System.in)));
    static PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
    public static double nextDouble() throws IOException{ in.nextToken(); return in.nval; }
    public static float nextFloat() throws IOException{ in.nextToken(); return (float)in.nval; }
    public static int nextInt() throws IOException { in.nextToken(); return (int)in.nval; }
    public static long nextLong() throws IOException { in.nextToken(); return (long)in.nval; }
    public static String next() throws IOException{ in.nextToken(); return in.sval;}
    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    static void printYesNo(boolean b) {out.println(b?"Yes":"No");}

    public static class OffsetSegTree {

        class Node {
            Node left;
            Node right;
            boolean hasVal;
            long offset;
            int ls, rs;//debug用
        }

        int maxN;
        Node root;

        public OffsetSegTree(int maxN) {
            this.maxN = maxN;
            this.root = new Node();
            root.ls = 0;
            root.rs = maxN;
        }

        public void add(int l, int r, long val) {
            add(root, l, r, val, 0, maxN);
        }

        /**
         * 当前Node的范围: [ls,rs]
         */
        private void add(Node node, int l, int r, long offset, int ls, int rs) {
            if (l < 0 || r > maxN) {
                throw new IllegalArgumentException();
            }
            if (l <= ls && rs <= r) {
                //[l,r]覆盖了当前子树
                node.hasVal=true;
                node.offset = offset;
                return;
            }

            pushDown(node, ls, rs);
            int mid = ls + rs >> 1;
            //左子树[ls,mid]
            //右子树[mid+1,rs]
            if (l <= mid) {
                add(node.left, l, r, offset, ls, mid);
            }
            if (r >= mid + 1) {
                add(node.right, l, r, offset, mid + 1, rs);
            }
        }

        //对孩子节点的递归调用时要先下传懒标记
        void pushDown(Node node, int ls, int rs) {
            int mid = ls + rs >> 1;
            if (node.left == null) {
                node.left = new Node();
                node.left.ls = ls;
                node.left.rs = mid;
            }
            if (node.right == null) {
                node.right = new Node();
                node.right.ls = mid + 1;
                node.right.rs = rs;
            }
            if (node.hasVal) {
                add(node.left, ls, mid, node.offset, ls, mid);
                add(node.right, mid + 1, rs, node.offset, mid + 1, rs);
                node.hasVal = false;
            }
        }

        public long offset(int i) {
            return offset(root, i,0, maxN);
        }

        private long offset(Node node, int i, int ls, int rs) {
            if (i < 0 || i > maxN) {
                throw new IllegalArgumentException();
            }
            if (node.hasVal) {
                return node.offset;
            }
            if (ls == rs) {
                return 0;
            }
            pushDown(node, ls, rs);
            int mid = ls + rs >> 1;
            if (i <= mid) {
                return offset(node.left, i, ls, mid);
            } else {
                return offset(node.right, i, mid+1, rs);
            }
        }

        //找第一个>=val的下标
        public long bisectLeft(long val, long maxR) {
            long lo=1,hi=maxR;
            while (lo<=hi) {
                long mid=lo+hi>>1;
                if (offset((int) mid)+mid>=val) {
                    hi=mid-1;
                } else {
                    lo=mid+1;
                }
            }
            return lo;
        }
    }

    public boolean resolve() throws Exception {
        int n=nextInt();
        long maxLen=0;
        OffsetSegTree tree = new OffsetSegTree(1000000001);
        for (int i = 0; i < n; i++) {
            int l=nextInt(),r=nextInt();
            long j = tree.bisectLeft(l, maxLen);
            maxLen = Math.max(maxLen,j+(r-l));
            tree.add((int) j, (int) (j+(r-l)), l-j);
        }
        out.println(maxLen);
        return true;
    }

    public static void main(String[] args) throws Exception {
        new MainD().resolve();
        out.flush();
    }
}
