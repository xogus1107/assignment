//20146110 Moon - Tae hyun

#include<stdio.h>
#include<stdlib.h>
#include<time.h>
#include<string.h>

void selection_sort(int * arr, int n)
{
	int i, j;
	int max;
	int temp;

	for (i = 0; i < n - 1; i++)
	{
		max = i;

		for (j = i+1; j < n; j++)
		{
			if (arr[j] > arr[max])
				max = j;
		}//find max integer

		temp = arr[max];
		arr[max] = arr[i];
		arr[i] = temp;
		//swap
	}
}//selection_sort algorithm

void showdata(int * array, int num)
{
	int i;
	for (i = 0; i<num; i++)
	{
		printf("%4d\n", array[i]);
	}
}// show  data function

int main(int argc, char * argv[])
{
	int j = 0;
	int k = 0;
	int num = 0;
	int n = 0;
	int i;
	clock_t start;
	clock_t end;
	double elapsed;
	double timetask;
	int counter;
	
	if(argc!=3)
	{
		printf("the number of argument must be three \n");	
		exit(1);
	}
	
	while(argv[1][n]!=0)
	{
		if('0'>argv[1][n] || argv[1][n]>'9')
		{
			printf("input is incorrect\n");
			exit(1);
		}
		n++;
	}//exception about incorrect command line

	if (atoi(argv[1]) == 0)
	{
		printf("input is incorrect\n");
		exit(1);
	}//exception about incorrect command line
	
	
	
	num = atoi(argv[1]); //get the number of data
	
	int * array = (int*)malloc(sizeof(int)*(num));
	
	FILE * fp = fopen(argv[2], "rt"); //file I/o
	if (fp == NULL)
	{
		printf("you can not open text or text is not exist\n");
		exit(1);
	}	//exception about no input file
	
	for (j = 0; j < num; j++)
	{
		if (EOF == fscanf(fp, "%d", array + j))
			break;
	}//get data from text 
		
	fclose(fp);

	
	
		
		int * tmp = (int*)malloc(sizeof(int)*(j));

		memcpy(tmp,array,sizeof(int)*(j));

		start = clock();
		counter=0;
		do{
		counter++;
		memcpy(array,tmp,sizeof(int)*(j));
		selection_sort(array,j); //" j" is always same with the number of data. so i use "j"
		elapsed=(double)(clock() - start)*1000/CLOCKS_PER_SEC;
	
		}while(elapsed<1000);
		
		timetask = elapsed/counter;
		free(tmp);

	showdata(array, j); 

		printf("running time =  %3f ms \n", timetask);
		free(array);
	return 0;
}

