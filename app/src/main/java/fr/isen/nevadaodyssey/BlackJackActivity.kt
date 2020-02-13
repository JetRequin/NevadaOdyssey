package fr.isen.nevadaodyssey

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_black_jack.*
import kotlin.random.Random

class BlackJackActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_black_jack)
        var initMoney = 1000
        var deck : ArrayList<Card> = createDeck()
        var dealer = Player()
        var player = Player()
        for(i in 0 until deck.size)
        {
            Log.d("Deck","Your card is "+deck[i].value +" of " +deck[i].type)
        }
        Log.d("Deck", "This is the size of the deck :"+deck.size)
        player.initMoney(initMoney)
        initParty(deck,player, dealer)
        for(i in player.cards.indices)
        {
            Log.d("InitParty","Player has "+player.cards[i].value +" of " +player.cards[i].type)
        }
        for(i in dealer.cards.indices)
        {
            Log.d("InitParty","Dealer has "+dealer.cards[i].value +" of " +dealer.cards[i].type)
        }
        Log.d("InitParty", "This is the new size of the deck :"+deck.size)
        Log.d("InitParty", "You have "+ player.getTotalPoints()+" points")
        Log.d("InitParty", "Dealer has "+ dealer.getTotalPoints()+" points")
        playerCard.text=printCard(player)
        dealerCard.text=printCard(dealer)
        standButton.setOnClickListener{
            while(dealer.getTotalPoints()<17 || dealer.getTotalPoints()<=player.getTotalPoints())
            {
                drawCard(deck = deck,player = dealer)
            }
            playerCard.text=printCard(player)
            dealerCard.text=printCard(dealer)
            stateParty(player,dealer,0)
            deck=resetParty(deck,player, dealer)
            Log.d("InitParty", "I am in the Stand button")
            Log.d("InitParty", "This is the new size of the deck :"+deck.size)


        }
        hitButton.setOnClickListener{
            Log.d("InitParty", "I am in the hit button part 1")
            drawCard(deck = deck,player = player)
            Log.d("InitParty", "You have "+ player.getTotalPoints()+" points")
            if(player.getTotalPoints()>21)
            {
                Log.d("InitParty", "I am in the hit button part 2")
                stateParty(player,dealer,0)
                deck=resetParty(deck, player, dealer)
            }
            else if(dealer.getTotalPoints()<17)
            {
                Log.d("InitParty", "I am in the hit button part 3")
                drawCard(deck = deck, player = dealer)
                Log.d("InitParty", "Dealer has "+ dealer.getTotalPoints()+" points")
            }
            else if(dealer.getTotalPoints()>21)
            {
                Log.d("InitParty", "I am in the hit button part 4")
                stateParty(player,dealer,0)
                playerCard.text=printCard(player)
                dealerCard.text=printCard(dealer)
                deck=resetParty(deck, player, dealer)
            }
            playerCard.text=printCard(player)
            dealerCard.text=printCard(dealer)
            Log.d("InitParty", "I am in the hit button part 5")
        }
    }
    fun initParty(deck: ArrayList<Card>,player:Player, dealer:Player)
    {
        drawCard(deck = deck,player = player)
        drawCard(deck = deck,player = dealer)
        drawCard(deck = deck,player = player)
        drawCard(deck = deck,player = dealer)
    }
    fun drawCard(deck: ArrayList<Card>,player:Player)
    {
        var rand = (0..deck.size).random()
        player.addCard(deck[rand])
        deck.removeAt(rand)
    }
    fun stateParty(player: Player,dealer: Player,bet: Int)
    {
        Log.d("InitParty", "Party Over")
        if(player.getTotalPoints()>dealer.getTotalPoints() && player.getTotalPoints()<=21)
        {
            Log.d("Party","Player win and dealer loose")
            player.addRemoveMoney(2*bet)
        }
        else if(player.getTotalPoints()==dealer.getTotalPoints() && player.getTotalPoints()<=21)
        {
            Log.d("Party","It's a draw")
            player.addRemoveMoney(bet)
        }
        else if(dealer.getTotalPoints()>21)
        {
            Log.d("Party","Player win and dealer loose")
            player.addRemoveMoney(2*bet)
        }
        else
        {
            Log.d("Party","Player loose and dealer win")
            player.addRemoveMoney(-bet)
        }
    }
    fun printCard(player: Player): String {
        var string:String=""
        for(i in player.cards.indices)
        {
            string+=player.cards[i].value
            string+=" "
            string+=player.cards[i].type
            string+=" "
        }
        return string
    }
    fun resetParty(deck: ArrayList<Card>, player: Player, dealer: Player) : ArrayList<Card>
    {
        var deckNew: ArrayList<Card>
        if(deck.size<6)
        {
            deckNew= createDeck()
            deck.clear()
            player.removeCards()
            dealer.removeCards()
        }
        else
        {
            deckNew=deck
            player.removeCards()
            dealer.removeCards()
        }
        return deckNew
    }
}

