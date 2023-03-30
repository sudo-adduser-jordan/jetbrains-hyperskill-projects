package main

import (
	"bufio"
	"crypto/md5"
	"encoding/hex"
	"fmt"
	"io"
	"io/fs"
	"log"
	"os"
	"path/filepath"
	"sort"
	"strconv"
	"strings"
)

func main() {
	//Declaring and initializing the structure of the user interface
	var ui UserInterface
	ui.Directory = ui.GetDirectoryArg()
	ui.FileFormat = ui.GetFileFormat()
	ui.SortingOption = ui.GetSortOption()

	// Declaring the structure of the file finder and initialize the search results storage
	var finder = FileFinder{Storage: make(map[int][]FileObject)}

	// Searching for files by user-defined parameters
	finder.FindingFiles(ui.Directory, ui.FileFormat)
	finder.SortedSizes = finder.SortingFilesBySize(ui.SortingOption)

	//Output the search results in the user interface with user-defined parameters
	ui.SortedOutput(finder)

	// If necessary, output duplicates
	if ui.IsCheckDuplicate() {
		ui.DuplicateOutput(finder)
	} else {
		os.Exit(0)
	}

	// If necessary, delete files
	if ui.isDeletingFiles() {
		filesToDelete := ui.GetFilesToDelete(finder.Storage)
		bytesToFree := finder.DeleteFiles(filesToDelete)
		ui.OutputResult(bytesToFree)
	} else {
		os.Exit(0)
	}
}

// UserInterface - all communication with the user takes place in this structure
type UserInterface struct {
	Directory     string
	FileFormat    string
	SortingOption int8
}

// GetDirectoryArg - Getting the directory name from the command line
func (ui UserInterface) GetDirectoryArg() string {
	if len(os.Args) != 2 {
		fmt.Println("Directory is not specified")
		os.Exit(0)
	}
	return os.Args[1]
}

// GetFileFormat - Getting the directory name from the command line
func (ui UserInterface) GetFileFormat() string {
	var data string
	fmt.Println("Enter file format:")
	fmt.Scanln(&data)
	return "." + data
}

// GetSortOption - Getting the sorting order of the result
func (ui UserInterface) GetSortOption() int8 {
	fmt.Println("Size sorting options:")
	fmt.Println("1. Descending")
	fmt.Println("2. Ascending")
	var option int8
	for {
		fmt.Println("Enter a sorting option:")
		fmt.Scanln(&option)
		if option != 1 && option != 2 {
			fmt.Println("Wrong option")
		} else {
			return option
		}
	}
}

// FileObject - All search results are stored in the form of such structures
type FileObject struct {
	Id   int
	Path string
	Size int
	Hash string
}

// FileFinder - structure for searching files by specified values and saving the result to the repository
type FileFinder struct {
	Storage     map[int][]FileObject
	SortedSizes []int
}

// FindingFiles - search for files in a given directory and save the result in a map
// with the key size and FileObject structure value
func (f FileFinder) FindingFiles(directory string, fileFormat string) {

	err := filepath.Walk(directory, func(path string, info fs.FileInfo, err error) error {
		if err != nil {
			log.Fatal(err)
		}

		if !info.IsDir() {
			if filepath.Ext(path) == fileFormat || fileFormat == "." {

				size := int(info.Size())
				// Calculate hash-sum for file and stored it
				file, err := os.Open(path)
				if err != nil {
					log.Fatal(err)
				}

				defer file.Close()
				h := md5.New()
				if _, err := io.Copy(h, file); err != nil {
					log.Fatal(err)
				}

				f.Storage[size] = append(f.Storage[size], FileObject{
					Size: size,
					Path: path,
					Hash: hex.EncodeToString(h.Sum(nil))})
			}
		}
		return nil
	})
	if err != nil {
		log.Fatal(err)
	}
}

// SortingFilesBySize - Get a sorted slice of the size values for further use in output
func (f FileFinder) SortingFilesBySize(option int8) []int {
	// Extract all the sizes from the search results and put them in a slice
	for key := range f.Storage {
		f.SortedSizes = append(f.SortedSizes, key)
	}
	// Sort the resulting slice with the sizes
	switch option {
	//dsc
	case 1:
		sort.Slice(f.SortedSizes, func(i, j int) bool {
			return f.SortedSizes[i] > f.SortedSizes[j]
		})
	//asc
	case 2:
		sort.Slice(f.SortedSizes, func(i, j int) bool {
			return f.SortedSizes[i] < f.SortedSizes[j]
		})
	}
	return f.SortedSizes
}

// SortedOutput - Output of search results in sorted mode
func (ui UserInterface) SortedOutput(f FileFinder) {

	// In the loop, for each size, extract the path from the storage and print it
	for _, size := range f.SortedSizes {
		fmt.Printf("%d bytes\n", size)
		for _, fileObj := range f.Storage[size] {
			fmt.Println(fileObj.Path)
		}
	}

}

// IsCheckDuplicate - Getting the need to search for duplicates
func (ui UserInterface) IsCheckDuplicate() bool {
	var result string

	for {
		fmt.Println("Check for duplicates?")
		fmt.Scanln(&result)

		switch result {
		case "yes":
			return true
		case "no":
			return false
		default:
			fmt.Println("Wrong option")
		}
	}
}

// DuplicateOutput - Output only duplicates
func (ui UserInterface) DuplicateOutput(f FileFinder) {
	var prevSize int
	var counterId = 1

	// In the loop, use sizes depending on the selected sorting
	for _, size := range f.SortedSizes {

		tmp := make(map[string][]FileObject)

		// In the loop, we create map with a keys from the hash-sums of the found files
		for _, fileObjects := range f.Storage[size] {
			tmp[fileObjects.Hash] = append(tmp[fileObjects.Hash], fileObjects)
		}

		// In the loop, use the obtained values of FileObjects to output
		for hash, fileObjects := range tmp {
			if len(fileObjects) > 1 {
				if prevSize != size {
					fmt.Printf("%d bytes\n", size)
					prevSize = size
				}
			}
			// If the received objects are at least 2 (got duplicates) - assign an ID and print
			if len(fileObjects) > 1 {
				fmt.Printf("HASH: %s\n", hash)
				for object := range fileObjects {
					tmp[hash][object].Id = counterId
					for n, o := range f.Storage[size] {
						if o.Path == tmp[hash][object].Path {
							f.Storage[size][n].Id = counterId
							break
						}
					}
					counterId++
					fmt.Printf("%d. %s\n", tmp[hash][object].Id, tmp[hash][object].Path)
				}
			}
		}
	}
}

// isDeletingFiles - Need to delete files?
func (ui UserInterface) isDeletingFiles() bool {
	var result string
	for {
		fmt.Println("Delete files?")
		fmt.Scanln(&result)
		switch result {
		case "yes":
			return true
		case "no":
			return false
		default:
			fmt.Println("Wrong option")
		}
	}
}

// GetFilesToDelete - Get a slice of the ID files to delete.
// Check the input for errors and the ID for their presence.
func (ui UserInterface) GetFilesToDelete(storage map[int][]FileObject) []int {
	var fileToDelete []int
	var isPresent = false
	var isWrongFormat = false

	for {
		fileToDelete = []int{}
		fmt.Println("Enter file numbers to delete:")
		scanner := bufio.NewScanner(os.Stdin)
		scanner.Scan()
		input := strings.Split(scanner.Text(), " ")
		for _, i := range input {
			num, err := strconv.Atoi(i)
			if err != nil {
				fmt.Println("Wrong format")
				isWrongFormat = true
				break
			}
			fileToDelete = append(fileToDelete, num)
		}
		if !isWrongFormat {
			for _, id := range fileToDelete {
				isPresent = false
				for _, fileObjs := range storage {
					for _, fileItem := range fileObjs {
						if fileItem.Id == id {
							isPresent = true
							break
						}
					}
					if isPresent {
						break
					}
				}
				if !isPresent {
					fmt.Println("Wrong format in search")
					break
				}
			}
			if isPresent {
				break
			}
		}
	}
	return fileToDelete
}

// DeleteFiles - Deleting files from the slice with their id for deletion
func (f FileFinder) DeleteFiles(toDelete []int) int {
	var bytesToFree int

	for _, idToDel := range toDelete {
		for _, fObjects := range f.Storage {
			for _, fObj := range fObjects {
				if fObj.Id == idToDel {
					bytesToFree += fObj.Size
					err := os.Remove(fObj.Path)
					if err != nil {
						log.Fatal(err)
					}
					break
				}
			}
		}
	}
	return bytesToFree
}

// OutputResult - Just output freed up space.
func (ui UserInterface) OutputResult(toFree int) {
	fmt.Printf("Total freed up space: %d bytes\n", toFree)
}
