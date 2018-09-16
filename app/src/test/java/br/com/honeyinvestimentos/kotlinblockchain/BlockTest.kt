package br.com.honeyinvestimentos.kotlinblockchain

import org.junit.Assert.assertEquals
import org.junit.Test
import java.security.MessageDigest

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class BlockTest {
    @Test
    fun hash_isCorrect() {

        val input = "Este é um conteúdo de texto para exemplificar o funcionamento de uma função Hash."

        val b =  Block(0,"",0,"","")
        val output = b.sha256(input)

        assertEquals("d2218b955bddbecd0721137edbc88eec3130d5e3a3a90e19b11441036ea9dafd",
                output)
    }

    fun sha256(blockData : String ): String {
        val bytes = blockData.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)

        return digest.fold("", { acc, it ->  "$acc%02x".format(it) })
    }

}
