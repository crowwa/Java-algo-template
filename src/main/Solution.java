package main;

import java.io.*;
import java.util.StringTokenizer;

public class Solution {
    public long solve(int l,long n) throws Exception {
        return 0;
    }

    public static void main(String[] args) throws Exception {
        int T=nextInt();
        for (int i = 0; i < T; i++) {
            int L=nextInt();
            int N=nextInt();
            long r = new Solution().solve(L,N);
            System.out.printf("Case #%d: %d\n", i+1,r);
        }
    }

    static PrintWriter out = new PrintWriter(System.out, true);
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

