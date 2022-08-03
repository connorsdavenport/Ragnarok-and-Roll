/*
Project 3 - Ragnarok'in roll
By: Connor, Alex, and Leslie
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class p3_t1
{
    public static void main(String[] args) throws FileNotFoundException
    {
        //decks of cards for reference
        ArrayList<Map> heroRefList = new ArrayList<>();
        ArrayList<Map> actionRefList = new ArrayList<>();
        ArrayList<Map> monsterRefList = new ArrayList<>();

        //init cards
        Map coin1_card = new HashMap();
        coin1_card.put("name","Coin1");
        coin1_card.put("cost", "0");
        coin1_card.put("coin","1");

        Map attack1_card = new HashMap();
        attack1_card.put("name","Attack1");
        attack1_card.put("cost", "0");
        attack1_card.put("attack", "1");

        //=== File Scanner =============================================================================================
        //create File scanner
        Scanner fileScanner = new Scanner( new File("C:\\Users\\conno\\IdeaProjects\\proj3Game\\src\\data.txt")); //<--file to read from

        //Read through the entire file and place data in respected arrayLists
        String currentCardType = "";
        String checkCardType = "";
        while (fileScanner.hasNext()) //loop until there is nothing left in the file to read
        {
            //First check the card type title
            checkCardType = fileScanner.next();
            if (checkCardType.equals("Heroes:"))
            {
                currentCardType = "Heroes";
            }
            //action cards
            else if (checkCardType.equals("ActionCards:"))
            {
                currentCardType = "ActionCards";
            }
            //monsters
            else if (checkCardType.equals("Monsters:"))
            {
                currentCardType = "Monsters";
            }
            else //not any of the card types which means it is the first input for a card
            {
                //heroes------------------------------------------------------------------------------------------------
                if (currentCardType.equals("Heroes"))
                {
                    //create a card to add data into
                    Map heroCard = new HashMap();

                    //name
                    String name = checkCardType;
                    heroCard.put("name",name);

                    //virtue
                    String virtue = fileScanner.next();
                    heroCard.put("virtue",virtue);

                    //loop attributes until ";"
                    //variables that check if the hero has a certain attribute
                    int attackChecker = 0;
                    int coinChecker = 0;
                    int actionChecker = 0;
                    int cardChecker = 0;

                    String nextThing = fileScanner.next();
                    while (!nextThing.equals(";"))
                    {
                        //attribute amount
                        int attributeAmount = Integer.parseInt(nextThing);

                        //attribute modifier
                        String attributeModifier = fileScanner.next();

                        heroCard.put(attributeModifier,attributeAmount);

                        //check if modifier has been scanned yet so we can make it 0 if not
                        switch(attributeModifier)
                        {
                            case "attack":
                                attackChecker++;
                                break;
                            case "coin":
                                coinChecker++;
                                break;
                            case "action":
                                actionChecker++;
                                break;
                            case "cards":
                                cardChecker++;
                                break;
                        }

                        //scan the next thing
                        nextThing = fileScanner.next();
                    }
                    //add the missing modifiers as 0 to the card
                    if (attackChecker == 0)
                    {
                        heroCard.put("attack",0);
                    }
                    if (coinChecker == 0)
                    {
                        heroCard.put("coin",0);
                    }
                    if (actionChecker == 0)
                    {
                        heroCard.put("action",0);
                    }
                    if (cardChecker == 0)
                    {
                        heroCard.put("cards",0);
                    }

                    //add extra placeholders that will store the modifier values from action cards
                    heroCard.put("attackMod",0);
                    heroCard.put("coinMod",0);
                    heroCard.put("actionMod",0);
                    heroCard.put("cardsMod",0);

                    //add the hero card to the hero reference list
                    heroRefList.add(heroCard);
                }

                //action cards------------------------------------------------------------------------------------------
                else if (currentCardType.equals("ActionCards"))
                {
                    //create a card to add data into
                    Map actionCard = new HashMap();

                    //name
                    String name = checkCardType;
                    actionCard.put("name",name);

                    //cost
                    int cost = fileScanner.nextInt();
                    actionCard.put("cost",cost);

                    //loop until ";"
                    String nextThing = fileScanner.next();
                    while (!nextThing.equals(";"))
                    {
                        //check if it is a virtue
                        if (nextThing.equals("Courage") || nextThing.equals("Honor")|| nextThing.equals("Industriousness"))
                        {
                            actionCard.put("virtue",nextThing);
                            //everything after this will be a virtue modifier
                            //amount
                            int attributeAmount = fileScanner.nextInt();

                            //modifier
                            String attributeModifier = fileScanner.next();
                            attributeModifier = actionCard.get("virtue") + attributeModifier;

                            actionCard.put(attributeModifier,attributeAmount);
                        }
                        else //if not a virtue
                        {
                            //amount
                            int attributeAmount = Integer.parseInt(nextThing);

                            //modifier
                            String attributeModifier = fileScanner.next();

                            actionCard.put(attributeModifier,attributeAmount);
                        }

                        //scan the next thing
                        nextThing = fileScanner.next();
                    }

                    //add the action card to the action reference list
                    actionRefList.add(actionCard);
                }

                //monsters----------------------------------------------------------------------------------------------
                else if (currentCardType.equals("Monsters"))
                {
                    //create a card to add data into
                    Map monsterCard = new HashMap();

                    //name
                    String name = checkCardType;
                    monsterCard.put("name",name);

                    //fight / attack points
                    int fight = fileScanner.nextInt();
                    monsterCard.put("fight",fight);

                    //glory
                    int glory = fileScanner.nextInt();
                    monsterCard.put("glory",glory);

                    //loop until ";"
                    String triggerName = fileScanner.next();
                    while (!triggerName.equals(";"))
                    {
                        //scan until next trigger
                        String theNext = fileScanner.next();
                        while (!theNext.equals("fight") && !theNext.equals("conqueror") && !theNext.equals("ambush") && !theNext.equals(";"))
                        {
                            //attribute amount
                            int attributeAmount = Integer.parseInt(theNext);

                            //attribute modifier
                            String attributeModifier = fileScanner.next();
                            attributeModifier = triggerName + attributeModifier;

                            monsterCard.put(attributeModifier,attributeAmount);
                            theNext = fileScanner.next();
                        }

                        triggerName = theNext;
                    }

                    //add the monster card to the monster reference list
                    monsterRefList.add(monsterCard);
                }
            }
        }
        //arraylists for board
        //hero board
        ArrayList<Map> hero_board = new ArrayList<>();
        //action deck
        ArrayList<Map> action_deck = new ArrayList<>();
        //player hand
        ArrayList<Map> player_hand = new ArrayList<>();
        //player discard deck
        ArrayList<Map> player_discard_deck = new ArrayList<>();
        //monster deck
        ArrayList<Map> monster_deck = new ArrayList<>();
        //monster board
        ArrayList<Map> monster_board = new ArrayList<>();
        //monsters defeated
        ArrayList<Map> monster_defeated = new ArrayList<>();
        //monster discard deck
        ArrayList<Map> monster_discard_deck = new ArrayList<>();
        //shop
        ArrayList<Map> shop = new ArrayList<>();

        //fill the monster board with blanks
        for (int i = 0; i < 6; i++)
        {
            Map blankCard = new HashMap();
            blankCard.put("null",0);
            monster_board.add(blankCard);
        }

        //=== Create display of cards for game setup ===================================================================
        //create scanner for user input
        Scanner scan = new Scanner(System.in);
        //user input
        String user_input[];
        //print out all action cards and have the user select 6 of them
        System.out.println("Select 6 different action cards by index, enter choices on a single line");
        CardPrinter(actionRefList, "action");
        user_input = scan.nextLine().split(" ");//scan in user input
        boolean dontcontinue = true; //

        while(dontcontinue) {
            if (user_input.length < 6) { //if the user did not input 6 action cards or put in a number that wasnt 1-6 put back error
                System.out.print("You chose less than 6 cards, please choose 6 different action cards, enter your choices on a single line below \n");
                user_input = scan.nextLine().split(" ");
            }
            else if(user_input.length>6){
                System.out.print("You chose more than 6 cards, please choose 6 different action cards, enter your choices on a single line below \n");
                user_input = scan.nextLine().split(" ");
            }else if(Integer.parseInt(user_input[0]) == Integer.parseInt(user_input[1]) || Integer.parseInt(user_input[0]) == Integer.parseInt(user_input[2]) || Integer.parseInt(user_input[0]) == Integer.parseInt(user_input[3]) || Integer.parseInt(user_input[0]) == Integer.parseInt(user_input[4]) || Integer.parseInt(user_input[0]) == Integer.parseInt(user_input[5])) {
                System.out.println("Please choose 6 DIFFERENT action cards, enter your choices on a single line below \n");
                user_input = scan.nextLine().split(" ");
            }else if(Integer.parseInt(user_input[1]) == Integer.parseInt(user_input[2]) || Integer.parseInt(user_input[1]) == Integer.parseInt(user_input[3]) || Integer.parseInt(user_input[1]) == Integer.parseInt(user_input[4]) || Integer.parseInt(user_input[1]) == Integer.parseInt(user_input[5]))
            {
                System.out.println("Please choose 6 DIFFERENT action cards, enter your choices on a single line below \n");
                user_input = scan.nextLine().split(" ");
            }else if(Integer.parseInt(user_input[2]) == Integer.parseInt(user_input[3] )|| Integer.parseInt(user_input[2]) == Integer.parseInt(user_input[4]) || Integer.parseInt(user_input[2]) == Integer.parseInt(user_input[5]))
            {
                System.out.println("Please choose 6 DIFFERENT action cards, enter your choices on a single line below \n");
                user_input = scan.nextLine().split(" ");
            }else if(Integer.parseInt(user_input[3]) == Integer.parseInt(user_input[4]) || Integer.parseInt(user_input[3]) == Integer.parseInt(user_input[5]))
            {
                System.out.println("Please choose 6 DIFFERENT action cards, enter your choices on a single line below \n");
                user_input = scan.nextLine().split(" ");
            }else if(Integer.parseInt(user_input[4]) == Integer.parseInt(user_input[5])){
                System.out.println("Please choose 6 DIFFERENT action cards, enter your choices on a single line below \n");
                user_input = scan.nextLine().split(" ");
            }
            else
            {
                dontcontinue = false;
            }
        }

        for ( int i = 0 ; i < 6 ; i++){
            int chosen_card = Integer.parseInt(user_input[i])-1;
            shop.add(actionRefList.get(chosen_card));
        }
        //create the 6 action decks with 8 cards
        //create 2- coin deck with 8 cards
        //add starter deck
        for(int i = 0 ; i <14; i++){
            if(i<10)
                action_deck.add(coin1_card);
            else
                action_deck.add(attack1_card);
        }

        //shuffle action deck
        Collections.shuffle(action_deck);
        //print out all monster cards and have the user select 5 of them
        System.out.println("select 5 monster cards by index, print all on one line");
        CardPrinter(monsterRefList, "monster");
        user_input = scan.nextLine().split(" ");

        dontcontinue = true;//set variable back to true
        //scan in user input
        while(dontcontinue) {
            if (user_input.length < 5) { //if the user did not input 4 hero cards or put in a number that wasnt 1-6 put back error
                System.out.print("You chose less than 5 monster cards, please choose 5 different monster cards, enter your choices below on a single line \n");
                user_input = scan.nextLine().split(" ");
            }else if(user_input.length>5){
                System.out.print("You chose more than 5 cards, please choose 5 different monster cards, enter your choices on a single line below \n");
                user_input = scan.nextLine().split(" ");
            }else if(Integer.parseInt(user_input[0]) == Integer.parseInt(user_input[1]) || Integer.parseInt(user_input[0]) == Integer.parseInt(user_input[2]) || Integer.parseInt(user_input[0]) == Integer.parseInt(user_input[3]) || Integer.parseInt(user_input[0]) == Integer.parseInt(user_input[4])) {
                System.out.println("Please choose 5 DIFFERENT monster cards, enter your choices on a single line below \n");
                user_input = scan.nextLine().split(" ");
            }else if(Integer.parseInt(user_input[1]) == Integer.parseInt(user_input[2]) || Integer.parseInt(user_input[1]) == Integer.parseInt(user_input[3]) || Integer.parseInt(user_input[1]) == Integer.parseInt(user_input[4])) {
                System.out.println("Please choose 5 DIFFERENT monster cards, enter your choices on a single line below \n");
                user_input = scan.nextLine().split(" ");
            }else if(Integer.parseInt(user_input[2]) == Integer.parseInt(user_input[3]) || Integer.parseInt(user_input[2]) == Integer.parseInt(user_input[4])) {
                System.out.println("Please choose 5 DIFFERENT monster cards, enter your choices on a single line below \n");
                user_input = scan.nextLine().split(" ");
            }else if(Integer.parseInt(user_input[3]) == Integer.parseInt(user_input[4])) {
                System.out.println("Please choose 5 DIFFERENT monster cards, enter your choices on a single line below \n");
                user_input = scan.nextLine().split(" ");
            }else{
                dontcontinue = false;
            }
        }

        for ( int i = 0 ; i < 5 ; i++){
            int chosen_card = Integer.parseInt(user_input[i])-1;
            //add 8 copies
            for(int j = 0 ; j< 1 ;j++){ //change back to j < 8 /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                monster_deck.add(monsterRefList.get(chosen_card));
            }
        }
        //shuffle monster deck
        Collections.shuffle(monster_deck);

        //print out all hero cards and have the user select 4 of them
        System.out.println("select 4 hero cards by index, enter all choice on a single line");

        CardPrinter(heroRefList, "hero");
        user_input = scan.nextLine().split(" ");
        dontcontinue = true; //set back to true
        //scan in user input
        while(dontcontinue) {
            if (user_input.length < 4) { //if the user did not input 4 hero cards or put in a number that wasnt 1-6 put back error
                System.out.print("You chose less than 4 hero cards; please choose 4 hero cards, enter your choices on a single line below \n");
                user_input = scan.nextLine().split(" ");
            }else if(user_input.length>4){
                System.out.print("You chose more than 4 cards, please choose 4 different hero cards, enter your choices on a single line below \n");
                user_input = scan.nextLine().split(" ");
            }else if(Integer.parseInt(user_input[0]) == Integer.parseInt(user_input[1]) || Integer.parseInt(user_input[0]) == Integer.parseInt(user_input[2]) || Integer.parseInt(user_input[0]) == Integer.parseInt(user_input[3])) {
                System.out.println("Please choose 4 DIFFERENT hero cards, enter your choices on a single line below \n");
                user_input = scan.nextLine().split(" ");
            }else if(Integer.parseInt(user_input[1]) == Integer.parseInt(user_input[2]) || Integer.parseInt(user_input[1]) == Integer.parseInt(user_input[3])) {
                System.out.println("Please choose 4 DIFFERENT hero cards, enter your choices on a single line below \n");
                user_input = scan.nextLine().split(" ");
            }else if(Integer.parseInt(user_input[2]) == Integer.parseInt(user_input[3])) {
                System.out.println("Please choose 4 DIFFERENT hero cards, enter your choices on a single line below \n");
                user_input = scan.nextLine().split(" ");
            }else{
                dontcontinue = false;
            }
        }
        for ( int i = 0 ; i < 4 ; i++){
            int chosen_card = Integer.parseInt(user_input[i])-1;
            hero_board.add(heroRefList.get(chosen_card));

        }

        //fill shop
        for(int i = 0 ; i < shop.size() ; i++)
        {
            shop.get(i).put("count", 8);
        }

        //=== Main game loop ===========================================================================================
        //Starting Variables
        //Starting Variables
        int draw = 7; //the player will always draw 7 a turn, unless they have extra draw
        boolean win = false;  // win condition met variable
        boolean empty_deck = false; // determines whether the deck will be reshuffled
        int monster_glory = 0;
        int hero_glory = 0;
        Map card_to_add = null; // variable to get cards (maps)



        //draw 7 cards, unless hero has + cards
        //get hero + cards
        int total_draw = HeroGetter(hero_board, "cards") ;
        total_draw += 7;
        for(int i = 0 ; i < total_draw; i++){
            //get card first from action deck and remove it from the deck
            card_to_add =  action_deck.get(0);
            action_deck.remove(0);
            //add card to player hand
            player_hand.add(card_to_add);
        }
        // game loop
        do {
            //set remaining coins and actions to the regular amounts
            int remainingActions = HeroGetter(hero_board,"action");
            int remainingCoins = HeroGetter(hero_board,"coin");

            //--- Monster Phase ----------------------------------------------------------------------------------------
            //flip monster cards over
            if (monster_deck.size() != 0) {
                flip_monster(monster_board, monster_discard_deck, monster_deck, player_hand, player_discard_deck);
            }

            //deal with trigger
            //flip monster cards over
            //deal with trigger
            Map currentCard = monster_board.get(0);
            if (currentCard.containsKey("ambushdiscard") || currentCard.containsKey("ambushremovecoin") || currentCard.containsKey("ambushremoveattack") || currentCard.containsKey("ambushmove") || currentCard.containsKey("ambushflip"))
            {

                if (currentCard.containsKey("ambushdiscard"))
                {
                    int discard_count = (int) currentCard.get("ambushdiscard");
                    for ( int i = 0 ; i<discard_count ; i++){
                        card_to_add =  player_hand.get(i);
                        player_hand.remove(0); //remove card from hand
                        player_discard_deck.add(card_to_add); //add card to discard deck
                    }
                    //print board
                }
                if (currentCard.containsKey("ambushmove"))
                {
                    int move_count = (int) currentCard.get("ambushmove");
                    ambush_move_resolver(monster_deck,monster_board,monster_discard_deck,move_count,player_hand,player_discard_deck);
                }
                if (currentCard.containsKey("ambushflip"))
                {
                    for ( int i = 0 ; i< (int)currentCard.get("ambushflip");i++){
                        flip_monster(monster_board,monster_discard_deck,monster_deck,player_hand,player_discard_deck);
                    }
                }
            }
            //print board
            BoardPrinter(monster_deck,monster_board,monster_discard_deck,shop,hero_board,player_hand,remainingActions,remainingCoins);

            //--- Action phase -----------------------------------------------------------------------------------------
            //loop until player is done playing action cards or out of actions
            Boolean actionDone = false;
            while (!actionDone)
            {
                System.out.println("Select Action cards from hand to put on Heroes; if you are done playing actions type 'done' - Formatting: Action Card number, then Hero card number | (ex. 2 1) (ex. done)");

                int bad = 0;
                String firstThing = "";
                firstThing = scan.next();
                int secondThing = 0;
                if (firstThing.equals("done"))
                {
                    actionDone = true;
                }
                else
                {
                    secondThing = scan.nextInt();
                    //check if the inputs are in bounds
                    if (Integer.parseInt(firstThing) > player_hand.size() || Integer.parseInt(firstThing) < 1)
                    {
                        System.out.println("The Action number that you selected is out of bounds. Please enter a number between 1-" + player_hand.size());
                    }
                    else if (secondThing > hero_board.size() || secondThing < 1)
                    {
                        System.out.println("The Hero number that you selected is out of bounds. Please enter a number between 1-" + hero_board.size());
                    }
                    else
                    {
                        System.out.println();
                        System.out.println(firstThing);
                        System.out.println();

                        //add the action values to the hero attribute modifiers
                        Map currentAction = player_hand.get(Integer.parseInt(firstThing) - 1);
                        Map currentHero = hero_board.get(secondThing-1);
                        //attack
                        if(currentAction.containsKey("attack"))
                        {
                            int currentAtt = (int) currentHero.get("attackMod");
                            String aValue = currentAction.get("attack").toString();
                            currentAtt = Integer.parseInt(aValue) + currentAtt;
                            currentHero.replace("attackMod", currentAtt);
                        }
                        //coin
                        if(currentAction.containsKey("coin"))
                        {
                            int currentAtt = (int) currentHero.get("coinMod");
                            currentAtt = Integer.parseInt((String)currentAction.get("coin")) + currentAtt;
                            currentHero.replace("coinMod", currentAtt);
                        }
                        //cards
                        if(currentAction.containsKey("cards"))
                        {
                            int currentAtt = (int) currentHero.get("cardsMod");
                            currentAtt = Integer.parseInt((String)currentAction.get("cards")) + currentAtt;
                            currentHero.replace("cardsMod", currentAtt);
                        }
                        //action
                        if(currentAction.containsKey("action"))
                        {
                            int currentAtt = (int) currentHero.get("actionMod");
                            currentAtt = Integer.parseInt((String)currentAction.get("action")) + currentAtt;
                            currentHero.replace("actionMod", currentAtt);
                        }

                        //virtue modifiers
                        if (currentAction.containsKey("virtue"))
                        {
                            String virtueName = (String) currentAction.get("virtue");
                            //check if hero has same virtue
                            if (currentHero.get("virtue").equals(virtueName))
                            {
                                //check if there are special modifiers for virtues
                                String attackName = virtueName + "attack";
                                String coinName = virtueName + "coin";
                                String cardName = virtueName + "cards";
                                String actionName = virtueName + "action";
                                //special attack
                                if (currentAction.containsKey(attackName))
                                {
                                    int currentModAtt = (int) currentHero.get("attackMod");
                                    String aValue = currentAction.get(attackName).toString();
                                    currentModAtt = Integer.parseInt(aValue) + currentModAtt;
                                    currentHero.replace("actionMod", currentModAtt);
                                }
                                //special coin
                                if (currentAction.containsKey(coinName))
                                {
                                    int currentModAtt = (int) currentHero.get("coinMod");
                                    currentModAtt = Integer.parseInt((String)currentAction.get(coinName)) + currentModAtt;
                                    currentHero.replace("coinMod", currentModAtt);
                                }
                                //special cards
                                if (currentAction.containsKey(cardName))
                                {
                                    int currentModAtt = (int) currentHero.get("cardsMod");
                                    currentModAtt = Integer.parseInt((String)currentAction.get(cardName)) + currentModAtt;
                                    currentHero.replace("cardsMod", currentModAtt);
                                }
                                //special action
                                if (currentAction.containsKey(actionName))
                                {
                                    int currentModAtt = (int) currentHero.get("actionMod");
                                    currentModAtt = Integer.parseInt((String)currentAction.get(actionName)) + currentModAtt;
                                    currentHero.replace("actionMod", currentModAtt);
                                }
                            }
                            else
                            {
                                //nothing happens
                            }
                        }

                        //remove the action card from hand and add it to the discard pile
                        player_discard_deck.add(currentAction);
                        player_hand.remove(Integer.parseInt(firstThing)-1);

                        //-1 to remaining actions
                        remainingActions--;
                        if (remainingActions == 0)
                        {
                            actionDone = true;
                        }
                        else
                        {
                            remainingCoins = HeroGetter(hero_board,"coin");
                            //reprint board
                            BoardPrinter(monster_deck,monster_board,monster_discard_deck,shop,hero_board,player_hand,remainingActions,remainingCoins);
                        }
                    }
                }
            }

            //--- Attack Phase -----------------------------------------------------------------------------------------
            //loop until valid
            BoardPrinter(monster_deck,monster_board,monster_discard_deck,shop,hero_board,player_hand,remainingActions,remainingCoins);
            Boolean attackResponse = false;
            while (!attackResponse)
            {
                int totalAttack = HeroGetter(hero_board,"attack");
                System.out.println("Total Attack: "+ totalAttack);
                System.out.println("Would you like to attack a monster? (enter 'yes' or 'no')");
                String answer = scan.next();
                //will perform the full attack phase
                if (answer.equals("yes"))
                {
                    //loop until there are no more monsters to fight or player is done fighting
                    Boolean stillAttacking = false;
                    do
                    {
                        //check if the player can kill anything in the first place
                        int pass = 0;
                        for (int i = 0; i < monster_board.size(); i++)
                        {
                            if (monster_board.get(i).containsKey("null"))
                            {
                                pass++;
                            }
                            else if ((int) monster_board.get(i).get("fight") > totalAttack)
                            {
                                pass++;
                            }
                        }
                        if (pass == monster_board.size())
                        {
                            break;
                        }

                        //print the monsters
                        CardPrinter(monster_board,"monster");

                        attackResponse = true;
                        //make the attack
                        System.out.println("Enter the number of a monster that is currently out on the board that you would like to attack | Remaining Attack: " + totalAttack);
                        String MonsterIndex = scan.next();
                        //check if valid
                        if (!monster_board.get(Integer.parseInt(MonsterIndex) - 1).containsKey("null") && (int)monster_board.get(Integer.parseInt(MonsterIndex) - 1).get("fight") <= totalAttack)
                        {
                            //trigger the monster's fight effects if any $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
                            Map aMonster = monster_board.get(Integer.parseInt(MonsterIndex) - 1);
                            if (aMonster.containsKey("fightdiscard") || aMonster.containsKey("fightremovecoin") || aMonster.containsKey("fightremoveattack") || aMonster.containsKey("fightflip"))
                            {
                                //discard
                                if (aMonster.containsKey("fightdiscard"))
                                {
                                    //find value
                                    int aValue = (int) aMonster.get("fightdiscard");
                                    for (int j = 0; j < aValue; j++)
                                    {
                                        if (player_hand.size() != 0)
                                        {
                                            Map currentHandCard = player_hand.get(0);
                                            player_discard_deck.add(currentHandCard);
                                            player_hand.remove(0);
                                        }
                                    }
                                }
                                //remove coin
                                if (aMonster.containsKey("fightremovecoin"))
                                {
                                    //find value
                                    int aValue = (int) aMonster.get("fightremovecoin");
                                    for (int j = 0; j < aValue; j++)
                                    {
                                        for (int i = 0; i < hero_board.size(); i++)
                                        {
                                            Map currentHero = hero_board.get(i);
                                            int currentCoinMod = (int) currentHero.get("coinMod");
                                            currentCoinMod --;
                                            currentHero.replace("coinMod", currentCoinMod);
                                        }
                                    }
                                    remainingCoins = HeroGetter(hero_board,"coin");
                                    if (remainingCoins < 0)
                                    {
                                        remainingCoins = 0;
                                    }
                                }
                                //remove attack
                                if (aMonster.containsKey("fightremoveattack"))
                                {
                                    //find value
                                    int aValue = (int) aMonster.get("fightremoveattack");
                                    totalAttack = totalAttack - (4 * aValue);
                                    if (totalAttack < 0)
                                    {
                                        totalAttack = 0;
                                    }
                                }
                                //flip
                                if (aMonster.containsKey("fightflip"))
                                {
                                    //find value
                                    int aValue = (int) aMonster.get("fightflip");
                                    for (int j = 0; j < aValue; j++)
                                    {
                                        if (monster_deck.size() != 0)
                                        {
                                            flip_monster(monster_board,monster_discard_deck,monster_deck,player_hand,player_discard_deck);
                                        }
                                    }
                                }
                            }

                            //add monster to defeated monster list
                            monster_defeated.add(monster_board.get(Integer.parseInt(MonsterIndex) - 1));
                            //decrease total attack by monster's fight
                            totalAttack =totalAttack - (int)monster_board.get(Integer.parseInt(MonsterIndex) - 1).get("fight");
                            //remove the monster from the monster board
                            Map blankCard = new HashMap();
                            blankCard.put("null",0);
                            monster_board.set(Integer.parseInt(MonsterIndex)-1, blankCard);
                        }
                        else if (totalAttack == 0)
                        {
                            break;
                        }
                        //not a valid attack and monsters still in list
                        else
                        {
                            System.out.println("Not a valid attack...");
                        }

                    }while(!stillAttacking);
                }
                //will exit the loop with no attacks
                else if (answer.equals("no"))
                {
                    attackResponse = true;
                }
                //not a good response will make the loop run back
                else
                {
                    System.out.print("Invalid response: ");
                }
            }

            //--- Buy Phase --------------------------------------------------------------------------------------------
            BoardPrinter(monster_deck,monster_board,monster_discard_deck,shop,hero_board,player_hand,remainingActions,remainingCoins);
            player_discard_deck = buyCards(scan, hero_board, player_discard_deck,shop);


            //--- cleanup phase ----------------------------------------------------------------------------------------
            //discard all remaining cards in hand
            while (player_hand.size() != 0)
            {
                currentCard = player_hand.get(0);
                player_discard_deck.add(currentCard);
                player_hand.remove(0);
            }
            //add 7+cards stat of cards to hand
            int extraDraws = HeroGetter(hero_board,"cards");
            for (int i = 0; i < (7+extraDraws); i++)
            {
                //get card first from action deck and remove it from the deck
                if (action_deck.size() != 0) // if the action deck still has cards...
                {
                    card_to_add =  action_deck.get(0);
                    action_deck.remove(0);
                    //add card to player hand
                    player_hand.add(card_to_add);
                }
                else // if the deck is out of cards, shuffle the discard and make it the new action deck, and then draw a card
                {
                    Collections.shuffle(player_discard_deck);
                    //put card from discard into action deck
                    while (player_discard_deck.size() != 0)
                    {
                        Map aCard = player_discard_deck.get(0);
                        action_deck.add(aCard);
                        player_discard_deck.remove(0);
                    }
                    card_to_add =  action_deck.get(0);
                    action_deck.remove(0);
                    //add card to player hand
                    player_hand.add(card_to_add);
                }
            }
            //end of turn... reset hero stats
            for (int i = 0; i < hero_board.size(); i++)
            {
                currentCard = hero_board.get(i);
                //reset attackMod
                currentCard.replace("attackMod",0);
                //reset coinMod
                currentCard.replace("coinMod",0);
                //reset cardsMod
                currentCard.replace("cardsMod",0);
                //reset actionMod
                currentCard.replace("actionMod",0);
            }

            //check if game is over
            if (monster_deck.size() == 0) //monster deck empty
            {
                //check if monster board is empty
                Boolean isEmpty = true;
                for (int i = 0; i < monster_board.size(); i++)
                {
                    if (monster_board.get(i).containsKey("null"))
                    {
                        //a blank
                    }
                    else
                    {
                        //not empty
                        isEmpty = false;
                    }
                }
                //if board is empty
                if (isEmpty)
                {
                    win = true;
                }
            }
        }while(!win);

        //=== Game ending ==============================================================================================
        //add up the player's total glory points
        int playersTotalGlory = 0;
        for (int i = 0; i < monster_defeated.size(); i++)
        {
            if (monster_defeated.size() != 0)
            {
                playersTotalGlory += (int) monster_defeated.get(i).get("glory");
            }
        }
        //add up the glory of the escaped monsters
        int monsterTotalGlory = 0;
        for (int i = 0; i < monster_defeated.size(); i++)
        {
            if (monster_discard_deck.size() != 0)
            {
                monsterTotalGlory += (int) monster_discard_deck.get(i).get("glory");
            }
        }
        //compare the two, if a tie count the size of the two lists and compare those, and then print out the winner
        if (playersTotalGlory > monsterTotalGlory)
        {
            //player wins
            System.out.println("Player wins!");
        }
        else if (playersTotalGlory < monsterTotalGlory)
        {
            //monsters win
            System.out.println("Monsters win!");
        }
        else
        {
            //a tie
            int monsterKills = monster_discard_deck.size();
            int playerkills = monster_defeated.size();
            if (playerkills > monsterKills)
            {
                //player wins
                System.out.println("Player wins!");
            }
            else if (playerkills < monsterKills)
            {
                //monsters win
                System.out.println("Monsters win!");
            }
            else
            {
                //a tie
                System.out.println("It's a tie!");
            }
        }

    }
    //check if monster reaches the end of the board
    public static void flip_monster(ArrayList monster_board, ArrayList monster_discard_deck , ArrayList monster_deck, ArrayList player_hand, ArrayList player_discard_deck){
        Map card_to_add =null;
        ArrayList<Map> escaped_monsters = new ArrayList<Map>();
        //check if there are less than 6 monsters in the list
        if(monster_board.size()<6)
        {
            card_to_add = (Map) monster_deck.get(0);
            monster_deck.remove(0);
            monster_board.add(0,card_to_add);
        }
        else
        {
            Map last_monster = (Map) monster_board.get(5); //get monster at position 6
            if (last_monster.containsKey("null"))
            {
                monster_board.remove(monster_board.size()-1);
                //need to add conquer checker
                card_to_add = (Map) monster_deck.get(0);
                monster_deck.remove(0);
                monster_board.add(0,card_to_add);
            }
            else
            {
                monster_discard_deck.add(last_monster); // add monster to
                monster_board.remove(monster_board.size()-1); //remove monster from the board
                escaped_monsters.add(last_monster);
                //put new monster on board
                card_to_add = (Map) monster_deck.get(0);
                //remove monster from deck
                //need to add conquer checker
                card_to_add = (Map) monster_deck.get(0);
                monster_deck.remove(0);
                monster_board.add(0,card_to_add);
                //check if escaped monsters have conqueror
                conqueror_resolver(monster_deck,monster_board,monster_discard_deck,escaped_monsters,player_hand,player_discard_deck);
            }
        }
    }
    public static void ambush_move_resolver( ArrayList monster_deck, ArrayList monster_board, ArrayList monster_discard_deck, int move_count,ArrayList<Map> player_hand,ArrayList<Map>player_discard_deck){
        //create blank card
        Map blank_card = new HashMap();
        blank_card.put("null",0);
        //create list of temp cards
        ArrayList<Map>temp = new ArrayList<Map>();
        //check if cards could possibly be kicked off
        boolean escape = false;
        if( ((monster_board.size()+move_count) - monster_board.size() )> 0 ){
            escape =true;
        }
        if(escape == true) {
            int cards_to_save = ((monster_board.size()+move_count) - monster_board.size() );
            //save last cards in list based on the size of the move count
            //start at end of the deck
            for (int i = cards_to_save; i > 0; i--) {
                temp.add((Map)(monster_board.get(monster_board.size()-1)));
                monster_board.remove(monster_board.size()-1);

            }
            //move monster n places
            for( int i = 0 ; i <move_count ; i++){
                monster_board.add(blank_card);
            }
        }
        else{
            for( int i = 0 ; i <move_count ; i++){
                monster_board.add(blank_card);
            }
        }
        //check if monster escaped
        for (int i = 0 ; i< temp.size(); i++) {
            //check if any of the monsters were blank cards
            if (temp.get(i).containsKey("null")) {
                //do nothing
            } else {
                //check conqueror of that
                if( temp.get(i).containsKey("conquerordiscard") || temp.get(i).containsKey("conquerorflip")){
                    conqueror_resolver(monster_deck,monster_board,monster_discard_deck,temp,player_hand,player_discard_deck);
                }
                //resolve conqueror
                //move to the monster discard deck
            }
        }
    }
    // conqueror resolver
    public static void conqueror_resolver(ArrayList<Map> monster_deck, ArrayList<Map> monster_board, ArrayList<Map> monster_discard_deck,ArrayList<Map> escaped_monsters,ArrayList<Map> player_hand,ArrayList<Map>player_discard_deck){
        //check what kind of conqueror each card has
        Map card_to_add = null;
        for( int i = 0 ; i < escaped_monsters.size() ; i++){
            if ( escaped_monsters.get(i).containsKey("conquerordiscard") || escaped_monsters.get(i).containsKey("conquerorflip")){
                //if trigger is discard
                if (escaped_monsters.get(i).containsKey("conquerordiscard"))
                {
                    int discard_count = (int) escaped_monsters.get(i).get("conquerordiscard");
                    for ( int j = 0 ; j<discard_count ; j++){
                        card_to_add =  player_hand.get(j);
                        player_hand.remove(0); //remove card from hand
                        player_discard_deck.add(card_to_add); //add card to discard deck
                    }
                }
                //if trigger is flip
                if (escaped_monsters.get(i).containsKey("conquerorflip")){
                    flip_monster(monster_board,monster_discard_deck,monster_deck,player_hand,player_discard_deck);
                }
            }
        }
    }

    //=== Getter =======================================================================================================
    public static int HeroGetter(ArrayList heroList, String thing)
    {
        String thingMod = thing + "Mod";
        int getCount = 0;
        for (int i = 0; i < heroList.size(); i++)
        {
            Map currentCard = (Map) heroList.get(i);
            getCount += (int) currentCard.get(thing);
            getCount += (int) currentCard.get(thingMod);
        }
        return getCount;
    }


    //=== buy cards ====================================================================================================
    public static ArrayList<Map> buyCards(Scanner scan, ArrayList heroList, ArrayList<Map> player_discard_deck,ArrayList shopList) // pass in the scanner we are using and the heroLoist for coin calculation
    {
        boolean repeat = true; // variable to use for while loop in case user enter incorrect input
        int index;
        int playerCoin;
        int cost;
        Map currentCard;
        //after an attack has been played this method will be called
        System.out.println("Would you like to buy any actions cards? Please enter yes or no");
        playerCoin = HeroGetter(heroList,"coin");// the amount of coins user has to buy things
        if (playerCoin < 0)
        {
            playerCoin = 0;
        }
        while(repeat){
            String answer = scan.nextLine();
            if(answer.equals("yes")){

                CardPrinter(shopList, "action"); //the list of action cards we can buy
                //print count of cards in shop
                for (int i = 0; i < shopList.size(); i++)
                {
                    System.out.print("        ");
                    Map currentThing = (Map) shopList.get(i);
                    System.out.print(currentThing.get("count") + "         ");
                }
                System.out.println();
                System.out.println("You have "+playerCoin+" coins to spend. Please enter index (1-6) of the action card you would like to buy below: ");

                index = scan.nextInt();
                if(index>0 && index<7){ // check the index entered by user
                    //System.out.println("Before the index call is where Im erroing out "+shopList.size());
                    currentCard= (Map) shopList.get(index-1);
                    //System.out.println("after the index call is where Im erroing out");
                    cost = (int)currentCard.get("cost");
                    //System.out.println("After the cost call");
                    //if player has enough money to buy a card, deduct card cost from their coins nad add card to the discard pile
                    if(cost<=playerCoin)
                    {
                        playerCoin = playerCoin - cost;
                        //lower the count of the card that is bought
                        int currentCount =(int)currentCard.get("count");
                        currentCard.replace("count",currentCount-1);
                        //System.out.println(player_discard_deck.size());
                        player_discard_deck.add(currentCard);
                        System.out.println("Buy successful. The chosen card has been added to your discard pile. \n You have "+playerCoin+" coins left,Would you like to buy another card?");
                        //should go through while loop again.
                        //repeat = true;
                        //System.out.println(player_discard_deck.size());
                    }else { //if the player does not have enough money, tell them they cannot buy that card and ask if they would like to try to buy another.
                        System.out.println("Buy failed. You do not have enough money to buy this card. \n Would you like to try to buy another?");
                        //should while loop again
                    }
                }else{
                    System.out.println("The index you entered is out of bounds, Would you like to try again? Enter yes or no.");
                }

            }else if(answer.equals("no")){
                repeat = false;
                //return false; // use did not buy cards, return false and exit this method.
            }else{
                System.out.println("Please enter a yes or no answer");
            }
        }
        return player_discard_deck;
    }


    //=== board printer ================================================================================================
    // Parameters are: Monster Deck, Monsters currently on hte board, Monsters that have escaped, list of cards in shop, list of heroes, list of cards in hand
    public static void BoardPrinter(ArrayList monsterList, ArrayList monsterBoard, ArrayList escapedList, ArrayList shopList, ArrayList heroList, ArrayList handList, int actions, int coins)
    {
        //print remaining cards in monster deck, monsters that are currently out,and the amount of monsters that escaped
        System.out.println("Monster Deck: " + monsterList.size() + " cards remaining");
        CardPrinter(monsterBoard, "monster");
        System.out.println("Escaped Monsters: " + escapedList.size());
        //print the shop
        System.out.println("Shop: ");
        CardPrinter(shopList, "action");
        //print count of cards in shop
        for (int i = 0; i < shopList.size(); i++)
        {
            System.out.print("        ");
            Map currentCard = (Map) shopList.get(i);
            System.out.print(currentCard.get("count") + "         ");
        }
        System.out.println();
        //print out the heroes
        System.out.println("Heroes: ");
        CardPrinter(heroList, "hero");
        //print out player hand
        System.out.println("Cards in Hand: ");
        CardPrinter(handList, "action");
        //print out the remaining actions and coins the player has
        System.out.println("Actions Remaining: " + actions + " | Coins Remaining: " + coins);
    }

    //=== card printer =================================================================================================
    public static void CardPrinter(ArrayList cardList, String listType)
    {
        final String ANSI_RESET = "\u001B[0m";
        final String ANSI_BLACK = "\u001B[30m";
        final String ANSI_RED = "\u001B[31m";
        final String ANSI_GREEN = "\u001B[32m";
        final String ANSI_YELLOW = "\u001B[33m";
        final String ANSI_BLUE = "\u001B[34m";
        final String ANSI_PURPLE = "\u001B[35m";
        final String ANSI_CYAN = "\u001B[36m";
        final String ANSI_WHITE = "\u001B[37m";

        final String BLACK_BOLD = "\033[1;30m";  // BLACK

        //check the listType to see how we will look at each card
        switch(listType)
        {
            case "hero": //deck of heroes-------------------------------------------------------------------------------
            {
                //top of card
                for (int i = 0; i < cardList.size(); i++)
                {
                    System.out.print("+~~~~~~~~~~~~~~~+ ");
                }
                System.out.println();

                //stats
                for (int i = 0; i < cardList.size(); i++)
                {
                    Map currentCard = (Map) cardList.get(i);
                    System.out.print("{ ");
                    System.out.print(ANSI_RED + ((int)currentCard.get("attack") + (int)currentCard.get("attackMod")) + ANSI_RESET + " | ");
                    System.out.print(ANSI_YELLOW + ((int)currentCard.get("coin") + (int)currentCard.get("coinMod")) + ANSI_RESET + " | ");
                    System.out.print(ANSI_CYAN + ((int)currentCard.get("cards") + (int)currentCard.get("cardsMod")) + ANSI_RESET + " | ");
                    System.out.print(ANSI_PURPLE + ((int)currentCard.get("action") + (int)currentCard.get("actionMod")) + ANSI_RESET);
                    System.out.print(" } ");
                }
                System.out.println();

                //divider line
                for (int i = 0; i < cardList.size(); i++)
                {
                    System.out.print("{---------------} ");
                }
                System.out.println();

                //name and virtue line
                for (int i = 0; i < cardList.size(); i++)
                {
                    //get the current card for easy reference
                    Map currentCard = (Map) cardList.get(i);
                    System.out.print("{");
                    String theName = (String)currentCard.get("name");
                    if (theName.length() > 8)
                    {
                        theName = theName.substring(0,8);
                    }
                    System.out.print(ANSI_GREEN + theName + ANSI_RESET + " | ");
                    String theVirtue = (String)currentCard.get("virtue");
                    int virtueLen = 12 - theName.length();
                    //if the virtue name is going to be too long
                    if (theVirtue.length() > virtueLen)
                    {
                        theVirtue = theVirtue.substring(0,virtueLen);
                    }
                    //if we need to fill more space
                    else
                    {
                        int iNum = (theVirtue.length() + theName.length());
                        if (iNum < 12)
                        {
                            for (int j = 0; j < (12 - iNum); j++)
                            {
                                System.out.print(" ");
                            }
                        }
                    }
                    System.out.print(theVirtue);
                    System.out.print("} ");
                }
                System.out.println();

                //bottom of card
                for (int i = 0; i < cardList.size(); i++)
                {
                    System.out.print("+~~~~~~~~~~~~~~~+ ");
                }
                System.out.println();
                System.out.println();
                break;
            }
            case "action": //deck of actions----------------------------------------------------------------------------
            {
                //top of card
                for (int i = 0; i < cardList.size(); i++)
                {
                    System.out.print("+~~~~~~~~~~~~~~~+ ");
                }
                System.out.println();

                //stats that are being incremented
                for (int i = 0; i < cardList.size(); i++)
                {
                    //get the current card for easy reference
                    Map currentCard = (Map) cardList.get(i);
                    System.out.print("{ ");

                    if (currentCard.containsKey("attack"))
                    {
                        System.out.print(ANSI_RED + currentCard.get("attack") + ANSI_RESET + " | ");
                    }
                    else
                    {
                        System.out.print(ANSI_RED + "0" + ANSI_RESET + " | ");
                    }
                    if (currentCard.containsKey("coin"))
                    {
                        System.out.print(ANSI_YELLOW + currentCard.get("coin") + ANSI_RESET + " | ");
                    }
                    else
                    {
                        System.out.print(ANSI_YELLOW + "0" + ANSI_RESET + " | ");
                    }
                    if (currentCard.containsKey("cards"))
                    {
                        System.out.print(ANSI_CYAN + currentCard.get("cards") + ANSI_RESET + " | ");
                    }
                    else
                    {
                        System.out.print(ANSI_CYAN + "0" + ANSI_RESET + " | ");
                    }
                    if (currentCard.containsKey("action"))
                    {
                        System.out.print(ANSI_PURPLE + currentCard.get("cards") + ANSI_RESET + " } ");
                    }
                    else
                    {
                        System.out.print(ANSI_PURPLE + "0" + ANSI_RESET + " } ");
                    }
                }
                System.out.println();

                //divider line
                for (int i = 0; i < cardList.size(); i++)
                {
                    System.out.print("{---------------} ");
                }
                System.out.println();

                //Name and cost
                for (int i = 0; i < cardList.size(); i++)
                {
                    //get the current card for easy reference
                    Map currentCard = (Map) cardList.get(i);
                    System.out.print("{");
                    //name
                    String theName = (String)currentCard.get("name");
                    if (theName.length() > 8)
                    {
                        theName = theName.substring(0,8);
                        System.out.print(ANSI_GREEN + theName + ANSI_RESET);
                    }
                    else
                    {
                        int diff = 9 - theName.length();
                        System.out.print(ANSI_GREEN + theName + ANSI_RESET);
                        for (int j = 0; j < diff; j++)
                        {
                            System.out.print(" ");
                        }
                    }
                    //cost
                    System.out.print("Cost:" + currentCard.get("cost"));
                    System.out.print("} ");
                }
                System.out.println();

                //divider line
                for (int i = 0; i < cardList.size(); i++)
                {
                    System.out.print("{---------------} ");
                }
                System.out.println();

                //Virtue Bonuses
                for (int k = 0; k < cardList.size(); k++)
                {
                    //get the current card for easy reference
                    Map currentCard = (Map) cardList.get(k);
                    if (currentCard.containsKey("virtue"))
                    {
                        String virtueName = (String) currentCard.get("virtue");
                        int diff = 15 - virtueName.length();
                        System.out.print("{" + ANSI_BLUE);
                        if (diff < 15)
                        {
                            for (int j = 0; j < diff/2; j++)
                            {
                                System.out.print(" ");
                            }
                        }
                        System.out.print(virtueName);
                        if (diff < 15)
                        {
                            for (int j = 0; j < diff/2; j++)
                            {
                                System.out.print(" ");
                            }
                        }
                        if (diff%2 == 1)
                        {
                            System.out.print(" ");
                        }
                        System.out.print(ANSI_RESET + "} ");
                    }
                    else
                    {
                        System.out.print("{               } ");
                    }
                }
                System.out.println();
                for (int i = 0; i < cardList.size(); i++)
                {
                    //get the current card for easy reference
                    Map currentCard = (Map) cardList.get(i);
                    System.out.print("{ ");
                    //check if there are special modifiers for virtues
                    String attackName = "attack";
                    String coinName = "coin";
                    String cardName = "cards";
                    String actionName = "action";
                    if (currentCard.containsKey("virtue"))
                    {
                        String virtueName = (String) currentCard.get("virtue");
                        System.out.print(ANSI_BLUE);
                        attackName = virtueName + attackName;
                        coinName = virtueName + coinName;
                        cardName = virtueName + cardName;
                        actionName = virtueName + actionName;
                        //check if there are any special modifiers on card
                        if (currentCard.containsKey(attackName))
                        {
                            System.out.print(currentCard.get(attackName) + " | ");
                        }
                        else
                        {
                            System.out.print("0 | ");
                        }
                        if (currentCard.containsKey(coinName))
                        {
                            System.out.print(currentCard.get(coinName) + " | ");
                        }
                        else
                        {
                            System.out.print("0 | ");
                        }
                        if (currentCard.containsKey(cardName))
                        {
                            System.out.print(currentCard.get(cardName) + " | ");
                        }
                        else
                        {
                            System.out.print("0 | ");
                        }
                        if (currentCard.containsKey(actionName))
                        {
                            System.out.print(currentCard.get(actionName));
                        }
                        else
                        {
                            System.out.print("0");
                        }
                    }
                    else
                    {
                        System.out.print("             ");
                    }
                    System.out.print(ANSI_RESET);
                    System.out.print(" } ");
                }
                System.out.println();

                //bottom of card
                for (int i = 0; i < cardList.size(); i++)
                {
                    System.out.print("+~~~~~~~~~~~~~~~+ ");
                }
                System.out.println();
                System.out.println();
                break;
            }
            case "monster": //deck of monsters--------------------------------------------------------------------------
            {
                ////////////////////////////////////////////////////////////////////////////////
                //go through each card in the list
                for (int i = 0; i < cardList.size(); i++)
                {
                    //get the current card for easy reference
                    Map currentCard = (Map) cardList.get(i);

                    //print name, fight, glory value, triggers and their corresponding effects
                    System.out.print((i+1) + ".) *=|===> ");
                    //if there is no card at this index
                    if (currentCard.containsKey("null"))
                    {
                        //nothing will print because there will be no card in this place
                        System.out.println();
                    }
                    else
                    {
                        String theName = (String)currentCard.get("name");
                        System.out.print(ANSI_GREEN + theName+ ANSI_RESET + " | ");
                        System.out.print(ANSI_RED + "Attack: " + currentCard.get("fight") + ANSI_RESET + " | ");
                        System.out.print(ANSI_YELLOW + "Glory: " + currentCard.get("glory") + ANSI_RESET);
                        //check for triggers
                        //ambush trigger
                        if (currentCard.containsKey("ambushdiscard") || currentCard.containsKey("ambushremovecoin") || currentCard.containsKey("ambushremoveattack") || currentCard.containsKey("ambushmove") || currentCard.containsKey("ambushflip"))
                        {
                            System.out.print(" | " + ANSI_BLUE + "Ambush -->");
                            if (currentCard.containsKey("ambushdiscard"))
                            {
                                System.out.print(" [Discard " + currentCard.get("ambushdiscard") + "]");
                            }
                            if (currentCard.containsKey("ambushremovecoin"))
                            {
                                System.out.print(" [Remove Coin " + currentCard.get("ambushremovecoin") + "]");
                            }
                            if (currentCard.containsKey("ambushremoveattack"))
                            {
                                System.out.print(" [Remove Attack " + currentCard.get("ambushremoveattack") + "]");
                            }
                            if (currentCard.containsKey("ambushmove"))
                            {
                                System.out.print(" [Move " + currentCard.get("ambushmove") + "]");
                            }
                            if (currentCard.containsKey("ambushflip"))
                            {
                                System.out.print(" [Flip " + currentCard.get("ambushflip") + "]");
                            }
                        }
                        //fight trigger
                        if (currentCard.containsKey("fightdiscard") || currentCard.containsKey("fightremovecoin") || currentCard.containsKey("fightremoveattack") || currentCard.containsKey("fightflip"))
                        {
                            System.out.print(" | " + ANSI_BLUE + "Fight -->");
                            if (currentCard.containsKey("fightdiscard"))
                            {
                                System.out.print(" [Discard " + currentCard.get("fightdiscard") + "]");
                            }
                            if (currentCard.containsKey("fightremovecoin"))
                            {
                                System.out.print(" [Remove Coin " + currentCard.get("fightremovecoin") + "]");
                            }
                            if (currentCard.containsKey("fightremoveattack"))
                            {
                                System.out.print(" [Remove Attack " + currentCard.get("fightremoveattack") + "]");
                            }
                            if (currentCard.containsKey("fightflip"))
                            {
                                System.out.print(" [Flip " + currentCard.get("fightflip") + "]");
                            }
                        }
                        //conqueror trigger
                        if (currentCard.containsKey("conquerordiscard") || currentCard.containsKey("conquerorremovecoin") || currentCard.containsKey("conquerorremoveattack") || currentCard.containsKey("conquerorflip"))
                        {
                            System.out.print(ANSI_RESET + " | " + ANSI_BLUE + "Conqueror -->");
                            if (currentCard.containsKey("conquerordiscard"))
                            {
                                System.out.print(" [Discard " + currentCard.get("conquerordiscard") + "]");
                            }
                            if (currentCard.containsKey("conquerorremovecoin"))
                            {
                                System.out.print(" [Remove Coin " + currentCard.get("conquerorremovecoin") + "]");
                            }
                            if (currentCard.containsKey("conquerorremoveattack"))
                            {
                                System.out.print(" [Remove Attack " + currentCard.get("conquerorremoveattack") + "]");
                            }
                            if (currentCard.containsKey("conquerorflip"))
                            {
                                System.out.print(" [Flip " + currentCard.get("conquerorflip") + "]");
                            }
                        }
                        System.out.print(ANSI_RESET + " <===|=* ");
                        System.out.println();
                    }
                }
                System.out.println();
                break;
            }
        }
    }
}
