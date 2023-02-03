package tic.tac.toe.with.ai;

import java.util.Random;

class EasyCpuManeuver extends TicTacToe implements Maneuver {

    @Override
    public void makeManeuver(char symbol) {

        System.out.println("Making move level \"easy\"");

        Random random = new Random();

        int x, y, row, column;

        do {

            // Determining the row
            x = random.nextInt(rowTracker.length());
            row = rowTracker.charAt(x) - 48;

            //Determining the column
            y = random.nextInt(columnTracker.length());
            column = columnTracker.charAt(y) - 48;

        } while (mainGrid[row][column] != ' ');
        rowTracker.deleteCharAt(x);
        columnTracker.deleteCharAt(y);
        mainGrid[row][column] = symbol;
        depth++;
    }
}
