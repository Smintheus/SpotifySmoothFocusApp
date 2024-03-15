package js.spotifytool.focuscounter.spotify

fun main(){
    val numbers = listOf("one", "two", "three", "four", "five")
    var gr = numbers.groupBy { it.first().uppercase() }
    var numbers_grouped = numbers.groupBy { it.first().uppercase() }.mapValues {  it.value.size}
}