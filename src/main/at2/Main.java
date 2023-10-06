package main.at2;

import java.io.*;
import java.util.*;

/**
 * 最大二分图匹配
 * 左侧点数为n，右侧点数为m，为左侧每个点找到一个匹配，左右侧任一点只能匹配一个。
 */
class BipartiteMatching {
    List<List<Integer>> g;
    int[] pa, pb;
    int n, m;
    boolean[] vis;

    public BipartiteMatching(int _n, int _m) {
        n = _n;
        m = _m;
        pa = new int[n];
        pb = new int[m];
        Arrays.fill(pa, -1);
        Arrays.fill(pb, -1);
        g = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            g.add(new ArrayList<>());
        }
    }

    public void add(int from, int to) {
        g.get(from).add(to);
    }

    public boolean dfs(int v) {
        vis[v]=true;
        for (int u : g.get(v)) {
            if (pb[u] == -1) {
                pa[v] = u;
                pb[u] = v;
                return true;
            }
        }
        for (int u : g.get(v)) {
            if (!vis[pb[u]] && dfs(pb[u])) {
                pa[v] = u;
                pb[u] = v;
                return true;
            }
        }
        return false;
    }

    public int solve() {
        int ans=0;
        for (int i = 0; i < n; i++) {
            vis=new boolean[n];
            if (dfs(i)) {
                ans++;
            }
        }
        return ans;
    }
}

public class Main {

    public void solve() throws Exception {
        int n=nextInt(),m=nextInt();
        List<List<List<Integer>>> ar=new ArrayList<>();
        for (int i = 0; i < n; i++) {
            ar.add(new ArrayList<>());
            for (int j = 0; j < 10; j++) {
                ar.get(i).add(new ArrayList<>());
            }
            String s=next();
            for (int j = 0; j < m; j++) {
                ar.get(i).get(s.charAt(j)-'0').add(j);
            }
        }
        //注意题目中时间是0-indexed
        int lo=n-1,hi=n*m;
        while (lo<=hi){
            int mid=lo+hi>>1;
            boolean succ=false;
            outer:
            //遍历所有d，理论复杂度是超了一个10，达到1e8，但不知道为什么能过，可能是数据的局限性，mid小的时候边数很小，mid大的时候只需要尝试很少的d就可以
            for (int d = 0; d <= 9; d++) {
                List<List<Integer>> graph = new ArrayList<>(n);
                int bc=0;
                Map<Integer,Integer> bs=new HashMap<>();
                for (int i = 0; i < n; i++) {
                    graph.add(new ArrayList<>());
                    List<Integer> li = ar.get(i).get(d);
                    int sz=li.size();
                    if (li.isEmpty()) {
                        continue outer;
                    }
                    //对每个reel找一个时间，总共n个reel，每个reel最大可以连接到n*m个时间
                    //但图模型不需要真的连接到n*m，如果每个reel都连接到n个时间，可以证明二分匹配是必有解的，所以每个reel只需取n条边
                    //如果存在边数不足n的reel，又对边数超过n的reel剪枝，也能得到最优解吗？答案是可以，不影响
                    for (int j = 0; j < n; j++) {
                        //对时间离散化
                        int time=li.get(j%sz)+(j/sz)*m;
                        if (time<=mid){
                            if (!bs.containsKey(time)) {
                                bs.put(time,bc++);
                            }
                            graph.get(i).add(bs.get(time));
                        }
                    }
                }
                BipartiteMatching matching = new BipartiteMatching(n, bs.size());
                matching.g = graph;
                if (matching.solve()==n) {
                    succ=true;
                    break;
                }
            }
            if (succ) {
                hi=mid-1;
            } else {
                lo=mid+1;
            }
        }
        out.println(lo>=n*m?-1:lo);
    }

    public static void main(String[] args) throws Exception {
        new Main().solve();
        out.flush();
    }

    static PrintWriter out = new PrintWriter(System.out, false);
    static InputReader in = new InputReader(System.in);
    static String next() { return in.next(); }
    static int nextInt() { return Integer.parseInt(in.next()); }
    static long nextLong() { return Long.parseLong(in.next()); }
    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }
    }
}
