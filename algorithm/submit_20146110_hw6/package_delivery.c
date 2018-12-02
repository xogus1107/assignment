// 20146110 ¹®ÅÂÇö

#include <stdio.h>
#include <stdlib.h>


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
	int i, j, k;
	int ** dronetime;
	int dronecount = 0;
	int * findminimumware;
	int * findminimumdistance;
	int * itemgoto;
	int sum = 0;
	int mintime;
	int selecteddrone;
	int maximumdronetime;
	int maximumdroneware;
	int maximumdroneindex;
	if (argc!= 2)
	{
	printf("the number of argument must be two\n");
	}

	FILE * fp = fopen(argv[1], "rt");

	if (fp == NULL)
	{
		printf("you can not open text or text is not exist\n");
		fclose(fp);
		exit(1);
	}	//exception about no input file

	fscanf(fp, "%d", &W);
	fscanf(fp, "%d", &L);
	wn = (int*)malloc(sizeof(int)*(W));
	kn = (int*)malloc(sizeof(int)*(W));
	t = (int**)malloc(sizeof(int*)*(W));
	findminimumware = (int*)malloc(sizeof(int)*L);
	findminimumdistance = (int*)malloc(sizeof(int)*L);

	for (i = 0; i < W; i++)
	{
		*(t + i) = (int*)malloc(sizeof(int)*L);
	}
	//printf("\n");
	i = 0;
	while (i<W)
	{
		fscanf(fp, "%d", wn + i);
		fscanf(fp, "%d", kn + i);
		for (j = 0; j < L; j++)
		{
			fscanf(fp, "%d", &temp);
			t[i][j] = temp;
		}
		i++;
	}

	dronetime = (int**)malloc(sizeof(int*)*W);
	for (i = 0; i < W; i++)
	{

		*(dronetime + i) = (int*)malloc(sizeof(int)*(kn[i]));
		for (j = 0; j < kn[i]; j++)
		{
			dronetime[i][j] = 0;
			dronecount++;
		}
	}
	
	
	fscanf(fp, "%d", &N);
	destination = (int*)malloc(sizeof(int)*(N));
	itemN = (int*)malloc(sizeof(int)*N);
	for (i = 0; i<N; i++)
	{
		fscanf(fp, "%d", &j);
		itemN[i] = j;
		fscanf(fp, "%d", &temp);
		destination[i] = temp;
	}

	//file IO is over
	
	//find minimumdistance warehouse
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
	
	itemgoto = (int*)malloc(sizeof(int)*N);
	//find warehouse, each item goto
	for (i = 0; i < N; i++)
	{	
		itemgoto[i] = findminimumware[destination[i] - 1];
		
	}
	
	//print item's warehouse
	printf("%d\n", N);
	for (i = 0; i < N; i++)
	{
		printf("%d %d\n", i + 1, itemgoto[i] + 1);
	}
	temp = 0;
		

	//measuring delivering time
	maximumdronetime = 0;
	for (k = 0; k < N; k++) {
		
		i = itemgoto[k];
		mintime = 2000000000;//c has no infinity ,so i set this bit number.
		for (j = 0; j < kn[i]; j++)
		{
			if (dronetime[i][j] < mintime)
			{
				mintime = dronetime[i][j];
				selecteddrone = j;
			}
		}
		dronetime[i][selecteddrone] += t[i][destination[k] - 1];
		if (maximumdronetime < dronetime[i][selecteddrone])
		{
			maximumdronetime = dronetime[i][selecteddrone];			
		}
	}
	
	printf("Total delivery time = %d\n", maximumdronetime);

	for (i = 0; i < W; i++)
		free(*(t + i));
	for (i = 0; i < W; i++)
		free(*(dronetime + i));
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