package br.com.honeyinvestimentos.kotlinblockchain

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainActivity : AppCompatActivity() {

    val blockchain = mutableListOf<Block>()
    val blockchainUI = mutableListOf<View>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        //Create Genesis Block
        createBlock(1,"Genesis block", "0000")

        fab.setOnClickListener {

            if(isBlockchainValid()) {
                val lastBlock = blockchain.last()

                createBlock(lastBlock.id + 1, "", lastBlock.hash)

                scroolBlockchain.post {
                    scroolBlockchain.fullScroll(View.FOCUS_DOWN)
                }
            }
        }

    }


    fun createBlock(id:Int, data:String,prevHash:String){

        val newBlock = Block(id, data,0,"",prevHash)
        newBlock.calculateHash()

        val v = LayoutInflater
                .from(this)
                .inflate(R.layout.block_layout, scroolBlockchain, false )

        val txtData = v.findViewById<EditText>(R.id.txtData)
        val txtHash = v.findViewById<TextView>(R.id.txtHash)
        val txtNouceNumber = v.findViewById<TextView>(R.id.txtNouceNumber)
        val btnMineBlock = v.findViewById<Button>(R.id.btnMineBlock)
        val txtBlockId = v.findViewById<TextView>(R.id.txtBlockId)
        val prevHash = v.findViewById<TextView>(R.id.txtPrevHash)
        val linear = v.findViewById<View>(R.id.linearBlock)


        txtData.setText(newBlock.data)
        txtHash.text = newBlock.hash
        txtNouceNumber.text = "${newBlock.nounce}"
        txtBlockId.text = "# ${newBlock.id}"
        prevHash.text = newBlock.prevHash

        val bg = if (newBlock.isValid()) R.drawable.round_valid else R.drawable.round_invalid
        linear.setBackgroundResource(bg)


        btnMineBlock.setOnClickListener {
            val btn = it as Button
            btn.text = "Mining ..."

            doAsync {
                newBlock.mine()

                uiThread {

                    btn.text = "Mine"
                    updateBlock(v, newBlock)
                }
            }
        }


        txtData.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                val v1 = blockchainUI[newBlock.id - 1]
                updateBlock(v1, newBlock)

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                newBlock.data = "$s"
            }

        })

        //update UI
        linearBlockchain.addView(v)

        //update Blockchain
        blockchain.add(newBlock)

        //AddUI
        blockchainUI.add(v)
    }


    fun updateBlock(v:View, block:Block ){

        val txtHash = v.findViewById<TextView>(R.id.txtHash)
        val txtPrev = v.findViewById<TextView>(R.id.txtPrevHash)
        val view = v.findViewById<View>(R.id.linearBlock)
        val txtNouceNumber = v.findViewById<TextView>(R.id.txtNouceNumber)

        block.calculateHash()

        txtPrev.text = block.prevHash
        txtHash.text = block.hash
        txtNouceNumber.text = block.nounce.toString()

        val background = if (block.isValid()) {
            R.drawable.round_valid
        } else {
            R.drawable.round_invalid
        }

        view.setBackgroundResource(background)

        //nextBlock
        val nextBlock = blockchain.filter{ it.id  == block.id +1 }.firstOrNull()

        nextBlock?.let {
            it.prevHash = block.hash

            updateBlock(blockchainUI[block.id], it)
        }
    }

    fun isBlockchainValid () =
            blockchain.filter { it.isValid() }.size == blockchain.size


}
