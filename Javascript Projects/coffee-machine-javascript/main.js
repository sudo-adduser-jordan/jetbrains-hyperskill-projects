const input = require("sync-input");

const coffeeMachine = {
    waterAmount: 400,
    milkAmount: 540,
    coffeeBeanAmount: 120,
    sugarAmount: 80,
    cups: 9,
    cash: 550,
}

const coffeeTypes = [
    {name: "espresso", water: 250, milk: 0, coffee: 16, cost: 4},
    {name: "latte", water: 350, milk: 75, coffee: 20, cost: 7},
    {name: "cappuccino", water: 200, milk: 100, coffee: 12, cost: 6},
]

const sugarCostPerServing = 0.5;
const gramsOfSugarPerServing = 4;

function checkSupplies(coffeeType, sugarAmount) {
    if (coffeeMachine.cups - 1 < 0) {
        console.log("Sorry, not enough cups!");
        return false;
    } else if (coffeeMachine.waterAmount - coffeeTypes[coffeeType].water < 0) {
        console.log("Sorry, not enough water!");
        return false;
    } else if (coffeeMachine.milkAmount - coffeeTypes[coffeeType].milk < 0) {
        console.log("Sorry, not enough milk!");
        return false;
    } else if (coffeeMachine.sugarAmount - sugarAmount < 0) {
        console.log("Sorry, not enough sugar!");
        return false;
    } else if (coffeeMachine.coffeeBeanAmount - coffeeTypes[coffeeType].coffee < 0) {
        console.log("Sorry, not enough coffee!");
        return false;
    } else {
        return true;
    }
}

function brewCoffee(coffeeType, servingsOfSugar) {
    const sugarAmount = servingsOfSugar * gramsOfSugarPerServing;
    if (checkSupplies(coffeeType, sugarAmount)) {
        console.log("I have enough resources, making you a coffee!");
        coffeeMachine.waterAmount -= coffeeTypes[coffeeType].water;
        coffeeMachine.milkAmount -= coffeeTypes[coffeeType].milk;
        coffeeMachine.sugarAmount -= sugarAmount;
        coffeeMachine.coffeeBeanAmount -= coffeeTypes[coffeeType].coffee;
        coffeeMachine.cups -= 1;
        coffeeMachine.cash += coffeeTypes[coffeeType].cost;
        coffeeMachine.cash += servingsOfSugar * sugarCostPerServing;
    }
    console.log();
}


let exit = false;
do {
    console.log("Write action (buy, fill, take, remaining, exit):");
    const action = input();
    console.log();
    switch (action) {
        case "buy":
            buyCoffee();
            break;
        case "fill":
            fillMachine();
            break;
        case "take":
            takeCash();
            break;
        case "remaining":
            coffeeMachineStatus();
            break;
        case "exit":
            exit = true;
            break;
        default:
            console.log(`Invalid choice: ${action}`);
            break;
    }
} while (!exit);

function buyCoffee() {
    console.log("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:");
    const coffeeType = input();
    if (coffeeType === "back") {
        console.log();
        return;
    }
    console.log("Would you like to add sugar (y/n)?");
    const response = input();
    let servingsOfSugar = 0;
    if (response.toLowerCase() === "y") {
        console.log(`How many servings of sugar? (${gramsOfSugarPerServing}g per servings)`)
        servingsOfSugar = Number(input())
    }
    brewCoffee(Number(coffeeType) - 1);
    console.log();
}

function fillMachine() {
    console.log("Write how many ml of water you want to add:");
    const water = Number(input());
    console.log("Write how many ml of milk you want to add:");
    const milk = Number(input());
    console.log("Write how many grams of coffee beans you want to add:");
    const coffee = Number(input());
    console.log("Write how many grams of sugar you want to add:");
    const sugar = Number(input());
    console.log("Write how many disposable coffee cups you want to add:");
    const cups = Number(input());

    coffeeMachine.waterAmount += water;
    coffeeMachine.milkAmount += milk;
    coffeeMachine.coffeeBeanAmount += coffee;
    coffeeMachine.sugarAmount += sugar;
    coffeeMachine.cups += cups;
    console.log();
}

function takeCash() {
    console.log(`I gave you $${coffeeMachine.cash}`);
    coffeeMachine.cash = 0;
    console.log();
}

function coffeeMachineStatus() {
    console.log("The coffee machine has:");
    console.log(`${coffeeMachine.waterAmount} ml of water`);
    console.log(`${coffeeMachine.milkAmount} ml of milk`);
    console.log(`${coffeeMachine.coffeeBeanAmount} g of coffee beans`);
    console.log(`${coffeeMachine.sugarAmount} g of sugar`);
    console.log(`${coffeeMachine.cups} disposable cups`);
    console.log(`$${coffeeMachine.cash} of money`);
    console.log();
}
