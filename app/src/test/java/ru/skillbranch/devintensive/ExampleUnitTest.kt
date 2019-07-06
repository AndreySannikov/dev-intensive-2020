package ru.skillbranch.devintensive

import org.junit.Test

import org.junit.Assert.*
import ru.skillbranch.devintensive.extensions.*
import ru.skillbranch.devintensive.models.User
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun test_instance() {
//        val user = User("1", "Василий", "Пупок")
//        val user2 = User("2", "John", "Wick")
//        val user3 = User("3", "John", "Silverhand",
//            null, lastVisit = Date(), isOnline = true)

//        user2.printMe()
//        user3.printMe()


//        val chat = Chat("1")
//        val date =

//        BaseMessage.makeMessage(user, chat, date, "any text message", "text")

        User.Builder().id("99")
            .firstName("Петя")
            .lastName("Мороз")
            .avatar("згыгы")
            .rating(34)
            .respect(2)
            .lastVisit(Date().add(-5, TimeUnits.DAY))
            .isOnline(false)
            .build().toUserView().printMe()

//        println("<p class=\"title\">Образовательное IT-сообщество Skill Branch</p>".stripHtml() )
//        println("<p>Образовательное  &lt;     IT-сообщество Skill Branch</p>".stripHtml())
    }
}
