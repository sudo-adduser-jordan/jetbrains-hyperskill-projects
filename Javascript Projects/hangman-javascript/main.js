const input = require("sync-input");

//wordlist
const wordList = ["java", "python", "javascript", "swift"];

//counters
let wins = 0;
let loses = 0;

//funtion to replace character in string - strings are immutables in javascript
String.prototype.replaceAt = function (index, replacement) {
  return (
    this.substring(0, index) +
    replacement +
    this.substring(index + replacement.length)
  );
};

console.log(`H A N G M A N # 8 attempts`);

Menu();

function Menu() {
  let menuselection = input(
    'Type "play" to play the game, "results" to show the scoreboard, and "exit" to quit: '
  );

  switch (menuselection) {
    case "play":
      Play();
      break;
    case "results":
      Results();
      break;
    case "exit":
      Exit();
      break;
    default:
      Menu();
      break;
  }
}

function Results() {
  console.log(`You won: ${wins} times`);
  console.log(`You lost: ${loses} times`);
  Menu();
}

function Exit() {
  process.exit(0);
}

//initiate game loop
function Play() {
  let attemptNumber = 8;
  let userGuessArray = [];
  //choose random word
  let word = wordList[Math.floor(Math.random() * wordList.length)];
  //change randomly chosen word to "-"
  let wordModified = "";
  for (i = 0; i < word.length; i++) {
    wordModified += "-";
  }
  console.log();
  console.log(wordModified);

  while (attemptNumber > 0) {
    let userGuess = input("Input a letter: ");
    if (userGuess == "" || userGuess.length > 1 || userGuess.length <= 0) {
      console.log("Please, input a single letter.");
      console.log();
      console.log(wordModified);
    } else if (
      (userGuess != userGuess.toLowerCase() &&
        userGuess.match(/[a-z]/i) &&
        userGuess.length == 1) ||
      !userGuess.match(/[a-z]/i)
    ) {
      console.log(
        "Please, enter a lowercase letter from the English alphabet."
      );
      console.log();
      console.log(wordModified);
    } else if (userGuessArray.includes(userGuess)) {
      console.log("You've already guessed this letter.");
      console.log();
      console.log(wordModified);
    } else if (
      !word.includes(userGuess) &&
      userGuess == userGuess.toLowerCase() &&
      userGuess.match(/[a-z]/i) &&
      userGuess.length == 1
    ) {
      console.log(
        `That letter doesn't appear in the word.  # ${
          attemptNumber - 1
        } attempts`
      );
      console.log();
      console.log(wordModified);
      attemptNumber--;
    }
    userGuessArray.push(userGuess);

    //add user guess to string
    let result;
    if (word.includes(userGuess) && !wordModified.includes(userGuess)) {
      for (j = 0; j < word.length; j++) {
        if (word.charAt(j) == userGuess) {
          result = wordModified.replaceAt(j, userGuess);
          wordModified = result;
          console.log();
          if (wordModified == word) {
            console.log(`${word}`);
            console.log("You guessed the word " + wordModified + "!");
            console.log("You survived!");
            wins++;
            Menu();
            break;
          }
        }
      }
      console.log(result);
    }
  }
  console.log("You lost!");
  loses++;
  Menu();
}
