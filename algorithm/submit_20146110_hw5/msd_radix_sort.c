/**
* "Msd radix sort"
* - measure running time of 'msd-radix-sort'
* StudentID : 20146110
* Name : moon-tae-hyun
**/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

#define bitsbyte    8
#define bytesword   4
#define R           (1 << bitsbyte)//R=256
#define digit(A,B)  (((A) >> (32-((B)+1)*bitsbyte)) & (R-1))

void insertion_sort(unsigned int * arr, int n)
{
	int i, j;
	unsigned int data;

	for (i = 1; i < n; i++)
	{
		data = arr[i];
		
		for (j = i - 1; j >= 0; j--)
		{
			if (arr[j] > data)
				arr[j + 1] = arr[j];
			else
				break;
		}
		arr[j + 1] = data;
	}//insertionsort algorithm
	
}

void radixMSD(unsigned int *a, int l, int r, int d, unsigned int * aux) {
	int i, j;
	int count[R+1];
	if (d > bytesword)
		return;
	if(r<=l)
		return;
	if (r-l <= 100) {
		insertion_sort(a+l, r-l+1);
		return;
	}//when subarray is too small, sorting by insertionsort. cutoff is 100
	
	for (j = 0; j < R; ++j) {
		count[j] = 0;
	}

	for (i = l; i <= r; ++i) {
		count[digit(a[i], d) + 1]++;
	}//compute frequency count
	
	for (j = 1; j < R; ++j) {
		count[j] += count[j-1];
	}//transform counts to indices(comulate)
	
	for (i = l; i <= r; ++i) {
		aux[count[digit(a[i], d)]++] = a[i];
	}//distribute
	
	for (i = l; i <= r; ++i) {
		a[i] = aux[i-l];
	}//copy back
	
	radixMSD(a, l, l+count[0]-1, d+1, aux);
	
	for (j = 0; j < R-1; ++j) {
		radixMSD(a, l+count[j], l+count[j+1]-1, d+1, aux);
	}//recursively sort
}

void sort(unsigned int *a, int l, int r, unsigned int *aux) {
	radixMSD(a, l, r, 0,aux);
}


void showdata(unsigned int * array, int num)
{
	int i;
	for (i = 0; i<num; i++)
	{
		printf("%u\n", array[i]);
	}
}// show  data function

int main(int argc, char* argv[]) {

	int j = 0;
	unsigned int N = 0;
	clock_t start;
	double elapsed;
	double duration;
	int counter;
	unsigned int * array;
	unsigned int * tmp;
	char * numcheck = NULL;	
	unsigned int * aux;

	if (argc != 3)
	{
		printf("the number of argument must be three \n");
		exit(1);
	}

	N = strtoul(argv[2], &numcheck, 10);

	if(*numcheck!='\0' || argv[2][0] == '-')
	{	
		printf("input is incorrect\n");
		exit(1);
	}//exception about incorrect input

	array = (unsigned int*)malloc(sizeof(int)*(N));
	
	FILE * fp = fopen(argv[1], "rt"); //file I/o
	if (fp == NULL)
	{
		printf("you can not open text or text is not exist\n");
		free(array);
		fclose(fp);
		exit(1);
	}	//exception about no input file

	for (j = 0; j < N; j++)
	{
		if (EOF == fscanf(fp, "%u", array + j))
			break;
	}//get data from text 
	

	fclose(fp);

	aux=(unsigned int*)malloc(sizeof(int)*(N+1));

	tmp = (unsigned int*)malloc(sizeof(int)*(j));

	memcpy(tmp, array, sizeof(int)*(j));

	start = clock();
	counter = 0;
	do {
		counter++;
		memcpy(array, tmp, sizeof(int)*(j));
		sort(array, 0, j - 1, aux);//" j" is always same with the number of data that sorted. so i use "j"
		elapsed = (double)(clock() - start) * 1000 / CLOCKS_PER_SEC;

	} while (elapsed<1000);
	duration = elapsed / counter;
	
	showdata(array, j);
	printf("N = %7u,\tRunning_Time = %.3f ms\n", N, duration);
	free(tmp);
	free(aux);
	free(array);
	return 0;

}

