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

#define bitsword    32
#define bitsbyte    8
#define bytesword   4
#define R           (1 << bitsbyte)
#define digit(A,B)  (((A) >> (bitsword-((B)+1)*bitsbyte)) & (R-1))

#define key(A)          (A)
#define less(A,B)       (key(A) < key(B))
#define exch(A,B)       { Item t = A; A = B; B = t; }
#define compexch(A,B)   if (less(B, A)) exch(A,B)

typedef int Item;

#define MAX_INPUT  1000000

static Item aux[MAX_INPUT];


void radixLSD(Item a[], int l, int r) {
	int i, j, w, count[R+1];
	
	//for (w = bytesword/2-1; w >= 0; --w) {
	for (w = bytesword-1; w >= 0; --w) {
		for (j = 0; j < R; ++j) {
			count[j] = 0;
		}
		
		for (i = l; i <= r; ++i) {
			count[digit(a[i], w) + 1]++;
		}
		
		for (j = 1; j < R; ++j) {
			count[j] += count[j-1];
		}
		
		for (i = l; i <= r; ++i) {
			aux[count[digit(a[i], w)]++] = a[i];
		}
		
		for (i = l; i <= r; ++i) {
			a[i] = aux[i-l];
		}
	}
}

int sort(Item a[], int l, int r) {
	radixLSD(a, l, r);
	//insertion(a, l, r);
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
	clock_t end;
	double elapsed;
	double duration;
	int counter;
	unsigned int * array;
	unsigned int * tmp;
	char * numcheck = NULL;	

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
		exit(1);
	}	//exception about no input file

	for (j = 0; j < N; j++)
	{
		if (EOF == fscanf(fp, "%u", array + j))
			break;
	}//get data from text 
	

	fclose(fp);

	srand(time(NULL));

		tmp = (unsigned int*)malloc(sizeof(int)*(j));

		memcpy(tmp, array, sizeof(int)*(j));

		start = clock();
		counter = 0;
		do {
			counter++;
			memcpy(array, tmp, sizeof(int)*(j));
			sort(array, 0, j - 1);//" j" is always same with the number of data that sorted. so i use "j"
		elapsed = (double)(clock() - start) * 1000 / CLOCKS_PER_SEC;

		} while (elapsed<1000);

		
		duration = elapsed / counter;
		free(tmp);
	
	showdata(array, j);


	printf("N = %7u,\tRunning_Time = %.3f ms\n", N, duration);
	free(array);
	return 0;

}

