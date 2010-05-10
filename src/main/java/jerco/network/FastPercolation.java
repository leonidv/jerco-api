package jerco.network;

import java.util.Random;

public class FastPercolation {
	private static final 	int  L = 5;        /* Linear dimension */
	 private static final 	int  N = (L*L);
	 private static final 	int  EMPTY = (-N-1);

	 private	int ptr[] = new int[N];          /* Array of pointers */
	 private	int nn[][] = new int[N][4];        /* Nearest neighbors */
	 private	int order[]=new int[N];        /* Occupation order */
	 void boundaries()
	 {
	   int i;

	   for (i=0; i<N; i++) {
	     nn[i][0] = (i+1)%N;
	     nn[i][1] = (i+N-1)%N;
	     nn[i][2] = (i+L)%N;
	     nn[i][3] = (i+N-L)%N;
	     if (i%L==0) nn[i][1] = i+L-1;
	     if ((i+1)%L==0) nn[i][0] = i-L+1;
	   }
	 }
	 void permutation()
	 {
	   Random a= new Random();
	   int i,j;
	   int temp;
	   for (i=0; i<N; i++) order[i] = i;
	   for (i=0; i<N; i++) {
	     j = (int)(i + (N-i)*a.nextDouble());
	     temp = order[i];
	     order[i] = order[j];
	     order[j] = temp;
	   }
	 }
	 private int findroot(int i)
	 {
	   if (ptr[i]<0) return i;
	   return ptr[i] = findroot(ptr[i]);
	 }
	 void percolate()
	 {
	   int i,j;
	   int s1,s2;
	   int r1,r2;
	   int big=0;

	   for (i=0; i<N; i++) ptr[i] = EMPTY;
	   for (i=0; i<N; i++) {
	     r1 = s1 = order[i];
	     ptr[s1] = -1;
	     for (j=0; j<4; j++) {
	       s2 = nn[s1][j];
	       if (ptr[s2]!=EMPTY) {
	         r2 = findroot(s2);
	         if (r2!=r1) {
	           if (ptr[r1]>ptr[r2]) {
	             ptr[r2] += ptr[r1];
	             ptr[r1] = r2;
	             r1 = r2;
	           } else {
	             ptr[r1] += ptr[r2];
	             ptr[r2] = r1;
	           }
	           if (-ptr[r1]>big) big = -ptr[r1];
	         }
	       }
	     }
	     int i1=i+1;
	     System.out.println(i1+" "+big);
	   }
	 }
}
