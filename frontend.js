var numPlayers = 2;

const controlButtons = document.querySelector("#controlButtons");
const displayBody = document.querySelector("#display_body");

const hands = [];
for(let i = 0; i < 9; i++)
{
    const chand = ["-1", "-1"];
    hands[i] = chand;
}

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
    return ("AKQJT98765432".indexOf(input[0]) != -1 && "dsch".indexOf(input[1]) != -1);
}

function recalcEquity()
{
    
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
            card0.removeChild(card0.lastChild);
            var input = prompt("Enter your card: ")
            input[0] = input[0].toUpperCase();
            input[1] = input[1].toLowerCase();
            while(!checkValid(input)) input = prompt("Enter your card: ");
            const makeCard = createCard(input[0], input[1]);
            card0.appendChild(makeCard);
            hands[index][0] = input;
        }
        else
        {
            while(card0.childNodes.length > 0) card0.removeChild(card0.lastChild);
            card0.appendChild(card_back0);
            hands[index][0] = "-1";
        }
        recalcEquity();
    });

    card1.addEventListener("click", function (e) {
        if(card1.childNodes[0].nodeName == "IMG") // currently is back of card
        {
            card1.removeChild(card1.lastChild);
            var input = prompt("Enter your card: ")
            input[0] = input[0].toUpperCase();
            input[1] = input[1].toLowerCase();
            while(!checkValid(input)) input = prompt("Enter your card: ");
            const makeCard = createCard(input[0], input[1]);
            card1.appendChild(makeCard);
            hands[index][1] = input;
        }
        else
        {
            while(card1.childNodes.length > 0) card1.removeChild(card1.lastChild);
            card1.appendChild(card_back1);
            hands[index][1] = "-1";
        }
        recalcEquity();
    });

    playerRow.appendChild(winPercentage);
    displayBody.appendChild(playerRow);
}

createGenericPlayer(0);
createGenericPlayer(1);

controlButtons.addEventListener("click", function (e) {
    choice = e.target['id'];
    if(choice == 'clearBoard')
    {
        
    }
    else if(choice == 'addPlayer')
    {
        createGenericPlayer(numPlayers);
        numPlayers++;
    }
    else if(choice == 'removePlayer')
    {
        if(numPlayers > 2 && numPlayers < 9) 
        {
            displayBody.removeChild(displayBody.lastChild);
            numPlayers--;
        }
    }
});