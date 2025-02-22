import java.util.Scanner;

public class MUDController {
    private Player player;
    private boolean running;

    public MUDController(Player player) {
        this.player = player;
        this.running = true;
    }

    public void runGameLoop() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("MUD ойыны басталды! 'help' командасын енгізіп, мүмкіндіктерді көріңіз.");

        while (running) {
            System.out.print("> ");
            String input = scanner.nextLine().trim().toLowerCase();
            handleInput(input);
        }

        System.out.println("Ойын аяқталды!");
        scanner.close();
    }

    private void handleInput(String input) {
        String[] parts = input.split(" ", 2);
        String command = parts[0];
        String argument = parts.length > 1 ? parts[1] : "";

        switch (command) {
            case "look":
                lookAround();
                break;
            case "move":
                move(argument);
                break;
            case "pick":
                pickUp(argument);
                break;
            case "inventory":
                checkInventory();
                break;
            case "help":
                showHelp();
                break;
            case "quit":
            case "exit":
                running = false;
                break;
            default:
                System.out.println("Белгісіз команда! 'help' командасын қолданып, мүмкіндіктерді қараңыз.");
        }
    }

    private void lookAround() {
        Room currentRoom = player.getCurrentRoom();
        System.out.println("Сіз: " + currentRoom.getName());
        System.out.println(currentRoom.getDescription());

        if (!currentRoom.getItems().isEmpty()) {
            System.out.println("Заттар: " + currentRoom.getItems());
        }

        if (!currentRoom.getNPC().isEmpty()) {
            System.out.println("NPC-лер: " + currentRoom.getNPC());
        }
    }

    private void move(String direction) {
        Room nextRoom = player.getCurrentRoom().getConnectedRoom(direction);
        if (nextRoom != null) {
            player.setCurrentRoom(nextRoom);
            System.out.println("Сіз " + direction + " бағытқа жылжыдыңыз.");
            lookAround();
        } else {
            System.out.println("Бұл бағытта жол жоқ!");
        }
    }

    private void pickUp(String itemName) {
        Room currentRoom = player.getCurrentRoom();
        Item item = currentRoom.getItem(itemName);

        if (item != null) {
            player.addItemToInventory(item);
            currentRoom.removeItem(item);
            System.out.println("Сіз " + itemName + " алдыңыз.");
        } else {
            System.out.println("Бұл бөлмеде " + itemName + " жоқ!");
        }
    }

    private void checkInventory() {
        if (player.getInventory().isEmpty()) {
            System.out.println("Сізде ештеңе жоқ.");
        } else {
            System.out.println("Сізде бар: " + player.getInventory());
        }
    }

    private void showHelp() {
        System.out.println("Қолжетімді командалар:");
        System.out.println("look - Бөлмені сипаттау");
        System.out.println("move <forward|back|left|right> - Көрсетілген бағытқа қозғалу");
        System.out.println("pick <itemName> - Затты алу");
        System.out.println("inventory - Инвентарьды көру");
        System.out.println("help - Командалар тізімін шығару");
        System.out.println("quit/exit - Ойынды аяқтау");
    }
}