var numPlayers = 2;

const controlButtons = document.querySelector("#controlButtons");
const displayBody = document.querySelector("#display_body");

const hands = [];
const board = ["-1", "-1", "-1", "-1", "-1"]
for(let i = 0; i < 9; i++)
{
    const chand = ["-1", "-1"];
    hands[i] = chand;
}

var deadCards = new Set();

while(displayBody.childNodes.length > 0)
{
    displayBody.removeChild(displayBody.lastChild);
}

function createCard(value, suit)
{
    const card_div = document.createElement("div");

    const value_div = document.createElement("div");
    value_div.id = "cardValue";
    value_div.classList.add("text-center");
    value_div.textContent = value;

    card_div.appendChild(value_div);

    const suit_div = document.createElement("div");
    suit_div.id = "cardSuit";
    suit_div.classList.add("text-center");

    card_div.appendChild(suit_div);

    var suitActual;
    if(suit == "h") suit = "hearts";
    else if(suit == "c") suit = "clubs";
    else if(suit == "s") suit = "spades";
    else suit = "diamonds";

    var suitImgSrc = "img/" + suit + ".png";

    const suitImg = document.createElement("img");
    suit_div.appendChild(suitImg);
    suitImg.src = suitImgSrc;
    suitImg.style = "height: 20px; width: auto; margin-bottom: 5px;"

    return card_div;
}   

function checkValid(input)
{
    if(input.length != 2) return false;
    input = input[0].toUpperCase() + input[1].toLowerCase();
    if(deadCards.has(input)) return false;
    return ("AKQJT98765432".indexOf(input[0]) != -1 && "dsch".indexOf(input[1]) != -1);
}

function displayEquities(equities)
{
    for(let index = 0; index < numPlayers; index++)
    {
        var hand1 = hands[index][0] != "-1"; // checks if the card is set
        var hand2 = hands[index][1] != "-1";
        const player = document.querySelector("#player" + index);
        if(!(hand1 && hand2)) player.lastChild.textContent = "-";
        else player.lastChild.textContent = equities[index];
    }
}

async function recalcEquity()
{
    var cntHands = 0;
    for(let hand of hands)
    {
        var hand1 = hand[0] == "-1";
        var hand2 = hand[1] == "-1";
        if(hand1 != hand2) return;
        else cntHands++;
    }
    if(cntHands < 2) return;

    var protocol = "http://";
    var APIBase = protocol + "localhost:8080/";
    var query = "equityCalc?";
    for(let index = 0; index < 9; index++)
    {
        query += "str" + index + "=";
        query += hands[index][0];
        query += hands[index][1];
        query += "&";
    }

    var space = "%20";
    query += "board=";
    for(let card of board)
    {
        query += card + space;
    }

    const response = await fetch(APIBase + query);
    const json = await response.json();

    var equities = json["content"].split(" ");
    equities.pop();
    
    displayEquities(equities);
}

function createGenericPlayer(index)
{
    const playerRow = document.createElement("tr");
    playerRow.id = "player" + index;

    const cardPair = document.createElement("td"); 
    cardPair.id = "cardPair";
    playerRow.appendChild(cardPair);

    const card0 = document.createElement("div");
    card0.id = "card" + index + "0";
    const card1 = document.createElement("div");
    card1.id = "card" + index + "1";
    card0.classList.add("card");
    card1.classList.add("card");

    cardPair.appendChild(card0);
    cardPair.appendChild(card1);

    const card_back0 = document.createElement("img");
    card_back0.src = "img/card_back.png";

    const card_back1 = document.createElement("img");
    card_back1.src = "img/card_back.png";
    
    card0.appendChild(card_back0);
    card1.appendChild(card_back1);
    // card1.appendChild(card_back);

    const winPercentage = document.createElement("td");
    winPercentage.classList.add("text-center");
    winPercentage.textContent = "-";

    card0.addEventListener("click", function (e) {
        if(card0.childNodes[0].nodeName == "IMG") // currently is back of card
        {
            var input = "";
            while(true) 
            { 
                input = prompt("Enter your card: ");
                if(input == null) return;
                if(!checkValid(input)) alert("That was not a valid card!");
                else break;
            }
            card0.removeChild(card0.lastChild);
            input = input[0].toUpperCase() + input[1].toLowerCase();
            const makeCard = createCard(input[0], input[1]);
            card0.appendChild(makeCard);
            deadCards.add(input);
            hands[index][0] = input;
        }
        else
        {
            while(card0.childNodes.length > 0) card0.removeChild(card0.lastChild);
            card0.appendChild(card_back0);
            deadCards.delete(hands[index][0]);
            hands[index][0] = "-1";
        }
        recalcEquity();
    });

    card1.addEventListener("click", function (e) {
        if(card1.childNodes[0].nodeName == "IMG") // currently is back of card
        {
            var input = "";
            while(true) 
            { 
                input = prompt("Enter your card: ");
                if(input == null) return;
                if(!checkValid(input)) alert("That was not a valid card!");
                else break;
            }
            card1.removeChild(card1.lastChild);
            input = input[0].toUpperCase() + input[1].toLowerCase();
            const makeCard = createCard(input[0], input[1]);
            card1.appendChild(makeCard);
            deadCards.add(input);
            hands[index][1] = input;
        }
        else
        {
            while(card1.childNodes.length > 0) card1.removeChild(card1.lastChild);
            card1.appendChild(card_back1);
            deadCards.delete(hands[index][1]);
            hands[index][1] = "-1";
        }
        recalcEquity();
    });

    playerRow.appendChild(winPercentage);
    displayBody.appendChild(playerRow);
}

createGenericPlayer(0);
createGenericPlayer(1);

const boardSelector = document.querySelector("#board");

for(let i = 0; i < 5; i++)
{
    const card = document.createElement("div");
    card.id = "board" + i;
    card.classList.add("card");
    boardSelector.appendChild(card);

    const card_back = document.createElement("img");
    card_back.src = "img/card_back.png";

    card.appendChild(card_back);

    card.addEventListener("click", function(e) {
        if(card.childNodes[0].nodeName == "IMG") // currently is back of card
        {
            var input = "";
            while(true) 
            { 
                input = prompt("Enter your card: ");
                if(input == null) return;
                if(!checkValid(input)) alert("That was not a valid card!");
                else break;
            }
            card.removeChild(card.lastChild);
            input = input[0].toUpperCase() + input[1].toLowerCase();
            const makeCard = createCard(input[0], input[1]);
            card.appendChild(makeCard);
            deadCards.add(input);
            board[i] = input;
        }
        else
        {
            while(card.childNodes.length > 0) card.removeChild(card.lastChild);
            card.appendChild(card_back);
            deadCards.delete(board[i]);
            board[i] = "-1";
        }
        recalcEquity();
    });
}

controlButtons.addEventListener("click", function (e) {
    choice = e.target['id'];
    if(choice == 'addPlayer' && numPlayers < 9)
    {
        createGenericPlayer(numPlayers);
        numPlayers++;
    }
    else if(choice == 'removePlayer' && numPlayers > 2)
    {
        displayBody.removeChild(displayBody.lastChild);
        numPlayers--;
        deadCards.delete(hands[numPlayers][0]);
        deadCards.delete(hands[numPlayers][1]);
        hands[numPlayers] = ["-1", "-1"];
        recalcEquity();
    }
});