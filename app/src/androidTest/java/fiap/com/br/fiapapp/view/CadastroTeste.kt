package fiap.com.br.fiapapp.view


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import fiap.com.br.fiapapp.R
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class CadastroTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun consultaTest() {
        Thread.sleep(1000)
        onView(withId(R.id.edit_email)).perform(replaceText("user_fiap@fiap.com.br"))
        onView(withId(R.id.edit_email)).perform(pressImeActionButton())

        onView(withId(R.id.edit_senha)).perform(replaceText("fiap123"))
        onView(withId(R.id.edit_senha)).perform(pressImeActionButton())

        Thread.sleep(500)
        onView(withId(R.id.btn_entrar)).perform(click())

        Thread.sleep(1000)
        onView(withId(R.id.btnCadastro)).perform(scrollTo(), click())

        Thread.sleep(1000)
        onView(withId(R.id.cmbMarca)).perform(scrollTo(), click())
        Thread.sleep(300)
        onView(withText("Citroen")).perform(click());

        Thread.sleep(200)
        onView(withId(R.id.cmbModelo)).perform(scrollTo(), click())
        Thread.sleep(300)
        onView(withText("c3")).perform(click());

        Thread.sleep(200)
        onView(withId(R.id.dtaAno)).perform(scrollTo(), replaceText("2018"))

        Thread.sleep(200)
        onView(withId(R.id.etnKm)).perform(scrollTo(), replaceText("75000"))

        Thread.sleep(200)
        onView(withId(R.id.cmbCor)).perform(scrollTo(), click())
        Thread.sleep(300)
        onView(withText("Branco")).perform(click());

        Thread.sleep(200)
        onView(withId(R.id.cmbFilial)).perform(scrollTo(), click())
        Thread.sleep(300)
        onView(withText("Confiance Veiculos LTDA")).perform(click());

        Thread.sleep(200)
        onView(withId(R.id.etnValor)).perform(scrollTo(), replaceText("25000"))

        Thread.sleep(200)
        onView(withId(R.id.etxPlaca)).perform(scrollTo(), replaceText("ABC1234"))

        Thread.sleep(200)
        onView(withId(R.id.etmDetalhes)).perform(scrollTo(), replaceText("Carro cadastrado no teste de interface"))

        Thread.sleep(200)
        onView(withId(R.id.btnSalvar)).perform(scrollTo(), scrollTo(), click())
    }
}
