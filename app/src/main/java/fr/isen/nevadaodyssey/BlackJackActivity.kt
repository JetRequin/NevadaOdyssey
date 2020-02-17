package fr.isen.nevadaodyssey

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_black_jack.*
import java.util.*


class BlackJackActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_black_jack)
        var deck : ArrayList<Card> = createDeck()
        var dealer = Player()
        var player = Player()
        var resetParty : Boolean = false
        for(i in 0 until deck.size)
        {
            Log.d("Deck","Your card is "+deck[i].value +" of " +deck[i].type)
        }
        Log.d("Deck", "This is the size of the deck :"+deck.size)
        val mIntent = intent
        var money = mIntent.getIntExtra("money", 10000)
        player.initMoney(money)
        Log.d("InitParty", "You have "+ player.money+" dollars")
        initParty(deck,player, dealer,playerSet,dealerSet)
        showConsoleCards(player, dealer, deck)
        playerCard.text=printCard(player)
        hitButton.setOnClickListener {
            if(!resetParty)
            {
                drawCard(deck = deck,player = player,linearLayout = playerSet)
                Log.d("InitParty", "You have "+ player.getTotalPoints()+" points")
                playerCard.text=printCard(player)
                if(burst(player))
                {
                    endPartyLoosePlayer(player,0)
                    winnerText.text="You loose"
                    Log.d("InitParty", "You have "+ player.getTotalPoints()+" points")
                    Log.d("InitParty", "Dealer has "+ dealer.getTotalPoints()+" points")
                    resetParty=true
                }
                Log.d("InitParty", "This is the new size of the deck :"+deck.size)
            }
        }
        standButton.setOnClickListener {
            if(!resetParty)
            {
                while(dealer.getTotalPoints()<17 && !burst(dealer) || player.getTotalPoints()>dealer.getTotalPoints())
                {
                    drawCard(deck=deck,player = dealer,linearLayout =dealerSet)
                }
                Log.d("InitParty", "Dealer has "+ dealer.getTotalPoints()+" points")
                if(burst(dealer))
                {
                    endPartyWinPlayer(player,0)
                    winnerText.text="You win"
                    removeCards(dealerSet)
                    showAllCardsEvenHide(dealer,dealerSet)
                }
                else
                {
                    winnerText.text=endPartyWithoutBurst(player,dealer,0)
                    removeCards(dealerSet)
                    showAllCardsEvenHide(dealer,dealerSet)

                }
                resetParty=true
            }
        }
        resetButton.setOnClickListener {
            if(resetParty)
            {
                deck=resetParty(deck, player, dealer)
                removeCards(playerSet)
                removeCards(dealerSet)
                playerCard.text=printCard(player)
                initParty(deck,player, dealer,playerSet,dealerSet)
                playerCard.text=printCard(player)
                showConsoleCards(player, dealer, deck)
                resetParty=false
            }
        }

    }
    fun initParty(deck: ArrayList<Card>,player:Player, dealer:Player, playerLayout: LinearLayout, dealerLayout: LinearLayout)
    {
        drawCard(deck = deck,player = player,linearLayout = playerLayout)
        drawHideCard(deck = deck, dealer = dealer, dealerLayout = dealerLayout)
        drawCard(deck = deck,player = player, linearLayout = playerLayout)
        drawCard(deck = deck,player = dealer, linearLayout = dealerLayout)
    }
    fun drawCard(deck: ArrayList<Card>,player:Player,linearLayout: LinearLayout)
    {
        var rand = (0 until deck.size).random()
        //Log.d("Party", "Random value :$rand")
        player.addCard(deck[rand])
        showCard(player.cards.last(),linearLayout)
        deck.removeAt(rand)
    }
    fun drawHideCard(deck: ArrayList<Card>, dealer: Player, dealerLayout: LinearLayout)
    {
        var rand = (0 until deck.size).random()
        dealer.addCard(deck[rand])
        val image = ImageView(this).apply {
            val id = context.resources.getIdentifier("gray_back", "drawable", context.packageName)
            //Log.d("Party", "Id value is $id")
            setImageResource(id)

            // set the ImageView bounds to match the Drawable's dimensions
            adjustViewBounds = true
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            layoutParams.width=300
            layoutParams.height=300
        }
        deck.removeAt(rand)


        // Add the ImageView to the layout.
        dealerLayout.addView(image)
    }

    fun showAllCardsEvenHide(dealer: Player,dealerLayout: LinearLayout)
    {
        for(i in 0 until dealer.cards.size)
        {
            val image = ImageView(this).apply {
                val id = context.resources.getIdentifier(associateCard(dealer.cards[i]), "drawable", context.packageName)
                //Log.d("Party", "Id value is $id")
                setImageResource(id)

                // set the ImageView bounds to match the Drawable's dimensions
                adjustViewBounds = true
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
                layoutParams.width=300
                layoutParams.height=300
            }


            // Add the ImageView to the layout.
            dealerLayout.addView(image)
        }
    }

    fun endPartyWinPlayer(player: Player,bet: Int)
    {
        player.addRemoveMoney(2*bet)
        Log.d("Party", "Player win")
    }
    fun endPartyLoosePlayer(player: Player,bet: Int)
    {
        player.addRemoveMoney(-bet)
        Log.d("Party", "Player loose")
    }

    fun endPartyWithoutBurst(player: Player,dealer: Player,bet: Int) : String
    {
        when {
            player.getTotalPoints()>dealer.getTotalPoints() -> {
                endPartyWinPlayer(player,bet)
                return "You win"
            }
            player.getTotalPoints()<dealer.getTotalPoints() -> {
                endPartyLoosePlayer(player,bet)
                return "You loose"
            }
            else -> {
                player.addRemoveMoney(bet)
                Log.d("Party", "it's a draw")
                return "It's a draw"
            }
        }
    }

    fun printCard(player: Player): String {
        return player.getTotalPoints().toString()+" points"
    }
    fun resetParty(deck: ArrayList<Card>, player: Player, dealer: Player) : ArrayList<Card>
    {
        var deckNew: ArrayList<Card>
        if(deck.size<10)
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

    fun burst(player: Player) : Boolean
    {
        return player.getTotalPoints()>21
    }

    fun delay(callBack:()-> Unit)
    {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                callBack.invoke()
            }
        }, 2000)
    }
    fun showConsoleCards(player: Player,dealer: Player,deck: ArrayList<Card>)
    {
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

    }
    fun showCard(card: Card, linearLayout: LinearLayout)
    {
        val image = ImageView(this).apply {
            val id = context.resources.getIdentifier(associateCard(card), "drawable", context.packageName)
            //Log.d("Party", "Id value is $id")
            setImageResource(id)

            // set the ImageView bounds to match the Drawable's dimensions
            adjustViewBounds = true
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            layoutParams.width=300
            layoutParams.height=300
        }


        // Add the ImageView to the layout.
       linearLayout.addView(image)

    }

    fun removeCards(linearLayout: LinearLayout)
    {
        linearLayout.removeAllViewsInLayout()
    }

    fun associateCard(card: Card): String
    {
        var stringCard : String = when (card.type) {
            CardType.DIAMONDS -> {
                "d_"
            }
            CardType.CLUBS -> {
                "c_"
            }
            CardType.HEARTS -> {
                "h_"
            }
            CardType.SPADES -> {
                "s_"
            }
        }

        when (card.value) {
            CardValue.AS -> {
                stringCard += "a"
            }
            CardValue.TWO -> {
                stringCard += "2"
            }
            CardValue.THREE -> {
                stringCard += "3"
            }
            CardValue.FOUR -> {
                stringCard += "4"
            }
            CardValue.FIVE -> {
                stringCard += "5"
            }
            CardValue.SIX -> {
                stringCard += "6"
            }
            CardValue.SEVEN -> {
                stringCard += "7"
            }
            CardValue.EIGHT -> {
                stringCard += "8"
            }
            CardValue.NINE -> {
                stringCard += "9"
            }
            CardValue.TEN -> {
                stringCard += "10"
            }
            CardValue.JACK -> {
                stringCard += "j"
            }
            CardValue.QUEEN -> {
                stringCard += "q"
            }
            CardValue.KING -> {
                stringCard += "k"
            }
        }
        //Log.d("Party", "String value is $stringCard")
        return stringCard
    }
}

