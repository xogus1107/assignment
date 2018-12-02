/**
 * "Quick Sort"
 * - ./basic_quick_sort <input_file_name> <N>
 * - measure running time of 'Quick Sort'
 * 20146110
 * moon-tae-hyun
  **/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>
#include <unistd.h>
#include <time.h>

void choose_pivot (int *data, unsigned int n) {
	
	int tmp;
	int pivotidx = rand()%(n);
	//choose pivotidx 0~n-1
	tmp = data[0];
	data[0] = data[pivotidx];
	data[pivotidx] = tmp;
	//swap pivot to first element	
}

void basic_quick_sort (int *data, unsigned int n) {
    
	int i, j; 
	int temp;
	
	if (n<=1)
	{
		return; // terminate function
	}
	
	choose_pivot(data, n);
	// choose pivot and  always place it at first element of array
	i = 1; //i initialize to 1 because array[0] is the pivot element

	for (j = 1; j < n; j++) //j initialize to 1 because array[0] is the pivot element
	{
		if (data[j] < data[0])
		{
			temp = data [j];
			data[j] = data[i];
			data[i] = temp;			
			i++;
		}
	}//partitioning array
	
	temp = data [0];
	data[0] = data[i-1];
	data[i-1] = temp;	
	//swap pivot with i-1 element
	
	basic_quick_sort(data, i-1);
	basic_quick_sort(data+i, n-i);
    //recursive call
}

void showdata(int * array, int num)
{
	int i;
	for (i = 0; i<num; i++)
	{
		printf("%4d\n", array[i]);
	}
}// show  data function

int main (int argc, char* argv[]) {

	int j = 0;
	int k = 0;
	int N = 0;
	int p = 0;
	int i;
	clock_t start;
	clock_t end;
	double elapsed;
	double duration;
	int counter;
	int * array;
	int * tmp;
	
	if (argc != 3)
	{
		printf("the number of argument must be three \n");
		exit(1);
	}

	while (argv[2][p] != 0)
	{
		if ('0'>argv[2][p] || argv[2][p]>'9')
		{
			printf("input is incorrect\n");
			exit(1);
		}
		p++;
	}//exception about incorrect command line

	if (atoi(argv[2]) == 0)
	{
		printf("input is incorrect\n");
		exit(1);
	}//exception about incorrect command line



	N = atoi(argv[2]); //get the number of data

	array = (int*)malloc(sizeof(int)*(N));

	FILE * fp = fopen(argv[1], "rt"); //file I/o
	if (fp == NULL)
	{
		printf("you can not open text or text is not exist\n");
		exit(1);
	}	//exception about no input file

	for (j = 0; j < N; j++)
	{
		if (EOF == fscanf(fp, "%d", array + j))
			break;
	}//get data from text 

	fclose(fp);

	srand(time(NULL));

		tmp = (int*)malloc(sizeof(int)*(j));

		memcpy(tmp, array, sizeof(int)*(j));

		start = clock();
		counter = 0;
		do {
			counter++;
			memcpy(array, tmp, sizeof(int)*(j));
			basic_quick_sort(array, j); //" j" is always same with the number of data that sorted. so i use "j"
			elapsed = (double)(clock() - start) * 1000 / CLOCKS_PER_SEC;

		} while (elapsed <1000);

		duration = elapsed / counter;
		free(tmp);

	//showdata(array, j);
	
	
	printf("N = %7d,\tRunning_Time = %.3f ms\n", N, duration);

	free(array);
	return 0;		

}

