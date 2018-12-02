// 20146110 Moon - tae hyun

#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include<string.h>

void merge(int * array, int left, int mid, int right)
{
	int * temp = (int*)malloc(sizeof(int)*(right - left + 1));
	int tempin = 0;
	int p = left;
	int q = mid + 1;
	int i;

	while (p <= mid && q <= right) 
	{
		if (array[p] >= array[q])				
			temp[tempin] = array[p++];	
		else
			temp[tempin] = array[q++];
			tempin++;
	}
	// compare two data and move to temp

	if (p > mid)
	{
		while (q <= right)
		{
			temp[tempin++] = array[q++];
			
		}
	}
	else 
	{
		while (p <= mid) 
		{
			temp[tempin++] = array[p++];
			
		}
	}
	//move lefted data
	
	for (i = left; i <= right; i++)
	{
		array[i] = temp[i - left];
	}// move all data to array from temp

	free(temp);
}	

void merge_sort(int *array, int left, int right)
{
	if (left >= right)
		return;
	int mid = (left + right) / 2;

	merge_sort(array, left, mid);
	merge_sort(array, mid + 1, right);

	merge(array, left, mid, right);
}// mergesort using recurrence

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
	int n=0;	
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
	
	if(atoi(argv[1])==0)
	{
		printf("input is incorrect\n");
		exit(1);
	}//exception about incorrect command line
	
	
	while(argv[1][n]!=0)
	{
		if('0'>argv[1][n] || argv[1][n]>'9')
		{
			printf("input is incorrect\n");	
			exit(1);
		}
		n++;
	}//exception about incorrect command line
	
	num = atoi(argv[1]); //get the number of data
	
	int * array = (int*)malloc(sizeof(int)*(num));

		
	FILE * fp = fopen(argv[2], "rt"); //file I/o
	if(fp==NULL)
	{
		printf("you can not open text or text is not exist\n");
		free(array);
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
		merge_sort(array, 0, j - 1);	//" j" is always same with the number of data. so i use "j"
		elapsed=(double)(clock() - start)*1000/CLOCKS_PER_SEC;

		}while(elapsed<1000);
		timetask = elapsed/counter;
		free(tmp);
		

	//showdata(array, j); 
	
	printf("running time =  %3f ms \n", timetask);
	
	free(array);
	return 0;

	
}


