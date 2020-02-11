package fr.isen.nevadaodyssey

class Card(
    var value: CardValue,
    var type: CardType
)

fun createDeck( ): ArrayList<Card> {
    val cards = arrayListOf<Card>()
    CardType.values().forEach {type ->
        CardValue.values().forEach {value ->
            cards.add(Card(type = type,value = value))
        }
    }
    return cards
}