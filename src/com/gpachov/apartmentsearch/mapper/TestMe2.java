package com.gpachov.apartmentsearch.mapper;


import java.util.LinkedList;
import java.util.Queue;

public class TestMe2 {

    static class Pair {
        int Y;
        int X;
        public Pair(int Y, int X) {
            this.Y = Y;
            this.X = X;
        }
    }
    public static void main(String[] args) {
        int result = solution();
        System.out.println(result);
    }

    private static int solution() {
        int A[][] = new int[7][3];
        int N = A.length;
        int M = A[0].length;
        initialize(A);
        // 1. get neighbours map
        // 2. start bfs and fill countries map

        int C[][] = new int[N][M];
        for (int i = 0; i < N; i++) {
            for (int j =0 ; j < M; j++) {
                C[i][j] = -1; //no country yet :/
            }
        }
        int country = 0;
        for (int y = 0; y < N; y++) {
            for (int x = 0; x < M; x++) {
                if (C[y][x] == -1) {
                    Queue<Pair> bfs = new LinkedList<>();
                    bfs.add(new Pair(y,x));
                    while (!bfs.isEmpty()) {
                        Pair current = bfs.poll();
                        C[current.Y][current.X] = country;
                        addPossibleNeighbors(bfs, A, current.Y, current.X, C);
                    }
                    country++;
                }
            }
        }
        return country;
    }

    private static void addPossibleNeighbors(Queue<Pair> bfs, int[][] a, int y, int x, int[][] c) {
        int upX = x;
        int upY = y+1;
        if (upY >= 0 && upY < a.length && a[upY][upX] == a[y][x] && c[upY][upX] ==-1) {
            bfs.add(new Pair(upY, upX));
        }

        int downX = x;
        int downY = y-1;
        if (downY >= 0 && downY < a.length && a[downY][downX] == a[y][x] && c[downY][downX] ==-1) {
            bfs.add(new Pair(downY, downX));
        }

        int leftX = x-1;
        int leftY = y;
        if (leftX >= 0 && leftX < a[0].length && a[leftY][leftX] == a[y][x] && c[leftY][leftX] == -1) {
            bfs.add(new Pair(leftY, leftX));
        }

        int rightX = x+1;
        int rightY = y;
        if (rightX >= 0 && rightX < a[0].length && a[rightY][rightX] == a[y][x] && c[rightY][rightX] == -1) {
            bfs.add(new Pair(rightY, rightX));
        }
    }

    private static void initialize(int[][] a) {
        a[0][0] = 5;
        a[0][1] = 4;
        a[0][2] = 4;
        a[1][0] = 4 ;
        a[1][1] = 3 ;
        a[1][2] = 4;
        a[2][0] = 3 ;
        a[2][1] = 2  ;
        a[2][2] = 4;
        a[3][0] = 2 ;
        a[3][1] = 2  ;
        a[3][2] = 2;
        a[4][0] = 3 ;
        a[4][1] = 3 ;
        a[4][2] = 4;
        a[5][0] = 1 ;
        a[5][1] = 4 ;
        a[5][2] = 4;
        a[6][0] = 4 ;
        a[6][1] = 1 ;
        a[6][2] = 1;
    }
}
