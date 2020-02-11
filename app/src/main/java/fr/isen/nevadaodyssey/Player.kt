package fr.isen.nevadaodyssey

class Player {
    var cards = ArrayList<Card>()
    var money: Int = 0

    fun addCard(card: Card) {
        this.cards.add(Card(type = card.type,value = card.value))
    }

    fun getTotalPoints() =
        cards.map { it.value.number }.sum()

    fun addRemoveMoney(bet: Int) {
        money += bet
    }
    fun initMoney(initMoney: Int)
    {
        this.money=initMoney
    }
}