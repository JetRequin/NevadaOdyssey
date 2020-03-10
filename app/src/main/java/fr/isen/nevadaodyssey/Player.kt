package fr.isen.nevadaodyssey

class Player {
    var cards = ArrayList<Card>()
    var money: Int = 0

    fun addCard(card: Card) {
        this.cards.add(Card(type = card.type,value = card.value))
    }

    fun removeCards()
    {
        this.cards.clear()
    }

    /*fun getTotalPoints() =
        cards.map { it.value.number }.sum()

    fun getTotalPointsModifyAS(): Int {
        return if (getTotalPoints() > 21 && CardValue.AS.number == 11) {
            CardValue.AS.number = 1;
            getTotalPoints()
        } else {
            return getTotalPoints()
        }

    }*/

    fun getTotalPointsModifyAS() =
        cards.map { it.value.number }.sum()

    fun getTotalPoints(): Int {
        return if (getTotalPointsModifyAS() > 21 && CardValue.AS.number == 11) {
            CardValue.AS.number = 1;
            getTotalPointsModifyAS()
        } else {
            return getTotalPointsModifyAS()
        }

    }

    fun addRemoveMoney(bet: Int) {
        money += bet
    }
    fun initMoney(initMoney: Int)
    {
        this.money=initMoney
    }
}