package main

import (
	"bufio"
	"flag"
	"fmt"
	"log"
	"os"
	"strconv"
	"strings"
)

type Ui struct {
	LogStorage   []string
	ImportOption string
	ExportOption string
}

type Card struct {
	Term       string
	Definition string
	Error      int
}

type Storage struct {
	Data []Card
}

type FileManager struct {
	FileName string
}

func main() {
	var storage = Storage{Data: make([]Card, 0)}
	var ui Ui = Ui{
		LogStorage: make([]string, 0),
	}

	importOpt := flag.String("import_from", "", "Enter file name to import")
	exportOpt := flag.String("export_to", "", "Enter file name to export")
	flag.Parse()
	ui.ImportOption = *importOpt
	ui.ExportOption = *exportOpt

	if ui.ImportOption != "" {
		storage.ImportCards(*importOpt, &ui)
	}

	for {
		command := ui.GetCommand()
		storage.ExecuteCommand(command, &ui)
	}
}

// USER INTERFACE ///////////////////////////////////////////////////////////////////////////////////////////////

func (u *Ui) GetUserInput() string {
	reader := bufio.NewReader(os.Stdin)
	line, _ := reader.ReadString('\n')
	line = strings.TrimSpace(line)
	u.LogStorage = append(u.LogStorage, line)
	return line
}

func (u *Ui) GetCommand() string {
	commandSet := map[string]struct{}{
		"add":          {},
		"remove":       {},
		"import":       {},
		"export":       {},
		"ask":          {},
		"log":          {},
		"hardest card": {},
		"reset stats":  {},
		"exit":         {},
	}
	var command string
	for {
		fmt.Println("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):")
		command = u.GetUserInput()

		if _, ok := commandSet[command]; ok {
			return command
		} else {
			fmt.Println("Wrong command. Try again:")
		}
	}
}

func (u *Ui) LogAndPrint(data string) {
	u.LogStorage = append(u.LogStorage, data)
	fmt.Println(data)
}

// CARD STORAGE /////////////////////////////////////////////////////////////////////////////////
func (s *Storage) ExecuteCommand(command string, ui *Ui) {
	switch command {
	case "add":
		s.createCard(ui)
	case "remove":
		s.removeCard(ui)
	case "import":
		s.ImportCards("", ui)
	case "export":
		s.ExportCards("", ui)
	case "ask":
		s.askUser(ui)
	case "log":
		s.logData(ui)
	case "hardest card":
		s.getHardestCard(ui)
	case "reset stats":
		s.setResetErrors(ui)
	case "exit":
		s.doExit(ui)
	}
}

func (s *Storage) createCard(ui *Ui) {
	var card Card
	var term, definition string

	ui.LogAndPrint("The card :")
	for {
		term = ui.GetUserInput()
		if s.isUniqData(term, "", ui) {
			break
		}
	}
	ui.LogAndPrint("The definition of the card :")
	for {
		definition = ui.GetUserInput()
		if s.isUniqData("", definition, ui) {
			break
		}
	}
	card = Card{
		Term:       term,
		Definition: definition,
		Error:      0,
	}
	s.addCard(&card)
	ui.LogAndPrint(fmt.Sprintf("The pair(\"%s\":\"%s\") has been added.", term, definition))
}

func (s *Storage) addCard(c *Card) {
	s.Data = append(s.Data, *c)
}

func (s *Storage) isUniqData(t string, d string, ui *Ui) bool {
	for _, datum := range s.Data {
		if len(t) != 0 {
			if datum.Term == t {
				ui.LogAndPrint(fmt.Sprintf("The card \"%s\" already exists. Try again:", t))
				return false
			}
		} else {
			if datum.Definition == d {
				ui.LogAndPrint(fmt.Sprintf("The definition \"%s\" already exists. Try again:", d))
				return false
			}
		}
	}
	return true
}

func (s *Storage) removeCard(ui *Ui) {
	var cardToRemove string
	var isDeleted = false

	ui.LogAndPrint("Which card?")
	cardToRemove = ui.GetUserInput()
	for i, datum := range s.Data {
		if datum.Term == cardToRemove {
			s.Data = append(s.Data[:i], s.Data[i+1:]...)
			isDeleted = true
			break
		}
	}
	if isDeleted {
		ui.LogAndPrint("The card has been removed.")
	} else {
		ui.LogAndPrint(fmt.Sprintf("Can't remove \"%s\": there is no such card.", cardToRemove))
	}
}

func (s *Storage) ImportCards(importOpt string, ui *Ui) {
	var fileName string

	if importOpt != "" {
		fileName = importOpt
	} else {
		ui.LogAndPrint("File name:")
		fileName = ui.GetUserInput()
	}

	fileManager := FileManager{FileName: fileName}
	if fileManager.IsFileExist() {
		loadedStrings := 0
		for _, card := range fileManager.ImportData() {
			isDoubleFound := false
			loadedStrings += 1
			for index, datum := range s.Data {
				if datum.Term == card.Term {
					s.Data[index].Definition = card.Definition
					isDoubleFound = true
					break
				}
			}
			if !isDoubleFound {
				s.Data = append(s.Data, card)
			}
		}
		ui.LogAndPrint(fmt.Sprintf("%d cards have been loaded.", loadedStrings))
	} else {
		ui.LogAndPrint("File not found.")
	}
}

func (s *Storage) ExportCards(exportOpt string, ui *Ui) {
	var fileName string

	if exportOpt != "" {
		fileName = exportOpt
	} else {
		ui.LogAndPrint("File name:")
		fileName = ui.GetUserInput()
	}

	fileManager := FileManager{FileName: fileName}
	ui.LogAndPrint(fmt.Sprintf("%d cards have been saved.", fileManager.ExportData(s.Data)))
}

func (s *Storage) askUser(ui *Ui) {
	var timesToAsk int

	ui.LogAndPrint("How many times to ask?")
	timesToAsk, _ = strconv.Atoi(ui.GetUserInput())
	for i := 0; i < timesToAsk; i++ {
		cardTerm := s.Data[i%len(s.Data)].Term
		cardDef := s.Data[i%len(s.Data)].Definition
		ui.LogAndPrint(fmt.Sprintf("Print the definition of \"%s\":", cardTerm))
		userAnswer := ui.GetUserInput()
		if cardDef == userAnswer {
			ui.LogAndPrint("Correct!")
		} else {
			rightAnswer, foundTerm := s.checkWrongAnswer(userAnswer, cardTerm)
			if foundTerm == "" {
				ui.LogAndPrint(fmt.Sprintf("Wrong. The right answer is \"%s\".", cardDef))
			} else {
				ui.LogAndPrint(fmt.Sprintf("Wrong. The right answer is \"%s\", "+
					"but your definition is correct for \"%s\"", rightAnswer, foundTerm))
			}
			s.Data[i%len(s.Data)].Error += 1
		}
	}
}

func (s *Storage) checkWrongAnswer(answer string, currentTerm string) (string, string) {
	var rightDef, foundTrm string
	for _, datum := range s.Data {
		if answer == datum.Definition {
			foundTrm = datum.Term
			break
		}
	}
	for _, datum := range s.Data {
		if datum.Term == currentTerm {
			rightDef = datum.Definition
			break
		}
	}
	return rightDef, foundTrm
}

func (s *Storage) logData(ui *Ui) {
	var fileName string

	ui.LogAndPrint("File name:")
	fileName = ui.GetUserInput()
	fileManager := FileManager{FileName: fileName}
	fileManager.ExportLogs(ui.LogStorage)
	ui.LogAndPrint("The log has been saved.")
}

func (s *Storage) getHardestCard(ui *Ui) {
	var hardestCards []string
	var maxError = 0

	for _, datum := range s.Data {
		if maxError < datum.Error {
			maxError = datum.Error
		}
	}

	if maxError > 0 {
		for _, datum := range s.Data {
			if datum.Error == maxError {
				hardestCards = append(hardestCards, datum.Term)
			}
		}
	}

	if len(hardestCards) == 0 {
		ui.LogAndPrint("There are no cards with errors.")
	} else if len(hardestCards) == 1 {
		ui.LogAndPrint(fmt.Sprintf("The hardest card is \"%s\". "+
			"You have %d errors answering it", hardestCards[0], maxError))
	} else {
		var ans = "The hardest card is "
		for _, card := range hardestCards {
			ans += "\"" + card + "\", "
		}
		ans = ans[:len(ans)-2] + fmt.Sprintf(". You have %d errors answering them.", maxError)
		ui.LogAndPrint(ans)
	}

}

func (s *Storage) setResetErrors(ui *Ui) {
	for i := range s.Data {
		s.Data[i].Error = 0
	}
	ui.LogAndPrint("Card statistics have been reset.")
}

func (s *Storage) doExit(ui *Ui) {
	if ui.ExportOption != "" {
		s.ExportCards(ui.ExportOption, ui)
	}
	fmt.Println("Bye bye!")
	os.Exit(0)

}

// FILE MANAGER //////////////////////////////////////////////////////////////////////////////////
func (m *FileManager) IsFileExist() bool {
	_, err := os.Stat(m.FileName)
	if os.IsNotExist(err) {
		return false
	}
	if err != nil {
		log.Fatal(err)
	}
	return true
}

func (m *FileManager) ExportData(data []Card) int {
	file, err := os.Create(m.FileName)
	if err != nil {
		log.Fatal(err)
	}
	for _, datum := range data {
		_, err := fmt.Fprintln(file, datum.Term+":"+datum.Definition+":"+strconv.Itoa(datum.Error)) // append the additional line
		if err != nil {
			log.Fatal(err)
		}
	}
	return len(data)
}

func (m *FileManager) ImportData() []Card {
	var loadedCards = make([]Card, 0)

	file, err := os.Open(m.FileName)
	if err != nil {
		log.Fatal(err)
	}
	defer file.Close()

	scanner := bufio.NewScanner(file) // create a new Scanner for the file

	for scanner.Scan() {
		cardItem := strings.Split(scanner.Text(), ":")
		cardError, _ := strconv.Atoi(cardItem[2])

		loadedCards = append(loadedCards, Card{
			Term:       cardItem[0],
			Definition: cardItem[1],
			Error:      cardError,
		})
	}

	if err := scanner.Err(); err != nil {
		log.Fatal(err)
	}
	return loadedCards
}

func (m *FileManager) ExportLogs(storage []string) {
	file, err := os.Create(m.FileName)
	if err != nil {
		log.Fatal(err)
	}
	for _, datum := range storage {
		_, err := fmt.Fprintln(file, datum)
		if err != nil {
			log.Fatal(err)
		}
	}
}
