package br.com.honeyinvestimentos.kotlinblockchain

import java.security.MessageDigest

class Block(val id:Int,var data:String,var nounce:Int, var hash:String, var prevHash:String  ) {


    fun mine(){
        if(isValid()) return

        nounce = 0
        calculateHash()

        while (!isValid()){
            nounce++
            calculateHash()
        }
    }

    fun calculateHash() {
        hash = sha256("$id$nounce$prevHash$data")
    }


    fun isValid() = hash.startsWith("000")

    fun sha256(blockData : String ): String {
        val bytes = blockData.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("", { acc, it ->  "$acc%02x".format(it) })
    }


}