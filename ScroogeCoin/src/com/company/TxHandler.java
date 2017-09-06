package com.company;
import java.util.ArrayList;

public class TxHandler {

    private UTXOPool utxoPool;

    /**
     * Creates a public ledger whose current UTXOPool (collection of unspent transaction outputs) is
     * {@code utxoPool}. This should make a copy of utxoPool by using the UTXOPool(UTXOPool uPool)
     * constructor.
     */
    public TxHandler(UTXOPool utxoPl) {
        utxoPool = new UTXOPool(utxoPl);
    }

    /**
     * @return true if:
     * (1) all outputs claimed by {@code tx} are in the current UTXO pool, 
     * (2) the signatures on each input of {@code tx} are valid, 
     * (3) no UTXO is claimed multiple times by {@code tx},
     * (4) all of {@code tx}s output values are non-negative, and
     * (5) the sum of {@code tx}s input values is greater than or equal to the sum of its output
     *     values; and false otherwise.
     */
    public boolean isValidTx(Transaction tx) {
        UTXOPool claimedUTXOs = new UTXOPool();
        int sumOfInputValues = 0;
        for(int i = 0; i < tx.numInputs(); i++) {
            Transaction.Input in = tx.getInput(i);
            UTXO utxo = new UTXO(in.prevTxHash, in.outputIndex);
            Transaction.Output output = utxoPool.getTxOutput(utxo);
            if (!utxoPool.contains(utxo)) return false;
            if (!Crypto.verifySignature(output.address, tx.getRawDataToSign(i), in.signature))
                return false;
            if (claimedUTXOs.contains(utxo)) return false;
            claimedUTXOs.addUTXO(utxo, output);
            sumOfInputValues += output.value;
        }
        ArrayList<Transaction.Output> outputs = tx.getOutputs();
        int sumOfOutputValues = 0;
        for(Transaction.Output output : outputs) {
            if(output.value<0) return false;
            sumOfOutputValues += output.value;
        }
        return sumOfInputValues >= sumOfOutputValues;
    }

    /**
     * Handles each epoch by receiving an unordered array of proposed transactions, checking each
     * transaction for correctness, returning a mutually valid array of accepted transactions, and
     * updating the current UTXO pool as appropriate.
     */
    public Transaction[] handleTxs(Transaction[] possibleTxs) {
        // IMPLEMENT THIS
    }

}
