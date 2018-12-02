#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

int main(int argc, char* argv[])
{
	int W; //number of warehouse
	int L; //number of locations
	int *wn;;//warehousenumber
	int *kn; // number of drones;
	int **t; //distance matrix
	int N; //number of item;
	int temp;
	int *D;
	int i,j;

	/*if (argc!= 2)
	{
		printf("the number of argument must be two\n");
	}*/

	FILE * fp = fopen("input-3-5-10-1.txt", "rt");

	if (fp == NULL)
	{
		printf("you can not open text or text is not exist\n");
		fclose(fp);
		exit(1);
	}	//exception about no input file
		
	fscanf(fp, "%d", &W);
	printf("%d", W);
	fscanf(fp, "%d", &L);
	printf("%d", L);
	wn = (int*)malloc(sizeof(int)*(W));
	kn = (int*)malloc(sizeof(int)*(L));
	t = (int**)malloc(sizeof(int*)*(W));
	
	for (i = 0; i < W; i++)
	{
		*(t+i) = (int*)malloc(sizeof(int)*L);
	}
	printf("\n");
	i = 0;
	while(i<3)
	{
		fscanf(fp, "%d", wn + i);
		printf("%d", wn[i]);
		fscanf(fp, "%d", kn + i);
		printf("%d", kn[i]);
		for (j = 0; j < L; j++)
		{
			fscanf(fp, "%d",&temp);
			printf("%d", temp);
			t[i][j] = temp;
		}
		i++;
		printf("\n");
	}

	fscanf(fp, "%d", &N);
	D = (int*)malloc(sizeof(int)*(N));
	for(i=0; i<N; i++)
	{
		fscanf(fp, "%d", &j);
		fscanf(fp, "%d", &temp);
		D[i] = temp;
	}
	for (i = 0; i < N; i++)
	{
		printf("%d", D[i]);
	}
	
	free(wn);
	free(kn);
	free(t);
	//free(D);
	fclose(fp);
}