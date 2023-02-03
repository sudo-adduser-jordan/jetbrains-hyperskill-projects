package contacts.logic;


import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class UserInterface {
    private Map<String, Action> actions;

    public void startProgram(String[] fileName) {
        Logic logic = new Logic(fileName);
        Scanner scanner = new Scanner(System.in);
        actions = new HashMap<>(Map.of(
                "add", logic::addAction,
                "remove", logic::removeAction,
                "edit", logic::editAction,
                "count", logic::countAction,
                "search", logic::searchAction,
                "info", logic::infoAction,
                "serialize", logic::serialize,
                "list", logic::listAction
        ));


        while (true) {
            System.out.print("[menu] Enter action (add, list, search, count, exit): ");
            String input = scanner.nextLine();

            if ("exit".equals(input)) {
                break;
            }
            resolveAction(input);
        }
    }

    private void resolveAction(String input) {
        Action action = actions.get(input);
        if (action == null) {
            System.out.println("Unknown action!\n");
            return;
        }
        action.execute();
        System.out.println();
    }
}
