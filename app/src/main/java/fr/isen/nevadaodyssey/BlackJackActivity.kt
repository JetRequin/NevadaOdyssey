package fr.isen.nevadaodyssey

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlin.random.Random

class BlackJackActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_black_jack)
        var initMoney = 1000
        var deck = createDeck()
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
}

