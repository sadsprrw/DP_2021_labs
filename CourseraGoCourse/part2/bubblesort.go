package main

import "fmt"

func BubbleSort(array []int) {
	for i := 0; i < len(array)-1; i++ {
		for j := 0; j < len(array)-i-1; j++ {
			if array[j] > array[j+1] {
				Swap(array, j)
			}
		}
	}
}

func Swap(array []int, index int) {
	temp := array[index]
	array[index] = array[index+1]
	array[index+1] = temp
}

func main() {
	var number int
	numberArray := []int{}
	for i := 0; i < 10; i++ {
		fmt.Print("Enter a number: ")
		fmt.Scan(&number)
		numberArray = append(numberArray, number)
	}
	BubbleSort(numberArray)
	fmt.Println(numberArray)
}