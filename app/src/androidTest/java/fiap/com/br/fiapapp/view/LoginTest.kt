package fiap.com.br.fiapapp.view


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import fiap.com.br.fiapapp.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class LoginTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun loginTest() {
        Thread.sleep(1000)
        onView(withId(R.id.edit_email)).perform(replaceText("user_fiap@fiap.com.br"), closeSoftKeyboard())
        onView(withId(R.id.edit_senha)).perform(replaceText("fiap123"), closeSoftKeyboard())

        Thread.sleep(500)
        onView(withId(R.id.btn_entrar)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.btnCadastro)).check(ViewAssertions.matches(isDisplayed()))
    }
}
