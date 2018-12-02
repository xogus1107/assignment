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
	int *itemN;
	int *destination;
	int i,j, k;
	int ** dronetime;
	int count=0;
	int * findminimumware;
	int * findminimumdistance;
	int * itemgoto;
	int sum = 0;
	int mintime;
	int selecteddrone;
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
	//printf("%d", W);
	fscanf(fp, "%d", &L);
	//printf("%d", L);
	wn = (int*)malloc(sizeof(int)*(W));
	kn = (int*)malloc(sizeof(int)*(W));
	t = (int**)malloc(sizeof(int*)*(W));
	findminimumware = (int*)malloc(sizeof(int)*L);
	findminimumdistance = (int*)malloc(sizeof(int)*L);

	for (i = 0; i < W; i++)
	{
		*(t+i) = (int*)malloc(sizeof(int)*L);
	} 
	//printf("\n");
	i = 0;
	while(i<W)
	{
		fscanf(fp, "%d", wn + i);
		//printf("%d", wn[i]);
		fscanf(fp, "%d", kn + i);
		//printf("%d", kn[i]);
		for (j = 0; j < L; j++)
		{
			fscanf(fp, "%d",&temp);
			//printf("%d", temp);
			t[i][j] = temp;
		}
		i++;
		//printf("\n");
	}

	dronetime = (int**)malloc(sizeof(int*)*W);
	for (i = 0; i < W; i++)
	{
		
		*(dronetime + i) = (int*)malloc(sizeof(int)*(kn[i]));
		for (j = 0; j < kn[i]; j++)
		{
			dronetime[i][j] = 0;
			count++;
		}
	}
	
	fscanf(fp, "%d", &N);
	destination = (int*)malloc(sizeof(int)*(N));
	itemN = (int*)malloc(sizeof(int)*N);
	for(i=0; i<N; i++)
	{
		fscanf(fp, "%d", &j);
		itemN[i] = j;
		fscanf(fp, "%d", &temp);
		destination[i] = temp;
	}

	//printf("%d\n", dronetime[2][0]);

	//item 할당할곳 찾기
	for (i = 0; i < L; i++)
	{
		findminimumdistance[i] = t[0][i];
		findminimumware[i] = 0;
		for (j = 0; j < W; j++) {
			
			if (findminimumdistance[i] > t[j][i])
			{
				findminimumdistance[i] = t[j][i];
				findminimumware[i] = j;
				
			}
		}
	}
	//printf("%d\n", findminimumdistance[4]);
	//findminimumdistance[destination[i]-1]
	itemgoto = (int*)malloc(sizeof(int)*N);


	for (i = 0; i < N; i++)
	{
		itemgoto[i] = findminimumware[destination[i] - 1];
	}

	printf("%d\n", N);
	for (i = 0; i < N; i++)
	{
		printf("%d %d\n", i+1, itemgoto[i]+1);
		
	}
	temp = 0;
	for (i = 0; i < W; i++)
	{
		temp += kn[i];
	}
	//measuring delivering time
	for (k = 0; k < N; k++) {
		for (i = 0; i < W; i++)
		{
			if (itemgoto[k] == i) {
				if (kn[i] == 1)
				{
					dronetime[i][0] += t[i][destination[k] - 1];
				}
				else 
				if (kn[i] > 1) {
					mintime = 99999999;
					for (j = 0; j < kn[i]; j++)
					{//find minimum dronetime[i(fixed)][j(변함)]
						if (dronetime[i][j] < mintime) {
							mintime = dronetime[i][j];
							selecteddrone = j;
						}


					}
					dronetime[i][selecteddrone] += t[i][destination[k] - 1];
					
				}
			}
		}
	}
	printf("%d", dronetime[0][1]);


	free(itemgoto);
	free(findminimumware);
	free(findminimumdistance);
	
	free(dronetime);
	free(wn);
	free(kn);
	free(t);
	free(destination);
	free(itemN);
	fclose(fp);
}